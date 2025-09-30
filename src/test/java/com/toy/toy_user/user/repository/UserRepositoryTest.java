package com.toy.toy_user.user.repository;


import com.toy.toy_user.domain.entity.Role;
import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest //Repository테스트만 할 때는 이게 더 가볍다고 함
@DisplayName("user레포지토리로")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 정보를 가져올 수 있다.")
    void getUser(){
//        given
        Users users = new Users();
        users.setUserId("userId");
        users.setEmail("email");
        users.setPassword("password");
        users.setName("Name");
        users.setNickName("NickName");
        users.setPhoneNumber("010-0000-0000");
        users.setRole(Role.ROLE_USER);
        Users saved = userRepository.save(users);
        Long savedId = saved.getId();
        System.out.println(users.getId());
        System.out.println(users.getUserId());

//        when
        Optional<Users> result = userRepository.findById(savedId);

//        then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("userId");
        assertThat(result.get().getNickName()).isEqualTo("NickName");
        assertThat(result.get().getPhoneNumber()).isEqualTo("010-0000-0000");
    }
}
