package com.review.movie.repository;


import com.review.movie.entity.Rating;
import com.review.movie.entity.Movie;
import com.review.movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMovie(Movie movie);

    List<Rating> findByUser(User user);

    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
