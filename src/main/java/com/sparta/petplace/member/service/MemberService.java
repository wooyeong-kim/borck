package com.sparta.petplace.member.service;

import com.sparta.petplace.auth.jwt.JwtUtil;
import com.sparta.petplace.auth.jwt.RefreshToken;
import com.sparta.petplace.auth.jwt.RefreshTokenRepository;
import com.sparta.petplace.auth.jwt.TokenDto;
import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessLoginResponse;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.dto.BusinessSignupRequestDto;
import com.sparta.petplace.member.dto.LoginRequestDto;
import com.sparta.petplace.member.dto.SignupRequestDto;
import com.sparta.petplace.member.entity.LoginType;
import com.sparta.petplace.member.entity.Member;
import com.sparta.petplace.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 회원가입 기능
     */
    @Transactional
    public ApiResponseDto<SuccessResponse> signup(SignupRequestDto signupRequestDto) {
        memberCheck(signupRequestDto.getEmail());
        memberRepository.save(Member.builder()
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .loginType(LoginType.USER)
                .build());
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }

    /**
     * 사업자 회원가입 기능
     */
    public ApiResponseDto<SuccessResponse> businessSignup(BusinessSignupRequestDto signupRequestDto) {
        memberCheck(signupRequestDto.getEmail());
        memberRepository.save(Member.builder()
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .business(signupRequestDto.getBusiness())
                .loginType(LoginType.BUSINESS)
                .build());
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }

    /**
     * 로그인 기능
     */
    @Transactional
    public ApiResponseDto<SuccessLoginResponse> login(LoginRequestDto requestDto, HttpServletResponse response) {
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


        return ResponseUtils.ok(SuccessLoginResponse.of(HttpStatus.OK, "로그인 성공", findMember.get().getNickname()));
    }

    /**
     * 이메일 중복 검사
     **/
    @Transactional
    public ApiResponseDto<SuccessResponse> memberCheck(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new CustomException(Error.DUPLICATED_EMAIL);
        }
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "사용 가능한 계정입니다."));
    }

    /**
     * 사업자 번호 중복검사
     **/
    @Transactional
    public ApiResponseDto<SuccessResponse> businessMemberCheck(String business) {
        Optional<Member> findMember = memberRepository.findByBusiness(business);
        if (findMember.isPresent()) {
            throw new CustomException(Error.DUPLICATED_BUSINESS);
        }
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "가입이 가능합니다.."));
    }


    /**
     * 토큰 갱신
     **/
    public ApiResponseDto<SuccessResponse> issueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.resolveToken(request, "Refresh");
        if (!jwtUtil.refreshTokenValidation(refreshToken)) {
            throw new CustomException(Error.WRONG_TOKEN);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(jwtUtil.getUserId(refreshToken), "Access"));
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "토큰 갱신 성공."));
    }


}
