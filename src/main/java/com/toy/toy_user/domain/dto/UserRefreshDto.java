package com.toy.toy_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserRefreshDto {
    @NotBlank(message = "refreshToken을 입력하세요")
    private String refreshToken;

    private String deviceType;
}
