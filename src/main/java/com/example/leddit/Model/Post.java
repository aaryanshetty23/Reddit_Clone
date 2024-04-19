package com.example.leddit.Model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import java.util.Base64;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000) //length adjusted for content
    private String content;

    @ManyToOne
    @JoinColumn(name = "subreddit_id", nullable = false)
    private Subreddit subreddit;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "image_data")
    private byte[] imageData;


    public Post() {
        // Default constructor
    }

    public Post(String title, String content, Subreddit subreddit, User author, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.subreddit = subreddit;
        this.author = author;
        this.createdDate = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdDate = createdAt;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImageDataAsBase64() {
        if (imageData != null) {
            return Base64.getEncoder().encodeToString(imageData);
        }
        return null;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
