package com.example.touringtalkies.controller;

import com.example.touringtalkies.model.Movie;
import com.example.touringtalkies.model.User;
import com.example.touringtalkies.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;

    @Value("${app.video.dir}")
    private String videoDir;

    @Value("${app.thumbnail.dir}")
    private String thumbnailDir;

    public AdminController(MovieService movieService){
        this.movieService = movieService;
    }

    // Admin dashboard
    @GetMapping({"","/"})
    public String adminHome(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user==null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        model.addAttribute("movies", movieService.findAll());
        return "admin/home";
    }

    // Add movie form
    @GetMapping("/add")
    public String addForm(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null || !"ADMIN".equals(user.getRole())) return "redirect:/login";
        return "admin/add";
    }

    // Add movie POST
    @PostMapping("/add")
    public String doAdd(@ModelAttribute Movie movie,
                        @RequestParam("videoFile") MultipartFile videoFile,
                        @RequestParam("thumbnailFile") MultipartFile thumbnailFile) throws IOException {

        // Video file handling
        if (videoFile != null && !videoFile.isEmpty()) {
            String videoName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            File videoDirFile = new File(videoDir);
            if (!videoDirFile.exists()) videoDirFile.mkdirs();
            videoFile.transferTo(new File(videoDirFile, videoName));
            movie.setFilename(videoName);
        }

        // Thumbnail file handling
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();
            File thumbDirFile = new File(thumbnailDir);
            if (!thumbDirFile.exists()) thumbDirFile.mkdirs();
            thumbnailFile.transferTo(new File(thumbDirFile, thumbName));
            movie.setThumbnail(thumbName);
        }

        movieService.save(movie);
        return "redirect:/admin/";
    }

    // Edit movie form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Optional<Movie> movieOpt = movieService.findById(id);
        if(movieOpt.isEmpty()) return "redirect:/admin/";
        model.addAttribute("movie", movieOpt.get());
        return "admin/edit";
    }

    // Edit movie POST
    @PostMapping("/edit")
    public String doEdit(
            @ModelAttribute Movie movie,
            @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) throws IOException {

        // Handle video file only if provided
        if (videoFile != null && !videoFile.isEmpty()) {
            String videoName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
            File videoDirFile = new File(videoDir);
            if (!videoDirFile.exists()) videoDirFile.mkdirs();
            videoFile.transferTo(new File(videoDir, videoName));
            movie.setFilename(videoName);
        }

        // Handle thumbnail only if provided
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();
            File thumbDirFile = new File(thumbnailDir);
            if (!thumbDirFile.exists()) thumbDirFile.mkdirs();
            thumbnailFile.transferTo(new File(thumbnailDir, thumbName));
            movie.setThumbnail(thumbName);
        }

        movieService.save(movie);
        return "redirect:/admin/";
    }



    // Delete movie safely
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        try {
            movieService.deleteById(id); // Should cascade delete related items
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            // Optionally, you can show an error page
        }
        return "redirect:/admin/";
    }
}
