package com.toy.toy_user.api.open;

import com.toy.toy_user.common.context.GatewayRequestHeaderUtils;
import com.toy.toy_user.common.dto.ApiResponseDto;
import com.toy.toy_user.domain.dto.UserEditDto;
import com.toy.toy_user.domain.dto.UserInfoDto;
import com.toy.toy_user.domain.dto.UserPasswordDto;
import com.toy.toy_user.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping(value = "/me")
    public ApiResponseDto<UserInfoDto> userInfo(){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        return ApiResponseDto.createOk(userInfoService.userInfo(userId));
    }

    @PostMapping(value = "/edit")
    public ApiResponseDto<String> editUser(@RequestBody @Valid UserEditDto userEditDto){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        userInfoService.editUser(userId, userEditDto);
        return ApiResponseDto.createOk("수정이 완료되었습니다.");
    }

    @PostMapping(value = "/editPassword")
    public ApiResponseDto<String> editPassword(@RequestBody @Valid UserPasswordDto userPasswordDto){
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        userInfoService.editPassword(userId, userPasswordDto);
        return ApiResponseDto.createOk("비밀번호 수정이 완료되었습니다.");
    }
}
