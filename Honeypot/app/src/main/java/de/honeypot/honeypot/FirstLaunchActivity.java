package de.honeypot.honeypot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.duration;
import static java.security.AccessController.getContext;

public class FirstLaunchActivity extends AppCompatActivity {

    private EditText NameField;

    private SharedPreferences sharedPref;

    private SharedPreferences.Editor editor;

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

                    final String name = NameField.getText().toString();

                    final String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);


                    editor = sharedPref.edit();
                    editor.putString("userName", name);
                    editor.putString("device", android_id);



                    editor.apply();

                    Log.i("name", name);
                    Log.i("device", android_id);


                    //TODO alles auf die cloud laden...



                    Thread t2 = new Thread() {
                        public void run() {


                            HttpClient client = new DefaultHttpClient();

                            String link = "http://honeypot4431.cloudapp.net";
                            HttpGet request = new HttpGet(link + "/register?device=" + android_id + "&name=" + name);
                            // replace with your url


                            HttpResponse response;
                            try {
                                response = client.execute(request);
                                //ResponseUtils.toString(response);

                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                response.getEntity().writeTo(out);
                                String responseString = out.toString();
                                out.close();

                                Log.i("Response of GET request", responseString);
                                //JsonReader reader = new JsonReader(response);
                                JSONObject jObject = new JSONObject(responseString);

                                String token = jObject.getString("token");
                                String id = jObject.getString("id");

                                Log.i("token", token);
                                Log.i("id", id);


                                //SharedPreferences.Editor editor = sharedPref.edit();
                                FirstLaunchActivity.this.editor.putString("token", token);
                                editor.putString("id", id);


                                editor.apply();



                            } catch (ClientProtocolException e) {  // falls kein Internet --> zurück

                                e.printStackTrace();
                                editor.putString("userName", "");
                                editor.apply();
                                //return false;
                                //openIntent();
                                FirstLaunchActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(FirstLaunchActivity.this, "Do hasch kein Internet", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return;

                                //Problem: fehler abfangen
                            } catch (IOException e) {

                                e.printStackTrace();
                                //return false;
                                //openIntent();

                                editor.putString("userName", "");
                                editor.apply();

                                FirstLaunchActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(FirstLaunchActivity.this, "Do hasch kein Internet", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("Server Fehler", "");
                            }
                            openIntent();

                        }
                    };


                    t2.start();






                    return true;
                }
                return false;
            }
        });


    }

    private void openIntent(){
        FirstLaunchActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Intent mainActivity = new Intent(FirstLaunchActivity.this, MainActivity.class);

                startActivity(mainActivity);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
