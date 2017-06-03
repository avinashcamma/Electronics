package com.example.avinash.electronics;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.avinash.electronics.Data.Contract;

/**
 * Created by avina on 16-05-2017.
 */

public class ItemCursorAdapter extends CursorAdapter {
    public ItemCursorAdapter(Context context,Cursor c){
        super(context,c);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView=(TextView) view.findViewById(R.id.name);
        TextView summaryTextView=(TextView) view.findViewById(R.id.type);

        int nameColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_NAME);
        int breedColumnIndex=cursor.getColumnIndex(Contract.Entry.COLUMN_TYPE);

        String petName= cursor.getString(nameColumnIndex);
        String petBreed=cursor.getString(breedColumnIndex);
        nameTextView.setText(petName);
        summaryTextView.setText(petBreed);

    }
}
