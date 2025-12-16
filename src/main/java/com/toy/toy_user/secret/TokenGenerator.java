package com.toy.toy_user.secret;

import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.secret.jwt.dto.TokenDto;
import com.toy.toy_user.secret.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtConfigProperties configProperties;
    private volatile SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configProperties.getSecretKey()));
                }
            }
        }
        return secretKey;
    }
    public TokenDto.AccessToken generateAccessToken(Users user, String deviceType) {
        TokenDto.JwtToken jwtToken = this.generateJwtToken(user,  deviceType, false);
        return new TokenDto.AccessToken(jwtToken);
    }

    public TokenDto.AccessRefreshToken generateAccessRefreshToken(Users user, String deviceType) {
        TokenDto.JwtToken accessJwtToken = this.generateJwtToken(user, deviceType, false);
        TokenDto.JwtToken refreshJwtToken = this.generateJwtToken(user, deviceType, true);
        return new TokenDto.AccessRefreshToken(accessJwtToken, refreshJwtToken);
    }
    public TokenDto.JwtToken generateJwtToken(Users user, String deviceType, boolean refreshToken
    ) {
        int tokenExpiresIn = tokenExpiresIn(refreshToken, deviceType);
        String tokenType = refreshToken ? "refresh" : "access";
        String token = Jwts.builder()
                .issuer("toy")
                .subject(user.getUserId())
                .claim("userId", user.getUserId())
                .claim("deviceType", deviceType)
                .claim("tokenType", tokenType)
                .claim("nickName", user.getNickName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiresIn * 1000L)).signWith(getSecretKey())
                .header().add("typ", "JWT")
                .and()
                .compact();
        return new TokenDto.JwtToken(token, tokenExpiresIn);
    }

    public String validateJwtToken(String refreshToken) {
        String userId = null;
        final Claims claims = this.verifyAndGetClaims(refreshToken);
        if (claims == null) {
            return null;
        }
        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            return null;
        }
        userId = claims.get("userId", String.class);
        String tokenType = claims.get("tokenType", String.class);
        if (!"refresh".equals(tokenType)) {
            return null;
        }
        return userId;
    }

    private Claims verifyAndGetClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }
    public Long getRemainingExpirationMillis(String accessToken) {
        final Claims claims = this.verifyAndGetClaims(accessToken);

        if (claims == null) {
            return 0L; // 토큰이 유효하지 않으면 0을 반환
        }

        long expirationMillis = claims.getExpiration().getTime();
        long currentMillis = System.currentTimeMillis();

        return Math.max(0, expirationMillis - currentMillis); // 이미 만료되었으면 0을 반환
    }

    private int tokenExpiresIn(boolean refreshToken, String deviceType) {
        int expiresIn = 60;
//        리프레쉬 토큰이고 deviceType에 따라서
        if (refreshToken) {
            if (deviceType != null) {
                if (deviceType.equals("WEB")) {
                    expiresIn = configProperties.getWebRefreshTokenValidityInSeconds();
                } else if (deviceType.equals("MOBILE")) {
                    expiresIn = configProperties.getMobileRefreshTokenValidityInSeconds();
                }
            }
        }
//            엑세스 토큰이고 DeviceType에 따라서
        else {
            if(deviceType != null){
                if(deviceType.equals("WEB")){
                    expiresIn = configProperties.getWebAccessTokenValidityInSeconds();
                } else if(deviceType.equals("MOBILE")){
                    expiresIn = configProperties.getMobileAccessTokenValidityInSeconds();
                }
            }
        }
        return expiresIn;
    }
}