package com.example.hiteshrexwal.baking.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.hiteshrexwal.baking.Activity.ReceipeDetailActivity;
import com.example.hiteshrexwal.baking.Extra.Food;
import com.example.hiteshrexwal.baking.R;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private ArrayList<Food> list;
    private Context context;
    public FoodAdapter(ArrayList<Food> list,Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Food food=list.get(position);
        holder.foodName.setText(food.getName());
        int index=food.getSteps().size()-1;
        String url="";
        if(food.getImage()==null){
            for(int i=0;i<food.getSteps().size();i++){
                if(food.getSteps().get(i).getThumbnailURL()==""){
                    continue;
                }
                else{
                    url+=food.getSteps().get(i).getThumbnailURL();
                    break;
                }
            }

        }
        else
        {
            url=food.getImage();
        }
        if(url==""){
            url+=food.getSteps().get(index).getVideoURL();
        }
        Glide.with(context).load(url).into(holder.foodPoster);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destination = ReceipeDetailActivity.class;
                Intent secondActivity = new Intent(context, destination);
                secondActivity.putParcelableArrayListExtra("steps",food.getSteps())
                        .putParcelableArrayListExtra("ing",food.getIngredients())
                .putExtra("id",String.valueOf(food.getId()))
                .putExtra("name",food.getName())
                .putExtra("serving",food.getServings());
                context.startActivity(secondActivity);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodPoster;
        TextView foodName;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            foodPoster = itemView.findViewById(R.id.img_poster);
            foodName=itemView.findViewById(R.id.receipe_name);
            cardView=  itemView.findViewById(R.id.card);
        }
    }



}
