package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;

public class StreamAssetPathFetcher extends AssetPathFetcher<InputStream> {
    public StreamAssetPathFetcher(AssetManager assetManager, String str) {
        super(assetManager, str);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override
    public InputStream loadResource(AssetManager assetManager, String str) throws IOException {
        return assetManager.open(str);
    }

    @Override
    public void close(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
