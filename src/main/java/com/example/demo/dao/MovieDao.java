package com.example.demo.dao;



import com.example.demo.model.Movie;

import java.util.List;

public interface MovieDao {

    void saveMovie(Movie movie);

    void removeMovieById(Long id);

    void updateMovie(Movie movie);

    List<Movie> getAllMovies();

    Movie getMovie(Long id);

    List<Movie> searchMovies(String title);

}
