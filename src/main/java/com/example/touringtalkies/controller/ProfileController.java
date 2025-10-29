package com.example.touringtalkies.controller;

import com.example.touringtalkies.model.User;
import com.example.touringtalkies.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @Value("${app.profile.dir}") // Correct property for profile pictures
    private String profileDir;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "profile";
    }

    @PostMapping("/update")
    public String update(User formUser,
                         @RequestParam(required = false) MultipartFile picture,
                         HttpSession session) throws IOException {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        u.setFullName(formUser.getFullName());
        u.setEmail(formUser.getEmail());

        // Handle profile picture upload
        if (picture != null && !picture.isEmpty()) {
            File dir = new File(profileDir);
            if (!dir.exists()) dir.mkdirs();

            String stored = UUID.randomUUID() + "_" + picture.getOriginalFilename();
            picture.transferTo(new File(dir, stored));
            u.setProfilePicture(stored);
        }

        userService.save(u);
        session.setAttribute("user", u);
        return "redirect:/profile";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String plan, HttpSession session) {

        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        switch (plan.toUpperCase()) {
            case "FREE" -> u.setSubscription(com.example.touringtalkies.model.SubscriptionType.FREE);
            case "BASIC" -> u.setSubscription(com.example.touringtalkies.model.SubscriptionType.BASIC);
            case "PREMIUM" -> u.setSubscription(com.example.touringtalkies.model.SubscriptionType.PREMIUM);
        }

        userService.save(u);
        session.setAttribute("user", u);
        return "redirect:/";
    }
}
