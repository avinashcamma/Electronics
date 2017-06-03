package com.example.avinash.electronics;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.avinash.electronics.Data.Contract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mSupplierEditText;
    private RadioGroup mInstockRadioGroup;
    private RadioButton mInstockRadioButton;
    private Spinner mTypeSpinner;
    private Uri currentPetUri;
    private String mType;
    private String mInstock;
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMG = 1;
    int ITEM_LOADER=0;
    String imgDecodableString;

    private boolean mPetHaschanged;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHaschanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent disintent=getIntent();
        currentPetUri=disintent.getData();
        setupSpinner();



        if (currentPetUri==null){
            setTitle("Add a Item");
        }
        else {
            setTitle("Edit Item");


            getLoaderManager().initLoader(ITEM_LOADER, null, this);

        }
        mTypeSpinner=(Spinner)findViewById(R.id.spinner_type);
        mNameEditText=(EditText)findViewById(R.id.edit_item_name);
        mPriceEditText=(EditText)findViewById(R.id.edit_price);
        mSupplierEditText=(EditText)findViewById(R.id.edit_supllier);
        mInstockRadioGroup=(RadioGroup)findViewById(R.id.radio_group);

        mNameEditText.setOnTouchListener(mTouchListener);
        mTypeSpinner.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);

        setupSpinner();




    }
    private void setupSpinner(){
        Spinner spinner=(Spinner)findViewById(R.id.spinner_type);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.array_type_option,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection=(String)parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals("IC")){
                        mType= "IC";

                    }
                    else if (selection.equals("Capacitor")){
                        mType="Capacitor";

                    }
                    else if (selection.equals("Diode")){
                        mType="Diode";

                    }
                    else if (selection.equals("LED")){
                        mType="LED";

                    }
                    else if (selection.equals("Resistance")){
                        mType="Resistance";

                    }
                    else if (selection.equals("Connector")){
                        mType="Connector";

                    }
                    else {
                        mType="Unknown";

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void onRadioButtonClicked(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radio_yes:
                if(checked){
                    mInstock="Yes";
                    Log.v("Debug Log","value of stock"+mInstock);


                }
                break;
            case R.id.radio_no:
                if (checked){
                    mInstock="No";
                    Log.v("Debug Log","value of stock"+mInstock);

                }
                break;
        }
    }

    private void insertItem() {
        ContentValues value = new ContentValues();


        String nameString = mNameEditText.getText().toString().trim();  // ye teeno input hai name, price, supplier par stock wala nhi liya yaha pe
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String inStock="";
        String selection="";
        if(mInstockRadioGroup.getCheckedRadioButtonId()!=-1){
            int id= mInstockRadioGroup.getCheckedRadioButtonId();
            View radioButton = mInstockRadioGroup.findViewById(id);
            int radioId = mInstockRadioGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) mInstockRadioGroup.getChildAt(radioId);
             selection = (String) btn.getText();
            Log.v("Debug Log","selection= "+selection);
        }


        Log.v("Debud Log","radio value"+selection);
        if(selection.equals("Yes")){ // yha pe radio button se input lene wala code likh diyo mujhe yaad nhi hai
            inStock = "Yes";
            Log.v("Debug Log","value of stock"+inStock);
        }else if(selection.equals("No")){
            inStock = "No";
            Log.v("Debug Log","value of stock"+inStock);
        }


        value.put(Contract.Entry.COLUMN_TYPE, mType);
        value.put(Contract.Entry.COLUMN_NAME, nameString);
        value.put(Contract.Entry.COLUMN_PRICE, priceString);
        value.put(Contract.Entry.COLUMN_SUPPLIER, supplierString);
        value.put(Contract.Entry.COLUMN_STOCK, inStock);
        if (currentPetUri == null){

        Uri newUri = getContentResolver().insert(Contract.Entry.CONTENT_URI, value);

        if (newUri == null) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving Item", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Item saved ", Toast.LENGTH_SHORT).show();
        }
    }
    else {

            int rowsAffected = getContentResolver().update(currentPetUri, value, null, null);

            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertItem();
                finish();
        }
        return super.onOptionsItemSelected(item);
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


        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_NAME);
            int typeColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_TYPE);
            int priceColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_PRICE);
            int instockColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_STOCK);
            int supplierColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_SUPPLIER);


            String petName= cursor.getString(nameColumnIndex);
            String type=cursor.getString(typeColumnIndex);
            String itemPrice=cursor.getString(priceColumnIndex);
            String itemStock=cursor.getString(instockColumnIndex);
            String itemSupplier=cursor.getString(supplierColumnIndex);
            mPriceEditText.setText(itemPrice);
            mSupplierEditText.setText(itemSupplier);
            mNameEditText.setText(petName);
            switch (type) {

                case "IC":
                    mTypeSpinner.setSelection(1);
                    break;
                case "Capacitor":
                    mTypeSpinner.setSelection(2);
                    break;
                case "Diode":
                    mTypeSpinner.setSelection(3);
                    break;
                case "LED":
                    mTypeSpinner.setSelection(4);
                    break;
                case "Resistance":
                    mTypeSpinner.setSelection(5);
                    break;
                case"Connector":
                    mTypeSpinner.setSelection(6);
                    break;
                    default:
                        mTypeSpinner.setSelection(0);

            }
            switch (itemStock){
                case "Yes":
                    mInstockRadioGroup.check(R.id.radio_yes);
                    break;
                case "No":
                    mInstockRadioGroup.check(R.id.radio_no);
                    break;
            }


        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
