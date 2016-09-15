package com.omegaspocktari.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

/**
 * Created by ${Michael} on 9/11/2016.
 */
public class InventoryProvider extends ContentProvider{

    static final String PROVIDER_NAME = "com.omegaspocktari.inventoryapp.provider.products";
    static final String URL = "content://" + PROVIDER_NAME + "/products";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private static final int PRODUCTS = 1;
    private static final int PRODUCTS_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "products", PRODUCTS);
        uriMatcher.addURI(PROVIDER_NAME, "products/#", PRODUCTS_ID);
        return uriMatcher;
    }

    private SQLiteDatabase db;

    /* This stuff is starting to get super confusing... */
    private InventoryDbHelper inventoryDbHelper = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        InventoryDbHelper dbHelper = new InventoryDbHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == PRODUCTS_ID) {
            /* Query is for one single product. Get the ID from the URI */
            //TODO: Understand any of this...
            id = uri.getPathSegments().get(1);
        }

        //TODO: Review this...
        // What's going on here is that the inventoryDbHElper would have a cursor of its own already
        // created and then it would be acquired with the method.
        return inventoryDbHelper.readInventoryInfo(id, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = inventoryDbHelper.createInventoryInfo(values);
            //TODO: Definitely look this up....
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == PRODUCTS_ID) {
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


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                return "";
            case PRODUCTS_ID:
                return "";
        }
        return "";
    }
}
