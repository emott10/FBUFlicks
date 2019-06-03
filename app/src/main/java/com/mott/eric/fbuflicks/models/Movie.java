package com.mott.eric.fbuflicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {
    public String title;
    public String overview;
    public String posterPath;
    public String backdropPath;
    public Double voteAverage;
    public String date;
    public Integer id;

    public Movie(){}

    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
        date = object.getString("release_date");

    }

    public String getDate() { return date; }

    public Integer getId() { return id; }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() { return voteAverage; }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() { return backdropPath; }
}
