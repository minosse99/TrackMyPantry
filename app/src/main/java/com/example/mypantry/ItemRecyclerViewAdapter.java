package com.example.mypantry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mypantry.item.Item;
import com.example.mypantry.item.ListItem;

import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
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
        holder.mContentView.setText(String.valueOf(mValues.get(position).getItem().getQuantity()));
        holder.mDescriptionView.setText(mValues.get(position).getItem().getName());
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

            ImageButton btnAdd = (ImageButton) view.findViewById(R.id.addBtn);//press Add Button to increment Quantity
            btnAdd.setOnClickListener(v-> {
                if(mValues.remove(mItem)) {
                    Item a = mItem.getItem();
                    db.delete(mItem.getItem().getProductID());
                    db.save(a.add());
                }
                fragment.onStart();                                 //necessary to call onStart function for checkDB and update UI
                    });


            ImageButton btnSub = (ImageButton) view.findViewById(R.id.dltBtn);
            btnSub.setOnClickListener(v-> {         //press Sub Button to decrement Quantity
                if(mItem.getItem().getQuantity() > 1 && mValues.remove(mItem)) {
                    Item a = mItem.getItem();
                    db.delete(mItem.getItem().getProductID());
                    db.save(a.sub());
                }else if(mItem.getItem().getQuantity() == 1){
                    Snackbar.make(Objects.requireNonNull(fragment.getView()),"Elemento Eliminato",Snackbar.LENGTH_LONG).show();
                    db.delete(mItem.getItem().getProductID());
                }
                fragment.onStart();
            });

            mDescriptionView.setOnLongClickListener(v->{
               AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

               builder.setTitle(mDescriptionView.getText()).setMessage("\nID Prodotto: "+mItem.getItem().getProductID()+"\nDescription: "+mItem.getItem().getDetails() +"\nBarcode: "+mItem.getItem().getBarcode()+"\nQuantity: "+mItem.getItem().getQuantity());
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