package com.example.juliagorelikova.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.juliagorelikova.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        final Button buttonBuy = view.findViewById(R.id.buy);


        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        final int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);

        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);
        final long id = cursor.getLong(idColumnIndex);


        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantity) - 1;
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, currentQuantity);
                    String selection = ProductEntry._ID + "?=";
                    Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    String[] selectionArgs = new String[]{String.valueOf(id)};
                    context.getContentResolver().update(currentUri, values, selection, selectionArgs);
                                }
        });

        if(Integer.parseInt(quantity) == 0) {
                  buttonBuy.setEnabled(false);
                  buttonBuy.setText("SOLD");
                  buttonBuy.setTextColor(ContextCompat.getColor(context, R.color.Sold));
        }

    }
}
