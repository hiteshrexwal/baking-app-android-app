package com.example.hiteshrexwal.baking.Network;


import android.net.Uri;

import com.example.hiteshrexwal.baking.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class UrlBuilderandConnector {


    public static String getDataFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
        InputStream inputStream=urlConnection.getInputStream();
        Scanner scanner=new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        boolean hasInput=scanner.hasNext();
        if(hasInput){
            return scanner.next();
        }
        return null;
    }
    public static URL link_builder() throws MalformedURLException {
        final  String base_url="d17h27t6h515a5.cloudfront.net";
        Uri.Builder builder=new Uri.Builder();
        builder.scheme("https")
                .authority(base_url)
                .appendPath("topher")
                .appendPath("2017")
                .appendPath("May")
                .appendPath("59121517_baking")
                .appendPath("baking.json");
        Uri img_uri=builder.build();
        //Log.i("url",img_uri.toString());
        return new URL(img_uri.toString());
    }

    public static URL pic_link(String s) throws MalformedURLException {
        final  String base_url="pixabay.com";
        Uri.Builder builder=new Uri.Builder();
        final String API_KEY_Param="key";
        final String NAME="q";
        final String PAGE_Param="per_page";
        builder.scheme("https")
                .authority(base_url)
                .appendPath("api")
                .appendQueryParameter(API_KEY_Param, BuildConfig.APIKEY)
                .appendQueryParameter(NAME,s)
                .appendQueryParameter(PAGE_Param,String.valueOf(3));
        Uri img_uri=builder.build();
        URL url=new URL(img_uri.toString());
        return url;
    }

}
