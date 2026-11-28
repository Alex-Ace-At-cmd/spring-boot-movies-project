package com.example.demo.controller;


import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserForm;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class MovieController {

    private final MovieService movieService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    public MovieController(MovieService movieService, UserDetailsServiceImpl userDetailsService,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.movieService = movieService;
        this.userDetailsService = userDetailsService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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
        model.addAttribute("allRoles", roleRepository.findAll());
        return "addUser";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("newUser") User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        Set<Role> selectedRoles = new HashSet<>(
                roleRepository.findAllById(user.getSelectedRoleIds())
        );

        userDetailsService.registerUserByAdmin(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                selectedRoles
        );

        return "redirect:/admin/users";
    }

    @GetMapping("/users/editor/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        User form = new User();
//        form.setUsername(user.getUsername());
//        form.setPassword(user.getPassword());
//        form.setEmail(user.getEmail());
//
//        form.setRoles(user.getRoles());

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User form = new User();

        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());

        form.setSelectedRoleIds(
                user.getRoles()
                        .stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet())
        );
        model.addAttribute("editUser", form);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "editUser";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@Valid @PathVariable Long id, @ModelAttribute("editUser") User form,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editUser";
        }
        userDetailsService.updateUser(id, form);
        return "redirect:/admin/users";
    }
}


