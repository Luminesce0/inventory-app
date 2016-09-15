package com.omegaspocktari.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

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

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    public void onDeleteTable(Context context, SQLiteDatabase db) {
        db.execSQL(SQL_DROP_PRODUCT_TABLE);
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

    /**
     * Checkit... SQLiteQueryBuilder built up this query.
     * TODO: Look up setTables method.
     * TODO: Look up SQLiteQueryBuilder.
     * @param id
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public Cursor readInventoryInfo(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(ProductEntry.TABLE_NAME);

        if(id != null) {
            sqLiteQueryBuilder.appendWhere("_id" + " = " + id);
        }

        if(sortOrder == null || sortOrder == "") {
            sortOrder = ProductEntry.COLUMN_PRODUCT_NAME;
        }

        Cursor cursor = sqLiteQueryBuilder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public long createInventoryInfo(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(ProductEntry.TABLE_NAME, null, values);
        if(id <= 0) {
            throw new SQLException("failed to add an image");
        }

        return id;
    }

    public int deleteInventoryInfo(String id) {
        if(id == null) {
            return getWritableDatabase().delete(ProductEntry.TABLE_NAME, null, null);
        } else {
            return getWritableDatabase().delete(ProductEntry.TABLE_NAME, "_id=?", new String[]{id});
        }
    }

    public int updateInventoryInfo(String id, ContentValues values) {
        if(id == null) {
            return getWritableDatabase().update(ProductEntry.TABLE_NAME, values, null, null);
        } else {
            return getWritableDatabase().update(ProductEntry.TABLE_NAME, values, "_id=?", new String[]{id});
        }
    }

}
