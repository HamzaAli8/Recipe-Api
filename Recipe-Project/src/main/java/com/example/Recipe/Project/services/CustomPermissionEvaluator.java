package com.example.Recipe.Project.services;

import com.example.Recipe.Project.models.CustomUserDetails;
import com.example.Recipe.Project.models.Recipe;
import com.example.Recipe.Project.models.Review;
import com.example.Recipe.Project.models.Role;
import com.example.Recipe.Project.repos.RecipeRepo;
import com.example.Recipe.Project.repos.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    ReviewRepo reviewRepo;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        if(!permission.getClass().equals("".getClass())){

            throw new SecurityException("Cannot execute hasPermission() calls where permission is not in String form");
        }
        if(userIsAdmin(authentication)){

            return true;

        }else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (targetType.equalsIgnoreCase("recipe")) {


                Optional<Recipe> recipe = recipeRepo.findById(Long.parseLong(targetId.toString()));
                if (recipe.isEmpty()) {
                    return true;
                }
                return recipe.get().getAuthor().equals(userDetails.getUsername());

            } else if (targetType.equalsIgnoreCase("review")) {
                Optional<Review> review = reviewRepo.findById(Long.parseLong(targetId.toString()));
                if (review.isEmpty()) {

                    throw new EntityNotFoundException("The review you are trying to access does not exist");
                }
                return review.get().getAuthor().equals(userDetails.getUsername());
            }
        }
        return true;
    }

    public boolean userIsAdmin(Authentication authentication) {
        Collection<Role> grantedAuthorities = (Collection<Role>) authentication.getAuthorities();

        for (Role r : grantedAuthorities) {
            if (r.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}

