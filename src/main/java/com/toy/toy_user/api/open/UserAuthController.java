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
@RequestMapping(value = "/api/v1/users/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    private final UserAuthService userAuthService;

    @PostMapping(value = "/register")
    public ApiResponseDto<String> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        userAuthService.registerUser(userRegisterDto);
        return ApiResponseDto.createOk("회원가입 성공");
    }

    @GetMapping(value = "/register/validateId")
    public ApiResponseDto<Boolean> validateId(@RequestParam String userId){
        return ApiResponseDto.createOk(userAuthService.validateId(userId));
    }

    @GetMapping(value = "/register/validateNickName")
    public ApiResponseDto<Boolean> validateNickName(@RequestParam String nickName){
        return ApiResponseDto.createOk((userAuthService.validateNickName(nickName)));
    }

    @PostMapping(value = "/tokens")
    public ApiResponseDto<TokenDto.AccessRefreshToken> tokens(@RequestBody @Valid UserLoginDto userLoginDto) {
        TokenDto.AccessRefreshToken accessToken = userAuthService.tokens(userLoginDto);
        return ApiResponseDto.createOk(accessToken);
    }

    @PostMapping(value = "/tokens/renew")
    public ApiResponseDto<TokenDto.AccessRefreshToken> renewTokens(@RequestBody @Valid UserRefreshDto userRefreshDto) {

        TokenDto.AccessRefreshToken refreshToken = userAuthService.renewTokens(userRefreshDto);
        return ApiResponseDto.createOk(refreshToken);
    }

    @PostMapping(value = "/tokens/removal")
    public ApiResponseDto<String> removalTokens(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken;
//        Bearer 토큰이 있단는 검증 로직
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        } else {
            throw new BadParameter("인증 토큰이 올바르지 않습니다.");
        }
        userAuthService.removalTokens(accessToken);
        return ApiResponseDto.createOk("로그아웃 성공");
    }
}
