package com.example.leddit.Controller;

import com.example.leddit.Model.User;
import com.example.leddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Return the Thymeleaf view name (login.html)
    }

    // Handle login
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        boolean isValid = userService.validateLogin(username, password);
        if (isValid) {
            // Successful login, generate token
            String token = userService.generateToken(); // Implement this method to generate a token
            User user = userService.findByUsername(username).orElse(null);
    
            if (user != null) {
                // Store token in the database
                userService.storeToken(user.getId(), token);
    
                System.out.println(token);

                // Store token in session
                session.setAttribute("token", token);
    
                // Redirect to home page
                return "redirect:/";
            }
        }
        // Invalid login, return to login page with error message
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    // Show registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // Return the Thymeleaf view name (register.html)
    }

    // Handle registration
    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user, Model model) {
        // Check if the username or email is already taken
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already in use");
            return "register";
        }

        // Register the user
        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "login";
    }

    // Handle logout
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        // Invalidate the session to log out the user
        session.invalidate();
        return "redirect:/login";  // Redirect to the login page
    }
}