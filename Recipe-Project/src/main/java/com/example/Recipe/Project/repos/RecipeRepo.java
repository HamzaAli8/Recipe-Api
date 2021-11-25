package com.example.Recipe.Project.repos;

import com.example.Recipe.Project.models.Recipe;
import com.example.Recipe.Project.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

    ArrayList<Recipe> findByNameContaining (String name);


    ArrayList<Recipe> findByUsername (String username);

    ArrayList<Recipe> findByDifficultyRatingLessThanEqual (Integer rating);

    ArrayList<Recipe> findByAverageReviewRatingGreaterThanEqual(Double rating);

    
}
