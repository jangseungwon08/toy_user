package com.toy.toy_user.domain.repository;


import com.toy.toy_user.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userId);
    boolean existsByNickName(String nickName);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    @Query(value = "SELECT is_activated FROM users where user_id = :user_id",nativeQuery =true)

    boolean isActivated(@Param("user_id") String userId);

    Optional<Users> findByUserId(String userId);
}
