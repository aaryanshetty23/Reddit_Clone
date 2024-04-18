package com.example.leddit.Repository;

import com.example.leddit.Model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);

    void deleteByToken(String token);

    List<UserToken> findByUserId(Long userId);

    int deleteByExpiresAtBefore(Date currentDate);
}
