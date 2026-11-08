package com.example.demo.dao;

import com.example.demo.model.Movie;
import jakarta.persistence.*;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDaoImpl implements MovieDao {




    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveMovie(Movie movie) {
        entityManager.persist(movie);
    }

    @Override
    public void removeMovieById(Long id) {

        Movie movie = entityManager.find(Movie.class, id);
        if (movie != null) {
            entityManager.remove(movie);
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        entityManager.merge(movie);
    }

    @Override
    public List<Movie> getAllMovies() {
        return entityManager.createQuery("from Movie", Movie.class).getResultList();
    }

    @Override
    public Movie getMovie(Long id) {
        return entityManager.find(Movie.class, id);
    }

    public List<Movie> searchMovies(String name) {
        return entityManager.createQuery(
                "from Movie m where lower(m.name) like lower(:name)",
                Movie.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
}
