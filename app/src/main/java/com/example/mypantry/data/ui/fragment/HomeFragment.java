package com.example.mypantry.data.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.example.mypantry.data.DBManager;
import com.example.mypantry.ItemRecyclerViewAdapter;
import com.example.mypantry.R;
import com.example.mypantry.data.DB_ITEM;
import com.example.mypantry.item.Item;
import com.example.mypantry.item.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    public static DBManager db = null;
    private List<ListItem> test = null;
    private RecyclerView recyclerView = null;
    private View view;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private ItemRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = root;
        if (test == null) {
            test = new ArrayList<>();
        }
        if (recyclerView == null) {
            recyclerView = (RecyclerView) root.findViewById(R.id.list);
        }
        if (db == null) {
            db = new DBManager(getActivity());
        }

        checkDB();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    if (adapter != null) {
                        adapter.getFilter().filter(newText);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkDB();
    }


    public void checkDB() {
        try {
            test.clear();
            Cursor cursor = db.query();
            
            while (cursor.moveToNext()) {

                String barcode = cursor.getString(cursor.getColumnIndex(DB_ITEM.FIELD_SUBJECT));
                String id = cursor.getString(cursor.getColumnIndex(DB_ITEM.FIELD_PRODUCTID));
                String name = cursor.getString(cursor.getColumnIndex(DB_ITEM.FIELD_TEXT));
                String description = cursor.getString(cursor.getColumnIndex(DB_ITEM.FIELD_DATE));
                int quantity = cursor.getInt(cursor.getColumnIndex(DB_ITEM.FIELD_QUANTITY));
                test.add(new ListItem(id, new Item(id, name, description, quantity, barcode)));

            }
        } catch (CursorIndexOutOfBoundsException e) {
            Log.e("Error : checkDB", String.valueOf(e));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemRecyclerViewAdapter(test, db, this);
        recyclerView.setAdapter(adapter);

    }

    public static DBManager getDBistance() {
        return db;
    }
}
