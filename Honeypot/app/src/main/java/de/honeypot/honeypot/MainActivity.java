package de.honeypot.honeypot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.logging.Logger;

import de.honeypot.honeypot.handlers.NetworkAdapter;
import de.honeypot.honeypot.services.GPSAdapter;
import de.honeypot.honeypot.services.DeviceDetection;
import de.honeypot.honeypot.services.WifiDirectAdapter;

public class MainActivity extends AppCompatActivity {

    public final static Logger mainLogger = Logger.getLogger("MainActivity");
    public static SharedPreferences sharedPreferences;
    public static MainActivity instance;

    @Override
    public void onPause() {
        super.onPause();
        WifiDirectAdapter.getInstance().stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        GPSAdapter.getInstance().start(this);
        WifiDirectAdapter.getInstance().start(this);
    }

    private class AuthenticationTask extends AsyncTask<String, Integer, Exception> {
        protected Exception doInBackground(String... names) {
            if (names.length < 1) return null;
            try {
                NetworkAdapter.getInstance().authenticate(names[0]);
                DeviceDetection thread = new DeviceDetection();
                thread.start();
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        protected void onPostExecute(Exception e) {
            if (e != null) {
                Toast.makeText(MainActivity.this, "You are not connected to the internet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CaptureTask extends AsyncTask<String, Void, Boolean> {
        public Boolean doInBackground(String... ssid) {
            try {
                NetworkAdapter.getInstance().capture(ssid[0]);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        public void onPostExecute(Boolean success) {
            String text = success ? "You just captured a hotspot!" : "This hotspot is not capturable.";
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("capture", false)) {
            String ssid = getIntent().getStringExtra("ssid");
            new CaptureTask().execute(ssid);
        }

        MainActivity.instance = this;

        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!sharedPreferences.contains("name")) {
            Intent setupActivity = new Intent(this, FirstLaunchActivity.class);
            startActivity(setupActivity);
        }

        AuthenticationTask authTask = new AuthenticationTask();
        authTask.execute(sharedPreferences.getString("name", "default"));

        //GPSAdapter.init(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2811);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2812);
        }

        // setContentView
        setContentView(R.layout.activity_main);

        // setup PagerAdapter
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //final ActionBar actionBar = getSupportActionBar();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_friends)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_stats)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    // PagerAdapter
    public class PagerAdapter extends FragmentPagerAdapter {
        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new Fragment();
            switch (i) {
                case 0:
                    fragment = new ProfileFragment();
                    break;
                case 1:
                    fragment = new FriendsFragment();
                    break;
                case 2:
                    fragment = new StatsFragment();
                    break;
            }
            return fragment;
        }

        // Changed to 2 for 2 tabs
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            String title = "";
            switch (i) {
                case 0:
                    title = getString(R.string.fragment_profile);
                    break;
                case 1:
                    title = getString(R.string.fragment_friends);
                    break;
                case 2:
                    title = getString(R.string.fragment_stats);
                    break;
            }

            return title;
        }
    }
}
