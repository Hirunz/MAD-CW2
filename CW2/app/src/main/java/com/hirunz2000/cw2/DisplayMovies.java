package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import java.util.ArrayList;

public class DisplayMovies extends AppCompatActivity {
    private static String LOG_TAG = DisplayMovies.class.getSimpleName();

    private Database database = new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Log.d(LOG_TAG, "aaaa");
        ArrayList<Movie> arr = database.getMovies();
        if (arr != null){
            for (Movie m: arr){
                Log.d(LOG_TAG, m.toString());
            }
        }
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