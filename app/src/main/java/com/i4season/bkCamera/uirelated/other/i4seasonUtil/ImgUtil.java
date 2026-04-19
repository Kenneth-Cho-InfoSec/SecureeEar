package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.Base64;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.uirelated.other.FunctionSwitch;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.i4season.i4season_camera.C0413R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class ImgUtil {
    public static final long LOADSIZE = 626688;
    private static final long LOADSIZE2 = 8000000;
    public static int displayHeight;
    public static int displayWidth;
    public static int mCurrPicIndex;
    public static int sHeight;
    public static int sWidth;

    public static int getScaleSize(int i) {
        long j = i;
        if (j <= LOADSIZE || j >= LOADSIZE2) {
            return j > LOADSIZE2 ? 8 : 0;
        }
        return 4;
    }

    public static Bitmap onDrawBitmap(Bitmap bitmap, int i, int i2) {
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap = null;
        }
        if (bitmap == null) {
            return bitmap;
        }
        float f = i;
        float f2 = i2;
        float width = (bitmap.getWidth() / bitmap.getHeight()) / (f / f2);
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f3 = width2;
        float fMin = (Math.min(1.0f, width * 1.0f) * f) / f3;
        float f4 = height;
        float fMin2 = (Math.min(1.0f, 1.0f / width) * f2) / f4;
        rect.left = (int) ((f3 * 0.5f) - (f / (fMin * 2.0f)));
        rect.top = (int) ((f4 * 0.5f) - (f2 / (2.0f * fMin2)));
        rect.right = (int) (rect.left + (f / fMin));
        rect.bottom = (int) (rect.top + (f2 / fMin2));
        rect2.left = 0;
        rect2.top = 0;
        rect2.right = i;
        rect2.bottom = i2;
        if (rect.left < 0) {
            rect2.left = (int) (rect2.left + ((-rect.left) * fMin));
            rect.left = 0;
        }
        if (rect.right > width2) {
            rect2.right = (int) (rect2.right - ((rect.right - width2) * fMin));
            rect.right = width2;
        }
        if (rect.top < 0) {
            rect2.top = (int) (rect2.top + ((-rect.top) * fMin2));
            rect.top = 0;
        }
        if (rect.bottom > height) {
            rect2.bottom = (int) (rect2.bottom - ((rect.bottom - height) * fMin2));
            rect.bottom = height;
        }
        sHeight = rect2.bottom - rect2.top;
        sWidth = rect2.right - rect2.left;
        return zoomBitmap(bitmap, sWidth, sHeight);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (bitmap != bitmapCreateBitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float f) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmapCreateBitmap;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        int i = height / 2;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, i, width, i, matrix, false);
        Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(width, i + height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        float f = height;
        float f2 = width;
        float f3 = height + 4;
        canvas.drawRect(0.0f, f, f2, f3, new Paint());
        canvas.drawBitmap(bitmapCreateBitmap, 0.0f, f3, (Paint) null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, bitmap.getHeight(), 0.0f, bitmapCreateBitmap2.getHeight() + 4, 1895825407, 16777215, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, f, f2, bitmapCreateBitmap2.getHeight() + 4, paint);
        return bitmapCreateBitmap2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r1v9 */
    public static Bitmap getPictureThumb(String str) throws Throwable {
        FileInputStream fileInputStream;
        Bitmap bitmapDecodeStream = null;
        try {
            File file = new File(str);
            ?? r1 = ((file.exists() ? file.length() : 0L) > LOADSIZE ? 1 : ((file.exists() ? file.length() : 0L) == LOADSIZE ? 0 : -1));
            try {
                if (r1 < 0) {
                    return readBitMap(str);
                }
                try {
                    fileInputStream = new FileInputStream(str);
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = 4;
                        bitmapDecodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                        r1 = fileInputStream;
                    } catch (FileNotFoundException e) {
                        e = e;
                        LogWD.writeMsg(e);
                        r1 = fileInputStream;
                        if (fileInputStream == null) {
                            return null;
                        }
                    }
                } catch (FileNotFoundException e2) {
                    e = e2;
                    fileInputStream = null;
                } catch (Throwable th) {
                    th = th;
                    r1 = 0;
                }
                r1.close();
                return bitmapDecodeStream;
            } catch (Throwable th2) {
                th = th2;
            }
            if (r1 != 0) {
                r1.close();
            }
            throw th;
        } catch (Exception e3) {
            LogWD.writeMsg(e3);
            return null;
        }
    }

    public static final Bitmap resizeBitmap(Bitmap bitmap, int i) {
        int i2;
        boolean z;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            if (width > i) {
                i2 = (height * i) / width;
                z = true;
            }
            z = false;
            i2 = i;
        } else {
            if (height > i) {
                int i3 = (width * i) / height;
                i2 = i;
                i = i3;
                z = true;
            }
            z = false;
            i2 = i;
        }
        return z ? Bitmap.createScaledBitmap(bitmap, i, i2, true) : bitmap;
    }

    public static Bitmap readBitMap(Context context, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(i), null, options);
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0039 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap readBitMap(String str) throws Throwable {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        LogWD.writeMsg(e);
                    }
                    return bitmapDecodeStream;
                } catch (FileNotFoundException e2) {
                    e = e2;
                    LogWD.writeMsg(e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e3) {
                            LogWD.writeMsg(e3);
                        }
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e4) {
                        LogWD.writeMsg(e4);
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            if (fileInputStream != null) {
            }
            throw th;
        }
    }

    public static Bitmap getImage(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i3 = options.outHeight;
        int i4 = options.outWidth / i;
        int i5 = i3 / i2;
        if (i4 < i5) {
            i5 = i4;
        }
        options.inSampleSize = i5 > 0 ? i5 : 1;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(str, options), i, i2, 2);
    }

    public static Bitmap fitSizeImg(String str, int i, int i2) {
        Bitmap bitmapDecodeFile = null;
        if (str != null) {
            try {
                if (str.length() >= 1) {
                    File file = new File(str);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    if (file.length() < 20480 || file.length() < 51200) {
                        options.inSampleSize = 1;
                    } else if (file.length() < 307200 || file.length() < 819200) {
                        options.inSampleSize = 2;
                    } else if (file.length() < 1048576) {
                        options.inSampleSize = 4;
                    } else {
                        options.inSampleSize = 6;
                    }
                    bitmapDecodeFile = BitmapFactory.decodeFile(file.getPath(), options);
                    return ThumbnailUtils.extractThumbnail(bitmapDecodeFile, i, i2, 2);
                }
            } catch (Exception e) {
                LogWD.writeMsg(e);
                return bitmapDecodeFile;
            }
        }
        return null;
    }

    public static boolean saveThumbImageFile(String str, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean zCreateNewFile = false;
        if (bitmap == null || str == null || str.isEmpty()) {
            return false;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        File file = new File(str);
        try {
            if (file.exists()) {
                file.delete();
            }
            zCreateNewFile = file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArray);
            fileOutputStream.flush();
            fileOutputStream.close();
            return zCreateNewFile;
        } catch (IOException e) {
            LogWD.writeMsg(e);
            return zCreateNewFile;
        }
    }

    public static byte[] getPicture(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String encodePic(Bitmap bitmap) {
        byte[] picture = getPicture(bitmap);
        if (picture == null || picture.length <= 0) {
            return null;
        }
        return Base64.encodeToString(picture, 0);
    }

    public static Bitmap decodePic(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, null);
    }

    public static Bitmap decodePic(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return decodePic(Base64.decode(str, 0));
    }

    public static Bitmap byteToBitmap(byte[] bArr) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Bitmap bitmap = (Bitmap) new SoftReference(BitmapFactory.decodeStream(byteArrayInputStream, null, options)).get();
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float f) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(f, width / 2, height / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap fzBitmap(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (z) {
            matrix.postScale(1.0f, -1.0f);
        } else {
            matrix.postScale(-1.0f, 1.0f);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public static Bitmap zoomBitmapNoRecyled(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap cropBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            return Bitmap.createBitmap(bitmap, ((width - height) / 2) + FunctionSwitch.offsetValue, 0, height, height, (Matrix) null, true);
        }
        return width == height ? bitmap : Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width, (Matrix) null, true);
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        float f = width / 2;
        canvas.drawCircle(f, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmapCreateBitmap;
    }

    public static Bitmap toRoundBitmapMask(Bitmap bitmap) {
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(WDApplication.getInstance().getApplicationContext().getResources(), C0413R.drawable.ic_circle_bg);
        if (bitmapDecodeResource.getWidth() != bitmap.getWidth()) {
            bitmapDecodeResource = zoomBitmapNoRecyled(bitmapDecodeResource, bitmap.getWidth(), bitmap.getWidth());
        }
        return toConformBitmap(bitmap, bitmapDecodeResource);
    }

    private static Bitmap toConformBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap.isRecycled() || bitmap2 == null || bitmap2.isRecycled()) {
            return null;
        }
        try {
            Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmapCopy);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), (Paint) null);
            return bitmapCopy;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap rotateBitmap2(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth() / 2;
        int height = bitmap.getHeight() / 2;
        matrix.postTranslate(-width, -height);
        matrix.postRotate(f);
        matrix.postTranslate(width, height);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        new Canvas(bitmapCreateBitmap).drawBitmap(bitmap, matrix, new Paint());
        return bitmapCreateBitmap;
    }

    public static Bitmap cuttingZoom(Bitmap bitmap, float f) {
        if (f == 1.0f) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f2 = width;
        int i = (int) (f2 * f);
        float f3 = height;
        int i2 = (int) (f * f3);
        if (i % 2 == 1) {
            i++;
        }
        int i3 = i;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, (width - i) / 2, (height - i2) / 2, i3, i2, (Matrix) null, false);
        Matrix matrix = new Matrix();
        matrix.postScale(f2 / i, f3 / i2);
        return Bitmap.createBitmap(bitmapCreateBitmap, 0, 0, i3, i2, matrix, true);
    }
}
