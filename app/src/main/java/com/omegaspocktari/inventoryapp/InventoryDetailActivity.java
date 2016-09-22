package com.omegaspocktari.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;
import com.omegaspocktari.inventoryapp.data.ProductValidation;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by ${Michael} on 9/14/2016.
 */



public class InventoryDetailActivity extends AppCompatActivity {

    /**
     * Views
     */
    private ImageView mProductPicture;
    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mProductQuantity;
    private TextView mProductID;
    private EditText mProductEditQuantity;
    private Button mProductDecreaseQuantity;
    private Button mProductIncreaseQuantity;
    private Button mProductOrder;
    private Button mProductDelete;

    private static final int PRODUCT_LOADER = 0;
    private Uri mCurrentProductUri;
    private static final String LOG_TAG = InventoryDetailActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.product_detail_list_item);

        /** Initializing the database and gathering all information from a table with a cursor */
        InventoryDbHelper dbHelper = new InventoryDbHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        final Uri uri = getIntent().getData();
        long uriID = ContentUris.parseId(uri);

        String selection = ProductEntry._ID + "=?";
        String[] selectionArgs = {Long.toString(uriID)};

        Cursor cursor = database.query(ProductEntry.TABLE_NAME,
                ProductEntry.COLUMNS_TO_BE_BOUND_DETAIL,
                selection,
                selectionArgs,
                null,
                null,
                null);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        String picture = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PICTURE));

        /** Initializing all relevant views within the product detail list item view */
        mProductPicture = (ImageView) findViewById(R.id.detail_product_picture);
        mProductName = (TextView) findViewById(R.id.detail_product_name);
        mProductPrice = (TextView) findViewById(R.id.detail_product_price);
        mProductQuantity = (TextView) findViewById(R.id.detail_product_quantity);
        mProductID = (TextView) findViewById(R.id.detail_product_id);
        mProductEditQuantity = (EditText) findViewById(R.id.product_button_edit_text_field);
        mProductDecreaseQuantity = (Button) findViewById(R.id.detail_button_decrease);
        mProductIncreaseQuantity = (Button) findViewById(R.id.detail_button_increase);
        mProductOrder = (Button) findViewById(R.id.detail_button_order);
        mProductDelete = (Button) findViewById(R.id.detail_button_delete);

        /** Creating On Click Listeners */
        mProductDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ProductValidation.checkBlank(mProductEditQuantity)) {
                    if (!ProductValidation.checkIsInteger(mProductEditQuantity)){

                        int quantityVariance = Integer.parseInt(mProductEditQuantity.getText().toString());
                        int quantityChange = quantity - quantityVariance;

                        if (ProductValidation.checkIsPositiveInteger(quantityChange)) {

                            String where = ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY + "=?";
                            String[] whereArgs = {Integer.toString(quantityChange)};

                            ContentValues values = new ContentValues();
                            values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, quantityChange);
                            getContentResolver().update(uri, values, where, whereArgs);
                            database.update(ProductEntry.TABLE_NAME, values, null, null);
                        } else {
                            Toast.makeText(InventoryDetailActivity.this, "Quantity Cannot Go Below 0...", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(InventoryDetailActivity.this, "Edit Quantity Field Is Not Integer...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(InventoryDetailActivity.this, "Edit Quantity Field Is Blank...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mProductIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ProductValidation.checkBlank(mProductEditQuantity)) {
                    if (!ProductValidation.checkIsInteger(mProductEditQuantity)){

                        int quantityVariance = Integer.parseInt(mProductEditQuantity.getText().toString());
                        int quantityChange = quantity + quantityVariance;

                        String where = ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY + "=?";
                        String[] whereArgs = {Integer.toString(quantityChange)};

                        ContentValues values = new ContentValues();
                        values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, quantityChange);
                        getContentResolver().update(uri, values, where, whereArgs);
                        database.update(ProductEntry.TABLE_NAME, values, null, null);
                    } else {
                        Toast.makeText(InventoryDetailActivity.this, "Edit Quantity Field Is Not Integer", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(InventoryDetailActivity.this, "Edit Quantity Field Is Blank...", Toast.LENGTH_SHORT).show();
                }

            }
            });
        mProductOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        /** Setting Image and Text views with the relevant data */
        mProductName.setText(name);
        mProductQuantity.setText(Integer.toString(quantity));
        mProductPrice.setText(Integer.toString(price));
        mProductID.setText(Integer.toString(id));

        Bitmap selectedImage = getBitmapFromUri(Uri.parse(picture));
        if (selectedImage != null) {
            mProductPicture.setImageBitmap(selectedImage);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error closing ParcelFile Descriptor");
            }
        }
    }
}