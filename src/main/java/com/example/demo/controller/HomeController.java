package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    UserDetailsServiceImpl userDetailsService;
    public HomeController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user";
    }

    @GetMapping("/user{id}")
    public String userPage(Model model, Principal principal) {
        User user = userDetailsService.findUserByName(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "user";
    }
}
