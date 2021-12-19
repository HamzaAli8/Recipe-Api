package com.example.Recipe.Project.repos;

import com.example.Recipe.Project.models.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<CustomUserDetails,Long> {

    CustomUserDetails findByUsername(String username);
}
