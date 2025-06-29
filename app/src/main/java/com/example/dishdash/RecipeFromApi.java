package com.example.dishdash;


import java.util.ArrayList;

public class RecipeFromApi extends Recipe {
    private String imageUrl;

    public RecipeFromApi(int id, String name, ArrayList<String> ingredients, String cookingTime, String difficulty, String imageUrl) {
        super(id, name, ingredients, cookingTime, difficulty);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}