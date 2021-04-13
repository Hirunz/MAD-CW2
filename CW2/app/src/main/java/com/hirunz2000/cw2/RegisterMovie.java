package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterMovie extends AppCompatActivity {
    private static String LOG_TAG =RegisterMovie.class.getSimpleName();
    private Database database = new Database(this);

    private EditText name, year, director, rating, review, actors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        // remove actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // initialise edit text views
        name = findViewById(R.id.viewMovie_title);
        year = findViewById(R.id.view_movie_year);
        director = findViewById(R.id.view_movie_director);
        rating = findViewById(R.id.register_rating);
        review = findViewById(R.id.register_movie_review);
        actors = findViewById(R.id.view_movie_actors);
    }

    public void onSave(View view) {
        if (!name.getText().toString().isEmpty() &&
                !year.getText().toString().isEmpty() &&
                year.getText().toString().length()==4 &&
                !director.getText().toString().isEmpty() &&
                !rating.getText().toString().isEmpty() &&
                !review.getText().toString().isEmpty() &&
                !actors.getText().toString().isEmpty()
        ){
            // validate inputs
            if (Integer.parseInt(year.getText().toString()) <1895){
                Toast.makeText(this, "Year must be greater than 1895",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(rating.getText().toString()) >10 ||
                    Integer.parseInt(rating.getText().toString()) <1  ){
                Toast.makeText(this, "Rating must be from 1-10",Toast.LENGTH_SHORT).show();
                return;
            }

            // add movie to database
            if (database.addMovie(new Movie(
                    name.getText().toString(),
                    Integer.parseInt(year.getText().toString()),
                    director.getText().toString(),
                    actors.getText().toString(),
                    Integer.parseInt(rating.getText().toString()),
                    review.getText().toString()
            ))){
                Log.d(LOG_TAG, "Added movie");
                Toast.makeText(this, "Movie added Successfully",Toast.LENGTH_SHORT).show();

            }
            else{
                Log.d(LOG_TAG, "Unable movie --> movie exists !");
                Toast.makeText(this, "Movie already exists !",Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Log.d(LOG_TAG, "Movie registration failed !");
            Toast.makeText(this, "Invalid details !",Toast.LENGTH_SHORT).show();
        }
    }
}