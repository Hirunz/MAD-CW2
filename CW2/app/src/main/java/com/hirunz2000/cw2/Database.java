package com.hirunz2000.cw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.hirunz2000.cw2.IMovieTableConstants.ACTORS;
import static com.hirunz2000.cw2.IMovieTableConstants.DIRECTOR;
import static com.hirunz2000.cw2.IMovieTableConstants.FAVOURITE;
import static com.hirunz2000.cw2.IMovieTableConstants.RATING;
import static com.hirunz2000.cw2.IMovieTableConstants.REVIEW;
import static com.hirunz2000.cw2.IMovieTableConstants.TABLE_NAME;
import static com.hirunz2000.cw2.IMovieTableConstants.TITLE;
import static com.hirunz2000.cw2.IMovieTableConstants.YEAR;

public class Database extends SQLiteOpenHelper {

    private static String LOG_TAG = Database.class.getSimpleName();

    private static final String DATABASE_NAME="movieopolis.db";
    private static final int DATABASE_VERSION=1;

    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, ACTORS, RATING, REVIEW, FAVOURITE};
    private static String ORDER_BY = _ID ;


    private static String empTable="CREATE TABLE "+ TABLE_NAME +"(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TITLE +" VARCHAR(60) NOT NULL, "+
            YEAR +" INTEGER(4) NOT NULL, "+
            DIRECTOR +" VARCHAR(60) NOT NULL, "+
            ACTORS +" VARCHAR(200) NOT NULL, "+
            RATING +" INTEGER(2) NOT NULL, "+
            REVIEW +" VARCHAR(200) NOT NULL, "+
            FAVOURITE +" INTEGER(1) NOT NULL "+
            ");";

    public Database(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(empTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public boolean addMovie(Movie movie){

        ArrayList<Movie> movies = getMovies();
        if (movies == null){
            Log.d(LOG_TAG, " Empty database !");
            return false;
        }
        if(movies.contains(movie)){
            return false;
        }

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE, movie.getTitle());
        values.put(YEAR, movie.getYear());
        values.put(DIRECTOR, movie.getDirector());
        values.put(ACTORS, movie.getActors());
        values.put(RATING, movie.getRating());
        values.put(REVIEW, movie.getReview());
        values.put(FAVOURITE, movie.getFavourite());

        db.insertOrThrow(TABLE_NAME, null, values);

        return true;
    }

    public void updateMovie(String title, int year, Movie movie) {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE, movie.getTitle());
        values.put(YEAR, movie.getYear());
        values.put(DIRECTOR, movie.getDirector());
        values.put(ACTORS, movie.getActors());
        values.put(RATING, movie.getRating());
        values.put(REVIEW, movie.getReview());
        values.put(FAVOURITE, movie.getFavourite());


        db.update(TABLE_NAME,values,
                " TITLE = ? AND YEAR = ? " , new String[]{title, String.valueOf(year)}
        );
    }

    public void updateFavourite(Movie movie, int favourite) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(FAVOURITE, favourite);

        db.update(TABLE_NAME,values,
                " TITLE = ? AND YEAR = ? " , new String[]{movie.getTitle(), String.valueOf(movie.getYear())}
        );
    }

    public ArrayList<Movie> getMovies(){
        ArrayList<Movie> movies = new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME, FROM, null, null,
                null, null,ORDER_BY);

        while (cursor.moveToNext()){
            String title = cursor.getString(1);
            int year = cursor.getInt(2);
            String director = cursor.getString(3);
            String actors = cursor.getString(4);
            int rating = cursor.getInt(5);
            String review = cursor.getString(6);
            int favourite = cursor.getInt(7);

            movies.add(new Movie(title,year,director,actors,rating,review,favourite));
        }
        return movies;
    }


    public void deleteMovie(Movie movie) {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, " TITLE = ? AND YEAR = ? ", new String[]{movie.getTitle(), String.valueOf(movie.getYear())});
    }




}
