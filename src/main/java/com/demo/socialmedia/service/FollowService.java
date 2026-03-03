package com.demo.socialmedia.service;

import com.demo.socialmedia.dao.FollowDAO;
import com.demo.socialmedia.model.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý nghiệp vụ liên quan đến Follow.
 */
@Service
public class FollowService {

    @Autowired
    private FollowDAO followDAO;

    // Follow
    public boolean follow(int userId, int targetId) {
        if (userId == targetId) return false; // Không tự follow mình
        if (followDAO.isFollowing(userId, targetId)) return false; // Đã follow rồi
        return followDAO.follow(userId, targetId) > 0;
    }

    // Unfollow
    public boolean unfollow(int userId, int targetId) {
        return followDAO.unfollow(userId, targetId) > 0;
    }

    // Kiểm tra đã follow chưa
    public boolean isFollowing(int userId, int targetId) {
        return followDAO.isFollowing(userId, targetId);
    }

    // Danh sách đang follow
    public List<Follow> getFollowing(int userId) {
        return followDAO.getFollowing(userId);
    }

    // Danh sách followers
    public List<Follow> getFollowers(int userId) {
        return followDAO.getFollowers(userId);
    }

    // Đếm following
    public int countFollowing(int userId) {
        return followDAO.countFollowing(userId);
    }

    // Đếm followers
    public int countFollowers(int userId) {
        return followDAO.countFollowers(userId);
    }
}
