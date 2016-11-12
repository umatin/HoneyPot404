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

    public static int relativeImageRes(Context c){

        int width = getScreenSize(c).x;

        return width/4;
    }

    // make profile picture
    // res = resolution in px
    // dark = Dark Theme
    public static Bitmap circularProfilePicture(Bitmap image, int res, boolean light) {
        Bitmap bitmap;

        // color to overlay
        int color = light ? Color.WHITE : Color.DKGRAY;

        Bitmap overlayColor = Bitmap.createBitmap(res, res, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlayColor);
        canvas.drawColor(color);

        Bitmap overlay = bitmapWithCircularCutout(overlayColor, res/2);

        // combine and return
        bitmap = combineBitmaps(image, overlay);
        return bitmap;
    }

    // BELOW IS ALL THAT JAZZ I DON'T UNDERSTAND

    public static Bitmap bitmapWithCircularCutout(Bitmap foreground, int radius) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float x = (float) foreground.getWidth()/2;
        float y = (float) foreground.getHeight()/2;
        canvas.drawCircle(x, y, radius, paint);
        return bitmap;
    }

    public static Bitmap combineBitmaps(Bitmap background, Bitmap foreground) {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(foreground, 0, 0, paint);
        return combinedBitmap;
    }

    public static Point getScreenSize(Context c) {
        WindowManager window = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
