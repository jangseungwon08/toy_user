package com.toy.toy_user.secret.jwt.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "jwt", ignoreUnknownFields = true)
@Getter
@Setter
public class JwtConfigProperties {
    private Integer  webAccessTokenValidityInSeconds;
    private Integer  webRefreshTokenValidityInSeconds;
    private Integer MobileAccessTokenValidityInSeconds;
    private Integer MobileRefreshTokenValidityInSeconds;
    private String secretKey;
}