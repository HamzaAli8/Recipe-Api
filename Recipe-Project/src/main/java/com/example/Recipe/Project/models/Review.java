package com.example.Recipe.Project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private CustomUserDetails user;


    @Column(name = "username")
    public String getAuthor() {
        return user.getUsername();
    }

    @NotNull
    private Integer rating;

    public void setRating(int rating){

        if(rating <= 0 || rating > 10){

            throw new IllegalStateException("Rating must be between 0 and 10.");

        }
        this.rating = rating;

    }
}
