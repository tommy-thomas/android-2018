package org.tthomas.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_ID;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_TITLE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.CONTENT_URI;

public class MovieDetailActivity extends AppCompatActivity  {

    private ImageView mImageDetail;

    private ToggleButton mToggleFavorite;

    private TextView mTitleDetail;

    private TextView mOverviewDetail;

    private TextView mReleaseDateDetail;

    private TextView mRatingDetail;

    private int FAVORITE_ID = -1;

    private String MOVIE_ID;

    private List<String> listIDs;
    private List<String> listKeys;
    private List<String> listNames;
    private List<String> listTypes;
    private List<String> listReviewContent;

    private MovieTrailerAdapter movieTrailerAdapter;

    private MovieReviewAdapter movieReviewAdapter;

    private String URL_TRAILER = "https://api.themoviedb.org/3/movie/%s/videos?api_key=";

    private String URL_REVIEW = "https://api.themoviedb.org/3/movie/%s/reviews?api_key=";

    public static final String MOVIE_API_TOKEN = BuildConfig.MOVIE_API_TOKEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_detail);

        mImageDetail = (ImageView) findViewById(R.id.iv_movie_detail_image);

        mTitleDetail = (TextView) findViewById(R.id.tv_movie_detail_title);

        mOverviewDetail = (TextView) findViewById(R.id.tv_movie_detail_overview);

        mReleaseDateDetail = (TextView) findViewById(R.id.tv_movie_detail_release_date);

        mRatingDetail = (TextView) findViewById(R.id.tv_movie_detail_rating);

        mToggleFavorite = (ToggleButton) findViewById(R.id.tb_toggle_favorite);

        Intent intentThatStartedThisActivity = getIntent();

        MOVIE_ID = intentThatStartedThisActivity.getStringExtra("ID");

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("Poster")) {

                Picasso.with(this)
                        .load(intentThatStartedThisActivity.getStringExtra("Poster"))
                        .into(mImageDetail);
            }

            mTitleDetail.setText(intentThatStartedThisActivity.getStringExtra("Title"));

            mOverviewDetail.setText(intentThatStartedThisActivity.getStringExtra("Overview"));

            mReleaseDateDetail.setText("Release Date: " + intentThatStartedThisActivity.getStringExtra("ReleaseDate"));

            mRatingDetail.setText("Rating: " + intentThatStartedThisActivity.getStringExtra("VoteAverage"));

            mToggleFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(FAVORITE_ID == -1 ) {

                        final Intent intentThatStartedThisActivity = getIntent();

                        // Not yet implemented
                        // Check if EditText is empty, if not retrieve input and store it in a ContentValues object
                        // If the EditText input is empty -> don't create an entry

                        // Insert new task data via a ContentResolver
                        // Create new empty ContentValues object
                        ContentValues contentValues = new ContentValues();
                        // Put the task description and selected mPriority into the ContentValues
                        contentValues.put(COLUMN_ID, intentThatStartedThisActivity.getStringExtra("ID"));
                        contentValues.put(COLUMN_TITLE, intentThatStartedThisActivity.getStringExtra("Title"));
                        contentValues.put(COLUMN_OVERVIEW, intentThatStartedThisActivity.getStringExtra("Overview"));
                        contentValues.put(COLUMN_POSTER_PATH, intentThatStartedThisActivity.getStringExtra("Poster"));
                        contentValues.put(COLUMN_RELEASE_DATE, intentThatStartedThisActivity.getStringExtra("ReleaseDate"));
                        contentValues.put(COLUMN_VOTE_AVERAGE, intentThatStartedThisActivity.getStringExtra("VoteAverage"));

                        // Insert the content values via a ContentResolver
                        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

                        FAVORITE_ID = Integer.parseInt(uri.getPathSegments().get(1));

                        // Display the URI that's returned with a Toast
                        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
                        if (uri != null) {
                            Toast.makeText(getBaseContext(), "Insert: " + uri.toString(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if (FAVORITE_ID > 0) {

                            Uri uri = ContentUris.withAppendedId(CONTENT_URI,FAVORITE_ID);
                            Toast.makeText(getBaseContext(), "Delete: " + Integer.toString(FAVORITE_ID) + "  " + uri.toString(), Toast.LENGTH_LONG).show();
                            getContentResolver().delete(uri, null, null);
                            FAVORITE_ID = -1;

                        }
                    }

                }
            });


            try {
                //Make call to AsyncTask so that we can get
                //the initial data from the network
                String ID = intentThatStartedThisActivity.getStringExtra("ID");
                Log.d("TRAILER ID",ID);
                new FetchMovieTrailersTask().execute( ID );

            }catch (Exception e) {
                e.printStackTrace();
                Log.d("TRAILER ID","oops");
            }
        }

    }

    private void initTrailerViews(){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_movie_trailer_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<MovieTrailer> movieTrailers = prepareTrailerData();
        movieTrailerAdapter = new MovieTrailerAdapter(getApplicationContext(),movieTrailers);
        recyclerView.setAdapter(movieTrailerAdapter);

    }

    private void initReviewViews(){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_movie_review_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<MovieReview> movieReviews = prepareReviewData();
        movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(), movieReviews);
        recyclerView.setAdapter(movieReviewAdapter);

    }

    private void initFavorite(){
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.tb_toggle_favorite);
        if(FAVORITE_ID > -1){
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
    }

    private void initViews(){
        initReviewViews();
        initTrailerViews();
        initFavorite();
    }

    private ArrayList<MovieTrailer> prepareTrailerData(){

        ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
        if (!listIDs.isEmpty()) {
            for (int i = 0; i < listIDs.size(); i++) {
                MovieTrailer movieTrailer = new MovieTrailer();
                movieTrailer.setID(listIDs.get(i));
                movieTrailer.setName(listNames.get(i));
                movieTrailer.setKey(listKeys.get(i));
                movieTrailer.setType(listTypes.get(i));
              movieTrailers.add(movieTrailer);
            }
        }
        return movieTrailers;
    }

    private ArrayList<MovieReview> prepareReviewData(){

        ArrayList<MovieReview> movieReviews = new ArrayList<>();
        if (!listReviewContent.isEmpty()) {
            for (int i = 0; i < listReviewContent.size(); i++) {
                MovieReview movieReview = new MovieReview();
                movieReview.setContent(listReviewContent.get(i));
               movieReviews.add(movieReview);
            }
        }
        return movieReviews;
    }


    private class FetchMovieTrailersTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {


                if (params.length > 0) {
                    URL_TRAILER = String.format( URL_TRAILER, params[0]  );

                    URL_REVIEW = String.format( URL_REVIEW , params[0] );
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode moviedata = (ObjectNode)mapper.readTree( new URL(URL_TRAILER + MOVIE_API_TOKEN));

                //root of important data
                JsonNode results = moviedata.path("results");
                Log.d("PAYLOAD" , moviedata.toString());

                List<JsonNode> idnodes = results.findValues("id");
                listIDs = new ArrayList<>();
                for(JsonNode id: idnodes) {
                    listIDs.add(id.asText());
                }

                //get the product view text data
                List<JsonNode> keynodes = results.findValues("key");
                listKeys = new ArrayList<>();
                for(JsonNode key: keynodes) {
                    listKeys.add(key.asText());
                }

                List<JsonNode> namenodes = results.findValues("name");
                listNames = new ArrayList<>();
                for(JsonNode name: namenodes) {
                    listNames.add(name.asText());
                }

                List<JsonNode> typenodes = results.findValues("type");
                listTypes = new ArrayList<>();
                for(JsonNode type: typenodes) {
                    listTypes.add(type.asText());
                }

                ObjectMapper reviewMapper = new ObjectMapper();
                ObjectNode moviereviwdata = (ObjectNode)reviewMapper.readTree( new URL(URL_REVIEW + MOVIE_API_TOKEN));

                JsonNode reviewresults = moviereviwdata.path("results");
                List<JsonNode> contentnodes = reviewresults.findValues("content");
                listReviewContent = new ArrayList<>();
                for(JsonNode content: contentnodes) {
                    listReviewContent.add(content.asText());
                }

                isFavorite(MOVIE_ID);


            }catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        private void isFavorite(String movieID){

            String[] mSelectionArgs = {movieID};

            String mSelectionClause = FavoriteEntry.COLUMN_ID + " = ?";

            String mProjection[] = {FavoriteEntry._ID};

            String mSortOrder = FavoriteEntry.COLUMN_ID;

            // Does a query against the table and returns a Cursor object
            Cursor mCursor = getContentResolver().query(
                    FavoriteEntry.CONTENT_URI,  // The content URI of the words table
                    mProjection,                       // The columns to return for each row
                    mSelectionClause ,                 // Either null, or the word the user entered
                    mSelectionArgs,                    // Either empty, or the string the user entered
                    mSortOrder);// The sort order for the returned rows
            if( null == mCursor || mCursor.getCount() < 0 ){
                FAVORITE_ID = -1;
            } else if( mCursor.getCount() > 0){
                int index = mCursor.getColumnIndex(FavoriteEntry._ID);
                mCursor.moveToFirst();
                FAVORITE_ID = mCursor.getInt(index);
            }

        }

        @Override
        protected void onPostExecute(String result) {
            initViews();
        }
    }

}