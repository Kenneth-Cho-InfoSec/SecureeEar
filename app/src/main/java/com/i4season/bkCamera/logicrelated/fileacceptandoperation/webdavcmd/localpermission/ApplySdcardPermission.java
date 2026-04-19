package com.i4season.bkCamera.logicrelated.fileacceptandoperation.webdavcmd.localpermission;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import androidx.documentfile.provider.DocumentFile;
import com.i4season.bkCamera.WDApplication;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ApplySdcardPermission {
    private static ApplySdcardPermission applySdcardPermission;
    private static Lock reentantLock = new ReentrantLock();
    private Context mContent;
    private Handler mPermissionHandler;
    private SharedPreferences saveUriPreference;
    private String saveUriPreferenceLibName = "saveurilib";
    private String saveUriPreferenceTagname = "uri";

    public static ApplySdcardPermission getInstance() {
        if (applySdcardPermission == null) {
            try {
                reentantLock.lock();
                if (applySdcardPermission == null) {
                    applySdcardPermission = new ApplySdcardPermission();
                }
            } finally {
                reentantLock.unlock();
            }
        }
        return applySdcardPermission;
    }

    private ApplySdcardPermission() {
    }

    public void initApplySdcardPermission(Context context) {
        this.mContent = context;
        this.saveUriPreference = this.mContent.getSharedPreferences(this.saveUriPreferenceLibName, 0);
    }

    public Uri getSaveUriPreference() {
        String string = this.saveUriPreference.getString(this.saveUriPreferenceTagname, null);
        if (string == null || string.isEmpty()) {
            return null;
        }
        return Uri.parse(string);
    }

    public DocumentFile getRootDocumentFile() {
        Uri saveUriPreference = getInstance().getSaveUriPreference();
        if (saveUriPreference == null) {
            return null;
        }
        return DocumentFile.fromTreeUri(WDApplication.getInstance().getApplicationContext(), saveUriPreference);
    }

    public DocumentFile getDocumentFile(String str) {
        return DocumentFile.fromTreeUri(WDApplication.getInstance().getApplicationContext(), Uri.parse("content://com.android.externalstorage.documents/tree/785D-6E79%3A/document/785D-6E79%3AShare%2FPictures"));
    }

    public void setSaveUriPreference(Uri uri) {
        String string = uri.toString();
        SharedPreferences.Editor editorEdit = this.saveUriPreference.edit();
        editorEdit.putString(this.saveUriPreferenceTagname, string);
        editorEdit.apply();
    }

    public Handler getmPermissionHandler() {
        return this.mPermissionHandler;
    }

    public void setmPermissionHandler(Handler handler) {
        this.mPermissionHandler = handler;
    }
}
