package com.example.avinash.electronics.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by avina on 13-05-2017.
 */

public class Contract  {

    private Contract(){

    }
    public static class Entry implements BaseColumns{

        public static final String CONTENT_AUTHORITY="com.example.avinash.electronics";
        public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
        public static final String PATH_ITEMS="electronics";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;


        public final static String TABLE_NAME="electronics";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_TYPE="type";
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_PRICE="price";
        public final static String COLUMN_STOCK="stock";
        public final static String COLUMN_SUPPLIER="supplier";
        public final static String COLUMN_IMAGE="image";







    }


}
