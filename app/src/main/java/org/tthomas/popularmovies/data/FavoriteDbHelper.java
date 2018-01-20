package org.tthomas.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_ID;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_TITLE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry.TABLE_NAME;
import static org.tthomas.popularmovies.data.FavoriteContract.FavoriteEntry._ID;


public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritesDB.db";

    private static final int VERSION = 2;

    FavoriteDbHelper(Context context){ super(context, DATABASE_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                _ID                + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_ID    + " TEXT NOT NULL, " +
                COLUMN_OVERVIEW   + " TEXT NOT NULL, "+
                COLUMN_POSTER_PATH   + " TEXT NOT NULL," +
                COLUMN_RELEASE_DATE   + " TEXT NOT NULL, " +
                COLUMN_VOTE_AVERAGE   + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
