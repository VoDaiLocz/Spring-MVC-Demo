package com.demo.socialmedia.service;

import com.demo.socialmedia.dao.UserDAO;
import com.demo.socialmedia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý nghiệp vụ liên quan đến User.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    // Đăng ký tài khoản
    public User register(String username, String password) {
        // Kiểm tra username đã tồn tại chưa
        if (userDAO.findByUsername(username) != null) {
            return null; // Username đã tồn tại
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Demo đơn giản, không mã hóa
        user.setRole("USER");
        userDAO.insert(user);
        return userDAO.findByUsername(username);
    }

    // Đăng nhập
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Sai username hoặc password
    }

    // Lấy thông tin user theo ID
    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}
