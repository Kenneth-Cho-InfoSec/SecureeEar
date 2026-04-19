package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import com.i4season.bkCamera.WDApplication;
import com.i4season.bkCamera.logicrelated.fileacceptandoperation.bean.FileNode;
import com.i4season.bkCamera.uirelated.other.AppPathInfo;
import com.i4season.bkCamera.uirelated.other.function.AdapterType;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtil {
    public static boolean saveFile2Byte(byte[] bArr, String str) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, 0, bArr.length);
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            byte[] bArr2 = new byte[16384];
            while (true) {
                int i = byteArrayInputStream.read(bArr2);
                if (i != -1) {
                    fileOutputStream.write(bArr2, 0, i);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    byteArrayInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogWD.writeMsg(e);
            return false;
        }
    }

    public static boolean bytesToImageFile(byte[] bArr, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            fileOutputStream.write(bArr, 0, bArr.length);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean bytesToImageBitmap(Bitmap bitmap, String str) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            fileOutputStream.write(byteArray, 0, byteArray.length);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getPicFromBytes(byte[] bArr) {
        if (bArr.length != 0) {
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static Bitmap decodeBitmap(int i) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeResource(WDApplication.getInstance().getResources(), i, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap toConformBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap.isRecycled() || bitmap2 == null || bitmap2.isRecycled()) {
            return null;
        }
        try {
            Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            new Canvas(bitmapCopy).drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), (Paint) null);
            return bitmapCopy;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static int getVideoTime(String str) {
        int duration = 0;
        if (str.endsWith(".avi")) {
            return 0;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            try {
                mediaPlayer.setDataSource(str);
                mediaPlayer.prepare();
                int duration2 = mediaPlayer.getDuration();
                mediaPlayer.release();
                return duration2;
            } catch (Exception e) {
                e.printStackTrace();
                return duration;
            }
        } catch (IOException unused) {
            mediaPlayer.setDataSource(new FileInputStream(new File(str)).getFD());
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mediaPlayer.release();
            return duration;
        }
    }

    public static void shareFile2LocalPath(String str, Context context) {
        File file = new File(str);
        Uri uriFromFile = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriFromFile);
        intent.setFlags(268435456);
        FileOpenSetOpenProperty fileOpenSetOpenProperty = new FileOpenSetOpenProperty();
        fileOpenSetOpenProperty.setOpenProperty(AdapterType.getFileTypeMarked(UtilTools.getFileTypeFromName(file.getName())));
        intent.setType(fileOpenSetOpenProperty.getMimeType());
        context.startActivity(Intent.createChooser(intent, ""));
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil$1] */
    public static void shareMultipleFile(final List<FileNode> list, final Context context) {
        new Thread() {
            @Override
            public void run() {
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    arrayList.add(Uri.fromFile(new File(((FileNode) it.next()).getmFileDevPath())));
                }
                final Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND_MULTIPLE");
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                intent.setType("image/*");
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.startActivity(Intent.createChooser(intent, ""));
                    }
                });
            }
        }.start();
    }

    public static void shareMultipleFile2Path(List<String> list, Context context) {
        ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Uri.fromFile(new File(it.next())));
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, ""));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.i4season.bkCamera.uirelated.other.i4seasonUtil.FileUtil$2] */
    public static void shareVideoFile(final List<FileNode> list, final Context context) {
        if (list.size() == 1) {
            Uri uriFromFile = Uri.fromFile(new File(list.get(0).getmFileDevPath()));
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uriFromFile);
            intent.setType("video/*");
            context.startActivity(Intent.createChooser(intent, ""));
            return;
        }
        new Thread() {
            @Override
            public void run() {
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    arrayList.add(Uri.fromFile(new File(((FileNode) it.next()).getmFileDevPath())));
                }
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.SEND_MULTIPLE");
                intent2.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                intent2.setType("video/*");
                context.startActivity(Intent.createChooser(intent2, ""));
            }
        }.start();
    }

    public static Bitmap screenView2Bitmap(View view) {
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    public static void writeImage(Bitmap bitmap, String str, int i) {
        try {
            deleteFile(str);
            if (createFile(str)) {
                FileOutputStream fileOutputStream = new FileOutputStream(str);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, i, fileOutputStream)) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean createFile(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return true;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean deleteFile(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void shareDoc(String str, Context context) {
        Uri uriFromFile = Uri.fromFile(new File(str));
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uriFromFile);
        intent.setType("text/*;application/*");
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static boolean writeAssetsFileToLocal(Context context, String str, String str2) {
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                InputStream inputStreamOpen = context.getResources().getAssets().open(str);
                try {
                    fileOutputStream = new FileOutputStream(str2);
                    try {
                        BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream, 1024);
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int i = inputStreamOpen.read(bArr, 0, 1024);
                                if (i < 0) {
                                    break;
                                }
                                bufferedOutputStream2.write(bArr, 0, i);
                            }
                            bufferedOutputStream2.close();
                            fileOutputStream.close();
                            if (inputStreamOpen == null) {
                                return true;
                            }
                            inputStreamOpen.close();
                            return true;
                        } catch (Throwable th) {
                            inputStream = inputStreamOpen;
                            th = th;
                            bufferedOutputStream = bufferedOutputStream2;
                            if (bufferedOutputStream != null) {
                                bufferedOutputStream.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        inputStream = inputStreamOpen;
                        th = th2;
                    }
                } catch (Throwable th3) {
                    inputStream = inputStreamOpen;
                    th = th3;
                    fileOutputStream = null;
                }
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                inputStream = null;
            }
        } catch (FileNotFoundException e) {
            LogWD.writeMsg(e);
            return false;
        } catch (Exception e2) {
            LogWD.writeMsg(e2);
            return false;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float f) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (bitmapCreateBitmap.equals(bitmap)) {
        }
        return bitmapCreateBitmap;
    }

    public static int getVideoTimeToRaw(Context context, Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        int duration = 0;
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mediaPlayer.release();
            return duration;
        } catch (IOException e) {
            e.printStackTrace();
            return duration;
        }
    }

    public static boolean saveBitmap(Context context, Bitmap bitmap, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", "This is an image");
        contentValues.put("_display_name", str);
        contentValues.put("mime_type", "image/png");
        contentValues.put("title", System.currentTimeMillis() + Constant.SAVE_PHOTO_SUFFIX);
        contentValues.put("relative_path", Environment.DIRECTORY_DCIM + AppPathInfo.app_package_name);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriInsert = contentResolver.insert(uri, contentValues);
        if (uriInsert != null) {
            OutputStream outputStreamOpenOutputStream = null;
            try {
                try {
                    outputStreamOpenOutputStream = contentResolver.openOutputStream(uriInsert);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStreamOpenOutputStream);
                    if (outputStreamOpenOutputStream == null) {
                        return true;
                    }
                    try {
                        outputStreamOpenOutputStream.close();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return true;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (outputStreamOpenOutputStream != null) {
                        try {
                            outputStreamOpenOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return false;
                }
            } catch (Throwable th) {
                if (outputStreamOpenOutputStream != null) {
                    try {
                        outputStreamOpenOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        }
        return false;
    }

    public static void copyPrivateToPictures(Context context, String str, String str2) throws Throwable {
        OutputStream outputStream;
        OutputStream outputStream2;
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", "This is an file");
        contentValues.put("_display_name", str2);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("title", str2);
        contentValues.put("relative_path", Environment.DIRECTORY_DCIM + AppPathInfo.app_package_name);
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriInsert = contentResolver.insert(uri, contentValues);
        FileInputStream fileInputStream = null;
        outputStreamOpenOutputStream = null;
        outputStreamOpenOutputStream = null;
        OutputStream outputStreamOpenOutputStream = null;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                FileInputStream fileInputStream3 = new FileInputStream(new File(str));
                if (uriInsert != null) {
                    try {
                        outputStreamOpenOutputStream = contentResolver.openOutputStream(uriInsert);
                    } catch (IOException unused) {
                        outputStream2 = outputStreamOpenOutputStream;
                        fileInputStream2 = fileInputStream3;
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        if (outputStream2 != null) {
                            outputStream2.close();
                            return;
                        }
                        return;
                    } catch (Throwable th) {
                        th = th;
                        outputStream = outputStreamOpenOutputStream;
                        fileInputStream = fileInputStream3;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException unused2) {
                                throw th;
                            }
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        throw th;
                    }
                }
                if (outputStreamOpenOutputStream != null) {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int i = fileInputStream3.read(bArr);
                        if (i == -1) {
                            break;
                        } else {
                            outputStreamOpenOutputStream.write(bArr, 0, i);
                        }
                    }
                }
                fileInputStream3.close();
                if (outputStreamOpenOutputStream != null) {
                    outputStreamOpenOutputStream.close();
                }
            } catch (IOException unused3) {
            }
        } catch (IOException unused4) {
            outputStream2 = null;
        } catch (Throwable th2) {
            th = th2;
            outputStream = null;
        }
    }

    public static boolean checkVideoFile(String str) {
        String strExtractMetadata;
        MediaMetadataRetriever mediaMetadataRetriever;
        String strExtractMetadata2 = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(str);
            strExtractMetadata = mediaMetadataRetriever.extractMetadata(19);
        } catch (Exception e) {
            e = e;
            strExtractMetadata = null;
        }
        try {
            strExtractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
        }
        return (TextUtils.isEmpty(strExtractMetadata2) || TextUtils.isEmpty(strExtractMetadata)) ? false : true;
    }
}
