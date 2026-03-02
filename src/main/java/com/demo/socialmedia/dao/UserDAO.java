package com.demo.socialmedia.dao;

import com.demo.socialmedia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO truy xuất dữ liệu bảng users bằng JdbcTemplate.
 */
@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper chuyển ResultSet thành đối tượng User
    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    };

    // Tìm user theo username
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> list = jdbcTemplate.query(sql, rowMapper, username);
        return list.isEmpty() ? null : list.get(0);
    }

    // Tìm user theo ID
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> list = jdbcTemplate.query(sql, rowMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    // Thêm user mới (đăng ký)
    public int insert(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole());
    }

    // Lấy tất cả user
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
