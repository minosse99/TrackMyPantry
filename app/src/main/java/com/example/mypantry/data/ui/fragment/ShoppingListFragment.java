package com.example.mypantry.data.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.mypantry.Utils;
import com.example.mypantry.data.DBManager;
import com.example.mypantry.R;
import com.example.mypantry.data.DB_ITEM;
import com.example.mypantry.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingListFragment extends Fragment {

    private DBManager db;
    private List<Item> list;
    private String text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(text == null){text = "";}
        View root = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        Button btnShare = root.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v->{
            EditText editText = Objects.requireNonNull(getActivity()).findViewById(R.id.editText);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
       
        if(list == null ){ list = Utils.getList();}
        if(db == null) { db = Utils.getDBIstance(); }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateText();
    }

    private void updateText() {
        EditText edittext = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.editText);
        edittext.setText(" ");
        for (Item element:list
             ) {
            if(element.getQuantity() < 1)
                edittext.append(element.getName()+" : "+element.getDetails()+" "+"\n");
        }
    }


}