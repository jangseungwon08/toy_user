package com.toy.toy_user.user.service;

import com.toy.toy_user.domain.dto.UserLoginDto;
import com.toy.toy_user.domain.dto.UserRegisterDto;
import com.toy.toy_user.domain.entity.Role;
import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.domain.repository.UserRepository;
import com.toy.toy_user.secret.TokenGenerator;
import com.toy.toy_user.secret.jwt.dto.TokenDto;
import com.toy.toy_user.service.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("유저 서비스로")
public class UserAuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입을 할 수 있다.")
    public void registerUser() {
//        given
        UserRegisterDto dto = UserRegisterDto.builder()
                .userId("userId")
                .email("email")
                .password("password")
                .name("name")
                .nickName("nickName")
                .phoneNumber("010-0000-0000")
                .build();
//        when
        userAuthService.registerUser(dto);
//        then
        Optional<Users> optionalUser = userRepository.findByUserId("userId");

//        조회된 데이터가 있는지 확인합니다.
        assertThat(optionalUser).isPresent();

//        있다면, 실제 User 객체를 가져옵니다.
        Users foundUser = optionalUser.get();

        // 저장된 User의 각 필드가 DTO의 값과 일치하는지 검증합니다.
        assertThat(foundUser.getUserId()).isEqualTo(dto.getUserId());
        assertThat(foundUser.getEmail()).isEqualTo(dto.getEmail());
        assertThat(foundUser.getName()).isEqualTo(dto.getName());
        assertThat(foundUser.getNickName()).isEqualTo(dto.getNickName());
        assertThat(foundUser.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(foundUser.getPassword()).isNotEqualTo(dto.getPassword());
    }

    @Test
    @DisplayName("엑세스 토큰과 리프레쉬 토큰을 발급할 수 있다.")
    public void token() {
//        given
        UserRegisterDto dto = UserRegisterDto.builder()
                .userId("userId")
                .email("email")
                .password("password")
                .name("name")
                .nickName("nickName")
                .phoneNumber("010-0000-0000")
                .build();
        userAuthService.validateId(dto.getUserId());
        userAuthService.validateNickName(dto.getNickName());
        userAuthService.registerUser(dto);
//        when
        UserLoginDto loginDto = UserLoginDto.builder()
                .userId("userId")
                .password("password")
                .deviceType("WeB")
                .build();
        TokenDto.AccessRefreshToken tokenDto = userAuthService.tokens(loginDto);

//        then
        assertThat(tokenDto.getRefresh().getExpiresIn() > tokenDto.getAccess().getExpiresIn());
        System.out.println(
                tokenDto.getAccess().getExpiresIn()
        );
        System.out.println(tokenDto.getRefresh().getExpiresIn());
    }
}
