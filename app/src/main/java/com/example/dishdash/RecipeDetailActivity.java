package com.example.dishdash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView, timeView, servingsView, sourceView, summaryView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        imageView = findViewById(R.id.recipe_detail_image);
        titleView = findViewById(R.id.recipe_detail_title);
        timeView = findViewById(R.id.recipe_detail_time);
        servingsView = findViewById(R.id.recipe_detail_servings);
        sourceView = findViewById(R.id.recipe_detail_source);
        summaryView = findViewById(R.id.recipe_detail_summary);
        progressBar = findViewById(R.id.detail_progress_bar);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "No Recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecipeDetails(recipeId);
    }

    private void loadRecipeDetails(int recipeId) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApiService api = retrofit.create(RecipeApiService.class);

        api.getRecipeInformation(recipeId, "5a4e9e77b2744c099044f2a710e25efb").enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetails detail = response.body();

                    titleView.setText(detail.title);
                    timeView.setText("Ready in: " + detail.readyInMinutes + " minutes");
                    servingsView.setText("Servings: " + detail.servings);

                    if (detail.summary != null) {
                        // Remove HTML tags from summary
                        String cleanSummary = detail.summary.replaceAll("<[^>]*>", "");
                        summaryView.setText(cleanSummary);
                    }

                    if (detail.image != null && !detail.image.isEmpty()) {
                        Glide.with(RecipeDetailActivity.this)
                                .load(detail.image)
                                .placeholder(R.drawable.ic_recipe_placeholder)
                                .error(R.drawable.ic_recipe_placeholder)
                                .into(imageView);
                    }

                    if (detail.sourceUrl != null && !detail.sourceUrl.isEmpty()) {
                        sourceView.setText("View Full Recipe");
                        sourceView.setVisibility(View.VISIBLE);
                        sourceView.setOnClickListener(v -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.sourceUrl));
                            startActivity(browserIntent);
                        });
                    } else {
                        sourceView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecipeDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}