package com.example.touringtalkies.controller;

import com.example.touringtalkies.model.User;
import com.example.touringtalkies.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){ this.userService=userService; }

    @GetMapping("/login")
    public String loginPage(){ return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model){
        var opt = userService.findByUsername(username);
        if(opt.isPresent()){
            User u = opt.get();
            if(u.getPassword().equals(password)){
                session.setAttribute("user", u);
                return "redirect:/";
            }
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(){ return "register"; }

    @PostMapping("/register")
    public String doRegister(User user, Model model){
        // basic checks
        if(userService.findByUsername(user.getUsername()).isPresent()){
            model.addAttribute("error", "Username exists");
            return "register";
        }
        user.setRole("USER");
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
