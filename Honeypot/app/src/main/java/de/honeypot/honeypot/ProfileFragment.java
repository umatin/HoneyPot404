package de.honeypot.honeypot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import de.honeypot.honeypot.handlers.CircularImage;
import de.honeypot.honeypot.handlers.NetworkAdapter;

import static de.honeypot.honeypot.handlers.StorageHandler.readFileToBitmap;

public class ProfileFragment extends Fragment {
    private TextView textName, textScore, textFriends;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            updateData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_profile_fragment, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bitmap bitmap = readFileToBitmap(getContext().getExternalFilesDir(null) + File.separator + "Profile.jpeg");

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
        textName = (TextView) getView().findViewById(R.id.textViewName);
        textScore = (TextView) getView().findViewById(R.id.textViewScore);
        textFriends = (TextView) getView().findViewById(R.id.textViewFriends);

    }

    public void updateData() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            int profileID = getActivity().getIntent().getIntExtra("profile", -1);
            String device = getActivity().getIntent().getStringExtra("device");
            if (profileID != -1) {
                if (getActivity().getIntent().getBooleanExtra("discover", false)) {
                    MeetTask meetTask = new MeetTask();
                    meetTask.execute(device);
                }
                ProfileTask profileTask = new ProfileTask();
                profileTask.execute(profileID);
            } else {
                OwnProfileTask task = new OwnProfileTask();
                task.execute();
            }
        } else {
            OwnProfileTask task = new OwnProfileTask();
            task.execute();
        }
    }

    private void setProfile(NetworkAdapter.Profile profile) {
        textScore.setText(profile.getPoints() + "");
        textName.setText("" + profile.getName());
        textFriends.setText("" + profile.getFriendsCount() + "");
    }

    private class MeetTask extends AsyncTask<String, Void, Boolean> {
        public Boolean doInBackground(String... device) {
            try {
                NetworkAdapter.getInstance().meet(device[0]);
            } catch (IOException e) {
                return true;
            }
            return false;
        }

        public void onPostExecute(Boolean result) {
            String text = result ? "Oh, ah known friend!" : "You have just met another honeybee!";
            Snackbar.make(getView(), text, Snackbar.LENGTH_LONG).show();
        }
    }

    private class OwnProfileTask extends AsyncTask<Void, Void, NetworkAdapter.Profile> {
        public NetworkAdapter.Profile doInBackground(Void... v) {
            try {
                return NetworkAdapter.getInstance().getOwnProfile();
            } catch (Exception e) {
                return null;
            }
        }

        public void onPostExecute(NetworkAdapter.Profile results) {
            if (results != null) {
                setProfile(results);
            }
        }
    }

    private class ProfileTask extends AsyncTask<Integer, Void, NetworkAdapter.Profile> {
        public NetworkAdapter.Profile doInBackground(Integer... ids) {
            try {
                return NetworkAdapter.getInstance().getProfile(ids[0]);
            } catch (Exception e) {
                return null;
            }
        }

        public void onPostExecute(NetworkAdapter.Profile results) {
            if (results != null) {
                setProfile(results);
            }
        }
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
                Intent pictureActivity = new Intent(getContext(), SetPictureActivity.class);
                startActivity(pictureActivity);
                //getActivity().getIntent().putExtra("profile", -1);
                //updateData();
                return true;
        }

        return false;
    }
}
