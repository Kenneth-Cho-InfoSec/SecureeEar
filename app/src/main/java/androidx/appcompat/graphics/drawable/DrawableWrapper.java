package androidx.appcompat.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableWrapper extends Drawable implements Drawable.Callback {
    private Drawable mDrawable;

    public DrawableWrapper(Drawable drawable) {
        setWrappedDrawable(drawable);
    }

    @Override
    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        this.mDrawable.setBounds(rect);
    }

    @Override
    public void setChangingConfigurations(int i) {
        this.mDrawable.setChangingConfigurations(i);
    }

    @Override
    public int getChangingConfigurations() {
        return this.mDrawable.getChangingConfigurations();
    }

    @Override
    public void setDither(boolean z) {
        this.mDrawable.setDither(z);
    }

    @Override
    public void setFilterBitmap(boolean z) {
        this.mDrawable.setFilterBitmap(z);
    }

    @Override
    public void setAlpha(int i) {
        this.mDrawable.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }

    @Override
    public boolean isStateful() {
        return this.mDrawable.isStateful();
    }

    @Override
    public boolean setState(int[] iArr) {
        return this.mDrawable.setState(iArr);
    }

    @Override
    public int[] getState() {
        return this.mDrawable.getState();
    }

    @Override
    public void jumpToCurrentState() {
        DrawableCompat.jumpToCurrentState(this.mDrawable);
    }

    @Override
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    @Override
    public boolean setVisible(boolean z, boolean z2) {
        return super.setVisible(z, z2) || this.mDrawable.setVisible(z, z2);
    }

    @Override
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    @Override
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    @Override
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    @Override
    public boolean getPadding(Rect rect) {
        return this.mDrawable.getPadding(rect);
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }

    @Override
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }

    @Override
    protected boolean onLevelChange(int i) {
        return this.mDrawable.setLevel(i);
    }

    @Override
    public void setAutoMirrored(boolean z) {
        DrawableCompat.setAutoMirrored(this.mDrawable, z);
    }

    @Override
    public boolean isAutoMirrored() {
        return DrawableCompat.isAutoMirrored(this.mDrawable);
    }

    @Override
    public void setTint(int i) {
        DrawableCompat.setTint(this.mDrawable, i);
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        DrawableCompat.setTintList(this.mDrawable, colorStateList);
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        DrawableCompat.setTintMode(this.mDrawable, mode);
    }

    @Override
    public void setHotspot(float f, float f2) {
        DrawableCompat.setHotspot(this.mDrawable, f, f2);
    }

    @Override
    public void setHotspotBounds(int i, int i2, int i3, int i4) {
        DrawableCompat.setHotspotBounds(this.mDrawable, i, i2, i3, i4);
    }

    public Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    public void setWrappedDrawable(Drawable drawable) {
        Drawable drawable2 = this.mDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }
}
