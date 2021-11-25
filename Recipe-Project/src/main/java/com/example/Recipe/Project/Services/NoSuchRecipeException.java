package com.example.Recipe.Project.Services;

public class NoSuchRecipeException extends Exception {


    public NoSuchRecipeException(String message) {
        super(message);
    }

    public NoSuchRecipeException(){


    }
}
