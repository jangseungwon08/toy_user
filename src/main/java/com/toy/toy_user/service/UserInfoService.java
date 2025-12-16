package com.toy.toy_user.service;

import com.toy.toy_user.common.exception.BadParameter;
import com.toy.toy_user.common.exception.NotFound;
import com.toy.toy_user.domain.dto.UserEditDto;
import com.toy.toy_user.domain.dto.UserInfoDto;
import com.toy.toy_user.domain.dto.UserPasswordDto;
import com.toy.toy_user.domain.entity.Users;
import com.toy.toy_user.domain.repository.UserRepository;
import com.toy.toy_user.kafka.producer.KafkaMessageProducer;
import com.toy.toy_user.kafka.producer.board.event.ChangeUserNickNameEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserInfoService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaMessageProducer kafkaMessageProducer;

    @Transactional(readOnly = true)
    public UserInfoDto userInfo(String userId) {
        if (!userRepository.isActivated(userId)) {
            throw new BadParameter("이미 탈퇴한 사용자입니다.");
        }
        Users users = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("사용자를 찾을 수 없습니다."));
        return UserInfoDto.fromEntity(users);
    }

    @Transactional
    public void editUser(String userId, UserEditDto userEditDto) {
//        userId 존재하는지 체크
        Users users = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("사용자를 찾을 수 없습니다."));
        if(!users.getIsActivated()){
            throw new NotFound("이미 탈퇴한 사용자입니다.");
        }
//        DTO에서 넘어온 email이 null이 아니고 기존 이메일과 다를때
        if (userEditDto.getEmail() != null && !users.getEmail().equals(userEditDto.getEmail())) {
//            기존에 DB에 같은 이메일이 있는지 확인
            if (userRepository.existsByEmail(userEditDto.getEmail())) {
                throw new BadParameter("이미 존재하는 이메일입니다.");
            }
            users.setEmail(userEditDto.getEmail());
        }

        // DTO에서 넘어온 nickname이 null이 아니고 기존 닉네임과 다를때
        if (userEditDto.getNickName() != null && !users.getNickName().equals(userEditDto.getNickName())) {
//            기존에 DB에 같은 닉네임이 있는지 확인
            if (userRepository.existsByNickName(userEditDto.getNickName())) {
                throw new BadParameter("이미 존재하는 닉네임입니다.");
            }
            users.setNickName(userEditDto.getNickName());
            ChangeUserNickNameEvent event = ChangeUserNickNameEvent.fromEntity(users);
            kafkaMessageProducer.send(ChangeUserNickNameEvent.TOPIC,event);
        }

        // DTO에서 넘어온 phoneNumber가 null이 아니고 기존 phonenumber와 다를때
        if (userEditDto.getPhoneNumber() != null && !users.getPhoneNumber().equals(userEditDto.getPhoneNumber())) {
//            기존에 DB에 같은 이메일이 있는지 확인
            if (userRepository.existsByPhoneNumber(userEditDto.getPhoneNumber())) {
                throw new BadParameter("이미 존재하는 휴대폰번호입니다.");
            }
            users.setPhoneNumber(userEditDto.getPhoneNumber());
        }
//       이름 변경 메서드
        if (userEditDto.getName() != null && !users.getName().equals(userEditDto.getName())) {
            users.setName(userEditDto.getName());
        }
    }

    @Transactional
    public void editPassword(String userId, UserPasswordDto userPasswordDto){
        Users users = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("존재하지 않는 사용자입니다."));
        if(!users.getIsActivated()){
            throw new NotFound("이미 탈퇴한 사용자입니다.");
        }
//        원래 비밀번호를 누르고 바뀐 비밀번호
        if(!passwordEncoder.matches(userPasswordDto.getOriginPassword(), users.getPassword())){
            throw new BadParameter("비밀번호가 맞지 않습니다.");
        }
        users.setPassword(passwordEncoder.encode(userPasswordDto.getChangePassword()));
    }
}