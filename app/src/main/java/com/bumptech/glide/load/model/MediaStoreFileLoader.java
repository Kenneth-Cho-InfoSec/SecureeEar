package com.bumptech.glide.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;

public final class MediaStoreFileLoader implements ModelLoader<Uri, File> {
    private final Context context;

    public MediaStoreFileLoader(Context context) {
        this.context = context;
    }

    @Override
    public ModelLoader.LoadData<File> buildLoadData(Uri uri, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(uri), new FilePathFetcher(this.context, uri));
    }

    @Override
    public boolean handles(Uri uri) {
        return MediaStoreUtil.isMediaStoreUri(uri);
    }

    private static class FilePathFetcher implements DataFetcher<File> {
        private static final String[] PROJECTION = {"_data"};
        private final Context context;
        private final Uri uri;

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        FilePathFetcher(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super File> dataCallback) {
            Cursor cursorQuery = this.context.getContentResolver().query(this.uri, PROJECTION, null, null, null);
            if (cursorQuery != null) {
                try {
                    string = cursorQuery.moveToFirst() ? cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("_data")) : null;
                } finally {
                    cursorQuery.close();
                }
            }
            if (TextUtils.isEmpty(string)) {
                dataCallback.onLoadFailed(new FileNotFoundException("Failed to find file path for: " + this.uri));
                return;
            }
            dataCallback.onDataReady(new File(string));
        }

        @Override
        public Class<File> getDataClass() {
            return File.class;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static final class Factory implements ModelLoaderFactory<Uri, File> {
        private final Context context;

        @Override
        public void teardown() {
        }

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ModelLoader<Uri, File> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreFileLoader(this.context);
        }
    }
}
