package de.honeypot.honeypot.handlers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class StorageHandler {


    // data filePath
    public static String storageDirectory(Context c, String rep) {
        //return Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.honeypot.honeypot/" + rep;
        return c.getExternalFilesDir(null) + File.separator + rep;
    }

    public static Bitmap readFileToBitmap(String fileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(fileName, options);

    }

}
