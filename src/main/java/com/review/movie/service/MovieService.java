package com.review.movie.service;

import com.review.movie.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Movie addMovie(Movie movie);

    Optional<Movie> getMovieById(Long id);

    List<Movie> getAllMovies();

    List<Movie> searchByGenre(String genre);

    List<Movie> searchByTitle(String keyword);

    void deleteMovie(Long id);
}

