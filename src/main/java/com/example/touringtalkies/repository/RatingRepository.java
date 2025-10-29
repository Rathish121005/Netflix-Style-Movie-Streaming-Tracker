package com.example.touringtalkies.repository;

import com.example.touringtalkies.model.Rating;
import com.example.touringtalkies.model.User;
import com.example.touringtalkies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
