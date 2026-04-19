package com.i4season.bkCamera.uirelated.other.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class AdapterType {
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0000. Please report as an issue. */
    public static int getPicID(int i) {
        switch (i) {
        }
        return 0;
    }

    public static int getFileTypeMarked(String str) {
        if (str == null) {
            return 0;
        }
        return new Power7AudioSupportFormat().getSupportFormatMarked(0, str.toUpperCase(Locale.getDefault()));
    }

    public static Bitmap getShowBitmap(Context context, int i) {
        Bitmap bitmapDecodeStream = null;
        if (context == null) {
            return null;
        }
        try {
            InputStream inputStreamOpenRawResource = context.getResources().openRawResource(i);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;
            bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenRawResource, null, options);
            inputStreamOpenRawResource.close();
            return bitmapDecodeStream;
        } catch (IOException e) {
            LogWD.writeMsg(e);
            return bitmapDecodeStream;
        }
    }
}
