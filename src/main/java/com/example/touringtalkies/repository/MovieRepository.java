package com.example.touringtalkies.repository;

import com.example.touringtalkies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String q);
    List<Movie> findByGenreContainingIgnoreCase(String genre);
}
