package com.sparta.petplace.post.service;


import com.sparta.petplace.S3Service;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ErrorResponse;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.like.entity.Likes;
import com.sparta.petplace.like.repository.LikesRepository;
import com.sparta.petplace.member.entity.LoginType;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.repository.MemberRepository;
import com.sparta.petplace.mypage.repository.MypageRepository;
import com.sparta.petplace.post.RequestDto.PostRequestDto;
import com.sparta.petplace.post.ResponseDto.PostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.entity.PostImage;
import com.sparta.petplace.post.entity.Sort;
import com.sparta.petplace.post.repository.PostImageRepository;
import com.sparta.petplace.post.repository.PostRepository;
import com.sparta.petplace.review.dto.ReviewResponseDto;
import com.sparta.petplace.review.entity.Review;
import com.sparta.petplace.review.repository.ReviewRepository;
import com.sparta.petplace.review.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final MypageRepository mypageRepository;
    private final LikesRepository likesRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;


    //게시글 전체 조회
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPosts(String category, Sort sort, String lat, String lng, int page, int size) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findByCategory(category, pageable);
        Double usrtLat = Double.parseDouble(lat);
        Double usrtLng = Double.parseDouble(lng);

        buildPostDtos(postResponseDtos, posts, usrtLat, usrtLng);

        sort(sort, postResponseDtos);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postResponseDtos.size());
        return new PageImpl<>(postResponseDtos.subList(start, end), pageable, postResponseDtos.size());
    }


    //메인 페이지 조회
    public List<PostResponseDto> getMain(String category, String lat, String lng) {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        List<Post> posts = postRepository.findByCategory(category);
        Double usrtLat = Double.parseDouble(lat);
        Double usrtLng = Double.parseDouble(lng);

        buildPostDtos(postResponseDtos, posts, usrtLat, usrtLng);

        Collections.sort(postResponseDtos, Comparator.comparing(PostResponseDto::getDistance));

        List<PostResponseDto> mainResponseDto = new ArrayList<>();
        int i = 0;
        while (i < postResponseDtos.size() && i < 3) {
            mainResponseDto.add(postResponseDtos.get(i));
            i++;
        }
        return mainResponseDto;
    }


    // 게시글 작성
    @Transactional
    public ApiResponseDto<PostResponseDto> createPost(PostRequestDto requestDto, Member member) {
        Optional<Member> member1 = memberRepository.findByEmail(member.getEmail());
        if (member1.isEmpty()) {
            throw new CustomException(Error.NOT_EXIST_USER);
        }
        if (!member1.get().getLoginType().equals(LoginType.BUSINESS)) {
            throw new CustomException(Error.NO_AUTHORITY);
        }
        if (requestDto.getImage() == null || requestDto.getImage().isEmpty()) {
            throw new CustomException(Error.WRONG_INPUT_CONTEN);
        }
        if (requestDto.getImage().size() > 4) {
            throw new CustomException(Error.MAX_INPUT_IMAGE);
        }
        Optional<Post> postName = postRepository.findByTitle(requestDto.getTitle());
        if (postName.isPresent()) {
            throw new CustomException(Error.DUPLICATED_BUSINESS);
        }

        Post posts = Post.of(requestDto, member);
        List<String> imgList = new ArrayList<>();
        List<String> img_url = s3Service.upload(requestDto.getImage());
        for (String image : img_url) {
            PostImage img = new PostImage(posts, image);
            postImageRepository.save(img);
            imgList.add(image);
        }

        try {
            String d = s3Uploader.upload(resizeImage(requestDto.getImage().get(0)), requestDto.getImage().get(0).getOriginalFilename());
            posts.setResizeImage(d);
        } catch (IOException e) {
            throw new CustomException(Error.FAIL_S3_SAVE);
        }
        postRepository.save(posts);
        return ResponseUtils.ok(PostResponseDto.from(posts, imgList));
    }


    //게시글 이름 중복 확인
    @Transactional(readOnly = true)
    public ApiResponseDto<SuccessResponse> postCheck(String title) {
        Optional<Post> findPost = postRepository.findByTitle(title);
        if (findPost.isPresent()) {
            throw new CustomException(Error.DUPLICATED_BUSINESS);
        }
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "등록 가능한 사업자입니다."));
    }


    //게시글 상세 조회
    @Transactional
    public PostResponseDto getPostId(Long post_id, Member member) {
        Post posts = postRepository.findById(post_id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_POST)
        );
        List<String> images = new ArrayList<>();
        for (PostImage postImage : posts.getImage()) {
            images.add(postImage.getImage());
        }
        posts.getReviews().sort(Comparator.comparing(Review::getCreatedAt).reversed());
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        Integer reviewStar = 0;
        int count = 0;
        for (Review r : posts.getReviews()) {
            reviewResponseDtos.add(ReviewResponseDto.from(r));
            reviewStar += r.getStar();
            count += 1;
        }
        Integer starAvr = null;
        if (count != 0) {
            starAvr = Math.round(reviewStar / count);
        }

        Likes likes = likesRepository.findByPostIdAndMember(post_id, member);
        if (likes == null) {
            return PostResponseDto.of(posts, images, reviewResponseDtos, false, count, starAvr);
        } else {
            return PostResponseDto.of(posts, images, reviewResponseDtos, true, count, starAvr);
        }
    }


    //게시글 수정
    @Transactional
    public ApiResponseDto<?> updatePost(Long post_id, PostRequestDto requestDto, Member member) {
        Optional<Post> postOptional = postRepository.findById(post_id);

        if (postOptional.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_POST);
        }
        Post post = postOptional.get();
        if (post.getMember().getEmail().equals(member.getEmail())) {

            for (PostImage postImage : post.getImage()) {
                s3Service.deleteFile(postImage.getImage());
                postImageRepository.delete(postImage);
            }

            List<String> imgList = new ArrayList<>();
            List<String> img_url = s3Service.upload(requestDto.getImage());
            for (String image : img_url) {
                PostImage img = new PostImage(post, image);
                postImageRepository.save(img);
                imgList.add(image);
            }

            List<PostImage> postImages = new ArrayList<>();
            for (String image : imgList) {
                PostImage postImage = new PostImage(post, image);
                postImages.add(postImage);
            }

            post.update(requestDto, postImages, post.getStar());
            return ResponseUtils.ok(PostResponseDto.of(post));
        } else {
            return ResponseUtils.ok(ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), "작성자만 게시물을 수정할 수 있습니다."));
        }
    }


    //게시글 삭제
    @Transactional
    public ApiResponseDto<SuccessResponse> deletePost(Long post_id, Member member) {
        Optional<Post> postOptional = postRepository.findById(post_id);
        if (postOptional.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_POST);
        }
        Post post = postOptional.get();
        if (!post.getMember().getEmail().equals(member.getEmail())) {
            throw new CustomException(Error.NO_AUTHORITY);
        }
        for (PostImage postImage : post.getImage()) {
            s3Service.deleteFile(postImage.getImage());
            postImageRepository.delete(postImage);
        }
        reviewRepository.deleteByPostId(post_id);
        likesRepository.deleteByPostId(post_id);
        mypageRepository.deleteByPostId(post_id);
        postRepository.deleteById(post_id);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, " 게시글 삭제 성공"));
    }


    @Transactional(readOnly = true)
    public ApiResponseDto<List<PostResponseDto>> searchPost(String category, String keyword, Sort sort , String lat, String lng) {
        log.info(category);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        List<Post> posts = postRepository.search(category, keyword);
        Double usrtLat = Double.parseDouble(lat);
        Double usrtLng = Double.parseDouble(lng);

        if (posts.isEmpty()){
            throw new CustomException(Error.NOT_FOUND_POST);
        }

        buildPostDtos(postResponseDtos, posts, usrtLat, usrtLng);

        log.info(postResponseDtos.get(0).getCategory());

        sort(sort, postResponseDtos);

        return ResponseUtils.ok(postResponseDtos);

    }



// ==================================== Method Extract ====================================
    //거리구하기
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // 지구 반지름
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }


    public File resizeImage(MultipartFile file) throws IOException {
        log.info(file.getContentType());

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        int originWidth = originalImage.getWidth();
        int originHeight = originalImage.getHeight();

        int newWidth = 220;
        int newHeight = 0;
        if (originWidth > newWidth) {
            newHeight = (originHeight * newWidth) / originWidth;
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();

        File outputfile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        log.info(outputfile.getAbsolutePath());
        try {
            if (outputfile.createNewFile()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                String type = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().indexOf("/") + 1);
                ImageIO.write(resizedImage, type, bos);

                InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());

                Files.copy(inputStream, outputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return outputfile;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return outputfile;

    }


    public static void buildPostDtos(List<PostResponseDto> postResponseDtos, List<Post> posts, Double usrtLat, Double usrtLng) {
        for (Post p : posts) {
            Double postLat = Double.parseDouble(p.getLat());
            Double postLng = Double.parseDouble(p.getLng());
            double distance = distance(usrtLat, usrtLng, postLat, postLng);
            p.getReviews().sort(Comparator.comparing(Review::getCreatedAt).reversed());
            List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
            Integer reviewStar = 0;
            int count = 0;
            for (Review r : p.getReviews()) {
                reviewResponseDtos.add(ReviewResponseDto.from(r));
                reviewStar += r.getStar();
                count += 1;
            }
            Integer starAvr = null;
            if (count != 0) {
                starAvr = Math.round(reviewStar / count);
            }
            postResponseDtos.add(PostResponseDto.builder()
                    .post(p)
                    .star(starAvr)
                    .distance(distance)
                    .reviewCount(count)
                    .build());
        }
    }


    private void sort(Sort sort, List<PostResponseDto> postResponseDtos) {
        switch (sort){
            case STAR:
                postResponseDtos.sort(Comparator.comparing(PostResponseDto::getStar).reversed());
            case REVIEW:
                postResponseDtos.sort(Comparator.comparing(PostResponseDto::getReviewCount).reversed());
            case DISTANCE:
                postResponseDtos.sort(Comparator.comparing(PostResponseDto::getDistance));
        }
    }
}
