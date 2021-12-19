package com.example.Recipe.Project.services;

import com.example.Recipe.Project.models.Recipe;
import com.example.Recipe.Project.exceptions.NoSuchRecipeException;
import com.example.Recipe.Project.repos.RecipeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

   private final RecipeRepo recipeRepo;

   RecipeService(RecipeRepo recipeRepo){

       this.recipeRepo = recipeRepo;
   }


    @Transactional
    public Recipe createNewRecipe(Recipe recipe) throws IllegalStateException{

        recipe.validate();
        recipe = recipeRepo.save(recipe);
        recipe.generateLocationURI();
        return recipe;

    }

    public Recipe getRecipeById(Long id) throws NoSuchRecipeException {

        Optional<Recipe> recipeOptional = recipeRepo.findById(id);

        if (recipeOptional.isEmpty()){

            throw new NoSuchRecipeException(" No recipe with ID" + id + "could be found.");
        }
        Recipe recipe = recipeOptional.get();
        recipe.generateLocationURI();
        return recipe;
    }

    public ArrayList<Recipe> getRecipesByName(String name) throws NoSuchRecipeException {
        ArrayList<Recipe> matchingRecipes = recipeRepo.findByNameContaining(name);

        if (matchingRecipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }

        for (Recipe r : matchingRecipes) {
            r.generateLocationURI();
        }
        return matchingRecipes;
    }

    public ArrayList<Recipe> getRecipesByAverageRating(Double rating) throws NoSuchRecipeException{

        ArrayList<Recipe> matchingRecipes = recipeRepo.findByAverageReviewRatingGreaterThanEqual(rating);

        if(matchingRecipes.isEmpty()){

            throw new NoSuchRecipeException("No recipes could be found with that rating");
        }
        return matchingRecipes;
    }

    public List<Recipe> getRecipeByNameAndAverageRating(String name, Double rating) throws NoSuchRecipeException{

        ArrayList<Recipe> matchingRecipes = getRecipesByName(name);

        List<Recipe> filterList = matchingRecipes.stream().filter(r -> r.getAverageReviewRating() >= rating).collect(Collectors.toList());


        if(filterList.isEmpty()){

            throw new NoSuchRecipeException("Sorry no matching recipes with that name and average rating");
        }
        return filterList;
    }

    public ArrayList<Recipe> getRecipeByUsername(String username) throws NoSuchRecipeException{

        ArrayList<Recipe> recipes = recipeRepo.findByUserUsername(username);

        if(recipes.isEmpty()){
            throw new NoSuchRecipeException("No recipes could be found for username " + username);
        }

        for (Recipe r : recipes) {
            r.generateLocationURI();
        }
        return recipes;
    }

    public ArrayList<Recipe> getAllRecipes() throws NoSuchRecipeException{

        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepo.findAll());

        if(recipes.isEmpty()){

            throw new NoSuchRecipeException("there are no recipes yet :( feel free to add one though");
        }
        return recipes;
    }

    @Transactional
    public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException{

        try {
            Recipe recipe = getRecipeById(id);
            recipeRepo.deleteById(id);
            return recipe;
        }catch (NoSuchRecipeException e){
            throw new NoSuchRecipeException(e.getMessage() + "Could not delete");
        }
    }

    public Recipe updateRecipe(Recipe recipe, boolean forceIdCheck) throws NoSuchRecipeException{
        try {
            if (forceIdCheck) {
                getRecipeById(recipe.getId());
            }

            recipe.validate();
            Recipe savedRecipe = recipeRepo.save(recipe);
            savedRecipe.generateLocationURI();
            return savedRecipe;
        }catch (NoSuchRecipeException e){
            throw new NoSuchRecipeException("The recipe you passed in did not have an ID found in the database." +
                    " Double check that it is correct. Or maybe you meant to POST a recipe not PATCH one.");
        }

    }

    public ArrayList<Recipe> getRecipesByDifficultyRating(Integer rating) throws NoSuchRecipeException {
        ArrayList<Recipe> matchingRecipes = recipeRepo.findByDifficultyRatingLessThanEqual(rating);

        if (matchingRecipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }

        return matchingRecipes;
    }
}





