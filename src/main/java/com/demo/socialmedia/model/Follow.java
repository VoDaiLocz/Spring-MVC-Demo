package com.demo.socialmedia.model;

import java.sql.Timestamp;

/**
 * Model đại diện cho bảng follows trong CSDL.
 */
public class Follow {
    private int followingUserId;
    private int followedUserId;
    private Timestamp createdAt;

    // Trường bổ sung: tên người dùng (dùng khi hiển thị danh sách)
    private String username;

    public Follow() {}

    public Follow(int followingUserId, int followedUserId, Timestamp createdAt) {
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getFollowingUserId() { return followingUserId; }
    public void setFollowingUserId(int followingUserId) { this.followingUserId = followingUserId; }

    public int getFollowedUserId() { return followedUserId; }
    public void setFollowedUserId(int followedUserId) { this.followedUserId = followedUserId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
