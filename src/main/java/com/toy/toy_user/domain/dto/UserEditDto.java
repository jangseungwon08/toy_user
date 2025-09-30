package com.toy.toy_user.domain.dto;

import com.toy.toy_user.domain.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditDto {
    @NotBlank(message = "이메일을 입력하세요")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "닉네임을 입력하세요")
    @Size(max=  10, message = "닉네임은 10글자까지만 작성해주세요")
    private String nickName;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "휴대폰 번호 형식이 올바르지 않습니다. .이나 -빼고 작성해주세요")
    @Size(max = 11, message = "전화번호는 11자리 이하여야합니다.")
    private String phoneNumber;
}
