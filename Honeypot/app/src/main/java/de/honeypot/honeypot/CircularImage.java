package de.honeypot.honeypot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Display;
import android.view.WindowManager;

public class CircularImage {

    // make profile picture
    // res = resolution in px
    // dark = Dark Theme
    public Bitmap circularProfilePicture(Bitmap image, Context c, int res, boolean dark) {
        Bitmap bitmap;

        // color to overlay
        int color = dark ? Color.WHITE : Color.DKGRAY;
        Bitmap overlayColor = Bitmap.createBitmap(
                new int[]{color}, 0, image.getWidth(), image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap overlay = bitmapWithCircularCutout(overlayColor, c);

        // combine and return
        bitmap = combineBitmaps(image, overlay);
        return bitmap;
    }

    // BELOW IS ALL THAT JAZZ I DON'T UNDERSTAND

    public Bitmap bitmapWithCircularCutout(Bitmap foreground, Context c) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float radius = (float) (getScreenSize(c).x * .35);
        float x = (float) ((getScreenSize(c).x * .5) + (radius * .5));
        float y = (float) ((getScreenSize(c).y * .5) + (radius * .5));
        canvas.drawCircle(x, y, radius, paint);
        return bitmap;
    }

    public Bitmap combineBitmaps(Bitmap background, Bitmap foreground) {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(foreground, 0, 0, paint);
        return combinedBitmap;
    }

    public Point getScreenSize(Context c) {
        WindowManager window = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
