package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

public class DisplayMovies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void onItemClick(View view) {
        ImageView i= (ImageView) view;
        if(i.getDrawable() == getDrawable(R.drawable.check)){
            i.setImageResource(R.drawable.checkbox_unchecked);
        }
        if(i.getDrawable() == getDrawable(R.drawable.checkbox_unchecked)){
            i.setImageResource(R.drawable.check);
        }
    }
}