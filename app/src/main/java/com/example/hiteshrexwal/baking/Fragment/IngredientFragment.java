package com.example.hiteshrexwal.baking.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hiteshrexwal.baking.Adapters.IngredientAdapter;
import com.example.hiteshrexwal.baking.Extra.Ingredient;
import com.example.hiteshrexwal.baking.R;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Ingredient> ingredients;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            ingredients=savedInstanceState.getParcelableArrayList("list");
        }
        View rootView = inflater.inflate(R.layout.recipe_ingredients_fragment, container, false);
        recyclerView= rootView.findViewById(R.id.ingredient_recyclerview);
        if(recyclerView!=null && ingredients!=null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new IngredientAdapter(ingredients));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("list",ingredients);
        super.onSaveInstanceState(outState);
    }
}
