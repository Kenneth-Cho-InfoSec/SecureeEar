package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;

public abstract class AppGlideModule extends LibraryGlideModule implements AppliesOptions {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
    }

    public boolean isManifestParsingEnabled() {
        return true;
    }
}
