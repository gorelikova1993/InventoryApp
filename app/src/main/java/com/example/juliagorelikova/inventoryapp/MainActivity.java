package com.example.juliagorelikova.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.juliagorelikova.inventoryapp.data.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int PRODUCT_LOADER = 0;
    ProductCursorAdapter mProductCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView productListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
        mProductCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mProductCursorAdapter);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                Log.i("MainActivity", ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id).toString());
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }

    private void insertProduct(){
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Mars");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 3);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "VCR");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, 889456998);
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection [] = {ProductEntry._ID,
                    ProductEntry.COLUMN_PRODUCT_NAME,
                    ProductEntry.COLUMN_PRODUCT_PRICE,
                    ProductEntry.COLUMN_PRODUCT_QUANTITY};

        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProductCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductCursorAdapter.swapCursor(null);

    }
}
