package androidx.appcompat.widget;

import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;

class ResourcesWrapper extends Resources {
    private final Resources mResources;

    public ResourcesWrapper(Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mResources = resources;
    }

    @Override
    public CharSequence getText(int i) throws Resources.NotFoundException {
        return this.mResources.getText(i);
    }

    @Override
    public CharSequence getQuantityText(int i, int i2) throws Resources.NotFoundException {
        return this.mResources.getQuantityText(i, i2);
    }

    @Override
    public String getString(int i) throws Resources.NotFoundException {
        return this.mResources.getString(i);
    }

    @Override
    public String getString(int i, Object... objArr) throws Resources.NotFoundException {
        return this.mResources.getString(i, objArr);
    }

    @Override
    public String getQuantityString(int i, int i2, Object... objArr) throws Resources.NotFoundException {
        return this.mResources.getQuantityString(i, i2, objArr);
    }

    @Override
    public String getQuantityString(int i, int i2) throws Resources.NotFoundException {
        return this.mResources.getQuantityString(i, i2);
    }

    @Override
    public CharSequence getText(int i, CharSequence charSequence) {
        return this.mResources.getText(i, charSequence);
    }

    @Override
    public CharSequence[] getTextArray(int i) throws Resources.NotFoundException {
        return this.mResources.getTextArray(i);
    }

    @Override
    public String[] getStringArray(int i) throws Resources.NotFoundException {
        return this.mResources.getStringArray(i);
    }

    @Override
    public int[] getIntArray(int i) throws Resources.NotFoundException {
        return this.mResources.getIntArray(i);
    }

    @Override
    public TypedArray obtainTypedArray(int i) throws Resources.NotFoundException {
        return this.mResources.obtainTypedArray(i);
    }

    @Override
    public float getDimension(int i) throws Resources.NotFoundException {
        return this.mResources.getDimension(i);
    }

    @Override
    public int getDimensionPixelOffset(int i) throws Resources.NotFoundException {
        return this.mResources.getDimensionPixelOffset(i);
    }

    @Override
    public int getDimensionPixelSize(int i) throws Resources.NotFoundException {
        return this.mResources.getDimensionPixelSize(i);
    }

    @Override
    public float getFraction(int i, int i2, int i3) {
        return this.mResources.getFraction(i, i2, i3);
    }

    @Override
    public Drawable getDrawable(int i) throws Resources.NotFoundException {
        return this.mResources.getDrawable(i);
    }

    @Override
    public Drawable getDrawable(int i, Resources.Theme theme) throws Resources.NotFoundException {
        return this.mResources.getDrawable(i, theme);
    }

    @Override
    public Drawable getDrawableForDensity(int i, int i2) throws Resources.NotFoundException {
        return this.mResources.getDrawableForDensity(i, i2);
    }

    @Override
    public Drawable getDrawableForDensity(int i, int i2, Resources.Theme theme) {
        return this.mResources.getDrawableForDensity(i, i2, theme);
    }

    @Override
    public Movie getMovie(int i) throws Resources.NotFoundException {
        return this.mResources.getMovie(i);
    }

    @Override
    public int getColor(int i) throws Resources.NotFoundException {
        return this.mResources.getColor(i);
    }

    @Override
    public ColorStateList getColorStateList(int i) throws Resources.NotFoundException {
        return this.mResources.getColorStateList(i);
    }

    @Override
    public boolean getBoolean(int i) throws Resources.NotFoundException {
        return this.mResources.getBoolean(i);
    }

    @Override
    public int getInteger(int i) throws Resources.NotFoundException {
        return this.mResources.getInteger(i);
    }

    @Override
    public XmlResourceParser getLayout(int i) throws Resources.NotFoundException {
        return this.mResources.getLayout(i);
    }

    @Override
    public XmlResourceParser getAnimation(int i) throws Resources.NotFoundException {
        return this.mResources.getAnimation(i);
    }

    @Override
    public XmlResourceParser getXml(int i) throws Resources.NotFoundException {
        return this.mResources.getXml(i);
    }

    @Override
    public InputStream openRawResource(int i) throws Resources.NotFoundException {
        return this.mResources.openRawResource(i);
    }

    @Override
    public InputStream openRawResource(int i, TypedValue typedValue) throws Resources.NotFoundException {
        return this.mResources.openRawResource(i, typedValue);
    }

    @Override
    public AssetFileDescriptor openRawResourceFd(int i) throws Resources.NotFoundException {
        return this.mResources.openRawResourceFd(i);
    }

    @Override
    public void getValue(int i, TypedValue typedValue, boolean z) throws Resources.NotFoundException {
        this.mResources.getValue(i, typedValue, z);
    }

    @Override
    public void getValueForDensity(int i, int i2, TypedValue typedValue, boolean z) throws Resources.NotFoundException {
        this.mResources.getValueForDensity(i, i2, typedValue, z);
    }

    @Override
    public void getValue(String str, TypedValue typedValue, boolean z) throws Resources.NotFoundException {
        this.mResources.getValue(str, typedValue, z);
    }

    @Override
    public TypedArray obtainAttributes(AttributeSet attributeSet, int[] iArr) {
        return this.mResources.obtainAttributes(attributeSet, iArr);
    }

    @Override
    public void updateConfiguration(Configuration configuration, DisplayMetrics displayMetrics) {
        super.updateConfiguration(configuration, displayMetrics);
        Resources resources = this.mResources;
        if (resources != null) {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return this.mResources.getDisplayMetrics();
    }

    @Override
    public Configuration getConfiguration() {
        return this.mResources.getConfiguration();
    }

    @Override
    public int getIdentifier(String str, String str2, String str3) {
        return this.mResources.getIdentifier(str, str2, str3);
    }

    @Override
    public String getResourceName(int i) throws Resources.NotFoundException {
        return this.mResources.getResourceName(i);
    }

    @Override
    public String getResourcePackageName(int i) throws Resources.NotFoundException {
        return this.mResources.getResourcePackageName(i);
    }

    @Override
    public String getResourceTypeName(int i) throws Resources.NotFoundException {
        return this.mResources.getResourceTypeName(i);
    }

    @Override
    public String getResourceEntryName(int i) throws Resources.NotFoundException {
        return this.mResources.getResourceEntryName(i);
    }

    @Override
    public void parseBundleExtras(XmlResourceParser xmlResourceParser, Bundle bundle) throws XmlPullParserException, IOException {
        this.mResources.parseBundleExtras(xmlResourceParser, bundle);
    }

    @Override
    public void parseBundleExtra(String str, AttributeSet attributeSet, Bundle bundle) throws XmlPullParserException {
        this.mResources.parseBundleExtra(str, attributeSet, bundle);
    }
}
