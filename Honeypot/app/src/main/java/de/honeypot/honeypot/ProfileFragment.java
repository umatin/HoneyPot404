package de.honeypot.honeypot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.honeypot.honeypot.handlers.CircularImage;

import static de.honeypot.honeypot.handlers.StorageHandler.readFileToBitmap;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_profile_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bitmap bitmap = readFileToBitmap(getContext().getExternalFilesDir(null) + File.separator +  "Profile.jpeg");

        int res = CircularImage.relativeImageRes(getActivity());
        if (bitmap != null) {
            bitmap = CircularImage.circularProfilePicture(bitmap, res, true);
        } else {
            int color = Color.parseColor("#ff4081");
            Bitmap colorBitmap = Bitmap.createBitmap(res, res, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(colorBitmap);
            canvas.drawColor(color);

            bitmap = CircularImage.circularProfilePicture(colorBitmap, res, true);
        }

        try {
            ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // initialize Views
        TextView textViewName = (TextView) getView().findViewById(R.id.textViewName);
        TextView textViewScore = (TextView) getView().findViewById(R.id.textViewScore);
        TextView textViewMeetCount = (TextView) getView().findViewById(R.id.textViewMeetCount);
/*

        String name = "", score = "", meetcount = "";

        textViewName.setText(getResources().getString(R.string.name) + ":\n" + name);
        textViewScore.setText(getResources().getString(R.string.score) + ":\n" + score);
        textViewMeetCount.setText(getResources().getString(R.string.meet_count) + ":\n" + meetcount);*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_profile:
                startActivity(new Intent(getActivity(), SettingsLoader.class));
                return true;
        }

        return false;
    }
}
