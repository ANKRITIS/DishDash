package com.example.dishdash;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipeList;

    public RecipeAdapter(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        holder.recipeCookingTime.setText(recipe.getCookingTime());
        holder.recipeDifficulty.setText(recipe.getDifficulty());
        holder.recipeIngredients.setText("Ingredients: " + recipe.getIngredientsAsString());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        TextView recipeCookingTime;
        TextView recipeDifficulty;
        TextView recipeIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCookingTime = itemView.findViewById(R.id.recipe_time);
            recipeDifficulty = itemView.findViewById(R.id.recipe_difficulty);
            recipeIngredients = itemView.findViewById(R.id.recipe_ingredients);
        }
    }
}
