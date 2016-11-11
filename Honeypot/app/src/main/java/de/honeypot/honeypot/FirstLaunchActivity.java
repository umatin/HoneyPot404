package de.honeypot.honeypot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.security.AccessController.getContext;

public class FirstLaunchActivity extends AppCompatActivity {

    private EditText NameField;

    private SharedPreferences sharedPref;

    // //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);


        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);




        //String userName = sharedPref.getString("userName", "");


        NameField = (EditText) findViewById(R.id.namefield);
        NameField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    String name = NameField.getText().toString();

                    String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);


                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userName", name);
                    editor.putString("id", android_id);

                    Log.i("name", name);
                    Log.i("ID", android_id);

                    editor.apply();




                    //TODO alles auf die cloud laden...



                    HttpClient client = new DefaultHttpClient();

                    String link = "http://honeypot4431.cloudapp.net";
                    HttpGet request = new HttpGet(link + "/register?device="+android_id+"&name="+name);
                    // replace with your url

                    HttpResponse response;
                    try {
                        response = client.execute(request);

                        Log.i("Response of GET request", response.toString());
                    } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }





                    Intent mainActivity = new Intent(FirstLaunchActivity.this, MainActivity.class);

                    startActivity(mainActivity);



                    return true;
                }
                return false;
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
