package de.honeypot.honeypot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "0");

        // no saved userName
        if (userName.equals("")) {
            Intent firstLaunchActivity = new Intent(this, FirstLaunchActivity.class);
            startActivity(firstLaunchActivity);
            finish();
        }

        // setContentView
        setContentView(R.layout.activity_main);

        // setup PagerAdapter
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //final ActionBar actionBar = getSupportActionBar();

    }

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

        @Override
        public int getCount() {
            return 3;
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
