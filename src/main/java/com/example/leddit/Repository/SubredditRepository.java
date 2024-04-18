package com.example.leddit.Repository;

import com.example.leddit.Model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

}
