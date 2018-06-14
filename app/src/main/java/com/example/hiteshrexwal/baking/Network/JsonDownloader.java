package com.example.hiteshrexwal.baking.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.hiteshrexwal.baking.Activity.MainActivity;
import com.example.hiteshrexwal.baking.Adapters.FoodAdapter;
import com.example.hiteshrexwal.baking.Extra.Food;
import com.example.hiteshrexwal.baking.Extra.Ingredient;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.IdlingResource.SimpleIdlingResource;
import com.victor.loading.rotate.RotateLoading;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class  JsonDownloader implements LoaderManager.LoaderCallbacks<String> {
    private Context context;
    private RecyclerView recyclerView;
    private RotateLoading avi;
    private ArrayList<Food> food;
    private static int FOOD_LOADER=10;
    private boolean grid;
    private LoaderManager manager;
    @Nullable
    private SimpleIdlingResource idlingResource;



    public JsonDownloader(Context context, ArrayList<Food> food, RecyclerView recyclerView, LoaderManager loaderManager, boolean grid,RotateLoading avi,@Nullable SimpleIdlingResource idlingResource) {
        this.context = context;
        this.food=food;
        this.avi=avi;
        this.recyclerView=recyclerView;
        this.manager=loaderManager;
        Loader<String> movie_loader = loaderManager.getLoader(FOOD_LOADER);
        if(movie_loader==null){
            loaderManager.initLoader(FOOD_LOADER, null,this);
        }
        this.idlingResource=idlingResource;
        loaderManager.restartLoader(FOOD_LOADER,null,this);
        this.grid=grid;
    }



    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id,final Bundle params) {
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }

        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                forceLoad();
                super.onStartLoading();
            }

            @Override
            public String loadInBackground() {
                URL url;
                try {
                    url = UrlBuilderandConnector.link_builder();
                    return UrlBuilderandConnector.getDataFromHttp(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<String> loader, String data) {

        try {
            JSONArray response = new JSONArray(data);
            for(int i=0;i<response.length();i++){
                try {
                    Food f=new Food(Parcel.obtain());
                    JSONObject temp=response.getJSONObject(i);
                    f.setId(temp.getString("id"));
                    f.setName(temp.getString("name"));
                    f.setServings(temp.getString("servings"));
                    ArrayList<Ingredient> list=new ArrayList<>();
                    JSONArray ing=temp.getJSONArray("ingredients");
                    for(int j=0;j<ing.length();j++){
                        JSONObject object=ing.getJSONObject(j);
                        Ingredient ingredients=new Ingredient(Parcel.obtain());
                        ingredients.setQuantity(object.getString("quantity"));
                        ingredients.setIngredient(object.getString("ingredient"));
                        ingredients.setMeasure(object.getString("measure"));
                        list.add(ingredients);
                    }
                    ArrayList<Step> arrayList=new ArrayList<>();
                    JSONArray step=temp.getJSONArray("steps");
                    for(int j=0;j<step.length();j++){
                        JSONObject object=step.getJSONObject(j);
                        Step steps=new Step(Parcel.obtain());
                        steps.setId(object.getString("id"));
                        steps.setShortDescription(object.getString("shortDescription"));
                        steps.setDescription(object.getString("description"));
                        steps.setVideoURL(object.getString("videoURL"));
                        steps.setThumbnailURL(object.getString("thumbnailURL"));
                        arrayList.add(steps);
                    }
                    f.setIngredients(list);
                    f.setSteps(arrayList);
                    food.add(f);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            avi.stop();
            avi.setVisibility(View.GONE);
            if(!grid) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                FoodAdapter adapter = new FoodAdapter(food, context);
                recyclerView.setAdapter(adapter);
            }
            else{
                GridLayoutManager layoutManager=new GridLayoutManager(context, MainActivity.calculateNoOfColumns(context));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                FoodAdapter adapter = new FoodAdapter(food, context);
                recyclerView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.destroyLoader(FOOD_LOADER);
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


}