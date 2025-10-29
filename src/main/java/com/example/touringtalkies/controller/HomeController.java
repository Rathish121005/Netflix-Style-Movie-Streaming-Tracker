package com.example.touringtalkies.controller;

import com.example.touringtalkies.model.Movie;
import com.example.touringtalkies.model.User;
import com.example.touringtalkies.service.MovieService;
import com.example.touringtalkies.repository.MovieLikeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final MovieService movieService;
    private final MovieLikeRepository likeRepo;
    public HomeController(MovieService movieService, MovieLikeRepository likeRepo){
        this.movieService = movieService; this.likeRepo = likeRepo;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session){
        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }
}
