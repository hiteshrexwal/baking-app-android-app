package com.example.hiteshrexwal.baking.Adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hiteshrexwal.baking.Extra.Ingredient;
import com.example.hiteshrexwal.baking.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private ArrayList<Ingredient> list;

    public IngredientAdapter(ArrayList<Ingredient> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Ingredient ingredient = list.get(position);
        String s= String.valueOf(position+1)+". "+ingredient.getIngredient()+"("+ingredient.getQuantity()+" "+ingredient.getMeasure()+")";
        holder.ingredientName.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
        }
    }
}
