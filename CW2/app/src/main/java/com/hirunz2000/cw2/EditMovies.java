package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class EditMovies extends AppCompatActivity {
    private static String LOG_TAG = EditMovies.class.getSimpleName();

    public static final String MOVIE_INDEX = "com.hirunz2000.cw2.EditMovies.MOVIEINDEX";

    private ArrayList<Movie> movies;

    private Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

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
                            tParams.setMargins(15,10,10,15);
                            tParams.gravity = Gravity.CENTER_VERTICAL;


                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(tParams);
                            tv.setText(m.getTitle());
                            tv.setTextSize(20);
                            tv.setTextColor(Color.parseColor("#000814"));


                            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.noto_sans);
                            tv.setTypeface(tf);
                            tv.setGravity(Gravity.CENTER);

                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ViewMovie.class);
                                    intent.putExtra(MOVIE_INDEX, movies.indexOf(m));
                                    startActivity(intent);
                                }
                            });

                            card.addView(tv);


                            LinearLayout linearLayout = findViewById(R.id.edit_linear_layout);
                            linearLayout.addView(card);
                        }
                    }
                });
            }
        }).start();

    }
}