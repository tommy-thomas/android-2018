package org.tthomas.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.tthomas.popularmovies.data.FavoriteContract;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_ID;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_TITLE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MOVIES_DB";
    private List<String> listIDs;
    private List<String> listPosters;
    private List<String> listTitles;
    private List<String> listOverviews;
    private List<String> listReleaseDates;
    private List<String> listVoteAverages;

    private ArrayList<MovieItem> movieItems;


    private MovieItemAdapter movieItemAdapter;

    public static final String MOVIE_ITEMS_SAVED_STATE = "movie-items-saved-state";

    public static final String URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=";

    public static final String URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=";

    public static final String MOVIE_API_TOKEN = BuildConfig.MOVIE_API_TOKEN;

    private String displayPreference = "display-preference";

    public static final String DISPLAY_POPULAR = "popular-display";
    public static final String DISPLAY_TOP_RATED = "top-rated-display";
    public static final String DISPLAY_FAVORITES = "favorites-display";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIE_ITEMS_SAVED_STATE)) {
            movieItems = savedInstanceState.getParcelableArrayList(MOVIE_ITEMS_SAVED_STATE);
            initViews(false);
        } else {
            // Shared prefs checking
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPref.edit();

            if (sharedPref.contains(displayPreference)) {
                String pref = sharedPref.getString(displayPreference, DISPLAY_POPULAR);
                if (pref.equals(DISPLAY_POPULAR)) {
                    try {
                        new FetchMoviesTask().execute(URL_POPULAR);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pref.equals(DISPLAY_TOP_RATED)) {
                    try {
                        new FetchMoviesTask().execute(URL_TOP_RATED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pref.equals(DISPLAY_FAVORITES)) {
                    try {
                        new FetchFavoritesTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    new FetchMoviesTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_ITEMS_SAVED_STATE, movieItems);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movie_poster, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (id == R.id.action_sort_popular) {
            new FetchMoviesTask().execute(URL_POPULAR);
            editor.putString(displayPreference, DISPLAY_POPULAR);
            editor.apply();
            return true;
        }

        if (id == R.id.action_sort_top_rated) {
            new FetchMoviesTask().execute(URL_TOP_RATED);
            editor.putString(displayPreference, DISPLAY_TOP_RATED);
            editor.apply();
            return true;
        }

        if (id == R.id.action_favorites) {
            new FetchFavoritesTask().execute();
            editor.putString(displayPreference, DISPLAY_FAVORITES);
            editor.apply();
            return true;
        }

        return true;
    }


    private void initViews( boolean prepareData ) {
        if( prepareData){
            prepareData();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_poster);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        }
        movieItemAdapter = new MovieItemAdapter(getApplicationContext(), movieItems);
        recyclerView.setAdapter(movieItemAdapter);
    }

    private  void prepareData() {
        movieItems = new ArrayList<>();
        if (!listPosters.isEmpty()) {
            for (int i = 0; i < listPosters.size(); i++) {
                MovieItem movieItem = new MovieItem(
                        listIDs.get(i),
                        listTitles.get(i),
                        listPosters.get(i),
                        listReleaseDates.get(i),
                        listOverviews.get(i),
                        listVoteAverages.get(i));
                movieItems.add(movieItem);
            }
        }

    }

    private class FetchFavoritesTask extends AsyncTask<String, String, String> {

        private boolean hasFavorites = false;

        @Override
        protected String doInBackground(String... params) {

            try {

                Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavoriteContract.FavoriteEntry._ID);

                if (cursor.moveToFirst()) {
                    listIDs = new ArrayList<>();
                    listTitles = new ArrayList<>();
                    listOverviews = new ArrayList<>();
                    listPosters = new ArrayList<>();
                    listReleaseDates = new ArrayList<>();
                    listVoteAverages = new ArrayList<>();
                    while (!cursor.isAfterLast()) {
                        listIDs.add(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                        listTitles.add(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                        listOverviews.add(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
                        listPosters.add(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                        listReleaseDates.add(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
                        listVoteAverages.add(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE)));
                        cursor.moveToNext();
                    }
                    hasFavorites = true;
                }

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (hasFavorites) {
                initViews(true);
            } else {
                Toast.makeText(getBaseContext(), "No favorites saved.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class FetchMoviesTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                String url = URL_POPULAR;

                if (params.length > 0) {
                    url = params[0];
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode moviedata = (ObjectNode) mapper.readTree(new URL(url + MOVIE_API_TOKEN));

                //root of important data
                JsonNode results = moviedata.path("results");
//                Log.v("PAYLOAD" , clientmodel_set.toString());

                List<JsonNode> idnodes = results.findValues("id");
                listIDs = new ArrayList<String>();
                for (JsonNode id : idnodes) {
                    listIDs.add(id.asText().trim());
                }

                //get the product view text data
                List<JsonNode> posternodes = results.findValues("poster_path");
                listPosters = new ArrayList<String>();
                for (JsonNode poster : posternodes) {
                    listPosters.add(poster.asText().trim());
                }

                List<JsonNode> titlenodes = results.findValues("title");
                listTitles = new ArrayList<String>();
                for (JsonNode title : titlenodes) {
                    listTitles.add(title.asText().trim());
                }

                List<JsonNode> overviewnodes = results.findValues("overview");
                listOverviews = new ArrayList<String>();
                for (JsonNode overview : overviewnodes) {
                    listOverviews.add(overview.asText().trim());
                }

                List<JsonNode> releasedatenodes = results.findValues("release_date");
                listReleaseDates = new ArrayList<String>();
                for (JsonNode releasedate : releasedatenodes) {
                    listReleaseDates.add(releasedate.asText().trim());
                }


                List<JsonNode> voteaveragenodes = results.findValues("vote_average");
                listVoteAverages = new ArrayList<String>();
                for (JsonNode voteaverage : voteaveragenodes) {
                    listVoteAverages.add(voteaverage.asText().trim());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            initViews(true);
        }
    }

}