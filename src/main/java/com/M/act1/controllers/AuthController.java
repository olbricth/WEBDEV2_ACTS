package com.M.act1.controllers;

import com.M.act1.models.User;
import com.M.act1.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // ✅ If already logged in, just redirect to car list
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }

        return "login"; // show login form
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // ✅ Prevent showing register page to logged-in users
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        userService.saveUser(user);
        // ✅ Redirect them to car list (or login page if you prefer)
        return "redirect:/";
    }

    // ✅ Handle GET /logout gracefully (just stay on car list)
    @GetMapping("/logout")
    public String handleManualLogout() {
        return "redirect:/"; // does NOT log out, just stays in car list
    }
}
