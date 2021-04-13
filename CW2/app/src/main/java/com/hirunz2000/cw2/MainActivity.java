package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }


    public void onRatingsClick(View view) {
        Intent intent = new Intent(this, Ratings.class);
        startActivity(intent);
    }

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, SearchMovie.class);
        startActivity(intent);
    }

    public void onEditMoviesClick(View view) {
        Intent intent = new Intent(this, EditMovies.class);
        startActivity(intent);
    }

    public void onFavouritesClick(View view) {
        Intent intent = new Intent(this, Favourites.class);
        startActivity(intent);
    }

    public void onDisplayClick(View view) {
        Intent intent = new Intent(this, DisplayMovies.class);
        startActivity(intent);
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }
}