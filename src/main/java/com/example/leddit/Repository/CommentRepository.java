package com.example.leddit.Repository;

import com.example.leddit.Model.Comment;
import com.example.leddit.Model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByAuthor(User author);
}
