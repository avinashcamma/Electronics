package com.example.avinash.electronics.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by avina on 14-05-2017.
 */

public class ItemProvider extends ContentProvider {
    public static final String LOG_TAG= ItemProvider.class.getSimpleName();

    private static final int ITEMS=100;
    private static final int ITEMS_ID=101;
    private DbHelper mDbhelper;

    private static final UriMatcher sUriMatcher= new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(Contract.Entry.CONTENT_AUTHORITY,Contract.Entry.PATH_ITEMS,ITEMS);
        sUriMatcher.addURI(Contract.Entry.CONTENT_AUTHORITY, Contract.Entry.PATH_ITEMS+"/#",ITEMS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbhelper=new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=mDbhelper.getReadableDatabase();
        Cursor cursor;
        int match= sUriMatcher.match(uri);

        switch (match){
            case ITEMS:
                cursor=database.query(Contract.Entry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);


                break;
            case ITEMS_ID:
                selection= Contract.Entry._ID+"=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(Contract.Entry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return Contract.Entry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return Contract.Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);


        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return insertItem(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertItem(Uri uri,ContentValues values){
        String name = values.getAsString(Contract.Entry.COLUMN_NAME);
        String price=values.getAsString(Contract.Entry.COLUMN_PRICE);
        String stock=values.getAsString(Contract.Entry.COLUMN_STOCK);
        String supplier=values.getAsString(Contract.Entry.COLUMN_SUPPLIER);
        String type=values.getAsString(Contract.Entry.COLUMN_TYPE);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }
        else if (price==null){
            throw new IllegalArgumentException("Item requires a price");
        }
        else if(stock== null){
            throw new IllegalArgumentException("Item requires a stock");
        }
        else if (supplier==null){
            throw new IllegalArgumentException("Item requires a supplier");
        }
        else if (type==null){
            throw new IllegalArgumentException("Item requires a type");
        }

        SQLiteDatabase database = mDbhelper.getWritableDatabase();
        long id = database.insert(Contract.Entry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = mDbhelper.getWritableDatabase();



        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                int rowsDeleted = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);}
                // Delete all rows that match the selection and selection args
                return database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
            case ITEMS_ID:
                // Delete a single row given by the ID in the URI
                //rowsDeleted = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted=database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);}
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);


        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);


        switch (match) {
            case ITEMS:

                return updateItem(uri, values, selection, selectionArgs);
            case ITEMS_ID:



                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        if (values.containsKey(Contract.Entry.COLUMN_NAME)) {
            String name = values.getAsString(Contract.Entry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(Contract.Entry.COLUMN_PRICE)) {
            Integer gender = values.getAsInteger(Contract.Entry.COLUMN_PRICE);
            if (gender == null ) {
                throw new IllegalArgumentException("Item requires valid Price");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
//        if (values.containsKey(Contract.Entry.COLUMN_STOCK)) {
//            // Check that the weight is greater than or equal to 0 kg
//            Integer weight = values.getAsInteger(Contract.Entry.COLUMN_STOCK);
//            if (weight == null) {
//                throw new IllegalArgumentException("Item requires valid stock");
//            }
//        }


        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // ye wali line upar update method me likhi hui thi, ye updatePet method hai isme likhni thi
        SQLiteDatabase database = mDbhelper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.Entry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;



    }



}
