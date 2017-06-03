package com.example.avinash.electronics;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avinash.electronics.Data.Contract;

public class displayResult extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private Uri currentPetUri;
    TextView Dname;

    int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        Intent intent=getIntent();
        currentPetUri=intent.getData();
        getLoaderManager().initLoader(LOADER_ID, null, this);   // initialize karna hota hai pehle
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_del:
                deletePet();

            case R.id.action_edit:
                editItem();

        }
        return super.onOptionsItemSelected(item);
    }
    private void editItem(){

        Intent disintent=new Intent(displayResult.this,EditorActivity.class);
        disintent.setData(currentPetUri);
        startActivity(disintent);

    }


    private void deletePet() {
        if (currentPetUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowDeleted = getContentResolver().delete(currentPetUri, null, null);
            if (rowDeleted== 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }


        }
        finish();

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                Contract.Entry._ID,
                Contract.Entry.COLUMN_NAME,
                Contract.Entry.COLUMN_TYPE,
                Contract.Entry.COLUMN_PRICE,
                Contract.Entry.COLUMN_STOCK,
                Contract.Entry.COLUMN_SUPPLIER


        };
        return new CursorLoader(this,currentPetUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            TextView nameTextView=(TextView) findViewById(R.id.tv_name);
            TextView typeTextView=(TextView) findViewById(R.id.tv_type);
            TextView priceTextView=(TextView)findViewById(R.id.tv_price);
            TextView instock=(TextView)findViewById(R.id.tv_instock);
            TextView supplier=(TextView)findViewById(R.id.tv_supplier);


            int nameColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_NAME);
            int breedColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_TYPE);
            int priceColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_PRICE);
            int instockColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_STOCK);
            int supplierColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_SUPPLIER);


            String petName= cursor.getString(nameColumnIndex);
            String petBreed=cursor.getString(breedColumnIndex);
            String itemPrice=cursor.getString(priceColumnIndex);
            String itemStock=cursor.getString(instockColumnIndex);
            String itemSupplier=cursor.getString(supplierColumnIndex);
            priceTextView.setText(itemPrice);
            instock.setText(itemStock);
            supplier.setText(itemSupplier);
            nameTextView.setText(petName);
            typeTextView.setText(petBreed);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
