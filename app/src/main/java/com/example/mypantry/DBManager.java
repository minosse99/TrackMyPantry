package com.example.mypantry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.mypantry.data.DB_ITEM;

import com.example.mypantry.item.Item;

public class DBManager{

    private ItemDBHelper dbhelper;

    public DBManager(Context context){
        dbhelper=new ItemDBHelper(context);
    }

    public void save(String object, String barcode, String description,int quantity,String productid)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DB_ITEM.FIELD_PRODUCTID,productid);
        cv.put(DB_ITEM.FIELD_SUBJECT, barcode);
        cv.put(DB_ITEM.FIELD_TEXT, object);
        cv.put(DB_ITEM.FIELD_DATE, description);
        cv.put(DB_ITEM.FIELD_QUANTITY,quantity);
        try {
            db.insert(DB_ITEM.TBL_NAME, null,cv);
        }
        catch (SQLiteException sqle) {
            Log.e("DB",sqle.getMessage());
        }
    }

    public void save(Item item){
        save(item.getName(),item.getBarcode(),item.getDetails(),item.getQuantity(),item.getProductID());
    }

    public boolean delete(String id)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            if (db.delete(DB_ITEM.TBL_NAME, DB_ITEM.FIELD_PRODUCTID+"=?", new String[]{(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }
    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query(false, DB_ITEM.TBL_NAME,null, null , null , null, null, null, null);

        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

}
/**
 * Query the given URL, returning a {@link Cursor} over the result set.
 *
 * @param distinct true if you want each row to be unique, false otherwise.
 * @param table The table name to compile the query against.
 * @param columns A list of which columns to return. Passing null will
 *            return all columns, which is discouraged to prevent reading
 *            data from storage that isn't going to be used.
 * @param selection A filter declaring which rows to return, formatted as an
 *            SQL WHERE clause (excluding the WHERE itself). Passing null
 *            will return all rows for the given table.
 * @param selectionArgs You may include ?s in selection, which will be
 *         replaced by the values from selectionArgs, in order that they
 *         appear in the selection. The values will be bound as Strings.
 * @param groupBy A filter declaring how to group rows, formatted as an SQL
 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
 *            will cause the rows to not be grouped.
 * @param having A filter declare which row groups to include in the cursor,
 *            if row grouping is being used, formatted as an SQL HAVING
 *            clause (excluding the HAVING itself). Passing null will cause
 *            all row groups to be included, and is required when row
 *            grouping is not being used.
 * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
 *            (excluding the ORDER BY itself). Passing null will use the
 *            default sort order, which may be unordered.
 * @param limit Limits the number of rows returned by the query,
 *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
 * @return A {@link Cursor} object, which is positioned before the first entry. Note that
 * {@link Cursor}s are not synchronized, see the documentation for more details.
 */