package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    }

    public void onSearchClick(View view) {
    }

    public void onEditMoviesClick(View view) {
    }

    public void onFavouritesClick(View view) {
    }

    public void onDisplayClick(View view) {
    }

    public void onRegisterClick(View view) {
    }
}