package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Follow;
import com.demo.socialmedia.service.FollowService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller xử lý Follow / Unfollow.
 *
 * Các endpoint:
 *   POST   /api/follows              — Follow
 *   DELETE /api/follows              — Unfollow
 *   GET    /api/follows/check        — Kiểm tra đã follow chưa
 *   GET    /api/follows/following/{userId} — Danh sách đang follow
 *   GET    /api/follows/followers/{userId} — Danh sách followers
 *   GET    /api/follows/count/{userId}     — Đếm following + followers
 */
@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    // === FOLLOW ===
    @PostMapping
    public ResponseEntity<Map<String, Object>> follow(
            @RequestBody Map<String, Integer> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        Integer targetId = body.get("targetId");
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }
        if (targetId == null) {
            response.put("success", false);
            response.put("message", "Thiếu targetId");
            return ResponseEntity.badRequest().body(response);
        }

        boolean ok = followService.follow(userId, targetId);
        response.put("success", ok);
        response.put("message", ok ? "Follow thành công" : "Không thể follow (đã follow hoặc trùng ID)");
        return ResponseEntity.ok(response);
    }

    // === UNFOLLOW ===
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> unfollow(
            @RequestParam int targetId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }

        boolean ok = followService.unfollow(userId, targetId);
        response.put("success", ok);
        response.put("message", ok ? "Unfollow thành công" : "Unfollow thất bại");
        return ResponseEntity.ok(response);
    }

    // === KIỂM TRA ĐÃ FOLLOW CHƯA ===
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFollow(
            @RequestParam int targetId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return unauthorized("Chưa đăng nhập");
        }

        response.put("isFollowing", followService.isFollowing(userId, targetId));
        return ResponseEntity.ok(response);
    }

    // === DANH SÁCH ĐANG FOLLOW ===
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<Follow>> getFollowing(@PathVariable int userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }

    // === DANH SÁCH FOLLOWERS ===
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable int userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    // === ĐẾM FOLLOWING + FOLLOWERS ===
    @GetMapping("/count/{userId}")
    public ResponseEntity<Map<String, Integer>> getCount(@PathVariable int userId) {
        Map<String, Integer> response = new HashMap<>();
        response.put("following", followService.countFollowing(userId));
        response.put("followers", followService.countFollowers(userId));
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(401).body(response);
    }
}
