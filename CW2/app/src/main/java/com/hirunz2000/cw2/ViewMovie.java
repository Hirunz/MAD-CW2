package com.hirunz2000.cw2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ViewMovie extends AppCompatActivity {

    private static String LOG_TAG = ViewMovie.class.getSimpleName();

    private ArrayList<Movie> movies;

    private int MOVIE_INDEX;
    private EditText name, year, director, review, actors;
    private CheckBox fav, notFav;
    private LinearLayout rating;

    private ImageView[] stars= new ImageView[10];

    private Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get the selected movie from the intent
        Intent intent = getIntent();
        MOVIE_INDEX = intent.getIntExtra(EditMovies.MOVIE_INDEX,-1);
        getMovies();
    }


    public void getMovies(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                movies = database.getMovies();
                Collections.sort(movies);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // iitialise views
                        name = findViewById(R.id.viewMovie_title);
                        year = findViewById(R.id.view_movie_year);
                        director = findViewById(R.id.view_movie_director);
                        review = findViewById(R.id.register_movie_review);
                        actors = findViewById(R.id.view_movie_actors);
                        rating = findViewById(R.id.view_movie_rating);
                        fav = findViewById(R.id.edit_checkbox_fav);
                        notFav = findViewById(R.id.edit_checkbox_notFav);

                        // get the selected movie
                        Movie m = movies.get(MOVIE_INDEX);
                        Log.d(LOG_TAG, m.toString());

                        // set the  movie data to edit text views
                        name.setText(m.getTitle());
                        year.setText(String.valueOf(m.getYear()));
                        director.setText(m.getDirector());
                        review.setText(m.getReview());
                        actors.setText(m.getActors());


                        // favourite status
                        if (m.getFavourite() ==1){
                            fav.setChecked(true);
                            notFav.setChecked(false);
                        }else{
                            fav.setChecked(false);
                            notFav.setChecked(true);
                        }

                        // set the rating in stars
                        setRating(m.getRating());


                    }
                });
            }
        }).start();
    }



    public void setRating(int rating1){
        /*
        * stars array contains the 10 stars imageviews.
        * if the rating is 6,
        *   first 6 imageviews in the stars array are  filled stars
        *   rest are empty stars.
        *
        * For rating of 6/10,
        *   add 6 filled stars and 4 empty stars to stars array.
        *   add an onclick listener to each imageview
        *       recurse the set rating method.
        *
        * if the previous rating is set to 3 next,
        *   3rd star onclick method will trigger
        *       and change the rating ad 3 filled stars and 7 empty stars.
        *
        * */
        rating.removeAllViews();
        for (int i = 0; i< rating1; i++){
            LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(70,70);
            iParams.setMargins(5,0,5,0);


            ImageView image = new ImageView(getApplicationContext());
            image.setLayoutParams(iParams);
            image.setImageResource(R.drawable.filled_star1);
            int finalI = i+1;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(finalI);
                }
            });
            // a tag to calculate stars count

            image.setTag("1");

            stars[i] = image;
            rating.addView(image);

        }

        for (int i = rating1; i<10; i++){
            LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(70,70);
            iParams.setMargins(5,0,5,0);
            iParams.gravity = Gravity.LEFT;


            ImageView image = new ImageView(getApplicationContext());
            image.setLayoutParams(iParams);
            image.setImageResource(R.drawable.empty_star);
            int finalI = i+1;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(finalI);
                }
            });
            // a tag to calculate stars count
            image.setTag("0");

            stars[i] = image;
            rating.addView(image);

        }
    }

    public void onUpdate(View view) {
        if (!name.getText().toString().isEmpty() &&
                !year.getText().toString().isEmpty() &&
                year.getText().toString().length()==4 &&
                !director.getText().toString().isEmpty() &&
                !review.getText().toString().isEmpty() &&
                !actors.getText().toString().isEmpty()
        ){

            if (Integer.parseInt(year.getText().toString()) <1895){
                Toast.makeText(this, "Year must be greater than 1895",Toast.LENGTH_SHORT).show();
                return;
            }

            int favourite =0;
            if (fav.isChecked()){
                favourite=1;
            }

            // get the stars count bt counting the tags
            int starsCount =0;
            for (int i =0; i<stars.length;i++){
                if(stars[i].getTag().equals("1")){
                    starsCount++;
                }
            }

//            Log.d(LOG_TAG, String.valueOf(starsCount));

            // create a new movie withupdated data
            Movie m =new Movie(
                    name.getText().toString(),
                    Integer.parseInt(year.getText().toString()),
                    director.getText().toString(),
                    actors.getText().toString(),
                    Integer.parseInt(String.valueOf(starsCount)),
                    review.getText().toString(),
                    favourite
            );

            Movie old = movies.get(MOVIE_INDEX);
            /*
            * if movie name or year is changed,
            *   create a new movie and delete the old one
            * else
            *   update the movie details*/
            if (movies.contains(m)){
                database.updateMovie(old.getTitle(), old.getYear(), m);
            }
            else{
                database.deleteMovie(old);
                database.addMovie(m);

            }
            movies=database.getMovies();
            Log.d(LOG_TAG, m.toString());
            Log.d(LOG_TAG, "Updated movie");
            Toast.makeText(this, "Movie updated Successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(LOG_TAG, "Movie update failed !");
            Toast.makeText(this, "Invalid details !",Toast.LENGTH_SHORT).show();
        }
    }

    public void onCheckboxClick(View view) {
        if (view.getId() == R.id.edit_checkbox_fav){
            fav.setChecked(true);
            notFav.setChecked(false);
        }
        else{
            fav.setChecked(false);
            notFav.setChecked(true);
        }
    }
}