package com.hirunz2000.cw2;

import java.util.Arrays;
import java.util.Objects;

public class Movie implements Comparable<Movie> {
    private String title;
    private int year;
    private String director;
    private String actors;
    private int rating;
    private String review;
    private int favourite;

    public Movie(String title, int year, String director, String actors, int rating, String review) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.review = review;
        favourite =0;
    }

    public Movie(String title, int year, String director, String actors, int rating, String review, int favourite) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.review = review;
        this.favourite =favourite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year &&
                Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }




    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", favourite=" + favourite +
                '}';
    }

    @Override
    public int compareTo(Movie o) {
        return o.title.compareTo(title);
    }
}
