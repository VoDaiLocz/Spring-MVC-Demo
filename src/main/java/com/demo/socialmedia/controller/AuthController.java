package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.User;
import com.demo.socialmedia.service.UserService;
import com.demo.socialmedia.util.SessionAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        return SessionAuth.getCurrentUserId(session) != null ? "redirect:/" : "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        Model model) {
        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "login";
        }

        SessionAuth.signIn(request, user.getId());
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        return SessionAuth.getCurrentUserId(session) != null ? "redirect:/" : "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        User user = userService.register(username, password);
        if (user == null) {
            model.addAttribute("error", "Username đã tồn tại");
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionAuth.signOut(session);
        return "redirect:/login";
    }
}
