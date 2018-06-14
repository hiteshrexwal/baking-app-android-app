package com.example.hiteshrexwal.baking.Activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.Fragment.VideoActivityFragment;
import com.example.hiteshrexwal.baking.R;

public class VideoActivity extends AppCompatActivity {
    private Step step;
    private int cur_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentManager manager = getSupportFragmentManager();
        if(savedInstanceState!=null) {
            step = savedInstanceState.getParcelable("step");
            cur_index = savedInstanceState.getInt("cur");
        }
        else {
            Intent fromActivity = getIntent();
            if (fromActivity.hasExtra("step")) {
                step = fromActivity.getParcelableExtra("step");
                cur_index = fromActivity.getIntExtra("cur", 0);
                VideoActivityFragment fragment = new VideoActivityFragment();
                fragment.setStep(step);
                fragment.setCur_index(cur_index);
                fragment.setManager(manager);
                fragment.setContext(this);
                manager.beginTransaction().add(R.id.fragment, fragment).commit();
            }
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
                setTitle(step.getShortDescription());
            }

        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportActionBar().hide();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("step",step);
        outState.putInt("cur",cur_index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
