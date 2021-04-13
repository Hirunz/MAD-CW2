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

    // api URLs
    private static final String  SEARCH_TITLE ="https://imdb-api.com/en/API/SearchTitle/k_o548umw0/";
    private static final String  GET_USER_RATINGS ="https://imdb-api.com/en/API/UserRatings/k_o548umw0/";
    private static final String  GET_RATINGS ="https://imdb-api.com/en/API/Ratings/k_o548umw0/";

    // variables to store movie data
    private String movie_title;
    private String movie_id ;
    private double movie_rating =-1;
    private String movie_image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        // remove default actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        displayMovies();
    }

    // display all movie titles
    public void displayMovies(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // initialise movies array and display movies alphabetically.
                movies = database.getMovies();
                Collections.sort(movies);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // prevent crashing when there are no movies.
                        if (movies == null || movies.isEmpty()){
                            Toast.makeText(getApplicationContext(),"No Movies Available !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (Movie m: movies){

                            /*
                            * Layout Design
                            *       CardView -> TextView
                            * */

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

                            // set font
                            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.noto_sans);
                            tv.setTypeface(tf);
                            tv.setGravity(Gravity.CENTER);
                            tv.setPadding(10,20,10,20);

                            // add TextView to CardView
                            card.addView(tv);

                            /*
                            * adding an onClick listener to each card
                            * if the card is clicked (selected) Background tint of the card will change to yellow,
                            *   and turn the backgorund tint of the rest of the cards to white.
                            * this will ensure there is only one card in yellow colour.
                            * */
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

                            /// ADD card to linear layout to display
                            LinearLayout linearLayout = findViewById(R.id.edit_linear_layout);
                            linearLayout.addView(card);

                            // adding the card to the cards array.
                            cards.add(card);
                        }
                    }
                });
            }
        }).start();

    }


    public void onSearchIMDB(View view) {
        /*
        * Searching happens as follows
        *   if a movie is selected,
        *       get data from IMDb api using the title of the movie
        *       from that data -> get the id and image of the movie
        *       using the id-> search UserRating for total rating
        *           if total rating is null,
        *               search for IMDb rating in Rating
        *                   if IMDb rating is null,
        *                       no rating*/

        // setting all the movie data to default;
        movie_title =null;
        movie_id =null;
        movie_image_url=null;
        movie_rating =-1;

        try {
            /*
            * Searching the selected card
            *   there is only one card in the system with yellow background tint at any time
            *   search that by comparing the background tints
            */
            CardView selected = null;
            for (CardView c : cards) {
                if (c.getBackgroundTintList().equals(ContextCompat.getColorStateList(getApplicationContext(), R.color.yellow))) {
                    selected = c;
                }
            }

            // get the relevant movie by card index. (card index == movie index)
            Movie m = movies.get(cards.indexOf(selected));

            movie_title = m.getTitle();

            // invoke the search method to get the movie from api
            search();
            Toast.makeText(getApplicationContext(),"Searching movie in IMDb",Toast.LENGTH_LONG).show();

        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Select a movie first",Toast.LENGTH_SHORT).show();
        }



    }

    public StringBuilder getData(String url){
        // parsing a uri
        Uri uri = Uri.parse(url);

        URL requestURL;
        HttpURLConnection conn =null;
        InputStreamReader is =null;
        StringBuilder stringBuilder=null;

        // establishing a new http connection
        try {
            requestURL= new URL(uri.toString());
            conn = (HttpURLConnection) requestURL.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // get the input stream and initialise a bufferedreader object to read the stream.
            is= new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader((is));

            // read the data and put it into a string builder object
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
        // a method used to get the movie id and image
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get all the data from the api url
                StringBuilder s = getData(SEARCH_TITLE + movie_title);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            /*
                            * The api returns a JSON Object containing all the data
                            * Inside that object, there is a JSON array of results,
                            *   which are sorted by relevance to title
                            * In this case, only need the most relevant movie and therefore,
                            *   the index = 0*/
                            JSONObject data = new JSONObject(s.toString());

                            JSONArray results = data.getJSONArray("results");

                            movie_id = results.getJSONObject(0).getString("id");
                            movie_image_url = results.getJSONObject(0).getString("image");

                            // after getting the IMDb id of the movie, get the total rating from IMDb
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
                // get all the data from the api url
                StringBuilder s = getData(GET_USER_RATINGS + movie_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        * The IMDb UserRating url returns a JSON Object
                        * Get the total rating and full movie title
                        *
                        * Sometimes api returns movies which doesn't have a totalRating
                        * If there is an error with parsing the rating,
                        *   use the getRating method and get the rating*/
                        try {
                           JSONObject data = new JSONObject(s.toString());

                           String rating= data.getString("totalRating");
                           movie_title = data.getString("fullTitle");

                           try{
                               movie_rating = Double.parseDouble(rating);
                           }catch (Exception e){
                               getRating();
                           }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // once the above process is complete, update the UI
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
                // get the data from api
                StringBuilder s = getData(GET_RATINGS + movie_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /*
                        * If the total rating is unavailable,
                        * this method is used too get the IMDb rating instead.*/
                        try {
                            JSONObject data = new JSONObject(s.toString());

                            String rating= data.getString("imDb");

                            try{
                                movie_rating = Double.parseDouble(rating);
                            }catch (Exception e){
                                // if still there is no rating, show error message.
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
                // first, get the relevant image from the obtained image url.
                try {
                  URL  url = new URL(movie_image_url);
                  Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          // update the UI
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

