package com.sparta.petplace.member.controller;

import com.sparta.petplace.common.ApiResponseDto;
import com.sparta.petplace.common.ResponseUtils;
import com.sparta.petplace.common.SuccessLoginResponse;
import com.sparta.petplace.common.SuccessResponse;
import com.sparta.petplace.exception.CustomException;
import com.sparta.petplace.exception.enumclass.Error;
import com.sparta.petplace.member.dto.LoginRequestDto;
import com.sparta.petplace.member.dto.SignupRequestDto;
import com.sparta.petplace.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 기능 Controller
     */

    @PostMapping("/members/signup")
    public ApiResponseDto<SuccessResponse> signup(@Valid @RequestBody SignupRequestDto signupRequestDto,
                                                  BindingResult result) {
        if (result.hasErrors()){
            if (result.getFieldError().getDefaultMessage().equals("패스워드에러"))
                throw new CustomException(Error.WRONG_PASSWORD_CHECK);
            throw new CustomException(Error.VALIDATE_EMAIL_ERROR);
        }
        return memberService.signup(signupRequestDto);
    }
    /**
     * 로그인 메서드
     **/
    @PostMapping("/members/login")
    public ApiResponseDto<SuccessLoginResponse> login(@RequestBody LoginRequestDto requestDto,
                                                      HttpServletResponse response){
        return memberService.login(requestDto,response);
    }

    /**
     * 회원명 중복 체크
     */

    @GetMapping("/members")
    public ApiResponseDto<SuccessResponse> memberCheck( @RequestParam("email") String email) {
        memberService.memberCheck(email);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"사용가능한 계정입니다"));
    }

    /**
     * 회원 토큰 갱신
     **/
    @GetMapping("/members/token")
    public  ApiResponseDto<SuccessResponse> issuedToken(HttpServletRequest request,
                                                        HttpServletResponse response){
        return memberService.issueToken(request,response);
    }

}
