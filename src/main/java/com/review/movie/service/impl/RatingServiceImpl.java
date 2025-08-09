package com.review.movie.service.impl;

import com.review.movie.entity.Movie;
import com.review.movie.entity.Rating;
import com.review.movie.entity.User;
import com.review.movie.repository.RatingRepository;
import com.review.movie.repository.MovieRepository;
import com.review.movie.repository.UserRepository;
import com.review.movie.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingServiceImpl(
            RatingRepository ratingRepository,
            MovieRepository movieRepository,
            UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatingsByMovie(Movie movie) {
        return ratingRepository.findByMovie(movie);
    }

    @Override
    public List<Rating> getRatingsByUser(User user) {
        return ratingRepository.findByUser(user);
    }

    @Override
    public Optional<Rating> getRatingByUserAndMovie(User user, Movie movie) {
        return ratingRepository.findByUserAndMovie(user, movie);
    }

    @Override
    public double getAverageRatingForMovie(Movie movie) {
        List<Rating> ratings = ratingRepository.findByMovie(movie);
        if (ratings.isEmpty()) return 0;
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0);
    }
}
