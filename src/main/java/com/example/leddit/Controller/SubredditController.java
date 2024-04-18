package com.example.leddit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import com.example.leddit.Model.Post;
import com.example.leddit.Model.Subreddit;
import com.example.leddit.Service.SubredditService;

@Controller
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @GetMapping("/subreddit/{id}")
    public String viewSubreddit(@PathVariable Long id, Model model) {
        // Fetch subreddit data using the service
        Subreddit subreddit = subredditService.getSubredditById(id);
        
        if (subreddit == null) {
            return "error/404";
        }
        
        // Fetch posts for the subreddit
        List<Post> posts = subredditService.getPostsForSubreddit(id);

        // Add the subreddit and posts to the model
        model.addAttribute("subreddit", subreddit);
        model.addAttribute("posts", posts);

        // Return the view name for the individual subreddit
        return "subreddit";
    }
}
