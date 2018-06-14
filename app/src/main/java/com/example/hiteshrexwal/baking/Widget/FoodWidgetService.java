package com.example.hiteshrexwal.baking.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.example.hiteshrexwal.baking.FoodWidget;

import io.paperdb.Paper;


public class FoodWidgetService extends IntentService {
    private static Context foodContext;

    public static final String ACTION_FOOD = "com.example.hiteshrexwal.baking.action.food";

    public FoodWidgetService() {
        super("FoodWidgetService");
    }

    public static void startActionFood(Context context) {
        foodContext=context;
        Intent intent = new Intent(context, FoodWidgetService.class);
        intent.setAction(ACTION_FOOD);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOOD.equals(action)) {
                handleActionUpdateFood();
            }
        }
    }


    private void handleActionUpdateFood() {

        Paper.init(this);
        String name=Paper.book().read("name");
        String serving=Paper.book().read("serve");
        String ing=Paper.book().read("ing");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FoodWidget.class));

        for (int appWidgetId : appWidgetIds) {
           FoodWidget.updateAppWidget(this, appWidgetManager, appWidgetId,name,serving,ing);
        }
    }
}
