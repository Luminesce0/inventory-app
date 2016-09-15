package com.omegaspocktari.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

/**
 * Created by ${Michael} on 9/13/2016.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * Edit Text Fields
     */

    private EditText mNameEditText;
    private EditText mCurrentQuantityEditText;
    private EditText mPriceEditText;
    private EditText mPictureEditText;

    private int mQuantity = 0;
    private InventoryDbHelper inventoryDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_editor);

        /* All relevant views */
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mCurrentQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate menu options from the res menu file. Adds menu items to app bar. */
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                /**
                 * Grabs the relevant files and then allocates them to a Content Values object.
                 * Thereafter, a uri is generated obtaining the content values, URI and content resolver.
                 * It then uses a method from InventoryProvider to insert the information into the
                 * database.
                 */
                addProduct();

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addProduct() {

        EditText productQuantity = (EditText) findViewById(R.id.edit_product_quantity);
        EditText productPrice = (EditText) findViewById(R.id.edit_product_price);
        EditText productName = (EditText) (EditText) findViewById(R.id.edit_product_name);
        ImageView productPicture = (ImageView) findViewById(R.id.edit_product_picture);

        Uri fileUri = Uri.parse("android.resource://com.omegaspocktari.inventoryapp/" + productPicture.getResources());
        /* Add a new student record */
        ContentValues values = new ContentValues();

        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME,
                productPrice.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                productName.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
                productQuantity.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PICTURE,
                fileUri.toString());

        Uri uri = getContentResolver().insert(InventoryProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }
}
