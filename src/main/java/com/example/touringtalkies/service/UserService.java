package com.example.touringtalkies.service;

import com.example.touringtalkies.model.User;
import com.example.touringtalkies.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }

    public User register(User user){
        // NOTE: password stored as plain text per user request (INSECURE)
        return repo.save(user);
    }

    public Optional<User> findByUsername(String username){
        return repo.findByUsername(username);
    }

    public User save(User u){ return repo.save(u); }
}
