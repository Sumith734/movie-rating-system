package com.review.movie.controller;

import com.review.movie.entity.Movie;
import com.review.movie.service.MovieService;
import com.review.movie.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    // Add a new movie
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.addMovie(movie);
            return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieData = movieService.getMovieById(id);
        
        if (movieData.isPresent()) {
            return new ResponseEntity<>(movieData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get movie by ID with average rating
    @GetMapping("/{id}/with-rating")
    public ResponseEntity<Map<String, Object>> getMovieWithRating(@PathVariable Long id) {
        Optional<Movie> movieData = movieService.getMovieById(id);
        
        if (movieData.isPresent()) {
            Movie movie = movieData.get();
            double averageRating = ratingService.getAverageRatingForMovie(movie);
            
            Map<String, Object> response = new HashMap<>();
            response.put("movie", movie);
            response.put("averageRating", averageRating);
            response.put("totalRatings", movie.getRatings().size());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Search movies by genre
    @GetMapping("/search/genre")
    public ResponseEntity<List<Movie>> searchMoviesByGenre(@RequestParam String genre) {
        try {
            List<Movie> movies = movieService.searchByGenre(genre);
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search movies by title
    @GetMapping("/search/title")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String keyword) {
        try {
            List<Movie> movies = movieService.searchByTitle(keyword);
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update movie
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Optional<Movie> movieData = movieService.getMovieById(id);
        
        if (movieData.isPresent()) {
            Movie existingMovie = movieData.get();
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setDescription(movie.getDescription());
            existingMovie.setGenre(movie.getGenre());
            existingMovie.setReleaseYear(movie.getReleaseYear());
            
            Movie updatedMovie = movieService.addMovie(existingMovie);
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete movie
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable Long id) {
        try {
            Optional<Movie> movieData = movieService.getMovieById(id);
            if (movieData.isPresent()) {
                movieService.deleteMovie(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all movies with their average ratings
    @GetMapping("/with-ratings")
    public ResponseEntity<List<Map<String, Object>>> getAllMoviesWithRatings() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            if (movies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            List<Map<String, Object>> moviesWithRatings = movies.stream()
                .map(movie -> {
                    Map<String, Object> movieData = new HashMap<>();
                    movieData.put("movie", movie);
                    movieData.put("averageRating", ratingService.getAverageRatingForMovie(movie));
                    movieData.put("totalRatings", movie.getRatings().size());
                    return movieData;
                })
                .toList();
            
            return new ResponseEntity<>(moviesWithRatings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
