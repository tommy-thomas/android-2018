package org.tthomas.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_ID;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_TITLE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.CONTENT_URI;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mImageDetail;

    private ToggleButton mToggleFavorite;

    private TextView mTitleDetail;

    private TextView mOverviewDetail;

    private TextView mReleaseDateDetail;

    private TextView mRatingDetail;

    private int FAVORITE_ID = -1;

    private String MOVIE_ID;

    private ArrayList<MovieTrailer> movieTrailers;
    private ArrayList<MovieReview> movieReviews;

    private MovieTrailerAdapter movieTrailerAdapter;

    private MovieReviewAdapter movieReviewAdapter;

    private MovieItem movieItem;

    private String URL_TRAILER = "https://api.themoviedb.org/3/movie/%s/videos?api_key=";

    private String URL_REVIEW = "https://api.themoviedb.org/3/movie/%s/reviews?api_key=";

    public static final String MOVIE_API_TOKEN = BuildConfig.MOVIE_API_TOKEN;

    private final static String MOVIE_ITEM_KEY = "movie-item";
    private final static String MOVIE_REVIEW_KEY = "movie-reviewq";
    private final static String MOVIE_TRAILER_KEY = "movie-trailer";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_ITEM_KEY)) {
            movieItem = savedInstanceState.getParcelable(MOVIE_ITEM_KEY);
            movieReviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEW_KEY);
            movieTrailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILER_KEY);
            isFavorite(movieItem.getID());
            initViews();
        } else {
            try {
                //Make call to AsyncTask so that we can get
                //the initial data from the network
                movieItem = getIntent().getParcelableExtra("Movie");
                new FetchMovieDetailsTask().execute(movieItem.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mImageDetail = (ImageView) findViewById(R.id.iv_movie_detail_image);

        mTitleDetail = (TextView) findViewById(R.id.tv_movie_detail_title);

        mOverviewDetail = (TextView) findViewById(R.id.tv_movie_detail_overview);

        mReleaseDateDetail = (TextView) findViewById(R.id.tv_movie_detail_release_date);

        mRatingDetail = (TextView) findViewById(R.id.tv_movie_detail_rating);

        mToggleFavorite = (ToggleButton) findViewById(R.id.tb_toggle_favorite);

        if (movieItem != null) {

            MOVIE_ID = movieItem.getID();

            if ( movieItem.getPoster_path() != "") {

                Picasso.with(this)
                        .load(movieItem.getPoster_path())
                        .into(mImageDetail);
            }

            mTitleDetail.setText(movieItem.getTitle());

            mOverviewDetail.setText(movieItem.getOverview());

            mReleaseDateDetail.setText("Release Date: " + movieItem.getRelease_date());

            mRatingDetail.setText("Rating: " + movieItem.getVote_average());

            mToggleFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (FAVORITE_ID == -1) {

                        // Not yet implemented
                        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
                        // If the EditText input is empty -> don't create an entry

                        // Insert new task data via a ContentResolver
                        // Create new empty ContentValues object
                        ContentValues contentValues = new ContentValues();
                        // Put the task description and selected mPriority into the ContentValues
                        contentValues.put(COLUMN_ID, movieItem.getID() );
                        contentValues.put(COLUMN_TITLE, movieItem.getTitle() );
                        contentValues.put(COLUMN_OVERVIEW, movieItem.getOverview() );

                        String poster_url = movieItem.getPoster_path();
                        String posterFileName = "/" + poster_url.substring(poster_url.lastIndexOf('/') + 1);

                        contentValues.put(COLUMN_POSTER_PATH, posterFileName);

                        contentValues.put(COLUMN_RELEASE_DATE, movieItem.getRelease_date());
                        contentValues.put(COLUMN_VOTE_AVERAGE, movieItem.getVote_average());

                        // Insert the content values via a ContentResolver
                        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

                        FAVORITE_ID = Integer.parseInt(uri.getPathSegments().get(1));

                        // Display the URI that's returned with a Toast
                        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
                        if (uri != null) {
                            Toast.makeText(getBaseContext(), "\"" + movieItem.getTitle() + "\"" +
                                    " added to favorites.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if (FAVORITE_ID > 0) {

                            Uri uri = ContentUris.withAppendedId(CONTENT_URI, FAVORITE_ID);
                            Toast.makeText(getBaseContext(), "\"" + movieItem.getTitle() + "\"" +
                                    " removed from favorites.", Toast.LENGTH_LONG).show();
                            getContentResolver().delete(uri, null, null);
                            FAVORITE_ID = -1;

                        }
                    }

                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_ITEM_KEY, movieItem);
        outState.putParcelableArrayList(MOVIE_TRAILER_KEY, movieTrailers);
        outState.putParcelableArrayList(MOVIE_REVIEW_KEY, movieReviews);
        super.onSaveInstanceState(outState);
    }

    private void initTrailerViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_trailer_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        recyclerView.setNestedScrollingEnabled(false);
        movieTrailerAdapter = new MovieTrailerAdapter(getApplicationContext(), movieTrailers);
        recyclerView.setAdapter(movieTrailerAdapter);

    }

    private void initReviewViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movie_review_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        recyclerView.setNestedScrollingEnabled(false);
        movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(), movieReviews);
        recyclerView.setAdapter(movieReviewAdapter);

    }

    private void initFavorite() {
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.tb_toggle_favorite);
        if (FAVORITE_ID > -1) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
    }

    private void initViews() {
        initReviewViews();
        initTrailerViews();
        initFavorite();
    }

    private class FetchMovieDetailsTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {


                if (params.length > 0) {
                    URL_TRAILER = String.format(URL_TRAILER, params[0]);

                    URL_REVIEW = String.format(URL_REVIEW, params[0]);
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode moviedata = (ObjectNode) mapper.readTree(new URL(URL_TRAILER + MOVIE_API_TOKEN));

                JsonNode results = moviedata.path("results");
                Iterator<JsonNode> iterator = results.iterator();
                movieTrailers = new ArrayList<>();
                while (iterator.hasNext()) {
                    JsonNode node = iterator.next();
                    if( node.get("type").asText().equals("Trailer")){
                    MovieTrailer movieTrailer = new MovieTrailer(
                          node.get("id").asText(),
                            node.get("key").asText(),
                            node.get("name").asText(),
                            node.get("type").asText()

                    );
                        movieTrailers.add(movieTrailer);
                    }
                }


                ObjectMapper reviewMapper = new ObjectMapper();
                ObjectNode moviereviwdata = (ObjectNode) reviewMapper.readTree(new URL(URL_REVIEW + MOVIE_API_TOKEN));

                JsonNode movieresults = moviereviwdata.path("results");
                iterator = movieresults.iterator();
                movieReviews = new ArrayList<>();
                while (iterator.hasNext()) {
                    JsonNode node = iterator.next();
                    MovieReview movieReview = new MovieReview(
                            node.get("id").asText(),
                            node.get("content").asText().trim(),
                            node.get("author").asText().trim()

                    );
                   movieReviews.add(movieReview);
                }


                isFavorite(MOVIE_ID);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            initViews();
        }
    }

    private void isFavorite(String movieID) {

        String[] mSelectionArgs = {movieID};

        String mSelectionClause = FavoriteEntry.COLUMN_ID + " = ?";

        String mProjection[] = {FavoriteEntry._ID};

        String mSortOrder = FavoriteEntry.COLUMN_ID;

        // Does a query against the table and returns a Cursor object
        Cursor mCursor = getContentResolver().query(
                FavoriteEntry.CONTENT_URI,  // The content URI of the words table
                mProjection,                       // The columns to return for each row
                mSelectionClause,                 // Either null, or the word the user entered
                mSelectionArgs,                    // Either empty, or the string the user entered
                mSortOrder);// The sort order for the returned rows
        if (null == mCursor || mCursor.getCount() < 0) {
            FAVORITE_ID = -1;
        } else if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            FAVORITE_ID = mCursor.getInt(mCursor.getColumnIndex(FavoriteEntry._ID));
        }

    }

}