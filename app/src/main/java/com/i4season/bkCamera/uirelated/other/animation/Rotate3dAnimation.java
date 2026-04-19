package com.i4season.bkCamera.uirelated.other.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
    private boolean isX;
    private Camera mCamera = new Camera();
    private final float mCenterX;
    private final float mCenterY;
    private float mFzAngle;

    public Rotate3dAnimation(float f, float f2, float f3, boolean z) {
        this.mCenterX = f;
        this.mCenterY = f2;
        this.mFzAngle = f3;
        this.isX = z;
    }

    @Override
    protected void applyTransformation(float f, Transformation transformation) {
        Camera camera = this.mCamera;
        Matrix matrix = transformation.getMatrix();
        camera.save();
        if (this.isX) {
            camera.rotateX(this.mFzAngle);
        } else {
            camera.rotateY(this.mFzAngle);
        }
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-this.mCenterX, -this.mCenterY);
        matrix.postTranslate(this.mCenterX, this.mCenterY);
    }
}
