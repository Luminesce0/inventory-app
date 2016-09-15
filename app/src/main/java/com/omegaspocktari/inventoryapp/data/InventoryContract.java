package com.omegaspocktari.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Michael Baird on 9/6/16.
 */
public class InventoryContract {


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
