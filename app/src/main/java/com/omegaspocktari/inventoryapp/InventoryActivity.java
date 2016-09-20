package com.omegaspocktari.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

public class InventoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = InventoryActivity.class.getSimpleName();

    /**
     * Below are the relevant arrays for the mCursor adapter for the list view
     */
    private static final String[] COLUMNS_TO_BE_BOUND = new String[]{
            ProductEntry._ID,
            ProductEntry.COLUMN_PRODUCT_NAME,
            ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[]{
            R.id.product_name,
            R.id.product_price,
            R.id.product_quantity,
    };

    /**
     * Below are the relevant arrays for the mCursor adapter for the detail view
     */
    private static final String[] COLUMNS_TO_BE_BOUND_DETAIL = new String[]{
            ProductEntry._ID,
            ProductEntry.COLUMN_PRODUCT_NAME,
            ProductEntry.COLUMN_PRODUCT_PRICE,
            ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
            ProductEntry.COLUMN_PRODUCT_PICTURE
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL_DETAIL = new int[]{
            R.id.detail_quantity_id,
            R.id.detail_product_name,
            R.id.detail_product_price,
            R.id.detail_product_quantity,
            R.id.detail_product_picture
    };

    private ListView mProductListView;
    private SimpleCursorAdapter adapter;
    private TextView mEmptyStateView;
    private InventoryDbHelper inventoryDbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

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

        /**
         * Use the InventoryDbHelper object to get a writable database and put that into an
         * SQLiteDatabase object to obtain database options to generate content for the cursor
         * to populate the listview/adapter
         */
        displayDatabaseInfo();



    }

    private void displayDatabaseInfo() {

        String[] projection = {
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY
        };

        Cursor cursor = getContentResolver().query(ProductEntry.CONTENT_URI,
                projection, null, null, null);

        ListView listView = (ListView) findViewById(R.id.list);

        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);

        listView.setAdapter(adapter);

        if (adapter.isEmpty()) {
            mEmptyStateView = (TextView) findViewById(R.id.empty_state_text_view);
            mProductListView.setEmptyView(mEmptyStateView);
            mEmptyStateView.setText(R.string.empty_state_view);
        } else {
            mProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    /* onItemclick will allow us to get the specific item clicked's information */
                    Cursor c = (Cursor) adapterView.getItemAtPosition(position);
                    //TODO: is this necessary if we've already got the item at that position?
                    c.moveToPosition(position);
                    long _id = c.getLong(c.getColumnIndexOrThrow(ProductEntry._ID));

                    Intent intent = new Intent(InventoryActivity.this, InventoryDetailActivity.class);
                    intent.putExtra("KEY", _id);
                    startActivity(intent);
                }
            });
        }
    }




}


/**
 * res/layout TODOS...
 */

