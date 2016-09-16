package com.omegaspocktari.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract;
import com.omegaspocktari.inventoryapp.data.InventoryDbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${Michael} on 9/13/2016.
 */
public class EditorActivity extends AppCompatActivity {

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
        Uri fileUri = Uri.parse("android.resource://com.omegaspocktari.inventoryapp/" + mPictureImageView.getResources());
        /* Add a new student record */
        ContentValues values = new ContentValues();

        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME,
                mPriceEditText.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                mNameEditText.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY,
                mQuantityEditText.getText().toString());
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_PICTURE,
                fileUri.toString());

        Uri uri = getContentResolver().insert(InventoryProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }

    private void takePicture() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.omegaspocktari.inventoryapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data!=null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPictureImageView.setImageBitmap(imageBitmap);
            }
        }
    }
}
