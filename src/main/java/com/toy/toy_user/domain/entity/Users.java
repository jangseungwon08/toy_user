package com.toy.toy_user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "users")
@Getter
@Setter //테스트 코드 짤 때만
@NoArgsConstructor
public class Users extends BaseTimeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //오토 인크리먼트
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "email", nullable = false, length = 25)
    private String email;

    @Column(name = "password", nullable = false)
    @Setter
    private String password;

    @Column(name = "user_name", nullable = false, length = 10)
    private String name;

    @Column(name = "nickname", unique = true, length = 10)
    @Setter
    private String nickName;

    @Column(name = "phone_number", nullable = false, unique = true, length = 11)
    @Setter
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "isActivated", nullable = false)
    private boolean isActivated;

    @Builder
    public Users(String userId, String email, String password, String name, String nickName, String phoneNumber, Role role){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isActivated = true;
    }
}
