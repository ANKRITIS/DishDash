package com.example.dishdash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
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

        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            holder.recipeIngredients.setText(android.text.TextUtils.join(", ", recipe.getIngredients()));
        } else {
            holder.recipeIngredients.setText("Tap to view ingredients");
        }

        // Load recipe image if available
        if (recipe instanceof RecipeFromApi) {
            RecipeFromApi apiRecipe = (RecipeFromApi) recipe;
            if (apiRecipe.getImageUrl() != null && !apiRecipe.getImageUrl().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(apiRecipe.getImageUrl())
                        .placeholder(R.drawable.ic_recipe_placeholder)
                        .error(R.drawable.ic_recipe_placeholder)
                        .into(holder.recipeImage);
            } else {
                holder.recipeImage.setImageResource(R.drawable.ic_recipe_placeholder);
            }
        } else {
            holder.recipeImage.setImageResource(R.drawable.ic_recipe_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void updateFromApi(List<RecipeResponse> apiRecipes) {
        List<Recipe> converted = new ArrayList<>();
        for (RecipeResponse r : apiRecipes) {
            converted.add(new RecipeFromApi(r.id, r.title, new ArrayList<>(), "Unknown", "Unknown", r.image));
        }
        this.recipeList = converted;
        notifyDataSetChanged();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCookingTime, recipeDifficulty, recipeIngredients;
        ImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCookingTime = itemView.findViewById(R.id.recipe_time);
            recipeDifficulty = itemView.findViewById(R.id.recipe_difficulty);
            recipeIngredients = itemView.findViewById(R.id.recipe_ingredients);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}