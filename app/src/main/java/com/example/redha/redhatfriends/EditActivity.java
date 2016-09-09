package com.example.redha.redhatfriends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Redha on 8/16/2016.
 */
public class EditActivity extends FragmentActivity{
    private final String LOG_TAG=EditActivity.class.getSimpleName();
    private TextView mNameTextView,mEmailTextView,mPhoneTextView;
    private Button mButton ;
    private ContentResolver mContentResolver ;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNameTextView = (TextView)findViewById(R.id.friendName);
        mEmailTextView = (TextView)findViewById(R.id.friendEmail);
        mPhoneTextView  = (TextView)findViewById(R.id.friendPhone);

        Intent intent = getIntent();
        final String _id = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_ID);
        String name = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_NAME);
        String phone = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_PHONE);
        String email = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_EMAIL);

        mNameTextView.setText(name);
        mPhoneTextView.setText(phone);
        mEmailTextView.setText(email);

        mContentResolver=EditActivity.this.getContentResolver();

        mButton = (Button)findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.FRIENDS_NAME,mNameTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE,mPhoneTextView.getText().toString());
                    values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL,mEmailTextView.getText().toString());
                    Uri uri = FriendsContract.Friends.buildFriendUri(_id);
                    int recordsUpdated = mContentResolver.update(uri,values,null,null);
                    Log.d(LOG_TAG,"number of records updated = "+recordsUpdated);
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
