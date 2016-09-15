package com.omegaspocktari.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;

public class InventoryActivity extends AppCompatActivity {

    private static final String PROVIDER_NAME = "com.omegaspocktari.inventoryapp.InventoryProvider.products";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/products");

    /**
     * Below are the relevant arrays for the mCursor adapter for the list view
     */
    private static final String [] COLUMNS_TO_BE_BOUND =  new String[] {
            ProductEntry._ID,
            ProductEntry.COLUMN_PRODUCT_NAME,
            ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[] {
            R.id.product_name,
            R.id.product_price,
            R.id.product_quantity,
    };

    /**
     * Below are the relevant arrays for the mCursor adapter for the detail view
     */
    private static final String [] COLUMNS_TO_BE_BOUND_DETAIL =  new String[] {
            ProductEntry._ID,
            ProductEntry.COLUMN_PRODUCT_NAME,
            ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
            ProductEntry.COLUMN_PRODUCT_PICTURE
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL_DETAIL = new int[] {
            R.id.detail_quantity_id,
            R.id.detail_product_name,
            R.id.detail_product_price,
            R.id.detail_product_quantity,
            R.id.detail_product_picture
    };

    private ListView productListView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        /* Setup FAB to open EditorActivity */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        productListView = (ListView) findViewById(R.id.list);

        adapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.product_list_item,
                null,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0 /* Flags? TODO: Look this up */);

        productListView.setAdapter(adapter);
//        refreshValuesFromContentProvider();

    }

    private void refreshValuesFromContentProvider() {
        CursorLoader cursorLoader = new CursorLoader(getBaseContext(), CONTENT_URI, null, null, null, null);
        Cursor c = cursorLoader.loadInBackground();
        adapter.swapCursor(c);
    }



}


/**
 * res/layout TODOS...
 */

