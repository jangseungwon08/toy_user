package com.toy.toy_user.domain.dto;

import com.toy.toy_user.domain.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String userId;
    private String email;
    private String name;
    private String nickName;
    private String phoneNumber;

    public static UserInfoDto fromEntity(Users users){
        return UserInfoDto.builder()
                .userId(users.getUserId())
                .email(users.getEmail())
                .name(users.getName())
                .nickName(users.getNickName())
                .phoneNumber(users.getPhoneNumber())
                .build();
    }
}
