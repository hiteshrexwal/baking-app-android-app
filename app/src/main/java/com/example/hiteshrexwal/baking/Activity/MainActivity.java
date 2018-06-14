package com.example.hiteshrexwal.baking.Activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.example.hiteshrexwal.baking.Adapters.FoodAdapter;
import com.example.hiteshrexwal.baking.Extra.Food;
import com.example.hiteshrexwal.baking.IdlingResource.SimpleIdlingResource;
import com.example.hiteshrexwal.baking.Network.JsonDownloader;
import com.example.hiteshrexwal.baking.R;
import com.victor.loading.rotate.RotateLoading;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Food> foods;
    private static Bundle bundle;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private ConstraintLayout constraintLayout;
    @Nullable
    private SimpleIdlingResource idlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(bundle==null) {
            if (findViewById(R.id.cons) == null) {
                constraintLayout=findViewById(R.id.constraint);
                recyclerView =  findViewById(R.id.recipe_recyclerview);
                Download(false);
            } else {
                constraintLayout=findViewById(R.id.big_cons);
                recyclerView = findViewById(R.id.recipe_recyclerview_big);
                Download(true);
            }
        }
        else{
            ArrayList<Food> temp=bundle.getParcelableArrayList("list");
            foods=temp;
            if (findViewById(R.id.cons) == null) {
                recyclerView = findViewById(R.id.recipe_recyclerview);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                FoodAdapter adapter = new FoodAdapter(temp, this);
                recyclerView.setAdapter(adapter);
            }
            else{
                recyclerView = findViewById(R.id.recipe_recyclerview_big);
                GridLayoutManager layoutManager=new GridLayoutManager(this, MainActivity.calculateNoOfColumns(this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                FoodAdapter adapter = new FoodAdapter(temp, this);
                recyclerView.setAdapter(adapter);
            }
        }
        getIdlingResource();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        bundle.putParcelableArrayList("list",foods);
        super.onSaveInstanceState(outState);
    }

    private void Download(boolean grid){
        boolean isavail=isNetworkConnectionAvailable();
        if(isavail) {
            RotateLoading avi = findViewById(R.id.loading);
            avi.setVisibility(View.VISIBLE);
            avi.start();
            foods = new ArrayList<>();
            JsonDownloader downloader = new JsonDownloader(getApplicationContext(), foods, recyclerView, getSupportLoaderManager(), grid, avi,idlingResource);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bundle = new Bundle();
        if(foods!=null&&foods.size()>0) {
            Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
            bundle.putParcelable(KEY_RECYCLER_STATE, listState);
            bundle.putParcelableArrayList("list", foods);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
    private boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if(isConnected) {
                return true;
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "No Internet",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (findViewById(R.id.cons) == null) {
                                    Download(false);
                                }
                                else{
                                    Download(true);
                                }
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return false;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}
