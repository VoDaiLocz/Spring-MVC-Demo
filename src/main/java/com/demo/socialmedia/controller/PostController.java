package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Post;
import com.demo.socialmedia.service.PostService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public String createPost(@RequestParam String title,
                             @RequestParam String body,
                             @RequestParam(required = false) String status,
                             HttpSession session) {
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        if (title == null || title.isBlank() || body == null || body.isBlank()) {
            return "redirect:/";
        }

        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setStatus(status == null || status.isBlank() ? "PUBLISHED" : status);
        post.setUserId(userId);
        postService.createPost(post);
        return "redirect:/";
    }
}
