package com.example.dishdash;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeResultsActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private ProgressBar progressBar;
    private TextView noResultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);

        // Initialize views
        recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        noResultsText = findViewById(R.id.no_results_text);

        // Setup RecyclerView
        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Get ingredients from intent
        String ingredients = getIntent().getStringExtra("INGREDIENTS");
        if (ingredients != null) {
            searchRecipes(ingredients);
        } else {
            Toast.makeText(this, "No ingredients provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void searchRecipes(String ingredients) {
        progressBar.setVisibility(View.VISIBLE);
        noResultsText.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApiService api = retrofit.create(RecipeApiService.class);

        Call<List<RecipeResponse>> call = api.getRecipesByIngredients(
                ingredients,
                20,
                "5a4e9e77b2744c099044f2a710e25efb"
        );

        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<RecipeResponse> recipes = response.body();
                    if (recipes.isEmpty()) {
                        noResultsText.setVisibility(View.VISIBLE);
                        noResultsText.setText("No recipes found with those ingredients");
                    } else {
                        recipeAdapter.updateFromApi(recipes);
                    }
                } else {
                    Toast.makeText(RecipeResultsActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
                    noResultsText.setVisibility(View.VISIBLE);
                    noResultsText.setText("Failed to load recipes");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecipeResultsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                noResultsText.setVisibility(View.VISIBLE);
                noResultsText.setText("Network error occurred");
            }
        });
    }
}