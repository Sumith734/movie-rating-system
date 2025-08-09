package com.review.movie.controller;

import com.review.movie.entity.Movie;
import com.review.movie.entity.Rating;
import com.review.movie.entity.User;
import com.review.movie.service.MovieService;
import com.review.movie.service.RatingService;
import com.review.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    // Add a new rating
    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody Map<String, Object> ratingRequest) {
        try {
            Long movieId = Long.valueOf(ratingRequest.get("movieId").toString());
            String username = ratingRequest.get("username").toString();
            Integer rating = Integer.valueOf(ratingRequest.get("rating").toString());
            String review = ratingRequest.get("review") != null ? ratingRequest.get("review").toString() : null;

            Optional<Movie> movieOpt = movieService.getMovieById(movieId);
            Optional<User> userOpt = userService.findByUsername(username);

            if (movieOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Movie movie = movieOpt.get();
            User user = userOpt.get();

            // Check if user has already rated this movie
            Optional<Rating> existingRating = ratingService.getRatingByUserAndMovie(user, movie);
            if (existingRating.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); // User already rated this movie
            }

            Rating newRating = new Rating(rating, review, user, movie);
            Rating savedRating = ratingService.addRating(newRating);
            
            return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a rating
    @PutMapping
    public ResponseEntity<Rating> updateRating(@RequestBody Map<String, Object> ratingRequest) {
        try {
            Long movieId = Long.valueOf(ratingRequest.get("movieId").toString());
            String username = ratingRequest.get("username").toString();
            Integer rating = Integer.valueOf(ratingRequest.get("rating").toString());
            String review = ratingRequest.get("review") != null ? ratingRequest.get("review").toString() : null;

            Optional<Movie> movieOpt = movieService.getMovieById(movieId);
            Optional<User> userOpt = userService.findByUsername(username);

            if (movieOpt.isEmpty() || userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Movie movie = movieOpt.get();
            User user = userOpt.get();

            Optional<Rating> existingRatingOpt = ratingService.getRatingByUserAndMovie(user, movie);
            if (existingRatingOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Rating existingRating = existingRatingOpt.get();
            existingRating.setRating(rating);
            existingRating.setReview(review);

            Rating updatedRating = ratingService.addRating(existingRating);
            return new ResponseEntity<>(updatedRating, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all ratings for a specific movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getRatingsByMovie(@PathVariable Long movieId) {
        try {
            Optional<Movie> movieOpt = movieService.getMovieById(movieId);
            if (movieOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Rating> ratings = ratingService.getRatingsByMovie(movieOpt.get());
            if (ratings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all ratings by a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable String username) {
        try {
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Rating> ratings = ratingService.getRatingsByUser(userOpt.get());
            if (ratings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get average rating for a specific movie
    @GetMapping("/movie/{movieId}/average")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long movieId) {
        try {
            Optional<Movie> movieOpt = movieService.getMovieById(movieId);
            if (movieOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Movie movie = movieOpt.get();
            double averageRating = ratingService.getAverageRatingForMovie(movie);
            List<Rating> ratings = ratingService.getRatingsByMovie(movie);

            Map<String, Object> response = new HashMap<>();
            response.put("movieId", movieId);
            response.put("movieTitle", movie.getTitle());
            response.put("averageRating", averageRating);
            response.put("totalRatings", ratings.size());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a specific rating by user and movie
    @GetMapping("/user/{username}/movie/{movieId}")
    public ResponseEntity<Rating> getRatingByUserAndMovie(@PathVariable String username, @PathVariable Long movieId) {
        try {
            Optional<Movie> movieOpt = movieService.getMovieById(movieId);
            Optional<User> userOpt = userService.findByUsername(username);

            if (movieOpt.isEmpty() || userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<Rating> ratingOpt = ratingService.getRatingByUserAndMovie(userOpt.get(), movieOpt.get());
            if (ratingOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(ratingOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
