package com.omegaspocktari.inventoryapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michael Baird on 9/6/16.
 */
public class InventoryContract {

    /* CONTENT_URI building blocks */
    //Package
    public static final String CONTENT_AUTHORITY = "com.omegaspocktari.inventoryapp";
    // Content + Package
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "products";
    public static final String PROVIDER_PATH = ".InventoryProvider";

    /* CONTENT_URI */
    public static final Uri CONTENT_URI_PROVIDER = Uri.parse(BASE_CONTENT_URI + PROVIDER_PATH);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
            appendPath(PATH).build();
    public static final String stringUri = CONTENT_URI.toString();

    /* CONTENT relevant strings for getType */
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH;

    /* URI to build items */
    public static Uri buildItemsUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
    /**
     * Inner class that defines constants for the inventory database table that contains individual
     * products of the store.
     */
    public static final class ProductEntry implements BaseColumns {


        /**
         * All keys for the columns of the inventory database product table.
         *<
         * TODO: Continued from activity_inventory
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
         * Gallery Image Search or Take a Picture from Camera. Store the path to the image (Like a movie).
         * Acquire that path and store that value as a string. Absolute path of uri. setBackground uri.
         * Tryout.
         */

    }

}
