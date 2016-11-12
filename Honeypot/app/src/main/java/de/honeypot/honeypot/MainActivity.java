package de.honeypot.honeypot;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceFragment;
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
import android.view.MenuItem;
import android.widget.Toast;
import de.honeypot.honeypot.services.GPS;
import de.honeypot.honeypot.services.WifiDirect;
import de.honeypot.honeypot.handlers.Network;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init for getting device mac address as soon as possible
        WifiDirect.init(this);

        MainActivity.sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "");

        Network.init(sharedPref.getString("token", ""), sharedPref.getString("id", ""));




        // no saved userName
        if (userName.equals("")) {
            Intent firstLaunchActivity = new Intent(this, FirstLaunchActivity.class);
            startActivity(firstLaunchActivity);
            finish();
            return;
        }

        GPS.init(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2811);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2812);
        }

        // setContentView
        setContentView(R.layout.activity_main);

        // setup PagerAdapter
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
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
