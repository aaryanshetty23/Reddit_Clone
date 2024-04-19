package com.example.leddit.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.time.LocalDateTime;
import com.example.leddit.Model.Post;
import com.example.leddit.Model.Comment;
import com.example.leddit.Model.Subreddit;
import com.example.leddit.Service.SubredditService;
import com.example.leddit.Service.PostService;
import com.example.leddit.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import com.example.leddit.Service.CommentService;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final SubredditService subredditService;
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public PostController(SubredditService subredditService, CommentService commentService, PostService postService, UserService userService) {
        this.subredditService = subredditService;
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/subreddit/{id1}/post/{id2}")
    public String viewPost(@PathVariable Long id1, @PathVariable Long id2, Model model) {
        Subreddit subreddit = subredditService.getSubredditById(id1);
        model.addAttribute("subreddit", subreddit);
        
        // Retrieve the post by ID
        Post post = postService.getPostById(id2);
        model.addAttribute("post", post);

        // Encode imageData to Base64
        String imageDataAsBase64 = post.getImageDataAsBase64();
        model.addAttribute("imageDataAsBase64", imageDataAsBase64);
        
        // Retrieve comments for the post
        List<Comment> comments = commentService.getCommentsByPostId(id2);
        model.addAttribute("comments", comments);
        
        return "view_post";
    }


    @GetMapping("/subreddit/{id}/post/create")
    public String showCreatePostForm(@PathVariable Long id, Model model) {
        Subreddit subreddit = subredditService.getSubredditById(id);

        if (subreddit == null) {
            return "redirect:/error";
        }

        model.addAttribute("subredditId", id);
        model.addAttribute("subredditList", subredditService.getAllSubreddits());
        return "create_post";
    }

    @PostMapping("/subreddit/{subredditId}/post")
    public String createPost(@PathVariable("subredditId") Long subredditId, @RequestHeader("Authorization") String authorizationHeader, 
    @RequestParam String title, @RequestParam String content, @RequestParam("image") MultipartFile image, HttpServletRequest request) {
        // Retrieve the token from the request header
        String token = authorizationHeader.replace("Bearer ", "");

        if (token == null || !userService.isTokenValid(token)) {
            System.out.println("User is Anonymous or has expired login");
        }

        // Validate the token and retrieve the user ID
        Long userId = userService.getUserIdFromToken(token);
        
        // userId won't be null since getUserIdFromToken will return 69 for anons
        if (userId != null) {
            postService.createPost(title, content, subredditId, userId, image);
            return "redirect:/subreddit/" + subredditId;
        } else {
            //this part's never used
            postService.createPost(title, content, subredditId, 69L, image);
            return "redirect:/subreddit/" + subredditId;
        }
        
    }

    @PostMapping("/post/{postId}/comment")
    public String createComment(@PathVariable("postId") Long postId,
                                @RequestParam("content") String content,
                                @RequestHeader("Authorization") String authorizationHeader) {
        // Retrieve the token from the request header
        String token = authorizationHeader.replace("Bearer ", "");

        System.out.println(authorizationHeader);

        // Validate the token and retrieve the user ID
        Long userId = userService.getUserIdFromToken(token);

        System.out.println(token);
        
        // If the token is invalid, then it's anon
        if (userId == null) {
            userId = 69L;
        }

        // Create the comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(postService.getPostById(postId));
        comment.setAuthor(userService.getUserById(userId));
        comment.setCreatedDate(LocalDateTime.now());

        // Save the new comment to the database
        commentService.createComment(comment);

        // Redirect back to the post page
        return "redirect:/subreddit/" + comment.getPost().getSubreddit().getId() + "/post/" + postId;
    }

}
