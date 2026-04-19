package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.Option;

public abstract class DownsampleStrategy {
    public static final DownsampleStrategy FIT_CENTER = new FitCenter();
    public static final DownsampleStrategy CENTER_OUTSIDE = new CenterOutside();
    public static final DownsampleStrategy AT_LEAST = new AtLeast();
    public static final DownsampleStrategy AT_MOST = new AtMost();
    public static final DownsampleStrategy CENTER_INSIDE = new CenterInside();
    public static final DownsampleStrategy NONE = new None();
    public static final DownsampleStrategy DEFAULT = CENTER_OUTSIDE;
    public static final Option<DownsampleStrategy> OPTION = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DEFAULT);

    public enum SampleSizeRounding {
        MEMORY,
        QUALITY
    }

    public abstract SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4);

    public abstract float getScaleFactor(int i, int i2, int i3, int i4);

    private static class FitCenter extends DownsampleStrategy {
        FitCenter() {
        }

        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.min(i3 / i, i4 / i2);
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }

    private static class CenterOutside extends DownsampleStrategy {
        CenterOutside() {
        }

        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.max(i3 / i, i4 / i2);
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }

    private static class AtLeast extends DownsampleStrategy {
        AtLeast() {
        }

        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            if (Math.min(i2 / i4, i / i3) == 0) {
                return 1.0f;
            }
            return 1.0f / Integer.highestOneBit(r1);
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }

    private static class AtMost extends DownsampleStrategy {
        AtMost() {
        }

        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int iCeil = (int) Math.ceil(Math.max(i2 / i4, i / i3));
            return 1.0f / (r2 << (Math.max(1, Integer.highestOneBit(iCeil)) >= iCeil ? 0 : 1));
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.MEMORY;
        }
    }

    private static class None extends DownsampleStrategy {
        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return 1.0f;
        }

        None() {
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }

    private static class CenterInside extends DownsampleStrategy {
        CenterInside() {
        }

        @Override
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.min(1.0f, FIT_CENTER.getScaleFactor(i, i2, i3, i4));
        }

        @Override
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }
}
