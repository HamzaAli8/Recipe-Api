package com.example.Recipe.Project.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @NotNull
    private String username;

    private String description;

    @NotNull
    private Integer rating;

    public void setRating(int rating){

        if(rating <= 0 || rating > 10){

            throw new IllegalStateException("Rating must be between 0 and 10.");

        }
        this.rating = rating;

    }
}
