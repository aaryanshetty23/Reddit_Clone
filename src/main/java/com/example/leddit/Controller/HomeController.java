package com.example.leddit.Controller;

import com.example.leddit.Model.Subreddit;
import com.example.leddit.Service.SubredditService;
import com.example.leddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

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
        return "home";  // Return the Thymeleaf view name (home.html)
    }

    @PostMapping("/subreddit/create")
    public String createSubreddit(@ModelAttribute Subreddit subreddit, HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null || !userService.isTokenValid(token)) {
            model.addAttribute("error", "You must be logged in to create a subreddit.");
            return "home";
        }

        // Proceed with creating the subreddit
        subredditService.createSubreddit(subreddit);
        return "redirect:/";
    }


}
