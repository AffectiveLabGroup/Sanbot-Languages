package com.example.sanbotapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "data";
    private static final String TAG              = "DatabaseHelper";

    /**
     * Database creation sql statement ACCIONES --> VOCABULARIO
     * Ejemplo: categoria (comida), nombre (apple), imagen (imagen de manzana), nivel (1)
     */
    private static final String VOCABULARIO = "create table vocabulario ("
            + "_id                  integer primary key autoincrement,"
            + "categoria    text    not null,"
            + "nombre    text    not null,"
            + "imagen               text    not null,"
            + "nivel           integer not null"
            + "); ";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VOCABULARIO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS vocabulario");

        onCreate(db);
    }
}
