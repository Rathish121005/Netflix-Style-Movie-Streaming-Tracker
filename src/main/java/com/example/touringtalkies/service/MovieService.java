package com.example.touringtalkies.service;

import com.example.touringtalkies.model.Movie;
import com.example.touringtalkies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository repo;
    public MovieService(MovieRepository repo){ this.repo = repo; }

    public Movie save(Movie m){ return repo.save(m); }
    public List<Movie> findAll(){ return repo.findAll(); }
    public Optional<Movie> findById(Long id){ return repo.findById(id); }
    public void deleteById(Long id){ repo.deleteById(id); }
    public List<Movie> search(String q){ return repo.findByTitleContainingIgnoreCase(q); }
}
