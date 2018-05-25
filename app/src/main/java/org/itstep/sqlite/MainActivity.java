package org.itstep.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FeedReaderDbHelper(this);
        insert();
        Cursor cursor = query();
        read(cursor);
        update();
        delete();
    }

    @Override
    protected void onDestroy()
    {
        dbHelper.close();
        super.onDestroy();
    }

    private void update()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// New value for one column
        String title = "MyNewTitle";
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE, title);

// Which row to update, based on the title
        String selection = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {"Hello"};

        int count = db.update(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private void delete()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define 'where' part of query.
        String selection = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {"Hello"};
        // Issue SQL statement.
        int deletedRows = db.delete(FeedReaderDbHelper.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void read(Cursor cursor)
    {
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();
    }

    private Cursor query()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = new String[]{
                BaseColumns._ID,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"Hello!"};

// How you want the results sorted in the resulting Cursor
        String sortOrder = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        return cursor;
    }

    private void insert()
    {
        //Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE, "Hello!");
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE, "My db :)");

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderDbHelper.FeedEntry.TABLE_NAME, null, values);
    }
}
