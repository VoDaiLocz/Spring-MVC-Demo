package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.User;
import com.demo.socialmedia.service.UserService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller xử lý Đăng ký / Đăng nhập.
 *
 * Các endpoint:
 *   POST /api/users/register  — Đăng ký tài khoản
 *   POST /api/users/login     — Đăng nhập
 *   GET  /api/users           — Lấy danh sách tất cả user
 *   GET  /api/users/{id}      — Lấy thông tin 1 user
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // === ĐĂNG KÝ ===
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Map<String, Object> response = new HashMap<>();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            response.put("success", false);
            response.put("message", "Username và password không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userService.register(username, password);
        if (user == null) {
            response.put("success", false);
            response.put("message", "Username đã tồn tại");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", true);
        response.put("message", "Đăng ký thành công");
        response.put("user", userToMap(user));
        return ResponseEntity.ok(response);
    }

    // === ĐĂNG NHẬP ===
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");

        Map<String, Object> response = new HashMap<>();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            response.put("success", false);
            response.put("message", "Username và password không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userService.login(username, password);
        if (user == null) {
            response.put("success", false);
            response.put("message", "Sai username hoặc password");
            return ResponseEntity.status(401).body(response);
        }

        SessionAuth.signIn(request, user.getId());
        response.put("success", true);
        response.put("message", "Đăng nhập thành công");
        response.put("user", userToMap(user));
        return ResponseEntity.ok(response);
    }

    // === USER HIỆN TẠI THEO SESSION ===
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            response.put("success", false);
            response.put("message", "Chưa đăng nhập");
            return ResponseEntity.status(401).body(response);
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            SessionAuth.signOut(session);
            response.put("success", false);
            response.put("message", "Phiên đăng nhập không hợp lệ");
            return ResponseEntity.status(401).body(response);
        }

        response.put("success", true);
        response.put("user", userToMap(user));
        return ResponseEntity.ok(response);
    }

    // === ĐĂNG XUẤT ===
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        SessionAuth.signOut(session);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(response);
    }

    // === LẤY DANH SÁCH USER ===
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> result = users.stream().map(this::userToMap).toList();
        return ResponseEntity.ok(result);
    }

    // === LẤY THÔNG TIN 1 USER ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userToMap(user));
    }

    // Helper: chuyển User thành Map (ẩn password)
    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("role", user.getRole());
        map.put("createdAt", user.getCreatedAt());
        return map;
    }
}
