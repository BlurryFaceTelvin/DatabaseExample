package com.example.blurryface.databaseexample.provider;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.blurryface.databaseexample.MyDBHandler;

public class MyContentProvider extends ContentProvider {
    //Authority
    private static final String AUTHORITY = "com.example.blurryface.databaseexample.provider.MyContentProvider";
    private static final String TABLE_NAME = "items";
    //content URI Which is Authority/TableName
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    //variables to check the URI type
    private static final int ITEMS = 1;
    private static final int ITEMS_ID = 2;
    private static final UriMatcher sUriMatched = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatched.addURI(AUTHORITY,TABLE_NAME,ITEMS);
        sUriMatched.addURI(AUTHORITY,TABLE_NAME+"/#",ITEMS_ID);
    }
    //instance of MYDBhandler
    private MyDBHandler myDB;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int rowDelete = 0;
        //check validity of the uri
        int UriType = sUriMatched.match(uri);
        //database
        SQLiteDatabase db = myDB.getWritableDatabase();
        switch (UriType)
        {
            case ITEMS:
                rowDelete = db.delete(MyDBHandler.TABLE_NAME,selection,selectionArgs);
                break;
            case ITEMS_ID:
                String id  = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowDelete = db.delete(MyDBHandler.TABLE_NAME,MyDBHandler.COLUMN_ID+"="+id,null);
                else
                    rowDelete = db.delete(MyDBHandler.TABLE_NAME,MyDBHandler.COLUMN_ID+"="+id+" and "+selection,selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowDelete;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        //check whether the URI is valid
        int UriType = sUriMatched.match(uri);
        //get a writeable database
        SQLiteDatabase db = myDB.getWritableDatabase();
        long id = 0;
        switch (UriType)
        {
            case ITEMS:
                id = db.insert(MyDBHandler.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(TABLE_NAME+"/"+id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        myDB = new MyDBHandler(getContext(),null,null,3);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //have a writeable database
        SQLiteDatabase db = myDB.getWritableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(MyDBHandler.TABLE_NAME);
        //check whether the uri is valid
        int UriType = sUriMatched.match(uri);
        switch (UriType)
        {
            case ITEMS_ID:
                sqLiteQueryBuilder.appendWhere(MyDBHandler.COLUMN_ID+"="+uri.getLastPathSegment());
                break;
            case ITEMS:
                break;
            default:
                throw new IllegalArgumentException("Unknown uri"+uri);
        }
        Cursor cursor = sqLiteQueryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int rowsUpdated = 0;
        //check validity of the URI
        int UriType = sUriMatched.match(uri);
        //database
        SQLiteDatabase db = myDB.getWritableDatabase();
        switch (UriType)
        {
            case ITEMS:
                rowsUpdated = db.update(MyDBHandler.TABLE_NAME,values,selection,selectionArgs);
                break;
            case ITEMS_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection))
                    rowsUpdated = db.update(MyDBHandler.TABLE_NAME,values,MyDBHandler.COLUMN_ID+"="+id,null);
                else
                    rowsUpdated = db.update(MyDBHandler.TABLE_NAME,values,MyDBHandler.COLUMN_ID+"="+id+" and "+selection,selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }
}
