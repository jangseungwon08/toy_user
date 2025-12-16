package com.toy.toy_user.kafka.producer.board.event;

import com.toy.toy_user.domain.entity.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserNickNameEvent {
    public static final String TOPIC = "changeUserNickName";

    private String userNickName;
    private String userId;

    public static ChangeUserNickNameEvent fromEntity(Users users){
//        객체 초기화
        ChangeUserNickNameEvent event = new ChangeUserNickNameEvent();
        event.setUserNickName(users.getNickName());
        event.setUserId(users.getUserId());
        return event;
    }
}
