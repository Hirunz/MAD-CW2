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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        name = findViewById(R.id.register_name);
        year = findViewById(R.id.register_year);
        director = findViewById(R.id.register_director);
        rating = findViewById(R.id.register_rating);
        review = findViewById(R.id.register_review);
        actors = findViewById(R.id.register_actors);
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
            Log.d(LOG_TAG, year.getText().toString());
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