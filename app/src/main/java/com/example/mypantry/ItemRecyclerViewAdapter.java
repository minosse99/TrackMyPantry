package com.example.mypantry;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mypantry.activity.MainActivity;
import com.example.mypantry.dummy.DummyItem;
import com.example.mypantry.ui.login.ListItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<ListItem> mValues;
    private DBManager db;
    private final Context ctx;

    public ItemRecyclerViewAdapter(List<ListItem> items, DBManager db, Context app) {
        mValues = items;
        this.db= db;
        this.ctx = app;
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
        holder.mContentView.setText(mValues.get(position).getItem().content);
        holder.mDescriptionView.setText(mValues.get(position).getItem().details);

        //holder.mSwitch.setChecked(false);
    }


    public void selectSwitch(final ViewHolder holder){
        holder.mSwitch.setChecked(false);
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        public final Switch mSwitch;
        public ListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mDescriptionView = (TextView) view.findViewById(R.id.description);
            mSwitch = (Switch) view.findViewById(R.id.switch1);

            Button btnDelete = (Button) view.findViewById(R.id.dltBtn);

            btnDelete.setOnClickListener(v->{
               try {
                   Snackbar.make(v, "Elemento eliminato", Snackbar.LENGTH_LONG).show();
                   db.delete(mItem.getKey());
                   mValues.remove(new ListItem(mItem.getKey(),mItem.getItem()));

               }catch (Exception e ){
                   Log.e("Errore", String.valueOf(e));
               }
            });
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}