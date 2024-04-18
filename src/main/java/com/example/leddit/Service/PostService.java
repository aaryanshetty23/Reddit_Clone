package com.example.leddit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.leddit.Model.Post;
import com.example.leddit.Model.User;
import com.example.leddit.Repository.PostRepository;

import com.example.leddit.Model.Subreddit;
import com.example.leddit.Repository.SubredditRepository;
import com.example.leddit.Repository.UserRepository;
import java.time.LocalDateTime;


@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository, SubredditRepository subredditRepository) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
    }

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private UserRepository userRepository;


    public Post getPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public void createPost(String title, String content, Long subredditId, Long userId) {
        // Retrieve the subreddit by ID
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() ->
                new IllegalArgumentException("Invalid subreddit ID: " + subredditId));
        
        // Retrieve the user by ID
        User author = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Invalid user ID: " + userId));

        // Create a new Post object
        Post newPost = new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setAuthor(author);
        newPost.setSubreddit(subreddit);
        newPost.setCreatedAt(LocalDateTime.now());


        // Save the new post to the repository
        postRepository.save(newPost);
    }
}

