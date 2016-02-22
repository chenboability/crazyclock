package com.example.crazyclock.tools;

import android.app.Application;
import android.graphics.Bitmap;

public class CropApplication extends Application
{

    private static Bitmap bitmap;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public static Bitmap getBitmap()
    {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap)
    {
        CropApplication.bitmap = bitmap;
    }

}
