package com.example.dishdash;

import java.util.ArrayList;
public class Recipe {
    private int id;
    private String name;
    private ArrayList<String> ingredients;
    private String cookingTime;
    private String difficulty;

    public Recipe(int id, String name, ArrayList<String> ingredients, String cookingTime, String difficulty) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getIngredientsAsString() {
        return String.join(", ", ingredients);
    }
}



