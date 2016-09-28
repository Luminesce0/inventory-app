package com.omegaspocktari.inventoryapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michael Baird on 9/6/16.
 */
public class InventoryContract {

    /** CONTENT_URI building blocks */
    //Package
    public static final String CONTENT_AUTHORITY = "com.omegaspocktari.inventoryapp";

    // Content + Package
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";




    /**
     * Inner class that defines constants for the inventory database table that contains individual
     * products of the store.
     */
    public static final class ProductEntry implements BaseColumns {

        /** CONTENT_URI - This will be used to find appropriate table information */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        public static final String stringUri = CONTENT_URI.toString();

        /** CONTENT relevant strings for getType */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /** URI to build items */
        public static Uri buildItemsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * All keys for the columns of the inventory database product table.
         *
         * So from information stored in the database one will be able to order more from a supplier with
         * an intent.
         *          +This means an additional email address column?
         *
         * There will also be pictures.
         *          +This means an additional picture value column?
         *
         * In tracking order shipments will there need to be a date or order time or something that's
         * null otherwise??? So confusing...
         *          +This means a possible additional order time column?
         *
         */
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_CURRENT_QUANTITY = "current_quantity";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_PICTURE = "picture";

        /**
         * Below are the relevant arrays for the mCursor adapter for the list view
         */
        public static final String[] COLUMNS_TO_BE_BOUND = new String[]{
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY
        };

        /**
         * Below are the relevant arrays for the mCursor adapter for the detail view
         */
        public static final String[] COLUMNS_TO_BE_BOUND_DETAIL = new String[]{
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PICTURE
        };



        /**
         * Gallery Image Search or Take a Picture from Camera. Store the path to the image (Like a movie).
         * Acquire that path and store that value as a string. Absolute path of uri. setBackground uri.
         * Tryout.
         */

    }

}
