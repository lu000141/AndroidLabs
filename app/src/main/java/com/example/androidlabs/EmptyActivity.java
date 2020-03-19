package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity {

    Bundle dataToPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        dataToPass = getIntent().getExtras();

        DetailsFragment dFrame = new DetailsFragment();
        dFrame.setArguments(dataToPass);
        dFrame.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.emptyMessageDetailFrame,dFrame)
                .addToBackStack("AnyName").commit();
    }
}
