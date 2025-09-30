package com.toy.toy_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRefreshDto {
    @NotBlank(message = "refreshToken을 입력하세요")
    private String refreshToken;
}
