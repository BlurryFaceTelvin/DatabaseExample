package com.example.blurryface.databaseexample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.blurryface.databaseexample.provider.MyContentProvider;

/**
 * Created by BlurryFace on 9/25/2017.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME ="itemDB.db";
    public static final String TABLE_NAME = "item";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productName";
    public static final String COLUMN_PRODUCTQUANTITY = "_quantity";
    private ContentResolver myCR;
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory,int version)
    {
        super(context,DATABASE_NAME,cursorFactory,DATABASE_VERSION);
        myCR = context.getContentResolver();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "Create table "+TABLE_NAME+"("+COLUMN_ID+" integer primary key autoincrement,"+COLUMN_PRODUCTNAME
                +" text,"+COLUMN_PRODUCTQUANTITY+" integer"+")";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteQuery = "drop table if exists "+TABLE_NAME;
        db.execSQL(deleteQuery);
        onCreate(db);
    }
    //method to add data
    public void addData(Product product)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCTNAME,product.getProductName());
        contentValues.put(COLUMN_PRODUCTQUANTITY,product.get_quantity());
        myCR.insert(MyContentProvider.CONTENT_URI,contentValues);

    }
    //method to search for data
    public Product searchData(String name)
    {
        String[] projection = {COLUMN_ID,COLUMN_PRODUCTNAME,COLUMN_PRODUCTQUANTITY};
        String selection = "productName ='"+name+"'";
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,projection,selection,null,null);
        Product product = new Product();
        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            product.set_id(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.set_quantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();

        }
        else
            product = null;
        return product;
    }
    //method to delete data
    public boolean removeData(String name)
    {
        boolean result = false;
        String selection = "productName='"+name+"'";

        int delete = myCR.delete(MyContentProvider.CONTENT_URI,selection,null);
        if(delete>0)
            return true;

        return result;
    }

}
