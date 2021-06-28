package com.example.mypantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypantry.activity.MainActivity;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.login.ListItem;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import kotlin.random.URandomKt;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class    ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<ListItem> mValues;
    private DBManager db;
    private final Fragment fragment;

    public ItemRecyclerViewAdapter(List<ListItem> items, DBManager db, Fragment fragment) {
        mValues = items;
        this.db= db;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(String.valueOf(mValues.get(position).getItem().quantity));
        holder.mDescriptionView.setText(mValues.get(position).getItem().name);
}

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        //public final Switch mSwitch;
        public ListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDescriptionView = (TextView) view.findViewById(R.id.description);

            Button btnAdd = (Button) view.findViewById(R.id.addBtn);//press Add Button to increment Quantity
            btnAdd.setOnClickListener(v-> {
                if(mValues.remove(mItem)) {
                    DummyItem a = mItem.getItem();
                    db.delete(mItem.getItem().productID);
                    db.save(a.add());
                }
                fragment.onStart();                                 //necessary to call onStart function for checkDB and update UI
                    });


            Button btnSub = (Button) view.findViewById(R.id.dltBtn);
            btnSub.setOnClickListener(v-> {         //press Sub Button to decrement Quantity
                if(mItem.getItem().quantity > 1 && mValues.remove(mItem)) {
                    DummyItem a = mItem.getItem();
                    db.delete(mItem.getItem().productID);
                    db.save(a.sub());
                }else if(mItem.getItem().quantity == 1){
                    Snackbar.make(Objects.requireNonNull(fragment.getView()),"Elemento Eliminato",Snackbar.LENGTH_LONG).show();
                    db.delete(mItem.getItem().productID);
                }
                fragment.onStart();
            });

            mDescriptionView.setOnLongClickListener(v->{
               AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

               builder.setTitle(mDescriptionView.getText()).setMessage("\nID Prodotto: "+mItem.getItem().productID+"\nDescription: "+mItem.getItem().details +"\nBarcode: "+mItem.getItem().barcode+"\nQuantity: "+mItem.getItem().quantity);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            });
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}