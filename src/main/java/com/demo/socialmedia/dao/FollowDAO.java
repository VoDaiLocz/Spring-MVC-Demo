package com.demo.socialmedia.dao;

import com.demo.socialmedia.model.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO truy xuất dữ liệu bảng follows bằng JdbcTemplate.
 */
@Repository
public class FollowDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Follow: userId follow targetId
    public int follow(int userId, int targetId) {
        String sql = "INSERT INTO follows (following_user_id, followed_user_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, targetId);
    }

    // Unfollow
    public int unfollow(int userId, int targetId) {
        String sql = "DELETE FROM follows WHERE following_user_id = ? AND followed_user_id = ?";
        return jdbcTemplate.update(sql, userId, targetId);
    }

    // Kiểm tra đã follow chưa
    public boolean isFollowing(int userId, int targetId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE following_user_id = ? AND followed_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, targetId);
        return count != null && count > 0;
    }

    // Danh sách người mà userId đang follow
    public List<Follow> getFollowing(int userId) {
        String sql = "SELECT f.*, u.username FROM follows f "
                   + "JOIN users u ON f.followed_user_id = u.id "
                   + "WHERE f.following_user_id = ?";
        return jdbcTemplate.query(sql, followRowMapper(), userId);
    }

    // Danh sách người follow userId
    public List<Follow> getFollowers(int userId) {
        String sql = "SELECT f.*, u.username FROM follows f "
                   + "JOIN users u ON f.following_user_id = u.id "
                   + "WHERE f.followed_user_id = ?";
        return jdbcTemplate.query(sql, followRowMapper(), userId);
    }

    // Đếm số following
    public int countFollowing(int userId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE following_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    // Đếm số followers
    public int countFollowers(int userId) {
        String sql = "SELECT COUNT(*) FROM follows WHERE followed_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    private RowMapper<Follow> followRowMapper() {
        return (rs, rowNum) -> {
            Follow f = new Follow();
            f.setFollowingUserId(rs.getInt("following_user_id"));
            f.setFollowedUserId(rs.getInt("followed_user_id"));
            f.setCreatedAt(rs.getTimestamp("created_at"));
            f.setUsername(rs.getString("username"));
            return f;
        };
    }
}
