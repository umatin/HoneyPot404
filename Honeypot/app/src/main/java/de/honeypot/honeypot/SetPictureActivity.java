package de.honeypot.honeypot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SetPictureActivity extends AppCompatActivity {


    ///Constants
    private static int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_picture);

        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                openIntent();

                compressPicture(bitmap, this);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //MainActivity.sharedPref.edit().putBoolean("alreadyHasPicture", true);
        }
    }

    public void compressPicture(Bitmap bmp, Activity activity)
    {
        bmp = cropToSquare(bmp);

        bmp = Bitmap.createScaledBitmap(bmp, 256, 256, false);


        //Write image to file
        String filePath = this.getExternalFilesDir(null) + File.separator +  "Profile.jpeg";

        Toast.makeText(activity, filePath, Toast.LENGTH_LONG).show();
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (bmp.compress(Bitmap.CompressFormat.JPEG, 60 , baos)) {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } else {
                Toast.makeText(activity, "Compression didnt Work", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error ", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap cropToSquare(Bitmap bitmap)
    {
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private void openIntent()
    {
        SetPictureActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Intent mainActivity = new Intent(SetPictureActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }
}
