package com.example.redha.redhatfriends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Redha on 8/6/2016.
 */
public class FriendsProvider extends ContentProvider {

    private FriendsDatabase mOpenHelper;

    private static String TAG =FriendsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "friends" ,FRIENDS);
        matcher.addURI(authority, "friends/*" ,FRIENDS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FriendsDatabase(getContext());
        return true ;
    }

    private void deleteDataBase(){
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper = new FriendsDatabase(getContext());
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case FRIENDS:
                return FriendsContract.Friends.COTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.COTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int  match  = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match){
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendID(uri);
                queryBuilder.appendWhere(BaseColumns._ID+"="+id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs , null , null,  sortOrder);
        return cursor;

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG,"insert(uri="+uri+", values="+values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS,null, values);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG,"update(uri="+uri+", values="+values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria=selection;
        switch (match){
            case FRIENDS:
                //do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendID(uri);
                selectionCriteria=BaseColumns._ID+"="+id
                        +(!TextUtils.isEmpty(selection) ? " AND ("+ selection + ")" : "");


                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
        }

        return db.update(FriendsDatabase.Tables.FRIENDS,values,selectionCriteria,selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG,"delete(uri="+uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        if(uri.equals(FriendsContract.URI_TABLE)){
            deleteDataBase();
            return 0;
        }


        switch (match){
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendID(uri);
                String selectionCriteria=BaseColumns._ID+"="+id
                        +(!TextUtils.isEmpty(selection) ? " AND ("+ selection + ")" : "");
                return db.delete(FriendsDatabase.Tables.FRIENDS,selectionCriteria,selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
        }
    }
}
