package com.example.mypantry;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypantry.data.DBManager;
import com.example.mypantry.data.ui.fragment.HomeFragment;
import com.example.mypantry.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
 * TODO: Replace the implementation with code for your data type.
 */
public class  ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final List<Item> mValues;
    private List<Item> fullList;
    private DBManager db;
    private final HomeFragment fragment;

    public ItemRecyclerViewAdapter(List<Item> items, DBManager db, HomeFragment fragment) {
        mValues = items;
        this.db= db;
        this.fragment = fragment;
        fullList = new ArrayList<>(items);
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
        holder.mContentView.setText(String.valueOf(mValues.get(position).getQuantity()));
        holder.mDescriptionView.setText(mValues.get(position).getName());
}

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private Filter productFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();
            if(constraint == null && constraint.length() == 0){
                filteredList.addAll(fullList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for ( Item exampleItem : fullList){
                    if(exampleItem.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(exampleItem);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues.clear();
            mValues.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public Filter getFilter(){
        return productFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDescriptionView = (TextView) view.findViewById(R.id.description);

            ImageButton btnAdd = (ImageButton) view.findViewById(R.id.addBtn);//press Add Button to increment Quantity
            btnAdd.setOnClickListener(v-> {
                Item obj = mItem;
                int i = mValues.indexOf(mItem);
                mValues.remove(obj);
                mValues.add(i,obj.add());
                notifyDataSetChanged();
                fragment.updateDB();
            });

            ImageButton btnSub = (ImageButton) view.findViewById(R.id.dltBtn);
            btnSub.setOnClickListener(v-> {         //press Sub Button to decrement Quantity
                Item obj = mItem;
                if(mItem.getQuantity() > 0 ) {
                    int i = mValues.indexOf(mItem);
                    mValues.remove(obj);
                    mValues.add(i,obj.sub());
                }else if(mItem.getQuantity() == 0){
                    Snackbar.make(Objects.requireNonNull(fragment.getView()),"Elemento Eliminato",Snackbar.LENGTH_LONG).show();
                    db.delete(mItem.getProductID());
                    mValues.remove(obj);
                }
                notifyDataSetChanged();
                fragment.updateDB();

            });

            mDescriptionView.setOnLongClickListener(v->{
               AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
               String content = "\nID Prodotto: "+mItem.getProductID()+"\nDescription: "+mItem.getDetails() +"\nBarcode: "+mItem.getBarcode()+"\nQuantity: "+mItem.getQuantity();
               builder.setTitle(mDescriptionView.getText())
                       .setMessage(content);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(fragment.getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Pantry", content);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText( fragment.getContext() , "Copiato negli Appunti",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setIcon(R.mipmap.ic_launcher);

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