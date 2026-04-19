package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader<Data> implements ModelLoader<File, Data> {
    private static final String TAG = "FileLoader";
    private final FileOpener<Data> fileOpener;

    public interface FileOpener<Data> {
        void close(Data data) throws IOException;

        Class<Data> getDataClass();

        Data open(File file) throws FileNotFoundException;
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    public FileLoader(FileOpener<Data> fileOpener) {
        this.fileOpener = fileOpener;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(File file, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(file), new FileFetcher(file, this.fileOpener));
    }

    private static final class FileFetcher<Data> implements DataFetcher<Data> {
        private Data data;
        private final File file;
        private final FileOpener<Data> opener;

        @Override
        public void cancel() {
        }

        FileFetcher(File file, FileOpener<Data> fileOpener) {
            this.file = file;
            this.opener = fileOpener;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            try {
                this.data = this.opener.open(this.file);
                dataCallback.onDataReady(this.data);
            } catch (FileNotFoundException e) {
                if (Log.isLoggable(FileLoader.TAG, 3)) {
                    Log.d(FileLoader.TAG, "Failed to open file", e);
                }
                dataCallback.onLoadFailed(e);
            }
        }

        @Override
        public void cleanup() {
            Data data = this.data;
            if (data != null) {
                try {
                    this.opener.close(data);
                } catch (IOException unused) {
                }
            }
        }

        @Override
        public Class<Data> getDataClass() {
            return this.opener.getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    public static class Factory<Data> implements ModelLoaderFactory<File, Data> {
        private final FileOpener<Data> opener;

        @Override
        public final void teardown() {
        }

        public Factory(FileOpener<Data> fileOpener) {
            this.opener = fileOpener;
        }

        @Override
        public final ModelLoader<File, Data> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new FileLoader(this.opener);
        }
    }

    public static class StreamFactory extends Factory<InputStream> {
        public StreamFactory() {
            super(new FileOpener<InputStream>() {
                /* JADX WARN: Can't rename method to resolve collision */
                @Override
                public InputStream open(File file) throws FileNotFoundException {
                    return new FileInputStream(file);
                }

                @Override
                public void close(InputStream inputStream) throws IOException {
                    inputStream.close();
                }

                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
            });
        }
    }

    public static class FileDescriptorFactory extends Factory<ParcelFileDescriptor> {
        public FileDescriptorFactory() {
            super(new FileOpener<ParcelFileDescriptor>() {
                /* JADX WARN: Can't rename method to resolve collision */
                @Override
                public ParcelFileDescriptor open(File file) throws FileNotFoundException {
                    return ParcelFileDescriptor.open(file, 268435456);
                }

                @Override
                public void close(ParcelFileDescriptor parcelFileDescriptor) throws IOException {
                    parcelFileDescriptor.close();
                }

                @Override
                public Class<ParcelFileDescriptor> getDataClass() {
                    return ParcelFileDescriptor.class;
                }
            });
        }
    }
}
