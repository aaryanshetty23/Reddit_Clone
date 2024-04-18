package com.example.leddit.Controller;

import com.example.leddit.Model.Subreddit;
import com.example.leddit.Model.User;
import com.example.leddit.Service.SubredditService;
import com.example.leddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model) {
        // Fetch all subreddits and add to model
        model.addAttribute("subreddits", subredditService.getAllSubreddits());
        return "home";
    }

    @GetMapping("/subreddit/new")
    public String showCreateSubredditForm(Model model) {
        model.addAttribute("subreddit", new Subreddit());
        return "create_subreddit";
    }

    @PostMapping("/subreddit/create")
    public String createSubreddit(@ModelAttribute Subreddit subreddit, @RequestHeader("Authorization") String authorizationHeader, Model model) {
        // Extract token from Authorization header
        String token = authorizationHeader.replace("Bearer ", "");
    
        if (token == null || !userService.isTokenValid(token)) {
            model.addAttribute("error", "You must be logged in to create a subreddit.");
            return "create_subreddit";
        }
    
        // Get user from token
        User user = userService.getUserFromToken(token);
        if (user == null) {
            model.addAttribute("error", "Invalid token or user not found.");
            return "create_subreddit";
        }

        // add subreddit owner
        subreddit.setCreator(user);
        
        try {
            subredditService.createSubreddit(subreddit);
            model.addAttribute("success", "Subreddit created successfully.");
        } catch (Exception e) {
            //database errors or other exceptions
            model.addAttribute("error", "Failed to create subreddit: " + e.getMessage());
            return "create_subreddit";
        }

        //list subs
        return "redirect:/";
    }
    

}
