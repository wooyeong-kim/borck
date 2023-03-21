package com.sparta.petplace.member.service;

import com.sparta.petplace.auth.jwt.JwtUtil;
import com.sparta.petplace.auth.jwt.RefreshToken;
import com.sparta.petplace.auth.jwt.RefreshTokenRepository;
import com.sparta.petplace.auth.jwt.TokenDto;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.dto.BusinessSignupRequestDto;
import com.sparta.petplace.member.dto.LoginRequestDto;
import com.sparta.petplace.member.dto.LoginResponseDto;
import com.sparta.petplace.member.dto.SignupRequestDto;
import com.sparta.petplace.member.entity.LoginType;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.entity.MemberHistory;
import com.sparta.petplace.member.repository.MemberHistoryRepository;
import com.sparta.petplace.member.repository.MemberRepository;
import com.sparta.petplace.post.ResponseDto.HistoryPostResponseDto;
import com.sparta.petplace.post.entity.Post;
import com.sparta.petplace.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.petplace.exception.enumclass.Error.NOT_EXIST_USER;
import static com.sparta.petplace.exception.enumclass.Error.PASSWORD_WRONG;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberHistoryRepository memberHistoryRepository;
    private final PostService postService;


    // 회원가입기능
    @Transactional
    public ApiResponseDto<SuccessResponse> signup(SignupRequestDto signupRequestDto) {
        memberCheck(signupRequestDto.getEmail());
//        String basicImg = "https://kunon-clean-project.s3.ap-northeast-2.amazonaws.com/defaultImage.webp";
        memberRepository.save(Member.builder()
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
//                .image(basicImg)
                .loginType(LoginType.USER)
                .build());
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }


    //사업자 회원가입 기능
    public ApiResponseDto<SuccessResponse> businessSignup(BusinessSignupRequestDto signupRequestDto) {
        memberCheck(signupRequestDto.getEmail());
//        String basicImg = "https://kunon-clean-project.s3.ap-northeast-2.amazonaws.com/defaultImage.webp";
        memberRepository.save(Member.builder()
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .business(signupRequestDto.getBusiness())
//                .image(basicImg)
                .loginType(LoginType.BUSINESS)
                .build());
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }


    //로그인
    @Transactional
    public ApiResponseDto<LoginResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isEmpty()) {
            throw new CustomException(NOT_EXIST_USER);
        }
        if (!passwordEncoder.matches(password, findMember.get().getPassword())) {
            throw new CustomException(PASSWORD_WRONG);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(email);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findAllByMemberId(email);

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefresh_Token()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefresh_Token(), email);
            refreshTokenRepository.save(newToken);
        }

        jwtUtil.setHeader(response, tokenDto);
        String img = findMember.get().getImage();
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .loginType(findMember.get().getLoginType())
                .nickcame(findMember.get().getNickname())
                .img(img)
                .build();

        return ResponseUtils.ok(loginResponseDto);
    }


    //메일 중복 검사
    @Transactional
    public ApiResponseDto<SuccessResponse> memberCheck(String email) {
        log.info(email);
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isPresent()) {
            log.info(findMember.get().getEmail());
            throw new CustomException(Error.DUPLICATED_EMAIL);
        }

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "사용 가능한 계정입니다."));
    }


    //사업자 번호 중복검사
    @Transactional
    public ApiResponseDto<SuccessResponse> businessMemberCheck(String business) {
        Optional<Member> findMember = memberRepository.findByBusiness(business);
        if (findMember.isPresent()) {
            throw new CustomException(Error.DUPLICATED_BUSINESS);
        }
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "가입이 가능합니다.."));
    }


    //토큰 갱신
    public ApiResponseDto<SuccessResponse> issueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.resolveToken(request, "Refresh");
        if (!jwtUtil.refreshTokenValidation(refreshToken)) {
            throw new CustomException(Error.WRONG_TOKEN);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(jwtUtil.getUserId(refreshToken), "Access"));
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "토큰 갱신 성공."));
    }

    //내가 본 게시물 조회하기 (3개)
    @Transactional
    public List<HistoryPostResponseDto> getMemberHistory(Member member) {
        List<MemberHistory> userHistories = memberHistoryRepository.findTop3ByMemberOrderByCreatedAtDesc(member);
        List<HistoryPostResponseDto> postResponseDtos = new ArrayList<>();

        for (MemberHistory history : userHistories) {
            Post post = history.getPost();
            HistoryPostResponseDto postResponseDto = postService.getPostIfoNoHistory(post.getId(), member);
            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }


}
