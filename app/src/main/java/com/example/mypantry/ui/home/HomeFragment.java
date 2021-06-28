package com.example.mypantry.ui.home;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;

import com.example.mypantry.DBManager;
import com.example.mypantry.ItemRecyclerViewAdapter;
import com.example.mypantry.R;
import com.example.mypantry.data.ITEM;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.login.ListItem;

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


/*        db.save("Patate","304323943025","Patate al forno ",1,"2974833240");

        db.save("Wurstel ","3083948275025","Wurstel di Suino ",1,"7234598970");
        db.save("Fragola","304323943025","Fragole ",1,"111123831220");
  */      return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkDB();
        if(test != null){Log.d("Test",test.toString());}

    }

    public void checkDB(){
        try {
            test.clear();

           Cursor cursor = db.query();
                while (cursor.moveToNext()) {

                    String barcode = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_SUBJECT));
                    String id = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_PRODUCTID));
                    String name = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_TEXT));
                    String description = cursor.getString(cursor.getColumnIndex(ITEM.FIELD_DATE));
                    int quantity = cursor.getInt(cursor.getColumnIndex(ITEM.FIELD_QUANTITY));
                    test.add(new ListItem(id, new DummyItem(id,name, description, quantity, barcode)));

                    }
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("Error : checkDB", String.valueOf(e));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(test, db, this);
        recyclerView.setAdapter(adapter);

    }


    public static DBManager getDBistance(){
        return db;
    }
}

/*
* test -> List<ListItem>

*  ListItem-> chiave , DummyItem
*
*
* */