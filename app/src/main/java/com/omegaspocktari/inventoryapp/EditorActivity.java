package com.omegaspocktari.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract;
import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;
import com.omegaspocktari.inventoryapp.data.ProductValidation;

import java.io.IOException;

/**
 * Created by ${Michael} on 9/13/2016.
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private ImageView mPictureImageView;
    private Button mPictureButton;
    private boolean mProductHasChanged;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };
    private Uri uri;
    private Uri mPictureUri;

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

            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                // Show only images, no videos or anything else
                checkWriteToExternalPerms();
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPictureImageView.setOnTouchListener(mTouchListener);
        mPictureButton.setOnTouchListener(mTouchListener);

        Log.e(LOG_TAG, "Loader Manager has been set.");
    }

    /**
     *
     */
    private void checkWriteToExternalPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Log.e(LOG_TAG, "URI: " + uri.toString());
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null

            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.edit_product_picture);
                imageView.setImageBitmap(bitmap);

                mPictureUri = uri;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
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
                if (addProduct()) {
                    finish();
                    return true;
                } else {
                    return false;
                }

                /* Return back to the previous application */

            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateField(EditText productName, EditText productPrice, EditText productQuantity,
                                  ImageView productPicture) {
        /** Checks to see if all fields are present */
        if (!ProductValidation.checkBlank(productName)
                && !ProductValidation.checkBlank(productPrice)
                && !ProductValidation.checkBlank(productQuantity)
                && !ProductValidation.checkBlank(productPicture)) {
            try {
                if (!ProductValidation.checkIsFloat(productPrice) || !ProductValidation.checkIsInteger(productQuantity)) {
                    throw new IllegalArgumentException("Incorrect Params");
                } else {
                    return true;
                }
            } catch (Exception e) {
                Toast.makeText(EditorActivity.this, "Price isn't Float or Quantity isn't integer...", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(EditorActivity.this, "Empty Inputs Detected", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean addProduct() {
        /** Check to see if adding the product is possible */
        if (!validateField(mNameEditText, mPriceEditText, mQuantityEditText, mPictureImageView)) {
            return false;
        } else {
        /* Add a new student record */
            ContentValues values = new ContentValues();

            float floatPrice = Float.valueOf(mPriceEditText.getText().toString());
            String price = ProductValidation.formatFloat(floatPrice);

            values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME,
                    mNameEditText.getText().toString());
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE,
                    price);
            values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PICTURE,
                    mPictureUri.toString());
            values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
                    mQuantityEditText.getText().toString());
            getContentResolver().insert(ProductEntry.CONTENT_URI, values);

            Toast.makeText(getBaseContext(), "Saved Product: " + mNameEditText.getText().toString(),
                    Toast.LENGTH_LONG).show();
            return true;
        }
    }
}


