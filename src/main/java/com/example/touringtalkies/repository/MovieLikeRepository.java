package com.example.touringtalkies.repository;

import com.example.touringtalkies.model.MovieLike;
import com.example.touringtalkies.model.Movie;
import com.example.touringtalkies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    long countByMovie(Movie movie);
    Optional<MovieLike> findByUserAndMovie(User user, Movie movie);
    List<MovieLike> findByUser(User user);
}
