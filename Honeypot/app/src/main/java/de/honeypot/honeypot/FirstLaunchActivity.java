package de.honeypot.honeypot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
//import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.honeypot.honeypot.handlers.NetworkAdapter;
import de.honeypot.honeypot.services.WifiDirectAdapter;
//import de.honeypot.honeypot.services.WifiDirect;

public class FirstLaunchActivity extends AppCompatActivity {

    private static EditText nameField;

    private static Button button;

    private static SharedPreferences sharedPref;

    private static SharedPreferences.Editor editor;

    private static RelativeLayout relativeLayout;

    private static Animation fadeIn, fadeOut;

    private static int randomCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);

        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        nameField = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_first_launch);

        // animations
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1500);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator()); //add this
        fadeOut.setDuration(1500);

        // set invisible
        nameField.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);

        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textView);
        textSwitcher.setInAnimation(fadeIn);
        textSwitcher.setOutAnimation(fadeOut);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView Text = new TextView(FirstLaunchActivity.this);
                Text.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                Text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
                return Text;
            }
        });

        textSwitcher.setText(getResources().getString(R.string.first_launch1));

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (randomCount){
                    case 0:
                        textSwitcher.setText(getResources().getString(R.string.first_launch2));
                        break;
                    case 1:
                        textSwitcher.setText(getResources().getString(R.string.first_launch3));
                        break;
                    case 2:
                        nameField.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        break;
                }
                randomCount++;
            }
        });

        // enter key
        nameField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    startAuthentication();
                    return true;
                }
                return false;
            }
        });

        // button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuthentication();
            }
        });
    }

    // handle key press
    private void startAuthentication() {
        final String name = nameField.getText().toString().trim();

        if (name.equals("")) {
            return;
        }

        MainActivity.sharedPreferences.edit().putString("name", name).apply();
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        WifiDirectAdapter.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiDirectAdapter.getInstance().start(this);
    }
}
