package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Ratings extends AppCompatActivity {
    private static String LOG_TAG = Ratings.class.getSimpleName();

    private ArrayList<Movie> movies;

    private ArrayList<CardView> cards = new ArrayList<>();

    private Database database = new Database(this);

    private static final String  SEARCH_TITLE ="https://imdb-api.com/en/API/SearchTitle/k_93p25plv/";
    private static final String  GET_USER_RATINGS ="https://imdb-api.com/en/API/UserRatings/k_93p25plv/";
    private static final String  GET_RATINGS ="https://imdb-api.com/en/API/Ratings/k_93p25plv/";

    private String movie_title;
    private String movie_id ;
    private double movie_rating =-1;
    private String movie_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        displayMovies();
    }

    public void displayMovies(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                movies = database.getMovies();
                Collections.sort(movies);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movies == null || movies.isEmpty()){
                            Toast.makeText(getApplicationContext(),"No Movies Available !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (Movie m: movies){
                            CardView.LayoutParams cardParams = new  CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
                            cardParams.setMargins(120,0,120,25);

                            CardView card = new CardView(getApplicationContext());
                            card.setLayoutParams(cardParams);
                            card.setRadius(20);
                            card.setCardElevation(8);

                            LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            tParams.setMargins(15,0,0,0);
                            tParams.gravity = Gravity.CENTER_VERTICAL;


                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(tParams);
                            tv.setText(m.getTitle());
                            tv.setTextSize(20);
                            tv.setTextColor(Color.parseColor("#000814"));


                            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.noto_sans);
                            tv.setTypeface(tf);
                            tv.setGravity(Gravity.CENTER);

                            card.addView(tv);
                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (CardView c: cards){
                                        if (c==card){
                                            c.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.yellow));
                                        }else{
                                            c.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.white));
                                        }

                                    }
                                }
                            });


                            LinearLayout linearLayout = findViewById(R.id.edit_linear_layout);
                            linearLayout.addView(card);

                            cards.add(card);
                        }
                    }
                });
            }
        }).start();

    }


    public void onSearchIMDB(View view) {

        movie_title =null;
        movie_id =null;
        movie_image_url=null;
        movie_rating =-1;

        try {
            CardView selected = null;
            for (CardView c : cards) {
                if (c.getBackgroundTintList().equals(ContextCompat.getColorStateList(getApplicationContext(), R.color.yellow))) {
                    selected = c;
                }
            }


            Movie m = movies.get(cards.indexOf(selected));

            movie_title = m.getTitle();

            search();
            Toast.makeText(getApplicationContext(),"Searching movie in IMDb",Toast.LENGTH_LONG).show();

        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Select a movie first",Toast.LENGTH_SHORT).show();
        }



    }

    public StringBuilder getData(String url){
        Uri uri = Uri.parse(url);

        URL requestURL;
        HttpURLConnection conn =null;
        InputStreamReader is =null;
        StringBuilder stringBuilder=null;

        try {
            requestURL= new URL(uri.toString());
            conn = (HttpURLConnection) requestURL.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is= new InputStreamReader(conn.getInputStream());

            BufferedReader reader = new BufferedReader((is));
            stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }

             stringBuilder.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect();
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return stringBuilder;
    }


    public void search(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder s = getData(SEARCH_TITLE + movie_title);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject data = new JSONObject(s.toString());

                            JSONArray results = data.getJSONArray("results");

                            movie_id = results.getJSONObject(0).getString("id");
                            movie_image_url = results.getJSONObject(0).getString("image");

                            getUserRating();

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public void getUserRating(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder s = getData(GET_USER_RATINGS + movie_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                           JSONObject data = new JSONObject(s.toString());

                           String rating= data.getString("totalRating");

                           try{
                               movie_rating = Double.parseDouble(rating);
                           }catch (Exception e){
                               getRating();
                           }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        updateData();



                    }
                });
            }
        }).start();
    }


    public void getRating(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder s = getData(GET_RATINGS + movie_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject data = new JSONObject(s.toString());

                            String rating= data.getString("imDb");

                            try{
                                movie_rating = Double.parseDouble(rating);
                            }catch (Exception e){
                                movie_rating=-1;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }).start();
    }

    public void updateData(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                  URL  url = new URL(movie_image_url);
                  Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          TextView title = findViewById(R.id.rating_title);
                          title.setText(movie_title);

                          TextView rating = findViewById(R.id.rating_totalRatiing);
                          if (movie_rating == -1){
                              rating.setText("Rating: Unavailable");
                          }
                          else{
                              rating.setText("Rating: "+movie_rating);
                          }

                          ImageView image = findViewById(R.id.ratings_image);
                          image.setImageBitmap(bmp);

                          CardView card = findViewById(R.id.rating_displayImdb);
                          card.setVisibility(View.VISIBLE);
                      }
                  });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


}

