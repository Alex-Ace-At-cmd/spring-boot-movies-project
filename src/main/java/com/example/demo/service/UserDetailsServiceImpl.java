package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(new Role("ROLE_USER")));
        return userRepository.save(user);
    }


    public Optional<User> findUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void existUserById(Long id) {
        userRepository.existsById(id);
    }


    @Override
    public UserDetails loadUserByUsername(@NotNull @NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
