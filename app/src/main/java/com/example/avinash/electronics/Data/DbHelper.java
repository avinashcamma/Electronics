package com.example.avinash.electronics.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avina on 13-05-2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="items.db";

    private static final int DATABASE_VERSION=1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEM_TABLE =  "CREATE TABLE " +Contract.Entry.TABLE_NAME + " ("
                + Contract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Entry.COLUMN_TYPE + " TEXT, "
                + Contract.Entry.COLUMN_NAME + " TEXT NOT NULL, "
                + Contract.Entry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + Contract.Entry.COLUMN_STOCK + " TEXT NOT NULL, "  // yha pe tune likha hai not null to null value accept nhi karega
                +Contract.Entry.COLUMN_SUPPLIER+ " TEXT, "
                + Contract.Entry.COLUMN_IMAGE + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_ITEM_TABLE);
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
