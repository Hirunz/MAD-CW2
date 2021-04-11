package com.hirunz2000.cw2;

import java.util.Arrays;
import java.util.Objects;

public class Movie {
    private String title;
    private int year;
    private String director;
    private String actors;
    private int rating;
    private String review;

    public Movie(String title, int year, String director, String actors, int rating, String review) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.review = review;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year &&
                rating == movie.rating &&
                Objects.equals(title.toLowerCase(), movie.title.toLowerCase()) &&
                Objects.equals(director.toLowerCase(), movie.director.toLowerCase()) &&
                Objects.equals(actors.toLowerCase(), movie.actors.toLowerCase()) &&
                Objects.equals(review.toLowerCase(), movie.review.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, director, actors, rating, review);
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
                '}';
    }
}
