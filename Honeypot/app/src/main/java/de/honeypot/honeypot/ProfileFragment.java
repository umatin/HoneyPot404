package de.honeypot.honeypot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        int color = Color.parseColor("#ff4081");
        int res = Math.round(getResources().getInteger(R.integer.image_res));
        Bitmap bitmap, colorBitmap = Bitmap.createBitmap(res, res, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(colorBitmap);
        canvas.drawColor(color);

        bitmap = CircularImage.circularProfilePicture(colorBitmap, colorBitmap.getHeight(), true);

        try {
            ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
