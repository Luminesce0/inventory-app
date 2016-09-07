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
     * The name of the database.
     */
    public static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version
     */
    public static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**
         * SQL Code to start up a table.
         *
         * TODO: Possibly add The tracking stuff? Also might be good to implement a picture area.
         * TODO: The above todo must also be done in the InventoryContract.ProductEntry class.
         * TODO: Delete this only when done with the above.
         */
        final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ( "
                +ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                +ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL"
                +ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL"
                +ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY + " INTEGER NOT NULL);";

        /**
         * Runs the given code
         */
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * TODO: I wonder if I'll have to put anything here.
         */
    }

    /**
     * Method to delete database
     * @param context   Context from the location where the method is being called
     * @param db        Database from the relevant context
     */
    public void onDeleteDatabase(Context context, InventoryDbHelper db) {

        db.close();
        context.deleteDatabase(db.getDatabaseName());
    }


}
