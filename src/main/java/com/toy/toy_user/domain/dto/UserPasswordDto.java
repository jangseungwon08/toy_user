package com.toy.toy_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordDto {
    @NotBlank(message = "현재 비밀번호를 입력하세요")
    private String originPassword;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8, max = 12 ,message = "비밀번호는 8자리부터 12자리어야합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$", message = "비밀번호는 영문 숫자 특수문자를 모두 포함해야합니다.")
    private String changePassword;
}
