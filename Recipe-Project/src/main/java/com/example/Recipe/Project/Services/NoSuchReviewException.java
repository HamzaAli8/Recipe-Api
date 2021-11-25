package com.example.Recipe.Project.Services;

public class NoSuchReviewException extends Exception {



    public NoSuchReviewException(String message) {
        super(message);
    }

    public NoSuchReviewException(){
    }
}
