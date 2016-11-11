package de.honeypot.honeypot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "");

        // no saved userName
        if(userName.equals("")){
            Intent firstLaunchActivity = new Intent(this, FirstLaunchActivity.class);
            startActivity(firstLaunchActivity);
            finish();
        }

        setContentView(R.layout.activity_main);
    }
}
