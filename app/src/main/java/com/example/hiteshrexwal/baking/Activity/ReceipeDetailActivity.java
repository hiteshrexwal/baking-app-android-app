package com.example.hiteshrexwal.baking.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.hiteshrexwal.baking.Extra.Ingredient;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.Fragment.IngredientFragment;
import com.example.hiteshrexwal.baking.Fragment.StepsFragment;
import com.example.hiteshrexwal.baking.Fragment.VideoActivityFragment;
import com.example.hiteshrexwal.baking.R;
import com.example.hiteshrexwal.baking.Widget.FoodWidgetService;
import java.util.ArrayList;
import io.paperdb.Paper;

public class ReceipeDetailActivity extends AppCompatActivity {
    private ArrayList<Step> steps;
    private ArrayList<Ingredient> ingredients;
    private Bundle bundle;
    private String name,serving;
    public static int position=0;
    private boolean twopane;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scrollView=findViewById(R.id.scrollview);
        RecyclerView step_recyclerview = findViewById(R.id.steps_recyclerview);
        RecyclerView ingredient_recyclerview = findViewById(R.id.ingredient_recyclerview);
        Paper.init(this);

        Intent fromActivity = getIntent();
        if (fromActivity.hasExtra("ing") && fromActivity.hasExtra("steps")&&fromActivity.hasExtra("id") && fromActivity.hasExtra("name")) {
            name=fromActivity.getStringExtra("name");
            serving=fromActivity.getStringExtra("serving");
            steps = fromActivity.getParcelableArrayListExtra("steps");
            ingredients = fromActivity.getParcelableArrayListExtra("ing");
        }

        FragmentManager manager = getSupportFragmentManager();

        if(savedInstanceState==null) {
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setRecyclerView(step_recyclerview);
            stepsFragment.setSteps(steps);

            setTitle(name);


            IngredientFragment fragment = new IngredientFragment();
            fragment.setIngredients(ingredients);
            fragment.setRecyclerView(ingredient_recyclerview);
            manager.beginTransaction().add(R.id.ingredient_container, fragment).commit();

            if (findViewById(R.id.recipe_big) != null) {
                twopane = true;
                Step step = steps.get(position);
                VideoActivityFragment videofragment = new VideoActivityFragment();
                videofragment.setStep(step);
                videofragment.setManager(manager);
                videofragment.setContext(this);
                videofragment.setCur_index(position);
                manager.beginTransaction().add(R.id.fragment, videofragment).commit();

                stepsFragment.setTwopane(true);
                stepsFragment.setManager(manager);
                stepsFragment.setTwopane(twopane);
                manager.beginTransaction().add(R.id.steps_container, stepsFragment).commit();
            } else {
                twopane = false;
                stepsFragment.setTwopane(twopane);
                manager.beginTransaction().add(R.id.steps_container, stepsFragment).commit();

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",name);
        outState.putString("serving",serving);
        outState.putParcelableArrayList("step",steps);
        outState.putParcelableArrayList("ing",ingredients);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name=savedInstanceState.getString("name");
        serving=savedInstanceState.getString("serving");
        steps=savedInstanceState.getParcelableArrayList("step");
        ingredients=savedInstanceState.getParcelableArrayList("ing");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addwidget) {
            StringBuilder temp= new StringBuilder();
            for(int i=0;i<ingredients.size();i++){
                 temp.append(ingredients.get(i).getIngredient()).append("(").append(ingredients.get(i).getQuantity()).append(" ").append(ingredients.get(i).getMeasure()).append(")").append("\n");


            }
            Paper.book().write("ing", temp.toString());
            Paper.book().write("name",name);
            Paper.book().write("serve",serving);
            FoodWidgetService.startActionFood(this);

        }
        else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        bundle = new Bundle();
        bundle.putInt("pos", position);
        if(scrollView!=null) {
            bundle.putIntArray("ARTICLE_SCROLL_POSITION",
                    new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bundle!=null) {
            final int[] position = bundle.getIntArray("ARTICLE_SCROLL_POSITION");
            if (position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }
    }
}
