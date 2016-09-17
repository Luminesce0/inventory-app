package com.omegaspocktari.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.omegaspocktari.inventoryapp.data.InventoryContract;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

/**
 * Created by ${Michael} on 9/11/2016.
 */
public class InventoryProvider extends ContentProvider {
    private static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private InventoryDbHelper inventoryDbHelper = null;
    private SQLiteDatabase db;

    /* This creates a relevant URI where it is getting all or a specific product? */
    private static final int PRODUCTS = 1;
    private static final int PRODUCTS_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH, PRODUCTS);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH + "/#", PRODUCTS_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        inventoryDbHelper = new InventoryDbHelper(context);
        return true;
    }


    /**
     * When this is called it is passed all the relevant information of a query. it then uses a uriMatcher
     * to see which specific uri we will be getting.
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if (uriMatcher.match(uri) == PRODUCTS_ID) {
            /* Query is for one single product. Get the ID from the URI */
            //TODO: Understand any of this...
            id = uri.getPathSegments().get(1);
        }

        //TODO: Review this...
        // What's going on here is that the inventoryDbHelper would have a cursor of its own already
        // created and then it would be acquired with the method. It has if statements that check for
        // the given id and other arguments.
        return inventoryDbHelper.readInventoryInfo(id, projection, selection, selectionArgs, sortOrder);
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            /* Call method to create inventory info */
            long id = inventoryDbHelper.createInventoryInfo(values);

            /* build the items URI with this method */
            Uri returnUri = InventoryContract.buildItemsUri(id);

            /* Notify the adapter of changes */
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if (uriMatcher.match(uri) == PRODUCTS_ID) {
            id = uri.getPathSegments().get(1);
        }
        return inventoryDbHelper.deleteInventoryInfo(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if (uriMatcher.match(uri) == PRODUCTS_ID) {
            id = uri.getPathSegments().get(1);
        }
        return inventoryDbHelper.updateInventoryInfo(id, values);
    }


    /**
     * Handles requests for the MIME type of the data at the given URI.
     * TODO: Study MIME types.
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                return InventoryContract.CONTENT_TYPE;
            case PRODUCTS_ID:
                return InventoryContract.CONTENT_ITEM_TYPE;
        }
        return "";
    }
}
