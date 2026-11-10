package com.example.demo.controller;

import com.example.demo.service.MovieServiceImpl;
import jakarta.validation.Valid;
import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String movies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("newMovie", new Movie());
        return "movies";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.removeMovieById(id);
        return "redirect:/movies";
    }

    @GetMapping("/new")
    public String newMovie(Model model) {
        model.addAttribute("newMovie", new Movie());
        return "add";
    }

    @PostMapping("/add")
    public String addMovie(@Valid @ModelAttribute("newMovie") Movie movies,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        movieService.saveMovie(movies);
        return "redirect:/movies";
    }


    @GetMapping("/editor/{id}")
    public String editMovie(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.getMovie(id);
        model.addAttribute("editMovie", movie);
        return "edit";
    }

    @PostMapping("/edit")
    public String updateMovie(@Valid @ModelAttribute("editMovie") Movie movies,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        movieService.updateMovie(movies);
        return "redirect:/movies";
    }




}
