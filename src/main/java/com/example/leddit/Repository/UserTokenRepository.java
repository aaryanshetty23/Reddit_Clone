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

    // Additional methods to support token operations
    
    /**
     * Find all tokens for a specific user.
     * 
     * @param userId the ID of the user
     * @return a list of tokens for the specified user
     */
    List<UserToken> findByUserId(Long userId);

    /**
     * Delete all expired tokens.
     * 
     * @param currentDate the current date and time
     * @return the number of tokens deleted
     */
    int deleteByExpiresAtBefore(Date currentDate);
}
