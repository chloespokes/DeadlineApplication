package com.example.craftycoders.deadlineapplication.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by simonwalker on 05/05/2016.
 */
public class DeadlineProvider extends ContentProvider
{
    private DbHelper mDbHelper;

    private static final int DEADLINES = 1;
    private static final int DEADLINE_ID = 2;

    private static final UriMatcher sUriMatcher;
    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DeadlinesContract.PROVIDER_NAME,"deadlines", DEADLINES);
        sUriMatcher.addURI(DeadlinesContract.PROVIDER_NAME, "deadlines/#", DEADLINE_ID);
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DbHelper(context);

        return (mDbHelper.getWritableDatabase() == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        switch (sUriMatcher.match(uri)) {
            case DEADLINES:
                c = mDbHelper.getAllDeadlinesProvider();
                break;

            case DEADLINE_ID:
                String deadlineId = uri.getPathSegments().get(1);
                c = mDbHelper.getDeadline(deadlineId);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDbHelper.addDeadline(values);

        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(DeadlinesContract.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (sUriMatcher.match(uri)){
            case DEADLINES:
                count = mDbHelper.deleteAllDeadlines(selection, selectionArgs);
                break;

            case DEADLINE_ID:
                String id = uri.getPathSegments().get(1);
                count = mDbHelper.deleteDeadline(id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (sUriMatcher.match(uri)){
            case DEADLINES:
                //count = db.update(STUDENTS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case DEADLINE_ID:
                String id = uri.getPathSegments().get(1);
                count = mDbHelper.updateDeadline(values, id);

                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
