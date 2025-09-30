package com.toy.toy_user.api.open;


import com.toy.toy_user.common.dto.ApiResponseDto;
import com.toy.toy_user.common.exception.BadParameter;
import com.toy.toy_user.domain.dto.UserLoginDto;
import com.toy.toy_user.domain.dto.UserRefreshDto;
import com.toy.toy_user.domain.dto.UserRegisterDto;
import com.toy.toy_user.secret.jwt.dto.TokenDto;
import com.toy.toy_user.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    private final UserAuthService userAuthService;

    @PostMapping(value = "/signup")
    public ApiResponseDto<String> register(@RequestBody @Valid UserRegisterDto userRegisterDto){
        userAuthService.registerUser(userRegisterDto);
        return ApiResponseDto.createOk("회원가입 성공");
    }

    @PostMapping(value = "/login")
    public ApiResponseDto<TokenDto.AccessRefreshToken> login(@RequestBody @Valid UserLoginDto userLoginDto){
        TokenDto.AccessRefreshToken accessToken = userAuthService.login(userLoginDto);
        return ApiResponseDto.createOk(accessToken);
    }

    @PostMapping(value = "/refresh")
    public ApiResponseDto<TokenDto.AccessRefreshToken> refresh(@RequestBody @Valid UserRefreshDto userRefreshDto){
        TokenDto.AccessRefreshToken refreshToken = userAuthService.refresh(userRefreshDto);
        return ApiResponseDto.createOk(refreshToken);
    }

    @PostMapping(value = "/logout")
    public ApiResponseDto<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String accessToken;
//        Bearer 토큰이 있단는 검증 로직
        if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            accessToken = authorizationHeader.substring(7);
        }
        else{
            throw new BadParameter("인증 토큰이 올바르지 않습니다.");
        }
        userAuthService.logout(accessToken);
        return ApiResponseDto.createOk("로그아웃 성공");
    }
}
