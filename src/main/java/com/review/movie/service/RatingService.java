package com.review.movie.service;

import com.review.movie.entity.Movie;
import com.review.movie.entity.Rating;
import com.review.movie.entity.User;

import java.util.List;
import java.util.Optional;

public interface RatingService {

    Rating addRating(Rating rating);

    List<Rating> getRatingsByMovie(Movie movie);

    List<Rating> getRatingsByUser(User user);

    Optional<Rating> getRatingByUserAndMovie(User user, Movie movie);

    double getAverageRatingForMovie(Movie movie);
}

