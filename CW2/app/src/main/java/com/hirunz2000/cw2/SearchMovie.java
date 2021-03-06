package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SearchMovie extends AppCompatActivity {
    private static String LOG_TAG = SearchMovie.class.getSimpleName();

    // array lists to store all movies and searched movies
    private ArrayList<Movie> searchedMovies =new ArrayList<>();
    private ArrayList<Movie> movies;

    private Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        //  remove actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get all movies
        movies = database.getMovies();

    }

    public void onSearch(View view) {
        EditText et = findViewById(R.id.search_EditText);
        String searchText = et.getText().toString().toLowerCase();
        Log.d(LOG_TAG, searchText);
        // clear the searched movies before searching
        searchedMovies.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // find the movies which has the user input in movie title, director or actors
                // then add it to the searched movies list
                for (Movie m: movies){

                    if (m.getTitle().toLowerCase().contains(searchText) ||
                            m.getDirector().toLowerCase().contains(searchText) ||
                            m.getActors().toLowerCase().contains(searchText)
                    ) {
                        Log.d(LOG_TAG, "movie found");
                        searchedMovies.add(m);
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update the ui
                        displayMovies();
                    }
                });
            }
        }).start();


    }


    public void displayMovies(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // if there are no movies
                        if (searchedMovies == null || searchedMovies.isEmpty()){
                            Toast.makeText(getApplicationContext(),"No Movies Available !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                       /*
                        * for each movie in the search
                        * add the title to a textview
                        * add textview to a card view
                        * and display it.
                        */
                        for (Movie m: searchedMovies){
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

                            // fonts
                            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.noto_sans);
                            tv.setTypeface(tf);
                            tv.setGravity(Gravity.CENTER);
                            tv.setPadding(10,10,10,10);

                            card.addView(tv);


                            LinearLayout linearLayout = findViewById(R.id.search_linear_layout);
                            linearLayout.addView(card);
                        }
                    }
                });
            }
        }).start();

    }
}