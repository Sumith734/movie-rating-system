# Movie Rating System

A simple REST API for rating and reviewing movies, built with Spring Boot and MySQL.

## What it does

- Add and manage movies
- Users can rate movies (1-5 stars) and write reviews
- Search movies by title or genre
- Calculate average ratings
- Prevent duplicate ratings from same user

## Tech Stack

- **Java 17** + **Spring Boot**
- **MySQL** database
- **Maven** for build management

## Quick Setup

### 1. Prerequisites
- Java 17+
- MySQL running
- Maven (or use included `./mvnw`)

### 2. Database Setup
```sql
CREATE DATABASE movie_rating;
CREATE USER 'movieratings'@'localhost' IDENTIFIED BY 'movieratings';
GRANT ALL PRIVILEGES ON movie_rating.* TO 'movieratings'@'localhost';
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

### 4. Test it works
Visit: `http://localhost:8080/api/movies`

## API Endpoints

### Movies
- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get movie by ID
- `POST /api/movies` - Add new movie
- `PUT /api/movies/{id}` - Update movie
- `DELETE /api/movies/{id}` - Delete movie
- `GET /api/movies/search/genre?genre={genre}` - Search by genre
- `GET /api/movies/search/title?keyword={keyword}` - Search by title

### Ratings
- `GET /api/ratings/movie/{movieId}` - Get all ratings for a movie
- `GET /api/ratings/user/{username}/movie/{movieId}` - Get specific rating
- `POST /api/ratings` - Add new rating
- `PUT /api/ratings` - Update existing rating

### Users
- `GET /api/users` - Get all users
- `POST /api/users` - Register new user

## Example Usage

### Add a Movie
```bash
POST /api/movies
{
    "title": "The Avengers",
    "description": "Superheroes save the world",
    "genre": "Action",
    "releaseYear": 2012
}
```

### Rate a Movie
```bash
POST /api/ratings
{
    "movieId": 1,
    "username": "john_doe",
    "rating": 5,
    "review": "Great movie!"
}
```

## Sample Data

The app automatically loads sample movies and users when you first run it:
- Movies: The Matrix, Inception, The Godfather, The Dark Knight
- Users: Avengers characters for testing

## Configuration

Database settings are in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movie_rating
spring.datasource.username=movieratings
spring.datasource.password=movieratings
```

## Common Errors

- **404 Not Found**: Resource doesn't exist (wrong ID, username, etc.)
- **409 Conflict**: User already rated this movie (use PUT to update)
- **500 Server Error**: Database connection issues

## Author

**Sumith Sunil** - Personal Project

---

*Built with Spring Boot and MySQL* 🎬
