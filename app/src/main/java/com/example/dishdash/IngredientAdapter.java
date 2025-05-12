package com.example.dishdash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<String> ingredientsList;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(int position);
    }

    public IngredientAdapter(ArrayList<String> ingredientsList, OnIngredientClickListener listener) {
        this.ingredientsList = ingredientsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        String ingredient = ingredientsList.get(position);
        holder.ingredientText.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientText;
        ImageButton removeButton;

        public IngredientViewHolder(@NonNull View itemView, final OnIngredientClickListener listener) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text);
            removeButton = itemView.findViewById(R.id.remove_button);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIngredientClick(position);
                        }
                    }
                }
            });
        }
    }
}