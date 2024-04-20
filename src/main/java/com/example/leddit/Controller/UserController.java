package com.example.leddit.Controller;

import com.example.leddit.Model.User;
import com.example.leddit.Model.Comment;
import com.example.leddit.Model.Post;
import com.example.leddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserService postService;

    @Autowired
    private UserService commentService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> handleLogin(@RequestParam String username, @RequestParam String password) {
        boolean isValid = userService.validateLogin(username, password);
        if (isValid) {
            // Successful login, generate token
            String token = userService.generateToken();
            User user = userService.findByUsername(username).orElse(null);

            if (user != null) {
                // Store token in the database
                userService.storeToken(user.getId(), token);

                // Return the token in the response body
                return ResponseEntity.ok(token);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }


    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already in use");
            return "register";
        }

        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "login";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user/{username}")
    public String viewUserPage(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<Post> userPosts = postService.getPostsByUser(user);
            List<Comment> userComments = commentService.getCommentsByUser(user);

            model.addAttribute("user", user);
            model.addAttribute("userPosts", userPosts);
            model.addAttribute("userComments", userComments);

            return "user_page";
        } else {
            return "user_not_found";
        }
    }
    
}