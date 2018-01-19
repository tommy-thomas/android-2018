package org.tthomas.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.tthomas.popularmovies.data.FavoriteContract.*;

/**
 * Created by tommy-thomas on 1/19/18.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritesDB.db";

    private static final int VERSION = 1;

    FavoriteDbHelper(Context context){ super(context, DATABASE_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_ID    + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
