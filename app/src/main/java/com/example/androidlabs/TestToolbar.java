package com.example.androidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity {

    Toolbar tb;
    NavigationView navigationView;
    public static final int REQUEST_RESULT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        tb = findViewById(R.id.testToolbar);
        setSupportActionBar(tb);

        //get nav view and set onclick event for each item
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if(itemId == R.id.item_chat){
                    Intent intent = new Intent(TestToolbar.this,ChatRoomActivity.class);
                    startActivity(intent);
                }
                if(itemId == R.id.item_weather){

                    Intent intent = new Intent(TestToolbar.this,WeatherForecast.class);
                    startActivity(intent);
                }
                if(itemId == R.id.item_login){
                    setResult(REQUEST_RESULT);
                    finish();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.choice1){
            Toast.makeText(this,"You clicked on item 1",Toast.LENGTH_LONG).show();
        }

        if(itemId == R.id.choice2){
            Toast.makeText(this,"You clicked on item 2",Toast.LENGTH_LONG).show();
        }

        if(itemId == R.id.choice3){
            Toast.makeText(this,"You clicked on item 3",Toast.LENGTH_LONG).show();
        }

        if(itemId == R.id.choice4){
            Toast.makeText(this,"You clicked on the overflow menu",Toast.LENGTH_LONG).show();
        }

        return true;
    }
}
