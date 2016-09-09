package com.example.redha.redhatfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(android.R.id.content)==null){
            FriendsListFragment frindsListFragment = new FriendsListFragment();
            fragmentManager.beginTransaction().add(android.R.id.content,frindsListFragment).commit();
        }
      //  setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.addRecord:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteDatabase:
                FriendsDialog dialog = new FriendsDialog();
                Bundle args = new Bundle();
                args.putString(FriendsDialog.DIALOG_TYPE,FriendsDialog.DELETE_DATABASE);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(),"delete-database");
                break;
            case R.id.searchRecords:
                Intent SearchIntent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(SearchIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
