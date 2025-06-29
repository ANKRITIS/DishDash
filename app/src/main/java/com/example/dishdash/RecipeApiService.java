package com.example.dishdash;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApiService {

    @GET("recipes/findByIngredients")
    Call<List<RecipeResponse>> getRecipesByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );
}