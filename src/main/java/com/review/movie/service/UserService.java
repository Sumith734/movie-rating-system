package com.review.movie.service;


import com.review.movie.entity.User;

import java.util.Optional;

public interface UserService {

    User register(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
