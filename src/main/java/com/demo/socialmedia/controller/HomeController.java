package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.User;
import com.demo.socialmedia.service.FollowService;
import com.demo.socialmedia.service.PostService;
import com.demo.socialmedia.service.UserService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Integer userId = SessionAuth.getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getUserById(userId);
        if (currentUser == null) {
            SessionAuth.signOut(session);
            return "redirect:/login";
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", postService.getFeedByUserId(userId));
        model.addAttribute("followingUsers", followService.getFollowing(userId));
        model.addAttribute("followerUsers", followService.getFollowers(userId));
        model.addAttribute("suggestedUsers", followService.getSuggestedUsers(userId));
        return "home";
    }
}
