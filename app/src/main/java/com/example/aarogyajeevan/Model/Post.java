package com.example.aarogyajeevan.Model;

public class Post {

    private String postId;
    private String postimage;
    private String description;
    private String publisher;

    public Post(String postId, String postimage, String description, String publisher) {
        this.postId = postId;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
    }

    public Post() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
