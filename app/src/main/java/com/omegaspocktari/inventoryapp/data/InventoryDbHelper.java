package com.omegaspocktari.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;

/**
 * Created by Michael Baird on 9/6/16.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {

    /**
     * Used for logging. Application tag.
     */
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /**
     * Database version
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * The name of the database.
     */
    public static final String DATABASE_NAME = "inventory.db";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * SQL Code to start up a table.
     *
     * TODO: Possibly add The tracking stuff? Also might be good to implement a picture area.
     * TODO: The above todo must also be done in the InventoryContract.ProductEntry class.
     * TODO: Delete this only when done with the above.
     */
    final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ( "
            +ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
            +ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
            +ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            +ProductEntry.COLUMN_PRODUCT_PICTURE + " TEXT NOT NULL);";

    final String SQL_DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

    final public String SQL_FULL_QUERY = "SELECT * FROM " + ProductEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * TODO: I wonder if I'll have to put anything here.
         */
    }
}
