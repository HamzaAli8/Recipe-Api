package com.example.Recipe.Project.services;

import com.example.Recipe.Project.models.Recipe;
import com.example.Recipe.Project.models.Review;
import com.example.Recipe.Project.exceptions.NoSuchRecipeException;
import com.example.Recipe.Project.exceptions.NoSuchReviewException;
import com.example.Recipe.Project.repos.ReviewRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;


    public Review getReviewById(Long id) throws NoSuchReviewException {

        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()) {

            throw new NoSuchReviewException("The review with ID " + id + "could not be found.");
        }
        return review.get();
    }

    public ArrayList<Review> getReviewByRecipeId(Long recipeId)throws NoSuchRecipeException, NoSuchReviewException{

        Recipe recipe = recipeService.getRecipeById(recipeId);

        ArrayList<Review> reviews = new ArrayList<>(recipe.getReviews());

        if(reviews.isEmpty()){

            throw new NoSuchReviewException("There are no reviews for this recipe");
        }
        return reviews;
    }

    public ArrayList<Review> getReviewByUsername(String username) throws NoSuchReviewException{

        ArrayList<Review> reviews = reviewRepo.findByUserUsername(username);

        if(reviews.isEmpty()){
            throw new NoSuchReviewException("No reviews could be found for username " + username);
        }
        return reviews;
    }

    public Recipe postNewReview (Review review, Long recipeId) throws NoSuchRecipeException{

        if(review.getRating() == null){

            throw new IllegalStateException("Sorry review currently not rated");
        }
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipe.getReviews().add(review);

        if (recipe.getUser().getUsername().equals(review.getUser().getUsername())){

            throw new NoSuchRecipeException("Sorry there mate, you cannot rate your own recipes");
        }


        recipe.setAverageReviewRating(calculateAvgRating(recipe.getReviews()));
        recipeService.updateRecipe(recipe,false);
        return recipe;
    }

    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);

        if (null == review) {
            throw new NoSuchReviewException("The review you are trying to delete does not exist.");
        }
        reviewRepo.deleteById(id);
        return review;
    }

    public Review updateReviewById(Review reviewToUpdate, Long id) throws NoSuchReviewException {
        try {
            Review review = getReviewById(id);
            if(review != null){
                if(reviewToUpdate.getRating() != null){

                    review.setRating(reviewToUpdate.getRating());
                }
                if(reviewToUpdate.getDescription() != null){
                    review.setDescription(reviewToUpdate.getDescription());
                }
                return reviewRepo.save(review);
            }
        } catch (NoSuchReviewException e) {
            throw new NoSuchReviewException("The review you are trying to update. Maybe you meant to create one? If not," +
                    "please double check the ID you passed in.");
        }
        return null;
    }


    public Double calculateAvgRating(Collection<Review> reviews){

            OptionalDouble avg = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average();

            if(avg.isEmpty()){

            return 0.0;

            }
            else{

                return avg.getAsDouble();
            }
    }

}

