package com.example.leddit.Service;

import com.example.leddit.Model.Subreddit;
import com.example.leddit.Repository.SubredditRepository;
import com.example.leddit.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.leddit.Model.Post;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;

    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }

    // Define the createSubreddit method
    public void createSubreddit(Subreddit subreddit) {
        subreddit.setCreatedDate(LocalDateTime.now());
        subredditRepository.save(subreddit);
    }

    @Autowired
    private PostRepository postRepository;

    // Fetch subreddit by ID
    public Subreddit getSubredditById(Long id) {
        return subredditRepository.findById(id).orElse(null);
    }

    // Fetch posts for the subreddit
    public List<Post> getPostsForSubreddit(Long id) {
        return postRepository.findBySubredditId(id);
    }
}
