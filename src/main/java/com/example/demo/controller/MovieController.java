package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class MovieController {

    private final MovieService movieService;
    private final UserDetailsServiceImpl userDetailsService;


    public MovieController(MovieService movieService, UserDetailsServiceImpl userDetailsService) {
        this.movieService = movieService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String movies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("newMovie", new Movie());
        return "movies";
    }

    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.removeMovieById(id);
        return "redirect:/admin";
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
        return "redirect:/admin";
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
        return "redirect:/admin";
    }

    @GetMapping("/user{id}")
    public String userPage(Model model, Principal principal) {
        User user = userDetailsService.findUserByName(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users",userDetailsService.findAllUsers());
        model.addAttribute("newUser", new User());
        return "users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDetailsService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("newUser", new User());
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("newUser") User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        userDetailsService.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
        return "redirect:/admin/users";
    }

    @GetMapping("/users/editor/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userDetailsService.findUserById(id);
        model.addAttribute("editUser", user);
        return "editUser";
    }

    @PostMapping("/users/edit")
    public String updateUser(@Valid @ModelAttribute("editUser") User user,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editUser";
        }
        userDetailsService.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );
        return "redirect:/admin/users";
    }
}


