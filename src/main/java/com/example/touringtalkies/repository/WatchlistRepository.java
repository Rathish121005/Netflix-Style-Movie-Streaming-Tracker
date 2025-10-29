package com.example.touringtalkies.repository;

import com.example.touringtalkies.model.WatchlistItem;
import com.example.touringtalkies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUser(User user);
    Optional<WatchlistItem> findByUserAndMovieId(User user, Long movieId);
}
