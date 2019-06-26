package com.mott.eric.fbuflicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mott.eric.fbuflicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends YouTubeBaseActivity{

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvDate;
    RatingBar rbVoteAverage;
    YouTubePlayerView player;
    String videoId;
    Integer movieId;
    public final static String API_PARAM = "api_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvDate = findViewById(R.id.tvDate);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        player = findViewById(R.id.player);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        movieId = movie.getId();

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDate.setText("Release Date: " + movie.getDate());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        getYoutubeVideo();
    }

    public void getYoutubeVideo(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + getString(R.string.api_key);

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    if(results.length() == 0){
                        return;
                    }

                    JSONObject movieTrailer = results.getJSONObject(0);
                    videoId = movieTrailer.getString("key");

                    // initialize with API key stored in secrets.xml
                    player.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieDetailsActivity", "Error initializing YouTube player");
                        }
                    });
                    Log.i("Movie", "Grabbed youtube video id");
                } catch (JSONException e) {
                    Log.i("Movie", "Failed to grabbed youtube video id");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("Movie", "Failed parse json request for video id");
            }
        });
    }
}
