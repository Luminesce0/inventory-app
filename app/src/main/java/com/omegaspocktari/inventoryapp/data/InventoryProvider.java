package com.omegaspocktari.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;

/**
 * Created by ${Michael} on 9/11/2016.
 */
public class InventoryProvider extends ContentProvider {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private static final int PRODUCTS = 1;
    private static final int PRODUCT_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        /** Sets the integer value for obtaining all rows of the products table */
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS, PRODUCTS);

        /** Sets the integer value for obtaining a single row of the products table */
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    /**
     * Database helper object
     */
    private InventoryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /** Get a readable database */
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        /** Cursor to hold the result of the query */
        Cursor cursor;

        /** Figure out if the URI matcher can match the URI to a specific code */
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                /** Perform a database query given the URI */
                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                /** Extract ID from URI to replace the "?" in the selection with the desired row ID */
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                /** Perform the query on the pets table where the _id equals 3 to return a cursor with that row */
                cursor = database.query(ProductEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        /**
         * Sets notification URI on the cursor so we know what content URI the cursor was created for.
         * If the data at this URI changes then we would know we need to update the cursor.
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for: " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {
        /** Obtain a writable database to add product */
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        /** Add the product to the database with the insert method */
        long id = database.insert(ProductEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            return null;
        }

        /** Notify all listeners that the data has changed with the given product content URI */
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                /** Notify all listeners that the data has changed with the given product content URI */
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    /** Notify all listeners that the data has changed for the pet content URI */
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                /** Extract the number out of the uri */
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for: " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY)) {
            String productQuantity = contentValues.getAsString(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY);

        }

        if (contentValues.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0 ) {
            /** Notify all listeners that the data has changed with the given content URI */
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}