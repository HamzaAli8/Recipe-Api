package com.example.Recipe.Project;

import com.example.Recipe.Project.models.*;
import com.example.Recipe.Project.repos.RecipeRepo;
import com.example.Recipe.Project.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RecipeProjectApplicationTests {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RecipeRepo recipeRepo;

    CustomUserDetails user = CustomUserDetails.builder()
            .id(null)
            .username("Chef Hamza")
            .password("123456")
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .isEnabled(true)
            .userMeta(UserMeta.builder().email("Hamza@gmail.com").name("Hamza Ali").id(null).build())
            .authorities(Collections.singletonList(new Role(Role.Roles.ROLE_USER)))
            .build();

    CustomUserDetails user2 = CustomUserDetails.builder()
            .id(null)
            .username("Chef Jared")
            .password("7891011")
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .isEnabled(true)
            .userMeta(UserMeta.builder().email("Jared@gmail.com").name("Jared Larsen").id(null).build())
            .authorities(Collections.singletonList(new Role(Role.Roles.ROLE_USER)))
            .build();


    Recipe recipe = Recipe.builder()
            .name("test recipe")
            .difficultyRating(1)
            .minutesToMake(5)
            .user(user)
            .ingredients(
                    Set.of(Ingredient.builder().amount("1 jar").name("pickles").build())
            )
            .steps(
                    Set.of(Step.builder().description("eat pickles").stepNumber(1).build())
            )
            .build();



    @Test
    public void testSaveUser()
    {
        userRepo.save(user);
    }

    @Test
    public void findSavedUser(){

        userRepo.findById(23L);

    }


    @Test
    public void testSaveRecipe(){

        recipeRepo.save(recipe);

    }






}
