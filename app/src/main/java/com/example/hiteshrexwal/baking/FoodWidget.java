package com.example.hiteshrexwal.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import com.example.hiteshrexwal.baking.Activity.MainActivity;

import io.paperdb.Paper;

public class FoodWidget extends AppWidgetProvider {


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,String name,String serving,String ing) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);

        Paper.init(context);

        if(name==null) {
            views.setTextViewText(R.id.name, (CharSequence) Paper.book().read("name"));
            views.setTextViewText(R.id.serving, (CharSequence) Paper.book().read("serve"));
            views.setTextViewText(R.id.ingedient, (CharSequence) Paper.book().read("ing"));
        }
        else{
            views.setTextViewText(R.id.name, (CharSequence) name);
            views.setTextViewText(R.id.serving, (CharSequence) serving);
            views.setTextViewText(R.id.ingedient, (CharSequence) ing);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.linear, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,null,null,null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

