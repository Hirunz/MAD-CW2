package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

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

public class Favourites extends AppCompatActivity {

    private static String LOG_TAG = DisplayMovies.class.getSimpleName();

    private ArrayList<Movie> movies;

    private Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        movies = database.getMovies();
        if (movies != null){
            displayMovies();
        }
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

                            if (m.getFavourite() ==0){
                                continue;
                            }

                            CardView.LayoutParams cardParams = new  CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
                            cardParams.setMargins(120,0,120,25);

                            CardView card = new CardView(getApplicationContext());
                            card.setLayoutParams(cardParams);
                            card.setRadius(20);
                            card.setCardElevation(8);

                            LinearLayout l = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams lparams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            l.setLayoutParams(lparams);
                            l.setOrientation(LinearLayout.HORIZONTAL);
                            l.setPadding(10,10,10,10);

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

                            LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(120,120);
                            iParams.setMargins(8,0,10,0);
                            iParams.gravity = Gravity.RIGHT;

                            ImageView image = new ImageView(getApplicationContext());
                            image.setLayoutParams(iParams);
                            image.setBackgroundResource(R.drawable.checkbox_bg);
                            image.setPadding(10,10,10,10);




                            if (m.getFavourite()==1){
                                image.setImageResource(R.drawable.check);
                            }

                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(m.getFavourite() == 0){
                                        m.setFavourite(1);
                                        image.setImageResource(R.drawable.check);
                                    }
                                    else{
                                        m.setFavourite(0);
                                        image.setImageResource(0);
                                    }
                                }
                            });

                            l.addView(tv);
                            l.addView(image);

                            card.addView(l);


                            LinearLayout linearLayout = findViewById(R.id.favourites_linear_layout);
                            linearLayout.addView(card);
                        }
                    }
                });
            }
        }).start();

    }



    public void onSave(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Movie m: movies){
                    database.updateFavourite(m, m.getFavourite());
                }
                movies = database.getMovies();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Updated Favourites", Toast.LENGTH_SHORT).show();

                        LinearLayout linearLayout = findViewById(R.id.favourites_linear_layout);
                        linearLayout.removeAllViews();
                        displayMovies();

                    }
                });
            }
        }).start();

    }
}