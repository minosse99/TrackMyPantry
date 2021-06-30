package com.example.mypantry.ui.shoppingList;

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
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import com.example.mypantry.DBManager;
import com.example.mypantry.R;
import com.example.mypantry.data.ITEM;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingListFragment extends Fragment {

    private DBManager db;
    private List<DummyItem> list;
    private ShoppingListViewModel shoppingListViewModel;
    private String text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(text == null){text = "";}
        shoppingListViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ShoppingListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        Button btnShare = root.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        shoppingListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        if(list == null ){ list = new ArrayList<>();}
        if(db == null) { db = HomeFragment.getDBistance(); }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        compileBody();
        updateText();
    }

    private void updateText() {
        EditText edittext = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.editText);
        edittext.setText(" ");
        for (DummyItem element:list
             ) {
            edittext.append("\n"+element.quantity+"- "+element.name+" : "+element.details+" "+"\n");
        }
    }


    public void compileBody(){
        try {

            list.clear();
            Cursor cursor = db.query();
            while (cursor.moveToNext()) {

                String barcode = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_SUBJECT));
                String id = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_PRODUCTID));
                String name = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_TEXT));
                String description = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_DATE));
                int quantity = cursor.getInt(cursor.getColumnIndex(ITEM.FIELD_QUANTITY));
                list.add(new DummyItem(id,name, description, quantity, barcode));

                Log.e("LIST",text);
            }
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("Error : checkDB", String.valueOf(e));
        }

    }
}