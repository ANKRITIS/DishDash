package com.example.dishdash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private EditText ingredientInput;
    private Button addButton, searchButton;
    private IngredientAdapter ingredientAdapter;
    private ArrayList<String> selectedIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ingredientInput = findViewById(R.id.ingredient_input);
        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);

        // Setup ingredient RecyclerView
        RecyclerView ingredientRecyclerView = findViewById(R.id.ingredient_recycler_view);
        ingredientAdapter = new IngredientAdapter(selectedIngredients);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        // Add ingredient button
        addButton.setOnClickListener(v -> {
            String input = ingredientInput.getText().toString().trim().toLowerCase();
            if (!TextUtils.isEmpty(input) && !selectedIngredients.contains(input)) {
                selectedIngredients.add(input);
                ingredientAdapter.notifyDataSetChanged();
                ingredientInput.setText("");
            } else if (selectedIngredients.contains(input)) {
                Toast.makeText(this, "Ingredient already added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Search recipes button
        searchButton.setOnClickListener(v -> {
            if (selectedIngredients.isEmpty()) {
                Toast.makeText(this, "Please add at least one ingredient!", Toast.LENGTH_SHORT).show();
                return;
            }

            String ingredientsCSV = TextUtils.join(",", selectedIngredients);

            Intent intent = new Intent(MainActivity.this, RecipeResultsActivity.class);
            intent.putExtra("INGREDIENTS", ingredientsCSV);
            startActivity(intent);
        });
    }
}