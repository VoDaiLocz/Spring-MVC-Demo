package com.demo.socialmedia.service;

import com.demo.socialmedia.dao.FollowDAO;
import com.demo.socialmedia.dao.UserDAO;
import com.demo.socialmedia.model.Follow;
import com.demo.socialmedia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service xử lý nghiệp vụ liên quan đến Follow.
 */
@Service
public class FollowService {

    @Autowired
    private FollowDAO followDAO;
    @Autowired
    private UserDAO userDAO;

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

    public List<User> getSuggestedUsers(int userId) {
        List<Follow> following = followDAO.getFollowing(userId);
        Set<Integer> followedIds = new HashSet<>();
        for (Follow follow : following) {
            followedIds.add(follow.getFollowedUserId());
        }

        return userDAO.findAll().stream()
                .filter(user -> user.getId() != userId)
                .filter(user -> !followedIds.contains(user.getId()))
                .toList();
    }
}
