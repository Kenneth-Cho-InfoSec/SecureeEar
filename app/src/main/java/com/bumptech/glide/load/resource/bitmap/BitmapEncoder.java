package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    private final ArrayPool arrayPool;
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");

    public BitmapEncoder(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:46|3|4|(2:44|5)|(5:50|6|(2:8|9)(1:10)|11|12)|42|13|28|29|(1:31)|32|33) */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0070 A[Catch: all -> 0x00c0, TRY_LEAVE, TryCatch #2 {all -> 0x00c0, blocks: (B:3:0x0021, B:13:0x004d, B:29:0x006a, B:31:0x0070, B:35:0x00bc, B:36:0x00bf), top: B:46:0x0021 }] */
    @Override
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean encode(Resource<Bitmap> resource, File file, Options options) {
        FileOutputStream fileOutputStream;
        Bitmap bitmap = resource.get();
        Bitmap.CompressFormat format = getFormat(bitmap, options);
        GlideTrace.beginSectionFormat("encode: [%dx%d] %s", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), format);
        try {
            long logTime = LogTime.getLogTime();
            int iIntValue = ((Integer) options.get(COMPRESSION_QUALITY)).intValue();
            boolean z = false;
            OutputStream bufferedOutputStream = null;
            try {
                try {
                    fileOutputStream = new FileOutputStream(file);
                } catch (IOException e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                bufferedOutputStream = this.arrayPool != null ? new BufferedOutputStream(fileOutputStream, this.arrayPool) : fileOutputStream;
                bitmap.compress(format, iIntValue, bufferedOutputStream);
                bufferedOutputStream.close();
                z = true;
            } catch (IOException e2) {
                e = e2;
                bufferedOutputStream = fileOutputStream;
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Failed to encode Bitmap", e);
                }
                if (bufferedOutputStream != null) {
                }
                if (Log.isLoggable(TAG, 2)) {
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream = fileOutputStream;
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException unused) {
                    }
                }
                throw th;
            }
            bufferedOutputStream.close();
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Compressed with type: " + format + " of size " + Util.getBitmapByteSize(bitmap) + " in " + LogTime.getElapsedMillis(logTime) + ", options format: " + options.get(COMPRESSION_FORMAT) + ", hasAlpha: " + bitmap.hasAlpha());
            }
            return z;
        } finally {
            GlideTrace.endSection();
        }
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        Bitmap.CompressFormat compressFormat = (Bitmap.CompressFormat) options.get(COMPRESSION_FORMAT);
        if (compressFormat != null) {
            return compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    @Override
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
