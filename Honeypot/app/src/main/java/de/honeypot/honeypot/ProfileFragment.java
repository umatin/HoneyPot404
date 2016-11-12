package de.honeypot.honeypot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.honeypot.honeypot.handlers.CircularImage;

import static de.honeypot.honeypot.handlers.StorageHandler.readFileToBitmap;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bitmap bitmap = readFileToBitmap("iconUser.png");
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

    }
}
