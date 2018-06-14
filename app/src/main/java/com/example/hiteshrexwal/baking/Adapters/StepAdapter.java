package com.example.hiteshrexwal.baking.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hiteshrexwal.baking.Activity.ReceipeDetailActivity;
import com.example.hiteshrexwal.baking.Activity.VideoActivity;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.Fragment.VideoActivityFragment;
import com.example.hiteshrexwal.baking.R;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private ArrayList<Step> list;
    private Context context;
    private boolean twopane=false;
    private FragmentManager manager;
    public StepAdapter(ArrayList<Step> list,Context context) {
        this.list = list;
        this.context=context;
    }
    public StepAdapter(ArrayList<Step> list,Context context,boolean twopane,FragmentManager manager) {
        this.list = list;
        this.context=context;
        this.twopane=twopane;
        this.manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_steps,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Step step=list.get(position);
        int x=position+1;
        holder.foodstep.setText(x+". "+step.getShortDescription());
        if(!twopane){
            holder.foodstep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destination = VideoActivity.class;
                    Intent secondActivity = new Intent(context, destination);
                    secondActivity.putExtra("step",step);
                    secondActivity.putExtra("cur",position);
                    context.startActivity(secondActivity);
                }
            });
        }
        else {
            holder.foodstep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ReceipeDetailActivity.position=position;
                    VideoActivityFragment fragment=new VideoActivityFragment();
                    fragment.setStep(step);
                    fragment.setCur_index(position);
                    fragment.setManager(manager);
                    manager.beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodstep;
        public ViewHolder(View itemView) {
            super(itemView);
            foodstep =itemView.findViewById(R.id.step_text);

        }
    }



}
