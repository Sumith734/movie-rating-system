CREATE DATABASE  IF NOT EXISTS `movie_rating`;
USE `movie_rating`;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `movies`;
DROP TABLE IF EXISTS `ratings`;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    genre VARCHAR(50),
    release_year INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    review TEXT,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



INSERT INTO users (username, email, password, role, created_at)
VALUES 
  ('tony_stark', 'tony@avengers.com', 'hashed_password_ironman', 'USER', NOW()),
  ('steve_rogers', 'steve@avengers.com', 'hashed_password_captain', 'USER', NOW()),
  ('bruce_banner', 'bruce@avengers.com', 'hashed_password_hulk', 'USER', NOW()),
  ('natasha_romanoff', 'natasha@avengers.com', 'hashed_password_blackwidow', 'USER', NOW()),
  ('thor_odinson', 'thor@avengers.com', 'hashed_password_thor', 'USER', NOW()),
  ('nick_fury', 'nick@shield.com', 'hashed_password_director', 'ADMIN', NOW());

INSERT INTO movies (title, description, genre, release_year, created_at)
VALUES
  ('The Matrix', 'A hacker discovers reality is a simulation.', 'Sci-Fi', 1999, NOW()),
  ('Inception', 'A thief who steals secrets through dream-sharing.', 'Sci-Fi', 2010, NOW()),
  ('The Godfather', 'Crime family drama in 1940s America.', 'Crime', 1972, NOW()),
  ('The Dark Knight', 'Batman faces the Joker in Gotham.', 'Action', 2008, NOW());


INSERT INTO ratings (rating, review, user_id, movie_id, created_at)
VALUES
  (5, 'Mind-blowing movie!', 1, 1, NOW()),
  (4, 'Great concept and action scenes.', 2, 1, NOW()),
  (5, 'Nolan is a genius.', 2, 2, NOW()),
  (3, 'Too confusing at times.', 1, 2, NOW()),
  (5, 'Classic crime drama.', 3, 3, NOW()),
  (4, 'Great performance by Heath Ledger.', 1, 4, NOW()),
  (5, 'My favorite superhero movie.', 2, 4, NOW());

