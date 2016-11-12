package de.honeypot.honeypot.handlers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class StorageHandler {


    // data filePath
    public static String storageDirectory(String rep) {
        return Environment.getExternalStorageDirectory().getPath() + "/Android/data/de.honeypot.honeypot/" + rep;
    }

    public static Bitmap readFileToBitmap(String fileName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(storageDirectory("bitmap") + "/" + fileName, options);

    }

    // set the .nomedia file
    public static void setNoMedia() {

        File file = new File(storageDirectory("bitmap") + "/.nomedia");
        try {
            new File(storageDirectory("bitmap")).mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
