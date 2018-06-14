package com.example.hiteshrexwal.baking.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hiteshrexwal.baking.Adapters.StepAdapter;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.R;

import java.util.ArrayList;

public class StepsFragment extends Fragment {
    private RecyclerView recyclerView;
    public static ArrayList<Step> steps;
    private boolean twopane=false;
    private FragmentManager manager;

    public StepsFragment() {
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setTwopane(boolean twopane) {
        this.twopane = twopane;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            steps=savedInstanceState.getParcelableArrayList("list");
            twopane=savedInstanceState.getBoolean("twopane");
        }
        View rootView = inflater.inflate(R.layout.receipe_step_fragment, container, false);
        recyclerView=rootView.findViewById(R.id.steps_recyclerview);
        if(recyclerView!=null && steps!=null && manager==null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new StepAdapter(steps,getContext()));


        }
        else if(manager!=null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new StepAdapter(steps,getContext(),twopane,manager));

        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("list",steps);
        outState.putBoolean("twopane",twopane);
        super.onSaveInstanceState(outState);
    }
}
