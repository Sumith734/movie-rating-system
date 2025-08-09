package com.review.movie.config;

import com.review.movie.entity.Movie;
import com.review.movie.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        long count = movieRepository.count();
        logger.info("Current movie count in database: {}", count);
        
        if (count == 0) {
            logger.info("No movies found. Loading sample data...");
            loadSampleData();
        } else {
            logger.info("Movies already exist in database. Skipping data loading.");
        }
    }

    private void loadSampleData() {
        try {
            Movie movie1 = new Movie("The Matrix", "A hacker discovers reality is a simulation.", "Sci-Fi", 1999);
            Movie movie2 = new Movie("Inception", "A thief who steals secrets through dream-sharing.", "Sci-Fi", 2010);
            Movie movie3 = new Movie("The Godfather", "Crime family drama in 1940s America.", "Crime", 1972);
            Movie movie4 = new Movie("The Dark Knight", "Batman faces the Joker in Gotham.", "Action", 2008);

            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);
            movieRepository.save(movie4);

            logger.info("Sample data loaded successfully. {} movies added.", 4);
        } catch (Exception e) {
            logger.error("Error loading sample data: ", e);
        }
    }
}
