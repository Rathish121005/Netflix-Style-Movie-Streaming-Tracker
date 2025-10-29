package com.example.touringtalkies.controller;

import com.example.touringtalkies.model.*;
import com.example.touringtalkies.repository.*;
import com.example.touringtalkies.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final MovieLikeRepository likeRepo;
    private final WatchlistRepository watchRepo;
    private final RatingRepository ratingRepo;
    private final UserRepository userRepo;
    private final MovieRepository movieRepo;

    public MovieController(MovieService movieService, MovieLikeRepository likeRepo, WatchlistRepository watchRepo, RatingRepository ratingRepo, UserRepository userRepo, MovieRepository movieRepo){
        this.movieService = movieService; this.likeRepo = likeRepo; this.watchRepo = watchRepo; this.ratingRepo = ratingRepo; this.userRepo = userRepo; this.movieRepo = movieRepo;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model, HttpSession session){
        Movie m = movieService.findById(id).orElseThrow();
        model.addAttribute("movie", m);
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "movie/view";
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable Long id, HttpSession session){
        User u = (User) session.getAttribute("user");
        if(u==null) return "redirect:/login";
        Movie m = movieService.findById(id).orElseThrow();
        if(likeRepo.findByUserAndMovie(u,m).isEmpty()){
            MovieLike ml = new MovieLike();
            ml.setMovie(m); ml.setUser(u);
            likeRepo.save(ml);
        }
        return "redirect:/movie/"+id;
    }

    @PostMapping("/watchlist/{id}")
    public String watchlist(@PathVariable Long id, HttpSession session){
        User u = (User) session.getAttribute("user");
        if(u==null) return "redirect:/login";
        Movie m = movieService.findById(id).orElseThrow();
        if(watchRepo.findByUserAndMovieId(u,m.getId()).isEmpty()){
            WatchlistItem w = new WatchlistItem();
            w.setMovie(m); w.setUser(u);
            watchRepo.save(w);
        }
        return "redirect:/movie/"+id;
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<FileSystemResource> stream(@PathVariable Long id, HttpSession session){
        Movie m = movieService.findById(id).orElseThrow();
        User u = (User) session.getAttribute("user");
        // access rules:
        if(m.isFreeContent()){
            // allowed
        } else {
            if(u==null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if(u.getSubscription()==SubscriptionType.FREE) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if(u.getSubscription()==SubscriptionType.BASIC && !m.isFreeContent()){
                // basic users must upgrade for premium-only content
                // We'll treat movies with freeContent=false as paid; admin can set freeContent flag.
                // For simplicity: BASIC cannot watch premium content: redirect to upgrade
                // but here return forbidden
                // PREMIUM can access
                if(u.getSubscription()!=SubscriptionType.PREMIUM) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        File f = new File(System.getProperty("user.dir") + File.separator + "" + m.getFilename());
        // fallback to application property storage
        if(!f.exists()){
            f = new File(System.getProperty("user.dir") + File.separator + "videos" + File.separator + m.getFilename());
        }
        if(!f.exists()) return ResponseEntity.notFound().build();
        FileSystemResource resource = new FileSystemResource(f);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaTypeFactory.getMediaType(m.getFilename()).orElse(MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Content-Disposition", "inline; filename=\""+m.getTitle()+"\"");
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id, HttpSession session){
        // Only allow streaming through the app; download endpoint will stream with attachment,
        // but can be blocked unless user has access. This cannot prevent user saving locally.
        Movie m = movieService.findById(id).orElseThrow();
        User u = (User) session.getAttribute("user");
        if(m.isFreeContent()){
        } else {
            if(u==null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if(u.getSubscription()==SubscriptionType.FREE) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            if(u.getSubscription()==SubscriptionType.BASIC && u.getSubscription()!=SubscriptionType.PREMIUM){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        File f = new File(System.getProperty("user.dir") + File.separator + "videos" + File.separator + m.getFilename());
        if(!f.exists()) return ResponseEntity.notFound().build();
        FileSystemResource resource = new FileSystemResource(f);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaTypeFactory.getMediaType(m.getFilename()).orElse(MediaType.APPLICATION_OCTET_STREAM));
        headers.setContentDisposition(ContentDisposition.attachment().filename(m.getFilename()).build());
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/search")
    public String search(@RequestParam String q, Model model, HttpSession session){
        List<Movie> results = movieService.search(q);
        model.addAttribute("movies", results);
        model.addAttribute("user", session.getAttribute("user"));
        return "home";
    }
}
