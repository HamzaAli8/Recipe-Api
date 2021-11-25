package com.example.Recipe.Project.controllers;

import com.example.Recipe.Project.Services.NoSuchRecipeException;
import com.example.Recipe.Project.Services.RecipeService;
import com.example.Recipe.Project.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> createNewRecipe(@RequestBody Recipe recipe) {

        try {
            Recipe insertedRecipe = recipeService.createNewRecipe(recipe);
            return ResponseEntity.created(insertedRecipe.getLocationURI()).body(insertedRecipe);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable("id") Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getRecipesByRating(@PathVariable("rating") Double rating) {
        try {

            ArrayList<Recipe> recipes = recipeService.getRecipesByAverageRating(rating);
            return ResponseEntity.ok(recipes);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipes() {
        try {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getRecipesByName(@PathVariable("name") String name) {

        try {
            ArrayList<Recipe> matchingRecipes = recipeService.getRecipesByName(name);
            return ResponseEntity.ok(matchingRecipes);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getRecipesByNameAndAverageRating(@RequestParam(value = "name", required = false) String name,
                                                              @RequestParam(value = "rating", required = false) Double rating) {
        try {
            if (name != null && rating != null) {
                return ResponseEntity.ok(recipeService.getRecipeByNameAndAverageRating(name, rating));
            }else if (name != null) {
                return ResponseEntity.ok(recipeService.getRecipesByName(name));
            }else if (rating != null){
                return ResponseEntity.ok(recipeService.getRecipesByAverageRating(rating));
            }else{
                return ResponseEntity.badRequest().body("No parameters found");
            }
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> getRecipesByUserName(@PathVariable("username") String username) {

        try {
            ArrayList<Recipe> matchingRecipes = recipeService.getRecipeByUsername(username);
            return ResponseEntity.ok(matchingRecipes);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeId(@PathVariable("id") Long id) {

        try {

            Recipe deletedRecipe = recipeService.deleteRecipeById(id);
            return ResponseEntity.ok("The recipe with ID" + deletedRecipe.getId() +
                    "and name " + deletedRecipe.getName() + " was deleted");
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe updatedRecipe) {
        try {
            Recipe returnedUpdatedRecipe = recipeService.updateRecipe(updatedRecipe, true);
            return ResponseEntity.ok(returnedUpdatedRecipe);
        } catch (NoSuchRecipeException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
