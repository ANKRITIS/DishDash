package com.example.dishdash;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText ingredientInput;
    private Button addButton;
    private Button searchButton;
    private RecyclerView ingredientsRecyclerView;
    private RecyclerView recipesRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView noRecipesTextView;
    private TextView statusTextView;
    private LinearLayout recipesContainer;

    private IngredientAdapter ingredientAdapter;
    private RecipeAdapter recipeAdapter;

    private ArrayList<String> ingredientsList = new ArrayList<>();
    private ArrayList<Recipe> recipesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        ingredientInput = findViewById(R.id.ingredient_input);
        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view);
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        loadingProgressBar = findViewById(R.id.loading_progress);
        noRecipesTextView = findViewById(R.id.no_recipes_text);
        statusTextView = findViewById(R.id.status_text);
        recipesContainer = findViewById(R.id.recipes_container);

        // Set up ingredients recycler view with a FlexboxLayoutManager
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        ingredientsRecyclerView.setLayoutManager(layoutManager);

        ingredientAdapter = new IngredientAdapter(ingredientsList, new IngredientAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(int position) {
                removeIngredient(position);
            }
        });
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        // Set up recipes recycler view
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(recipesList);
        recipesRecyclerView.setAdapter(recipeAdapter);

        // Set up button click listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRecipes();
            }
        });

        // Set up EditText action listener
        ingredientInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addIngredient();
                    return true;
                }
                return false;
            }
        });

        // Initialize with sample data
        initializeSampleData();
        updateSearchButtonState();
    }

    private void addIngredient() {
        String ingredient = ingredientInput.getText().toString().trim().toLowerCase();
        if (!TextUtils.isEmpty(ingredient) && !ingredientsList.contains(ingredient)) {
            ingredientsList.add(ingredient);
            ingredientAdapter.notifyDataSetChanged();
            ingredientInput.setText("");
            updateSearchButtonState();
        }
    }

    private void removeIngredient(int position) {
        if (position >= 0 && position < ingredientsList.size()) {
            ingredientsList.remove(position);
            ingredientAdapter.notifyDataSetChanged();
            updateSearchButtonState();
        }
    }

    private void updateSearchButtonState() {
        searchButton.setEnabled(!ingredientsList.isEmpty());
        searchButton.setAlpha(ingredientsList.isEmpty() ? 0.5f : 1.0f);
    }

    private void searchRecipes() {
        // Show loading state
        loadingProgressBar.setVisibility(View.VISIBLE);
        recipesRecyclerView.setVisibility(View.GONE);
        noRecipesTextView.setVisibility(View.GONE);
        statusTextView.setText("Searching...");

        // Simulate network delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Filter recipes based on ingredients
                ArrayList<Recipe> matchedRecipes = new ArrayList<>();
                for (Recipe recipe : getAvailableRecipes()) {
                    for (String ingredient : ingredientsList) {
                        if (recipe.getIngredients().contains(ingredient)) {
                            matchedRecipes.add(recipe);
                            break;
                        }
                    }
                }

                // Update UI with results
                recipesList.clear();
                recipesList.addAll(matchedRecipes);
                recipeAdapter.notifyDataSetChanged();

                loadingProgressBar.setVisibility(View.GONE);

                if (matchedRecipes.isEmpty()) {
                    noRecipesTextView.setVisibility(View.VISIBLE);
                    recipesRecyclerView.setVisibility(View.GONE);
                    statusTextView.setText("Enter ingredients to find recipes");
                } else {
                    noRecipesTextView.setVisibility(View.GONE);
                    recipesRecyclerView.setVisibility(View.VISIBLE);
                    statusTextView.setText("Recipes for you");
                }
            }
        }, 1500); // 1.5 seconds delay to simulate network request
    }

    private List<Recipe> getAvailableRecipes() {
        // This would typically come from a database or API
        ArrayList<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(
                1,
                "Pasta Carbonara",
                new ArrayList<>(Arrays.asList("pasta", "eggs", "bacon", "parmesan cheese", "black pepper")),
                "25 mins",
                "Easy"
        ));

        recipes.add(new Recipe(
                2,
                "Vegetable Stir Fry",
                new ArrayList<>(Arrays.asList("rice", "broccoli", "carrot", "soy sauce", "garlic")),
                "20 mins",
                "Easy"
        ));

        recipes.add(new Recipe(
                3,
                "Chicken Curry",
                new ArrayList<>(Arrays.asList("chicken", "curry powder", "onion", "coconut milk", "rice")),
                "40 mins",
                "Medium"
        ));

        recipes.add(new Recipe(
                4,
                "Tomato Soup",
                new ArrayList<>(Arrays.asList("tomatoes", "onion", "garlic", "vegetable broth", "cream")),
                "30 mins",
                "Easy"
        ));

        recipes.add(new Recipe(
                5,
                "Beef Tacos",
                new ArrayList<>(Arrays.asList("ground beef", "taco shells", "lettuce", "tomato", "cheese", "sour cream")),
                "25 mins",
                "Easy"
        ));

        return recipes;
    }

    private void initializeSampleData() {
        // Add sample ingredients for demo purposes
        // Uncomment below for testing
        /*
        ingredientsList.add("pasta");
        ingredientsList.add("eggs");
        ingredientAdapter.notifyDataSetChanged();
        updateSearchButtonState();
        */
    }
}