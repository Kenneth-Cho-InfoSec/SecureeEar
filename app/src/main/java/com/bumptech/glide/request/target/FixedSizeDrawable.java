package com.bumptech.glide.request.target;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.util.Preconditions;

public class FixedSizeDrawable extends Drawable {
    private final RectF bounds;
    private final Matrix matrix;
    private boolean mutated;
    private State state;
    private Drawable wrapped;
    private final RectF wrappedRect;

    public FixedSizeDrawable(Drawable drawable, int i, int i2) {
        this(new State(drawable.getConstantState(), i, i2), drawable);
    }

    FixedSizeDrawable(State state, Drawable drawable) {
        this.state = (State) Preconditions.checkNotNull(state);
        this.wrapped = (Drawable) Preconditions.checkNotNull(drawable);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        this.matrix = new Matrix();
        this.wrappedRect = new RectF(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        this.bounds = new RectF();
    }

    @Override
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        this.bounds.set(i, i2, i3, i4);
        updateMatrix();
    }

    @Override
    public void setBounds(Rect rect) {
        super.setBounds(rect);
        this.bounds.set(rect);
        updateMatrix();
    }

    private void updateMatrix() {
        this.matrix.setRectToRect(this.wrappedRect, this.bounds, Matrix.ScaleToFit.CENTER);
    }

    @Override
    public void setChangingConfigurations(int i) {
        this.wrapped.setChangingConfigurations(i);
    }

    @Override
    public int getChangingConfigurations() {
        return this.wrapped.getChangingConfigurations();
    }

    @Override
    @Deprecated
    public void setDither(boolean z) {
        this.wrapped.setDither(z);
    }

    @Override
    public void setFilterBitmap(boolean z) {
        this.wrapped.setFilterBitmap(z);
    }

    @Override
    public Drawable.Callback getCallback() {
        return this.wrapped.getCallback();
    }

    @Override
    public int getAlpha() {
        return this.wrapped.getAlpha();
    }

    @Override
    public void setColorFilter(int i, PorterDuff.Mode mode) {
        this.wrapped.setColorFilter(i, mode);
    }

    @Override
    public void clearColorFilter() {
        this.wrapped.clearColorFilter();
    }

    @Override
    public Drawable getCurrent() {
        return this.wrapped.getCurrent();
    }

    @Override
    public boolean setVisible(boolean z, boolean z2) {
        return this.wrapped.setVisible(z, z2);
    }

    @Override
    public int getIntrinsicWidth() {
        return this.state.width;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.state.height;
    }

    @Override
    public int getMinimumWidth() {
        return this.wrapped.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return this.wrapped.getMinimumHeight();
    }

    @Override
    public boolean getPadding(Rect rect) {
        return this.wrapped.getPadding(rect);
    }

    @Override
    public void invalidateSelf() {
        super.invalidateSelf();
        this.wrapped.invalidateSelf();
    }

    @Override
    public void unscheduleSelf(Runnable runnable) {
        super.unscheduleSelf(runnable);
        this.wrapped.unscheduleSelf(runnable);
    }

    @Override
    public void scheduleSelf(Runnable runnable, long j) {
        super.scheduleSelf(runnable, j);
        this.wrapped.scheduleSelf(runnable, j);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(this.matrix);
        this.wrapped.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int i) {
        this.wrapped.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.wrapped.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return this.wrapped.getOpacity();
    }

    @Override
    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.wrapped = this.wrapped.mutate();
            this.state = new State(this.state);
            this.mutated = true;
        }
        return this;
    }

    @Override
    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    static final class State extends Drawable.ConstantState {
        final int height;
        final int width;
        private final Drawable.ConstantState wrapped;

        @Override
        public int getChangingConfigurations() {
            return 0;
        }

        State(State state) {
            this(state.wrapped, state.width, state.height);
        }

        State(Drawable.ConstantState constantState, int i, int i2) {
            this.wrapped = constantState;
            this.width = i;
            this.height = i2;
        }

        @Override
        public Drawable newDrawable() {
            return new FixedSizeDrawable(this, this.wrapped.newDrawable());
        }

        @Override
        public Drawable newDrawable(Resources resources) {
            return new FixedSizeDrawable(this, this.wrapped.newDrawable(resources));
        }
    }
}
