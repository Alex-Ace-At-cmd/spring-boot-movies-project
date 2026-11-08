package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "movies")

public class Movie {

    public Movie(){}

    public Movie(String name, Long year, String director) {
        this.name = name;
        this.year = year;
        this.director = director;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Fill in the input field")
    private String name;

    @Column(name = "year")
    @NotNull(message = "Fill in the input field")
    @Min(value = 1800, message = "Invalid value")
    @Max(value = 2050, message = "Invalid value")
    private Long year;

    @Column(name = "director")
    @NotBlank(message = "Fill in the input field")
    @Pattern(regexp = "^[A-Za-zА-Яа-я .'-]+$", message = "Incorrect director name")
    private String director;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getYear() {
        return year;
    }

    public String getDirector() {return director;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public void setDirector(String director) {this.director = director;}
}
