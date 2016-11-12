package de.honeypot.honeypot.location.testing;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import de.honeypot.honeypot.R;
import de.honeypot.honeypot.location.GPSProvider;

public class GPSProviderTestActivity extends AppCompatActivity {

    TextView textView1, textView2;

    GPSProvider gpsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsprovider_test);

        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);

        new Thread(new Runnable() {
            @Override
            public void run() {

                gpsProvider = new GPSProvider(GPSProviderTestActivity.this, new GPSListener() {
                    @Override
                    public void onUpdate(Location location) {
                        if(gpsProvider.getGPSLocation() != null)
                            textView1.setText("GPSLoc: " + gpsProvider.getGPSLocation().getLatitude() + "   " + gpsProvider.getGPSLocation().getLongitude());
                        if(gpsProvider.getNetworkLocation() != null)
                            textView2.setText("NetLoc: " + gpsProvider.getNetworkLocation().getLatitude() + "   " + gpsProvider.getNetworkLocation().getLongitude());
                    }
                });


            }
        }).start();
    }
}
