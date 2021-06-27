package com.example.mypantry.ui.home;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;

import com.example.mypantry.DBManager;
import com.example.mypantry.ItemRecyclerViewAdapter;
import com.example.mypantry.R;
import com.example.mypantry.Utils;
import com.example.mypantry.connection.AuthToken;
import com.example.mypantry.data.ITEM;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.login.DialogLogout;
import com.example.mypantry.ui.login.ListItem;
import com.example.mypantry.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static DBManager db = null;
    private List<ListItem> test = null;
    private RecyclerView recyclerView = null;
    private View view;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = root;
        if(test == null ){ test = new ArrayList<>();}
        if(recyclerView == null){recyclerView = (RecyclerView) root.findViewById(R.id.list); }
        if(db == null) { db = new DBManager(getActivity()); }



        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkDB();

    }

    public void checkDB(){
        try {
            test.clear();

            Cursor cursor = db.query();
                while (cursor.moveToNext()) {

                    String subject = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_SUBJECT));
                    int id = cursor.getInt(cursor.getColumnIndex(ITEM.FIELD_ID));
                    String text = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_TEXT));

                    test.add(new ListItem(id, new DummyItem(text, subject)));
            }
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("Error : checkDB", String.valueOf(e));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(test, db, getContext());

        recyclerView.setAdapter(adapter);

    }


}