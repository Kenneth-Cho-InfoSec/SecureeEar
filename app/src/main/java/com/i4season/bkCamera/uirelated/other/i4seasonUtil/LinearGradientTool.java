package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.graphics.LinearGradient;
import android.graphics.Shader;

public class LinearGradientTool {
    public static LinearGradient getRotate90LinearGradient(float f, float f2, float f3, float f4, float f5, float f6, int[] iArr, int i) {
        float f7 = f5 - f3;
        float f8 = f - f2;
        float f9 = (f2 + f) - f7;
        return getLinearGradient(f8, f9, f8 + (f6 - f4), f9 + f7, iArr, i + 90);
    }

    public static LinearGradient getLinearGradient(float f, float f2, float f3, float f4, int[] iArr, int i) {
        if (i > 0) {
            i %= 360;
        } else if (i < 0) {
            i = (i % 360) + 360;
        }
        float f5 = (f + f3) * 0.5f;
        float f6 = (f2 + f4) * 0.5f;
        float f7 = f3 - f;
        float f8 = f4 - f;
        double dSqrt = (float) (Math.sqrt((f7 * f7) + (f8 * f8)) * 0.5d);
        double d = (((double) i) * 3.141592653589793d) / 180.0d;
        float fCos = (float) (Math.cos(d) * dSqrt);
        float fSin = (float) (dSqrt * Math.sin(d));
        int length = iArr.length;
        float[] fArr = new float[length];
        fArr[0] = 0.0f;
        int i2 = length - 1;
        fArr[i2] = 1.0f;
        float f9 = 1.0f / i2;
        for (int i3 = 1; i3 < i2; i3++) {
            fArr[i3] = i3 * f9;
        }
        return new LinearGradient(f5 - fCos, f6 + fSin, f5 + fCos, f6 - fSin, iArr, fArr, Shader.TileMode.CLAMP);
    }
}
