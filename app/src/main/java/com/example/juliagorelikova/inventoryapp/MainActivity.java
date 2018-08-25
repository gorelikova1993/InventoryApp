package com.example.juliagorelikova.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.juliagorelikova.inventoryapp.data.ProductContract;
import com.example.juliagorelikova.inventoryapp.data.ProductContract.ProductEntry;
import com.example.juliagorelikova.inventoryapp.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {

    private ProductDbHelper mProductDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProductDbHelper = new ProductDbHelper(this);

        insertProduct();

        displayInfoDatabase();

    }

    private void displayInfoDatabase() {
        SQLiteDatabase db = mProductDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ProductEntry.TABLE_NAME, null );

        TextView displayInfo = (TextView)findViewById(R.id.display_info);

        try {
            displayInfo.setText("The inventory table contains: " + cursor.getCount() + " products\n\n");
            displayInfo.append(ProductEntry._ID + " - " +
                ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " - "+
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - "+
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + "\n");


            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            while(cursor.moveToNext()){
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

                displayInfo.append("\n" + currentId + " - "
                        +currentName + " - "
                        + currentPrice + " - "
                        + currentQuantity + " - "
                        + currentSupplierName + " - "
                        + currentSupplierPhoneNumber);
            }
        }

        finally {
            cursor.close();
        }
    }

    private void insertProduct(){
        SQLiteDatabase db = mProductDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        values2.put(ProductEntry.COLUMN_PRODUCT_NAME, "Pencil");
        values2.put(ProductEntry.COLUMN_PRODUCT_PRICE, 3);
        values2.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
        values2.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Konix");
        values2.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "88945678998");

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Notebook");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 4);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 20);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Wiring");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "8945688098");

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        long newRowId2 = db.insert(ProductEntry.TABLE_NAME, null, values2);
    }
}
