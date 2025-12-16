package com.toy.toy_user.user.service;

import com.toy.toy_user.common.exception.NotFound;
import com.toy.toy_user.domain.dto.UserEditDto;
import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.domain.repository.UserRepository;
import com.toy.toy_user.service.UserInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "local")
@SpringBootTest
@DisplayName("유저닉네임 번경 카프카 테스트")
public class UserInfoServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoService userInfoService;

    @Test
    @DisplayName("닉네임 변경시 카프카 토픽을 발행한다.")
    public void changeUserNickNameKafkaTest(){
//        given
        String userId = "seungwon0808";
        String changedNickName = "숭이새로운닉네임";
        UserEditDto dto = UserEditDto.builder()
                .nickName(changedNickName)
                .build();
        Users users = userRepository.findById(1L)
                .orElseThrow(() -> new NotFound("존재하지 않는 유저입니다."));
//        when
        userInfoService.editUser(userId,dto);
//        given

    }
}
