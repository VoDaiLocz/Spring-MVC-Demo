package com.demo.socialmedia.dao;

import com.demo.socialmedia.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO truy xuất dữ liệu bảng posts bằng JdbcTemplate.
 */
@Repository
public class PostDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper chuyển ResultSet thành đối tượng Post (có JOIN tên tác giả)
    private final RowMapper<Post> rowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setBody(rs.getString("body"));
        post.setUserId(rs.getInt("user_id"));
        post.setStatus(rs.getString("status"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setAuthorName(rs.getString("username"));
        return post;
    };

    // Lấy tất cả bài viết (kèm tên tác giả)
    public List<Post> findAll() {
        String sql = "SELECT p.*, u.username FROM posts p "
                   + "JOIN users u ON p.user_id = u.id "
                   + "ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Lấy bài viết theo ID
    public Post findById(int id) {
        String sql = "SELECT p.*, u.username FROM posts p "
                   + "JOIN users u ON p.user_id = u.id "
                   + "WHERE p.id = ?";
        List<Post> list = jdbcTemplate.query(sql, rowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    // Lấy bài viết theo user_id
    public List<Post> findByUserId(int userId) {
        String sql = "SELECT p.*, u.username FROM posts p "
                   + "JOIN users u ON p.user_id = u.id "
                   + "WHERE p.user_id = ? ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    // Lấy bài viết từ những người mà userId đang follow (Newsfeed)
    public List<Post> findFeedByUserId(int userId) {
        String sql = "SELECT p.*, u.username FROM posts p "
                   + "JOIN users u ON p.user_id = u.id "
                   + "WHERE p.user_id IN ("
                   + "  SELECT followed_user_id FROM follows WHERE following_user_id = ?"
                   + ") OR p.user_id = ? "
                   + "ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, userId, userId);
    }

    // Thêm bài viết mới
    public int insert(Post post) {
        String sql = "INSERT INTO posts (title, body, user_id, status) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, post.getTitle(), post.getBody(),
                post.getUserId(), post.getStatus());
    }

    // Cập nhật bài viết
    public int update(Post post) {
        String sql = "UPDATE posts SET title = ?, body = ?, status = ? WHERE id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, post.getTitle(), post.getBody(),
                post.getStatus(), post.getId(), post.getUserId());
    }

    // Xóa bài viết
    public int delete(int id, int userId) {
        String sql = "DELETE FROM posts WHERE id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, id, userId);
    }
}
