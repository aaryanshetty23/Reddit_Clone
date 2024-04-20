package com.example.leddit.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import com.example.leddit.Model.Comment;
import com.example.leddit.Model.User;
import com.example.leddit.Repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        //might need to check that the comment text is not empty.
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        // Find the existing comment
        Comment existingComment = commentRepository.findById(id).orElse(null);
        if (existingComment != null) {
            // Update the comment properties
            existingComment.setContent(updatedComment.getContent());

            // Save the updated comment
            return commentRepository.save(existingComment);
        }
        return null; // Comment not found
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByUser(User user) {
        return commentRepository.findByAuthor(user);
    }
}
