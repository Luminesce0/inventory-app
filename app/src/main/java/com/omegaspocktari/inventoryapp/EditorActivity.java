package com.omegaspocktari.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract;
import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

/**
 * Created by ${Michael} on 9/13/2016.
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();

    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private ImageView mPictureImageView;
    private Button mPictureButton;

    private int mQuantity = 0;
    private InventoryDbHelper inventoryDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_editor);

        /* All relevant views */
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mPictureImageView = (ImageView) findViewById(R.id.edit_product_picture);
        mPictureButton = (Button) findViewById(R.id.edit_product_picture_button);
        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Create this method
                takePicture();
            }
        });

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

                /* Return back to the previous application */
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addProduct() {
        /* Add a new student record */
        ContentValues values = new ContentValues();

        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME,
                mPriceEditText.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                mNameEditText.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
                mQuantityEditText.getText().toString());
        // TODO: Figure this shit out later.
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PICTURE,
                fileUri.toString());

        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        Toast.makeText(getBaseContext(), "Saved Product: " + mPriceEditText.getText().toString(),
                Toast.LENGTH_LONG).show();
    }

}
