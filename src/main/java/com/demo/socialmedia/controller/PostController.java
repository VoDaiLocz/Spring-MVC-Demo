package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Post;
import com.demo.socialmedia.service.PostService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller xử lý CRUD Bài viết.
 *
 * Các endpoint:
 *   GET    /api/posts           — Lấy tất cả bài viết
 *   GET    /api/posts/feed      — Newsfeed của user đang đăng nhập
 *   GET    /api/posts/user/{userId} — Bài viết của 1 user
 *   GET    /api/posts/{id}      — Chi tiết 1 bài viết
 *   POST   /api/posts           — Tạo bài viết
 *   PUT    /api/posts/{id}      — Cập nhật bài viết
 *   DELETE /api/posts/{id}      — Xóa bài viết
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // === LẤY TẤT CẢ BÀI VIẾT ===
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // === NEWSFEED (bài viết của mình + người mình follow) ===
    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(HttpSession session) {
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }
        return ResponseEntity.ok(postService.getFeedByUserId(userId));
    }

    // === BÀI VIẾT CỦA 1 USER ===
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable int userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }

    // === CHI TIẾT 1 BÀI VIẾT ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    // === TẠO BÀI VIẾT MỚI ===
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Post post, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }

        if (post.getTitle() == null || post.getTitle().isBlank()) {
            response.put("success", false);
            response.put("message", "Tiêu đề không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        if (post.getBody() == null || post.getBody().isBlank()) {
            response.put("success", false);
            response.put("message", "Nội dung không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        if (post.getStatus() == null || post.getStatus().isBlank()) {
            post.setStatus("PUBLISHED");
        }

        post.setUserId(userId);
        boolean ok = postService.createPost(post);
        response.put("success", ok);
        response.put("message", ok ? "Tạo bài viết thành công" : "Tạo bài viết thất bại");
        return ResponseEntity.ok(response);
    }

    // === CẬP NHẬT BÀI VIẾT ===
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(
            @PathVariable int id, @RequestBody Post post, HttpSession session) {
        post.setId(id);
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }

        if (post.getTitle() == null || post.getTitle().isBlank()) {
            response.put("success", false);
            response.put("message", "Tiêu đề không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        if (post.getBody() == null || post.getBody().isBlank()) {
            response.put("success", false);
            response.put("message", "Nội dung không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        if (post.getStatus() == null || post.getStatus().isBlank()) {
            post.setStatus("PUBLISHED");
        }

        post.setUserId(userId);
        boolean ok = postService.updatePost(post);
        response.put("success", ok);
        response.put("message", ok ? "Cập nhật thành công" : "Cập nhật thất bại");
        return ResponseEntity.ok(response);
    }

    // === XÓA BÀI VIẾT ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable int id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }

        boolean ok = postService.deletePost(id, userId);
        response.put("success", ok);
        response.put("message", ok ? "Xóa thành công" : "Xóa thất bại");
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(401).body(response);
    }
}
