package com.omegaspocktari.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

    private static final int PRODUCT_LOADER = 0;
    private static final String LOG_TAG = InventoryDetailActivity.class.getSimpleName();
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
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_list_item);


        /** Initializing the database and gathering all information from a table with a cursor */
        InventoryDbHelper dbHelper = new InventoryDbHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        final Uri uri = getIntent().getData();
        final long uriID = ContentUris.parseId(uri);

        Log.e(LOG_TAG, "CHECK OUT THIS URI PLEASE NOTICE ME IN THE COOOOOODE ::::: " + uri.toString());

        String selection = ProductEntry._ID + "=?";
        String[] selectionArgs = {Long.toString(uriID)};

        Cursor cursor = database.query(ProductEntry.TABLE_NAME,
                ProductEntry.COLUMNS_TO_BE_BOUND_DETAIL,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        final String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY));
        final String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        final String picture = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PICTURE));

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


        /** Formatted Image and Text View strings */
        String quantityText = "Quantity: " + Integer.toString(quantity);
        String priceText = price;
        String productID = "ID: " + Integer.toString(id);

        /** Setting Image and Text views with the relevant data */
        mProductName.setText(name);
        mProductQuantity.setText(quantityText);
        mProductPrice.setText(priceText);
        mProductID.setText(productID);

        Bitmap selectedImage = getBitmapFromUri(Uri.parse(picture));
        if (selectedImage != null) {
            mProductPicture.setImageBitmap(selectedImage);
        }

        /** Creating On Click Listeners */
        mProductDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ProductValidation.checkBlank(mProductEditQuantity)) {
                    if (ProductValidation.checkIsInteger(mProductEditQuantity)) {

                        int quantityVariance = Integer.parseInt(mProductEditQuantity.getText().toString());
                        int quantityChange = quantity - quantityVariance;

                        if (ProductValidation.checkIsPositiveInteger(quantityChange)) {

                            ContentValues values = new ContentValues();
                            values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, quantityChange);
                            Log.e(LOG_TAG, "LOG EXAMPLE OF URI: " + uri.toString());
                            getContentResolver().update(uri, values, null, null);
                            finish();
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
                    if (ProductValidation.checkIsInteger(mProductEditQuantity)) {

                        int quantityVariance = Integer.parseInt(mProductEditQuantity.getText().toString());
                        int quantityChange = quantity + quantityVariance;

                        String where = ProductEntry._ID + "=?";
                        String[] whereArgs = {Integer.toString(id)};

                        ContentValues values = new ContentValues();
                        values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, quantityChange);
                        getContentResolver().update(uri, values, where, whereArgs);
//                        database.update(ProductEntry.TABLE_NAME, values, null, null);
                        finish();
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

                Uri pictureUri = Uri.parse(picture);
                String subject = "Order For: " + name;
                String stream = "Hello! \n"
                        + "We're looking to purchase more stock of " + name + ".\n\n"
                        + "Our current product listing is as follows... \n"
                        + "Product Name: " + name + "\n"
                        + "Product Price: " + price + "\n"
                        + "Product Quantity: " + quantity + "\n";

                Intent orderIntent = ShareCompat.IntentBuilder.from(InventoryDetailActivity.this)
                        .setStream(pictureUri)
                        .setSubject(subject)
                        .setText(stream)
                        .getIntent();

                orderIntent.setData(pictureUri);
                orderIntent.setType("message/rfc822");
                orderIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(orderIntent, "Share with"));

//                orderIntent.setData(Uri.parse("mailto:"));
//                orderIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                orderIntent.putExtra(Intent.EXTRA_TEXT, stream);
//                Log.e(LOG_TAG, "Uri: " + pictureUri);
//                orderIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
//                orderIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (orderIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(orderIntent);
                }

            }
        });
        mProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(InventoryDetailActivity.this);
                deleteAlert.setMessage("Delete Product?");
                deleteAlert.setCancelable(true);
                deleteAlert.setNegativeButton(R.string.delete_product_detail_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /** Delete product */
                        int rowsDeleted = getContentResolver().delete(uri, null, null);
                        Toast.makeText(InventoryDetailActivity.this, "Deleted Rows: " + rowsDeleted, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                deleteAlert.setPositiveButton(R.string.return_product_detail, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });
                deleteAlert.create();
                deleteAlert.show();
            }
        });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        super.onBackPressed();
        return;
    }
}