package com.example.leddit.Service;

import com.example.leddit.Model.Subreddit;
import com.example.leddit.Repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;

    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }

    // Define the createSubreddit method
    public void createSubreddit(Subreddit subreddit) {
        subredditRepository.save(subreddit);
    }
}
