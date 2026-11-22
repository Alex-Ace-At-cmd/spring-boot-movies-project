package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class AppRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Внедряем репозиторий через конструктор
    public AppRunner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));


        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new User("admin", passwordEncoder.encode("1234qwerty"),
                    new HashSet<>(Set.of(adminRole))));
            System.out.println("Базовая роль админа создана!");

        } if (userRepository.findByUsername("user").isEmpty()) {
            userRepository.save(new User("user", passwordEncoder.encode("1234qwerty"),
                    new HashSet<>(Set.of(userRole))));
            System.out.println("Базовая роль юзера создана!");
        }





    }
}
