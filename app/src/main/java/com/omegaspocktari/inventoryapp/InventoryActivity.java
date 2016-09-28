package com.omegaspocktari.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = InventoryActivity.class.getSimpleName();

    /**
     * Identification number for switch statement to identify the current loader
     */
    private static final int PRODUCT_LOADER = 0;

    /**
     * Global cursor adapter
     */
    InventoryCursorAdapter mInventoryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        /** Setup FAB to open EditorActivity */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);

                startActivity(intent);
            }
        });

        /** Obtain the list view */
        ListView productListView = (ListView) findViewById(R.id.list);

        /** Find empty view and set it to the List View's blank state */
        View emptyView = findViewById(R.id.empty_state_text_view);
        productListView.setEmptyView(emptyView);

        /** Adapter to create list items for each row of product data within the cursor */

        mInventoryCursorAdapter = new InventoryCursorAdapter(this, null);
        productListView.setAdapter(mInventoryCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /** Intent to detail activity */
                Intent intent = new Intent(InventoryActivity.this, InventoryDetailActivity.class);

                /** Create a URI with the correct ID */
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                /** Add that URI data to the intent */
                intent.setData(currentProductUri);

                /** Start the DetailActivity */
                startActivity(intent);

            }
        });

        /** Kickoff the loader */
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_table:
                deleteTable();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTable() {
        getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case PRODUCT_LOADER:
                /** Return a new CursorLoader */
                return new CursorLoader(this,
                        ProductEntry.CONTENT_URI,
                        ProductEntry.COLUMNS_TO_BE_BOUND,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mInventoryCursorAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mInventoryCursorAdapter.swapCursor(null);
    }
}

