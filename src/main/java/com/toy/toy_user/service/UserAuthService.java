package com.toy.toy_user.service;

import com.toy.toy_user.common.exception.AlreadyExists;
import com.toy.toy_user.common.exception.BadParameter;
import com.toy.toy_user.common.exception.NotFound;
import com.toy.toy_user.domain.dto.UserLoginDto;
import com.toy.toy_user.domain.dto.UserRefreshDto;
import com.toy.toy_user.domain.dto.UserRegisterDto;
import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.domain.repository.UserRepository;
import com.toy.toy_user.secret.TokenGenerator;
import com.toy.toy_user.secret.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void registerUser(UserRegisterDto userRegisterDto) {
//        unique 제약조건 확인
        if (userRepository.existsByUserId(userRegisterDto.getUserId())) {
            throw new AlreadyExists("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickName(userRegisterDto.getNickName())) {
            throw new AlreadyExists("이미 존재하는 닉네임입니다.");
        }
//        user엔티티 객체 생성
        Users users = userRegisterDto.toEntity(passwordEncoder);
//        동시성 제어
        try {
            userRepository.save(users);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExists("이미 등록된 사용자 정보가 있습니다.");
        }
    }
    @Transactional(readOnly = true)
    public boolean validateId(String userId){
        return !userRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean validateNickName(String nickName){
        return !userRepository.existsByNickName(nickName);
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken tokens(UserLoginDto userLoginDto) {
        Users users = userRepository.findByUserId(userLoginDto.getUserId()).
                orElseThrow(() -> new BadParameter("아이디 또는 비밀번호를 확인하세요"));
//        이미 인코딩 된 String값을 비교할 때는 matches를 사용해야됨
        if (!passwordEncoder.matches(userLoginDto.getPassword(), users.getPassword())) {
            throw new BadParameter("아이디 또는 비밀버호를 확인하세요");
        }
        return tokenGenerator.generateAccessRefreshToken(users, "web");
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken renewTokens(UserRefreshDto userRefreshDto) {
        String userId = tokenGenerator.validateJwtToken(userRefreshDto.getRefreshToken());
        if (userId == null) {
            throw new BadParameter("토큰이 유효하지 않습니다");
        }
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("사용자를 찾을 수 없습니다."));
        return tokenGenerator.generateAccessRefreshToken(user, "WEB");
    }

    @Transactional
    public void removalTokens(String accessToken) {
        Long expiresMillis = tokenGenerator.getRemainingExpirationMillis(accessToken);
        if (expiresMillis > 0) {
            redisTemplate.opsForValue().set(
                    accessToken,
                    "logout",
                    expiresMillis,
                    TimeUnit.MILLISECONDS
            );
        }
    }
}
