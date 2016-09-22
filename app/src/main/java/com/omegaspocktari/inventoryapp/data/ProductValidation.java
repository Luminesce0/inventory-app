package com.omegaspocktari.inventoryapp.data;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by ${Michael} on 9/21/2016.
 */
public class ProductValidation {
    private static final String LOG_TAG = ProductValidation.class.getSimpleName();

    public static boolean checkBlank(EditText editTextField) {
        return editTextField.getText().toString().trim().length() <= 0;
    }

    public static boolean checkIsFloat(EditText editTextField) {
        try {
            Float.parseFloat(editTextField.getText().toString());
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing float: " + editTextField.getText().toString(), e);
            return false;
        }
    }

    public static boolean checkIsInteger(EditText editTextField) {
        try {
            Integer.parseInt(editTextField.getText().toString());
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error parsing int: " + editTextField.getText().toString(), e);
            return false;
        }
    }

    public static boolean checkIsPositiveInteger(int integer) {
        if (integer >= 0) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean checkBlank(ImageView imageViewField) {
        if (imageViewField.getDrawable() == null) {
            return true;
        } else {
            return false;
        }
    }

}
