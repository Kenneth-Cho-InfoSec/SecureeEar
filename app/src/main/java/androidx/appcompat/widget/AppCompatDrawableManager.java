package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.appcompat.C0001R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.LruCache;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AppCompatDrawableManager {
    private static final boolean DEBUG = false;
    private static AppCompatDrawableManager INSTANCE = null;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManag";
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<>(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {C0001R.drawable.abc_textfield_search_default_mtrl_alpha, C0001R.drawable.abc_textfield_default_mtrl_alpha, C0001R.drawable.abc_ab_share_pack_mtrl_alpha};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = {C0001R.drawable.abc_ic_commit_search_api_mtrl_alpha, C0001R.drawable.abc_seekbar_tick_mark_material, C0001R.drawable.abc_ic_menu_share_mtrl_alpha, C0001R.drawable.abc_ic_menu_copy_mtrl_am_alpha, C0001R.drawable.abc_ic_menu_cut_mtrl_alpha, C0001R.drawable.abc_ic_menu_selectall_mtrl_alpha, C0001R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {C0001R.drawable.abc_textfield_activated_mtrl_alpha, C0001R.drawable.abc_textfield_search_activated_mtrl_alpha, C0001R.drawable.abc_cab_background_top_mtrl_alpha, C0001R.drawable.abc_text_cursor_material, C0001R.drawable.abc_text_select_handle_left_mtrl_dark, C0001R.drawable.abc_text_select_handle_middle_mtrl_dark, C0001R.drawable.abc_text_select_handle_right_mtrl_dark, C0001R.drawable.abc_text_select_handle_left_mtrl_light, C0001R.drawable.abc_text_select_handle_middle_mtrl_light, C0001R.drawable.abc_text_select_handle_right_mtrl_light};
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {C0001R.drawable.abc_popup_background_mtrl_mult, C0001R.drawable.abc_cab_background_internal_bg, C0001R.drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = {C0001R.drawable.abc_tab_indicator_material, C0001R.drawable.abc_textfield_search_material};
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = {C0001R.drawable.abc_btn_check_material, C0001R.drawable.abc_btn_radio_material};

    private interface InflateDelegate {
        Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    public static synchronized AppCompatDrawableManager get() {
        if (INSTANCE == null) {
            INSTANCE = new AppCompatDrawableManager();
            installDefaultInflateDelegates(INSTANCE);
        }
        return INSTANCE;
    }

    private static void installDefaultInflateDelegates(AppCompatDrawableManager appCompatDrawableManager) {
        if (Build.VERSION.SDK_INT < 24) {
            appCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-selector", new AsldcInflateDelegate());
        }
    }

    public synchronized Drawable getDrawable(Context context, int i) {
        return getDrawable(context, i, false);
    }

    synchronized Drawable getDrawable(Context context, int i, boolean z) {
        Drawable drawableLoadDrawableFromDelegates;
        checkVectorDrawableSetup(context);
        drawableLoadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
        if (drawableLoadDrawableFromDelegates == null) {
            drawableLoadDrawableFromDelegates = createDrawableIfNeeded(context, i);
        }
        if (drawableLoadDrawableFromDelegates == null) {
            drawableLoadDrawableFromDelegates = ContextCompat.getDrawable(context, i);
        }
        if (drawableLoadDrawableFromDelegates != null) {
            drawableLoadDrawableFromDelegates = tintDrawable(context, i, z, drawableLoadDrawableFromDelegates);
        }
        if (drawableLoadDrawableFromDelegates != null) {
            DrawableUtils.fixDrawable(drawableLoadDrawableFromDelegates);
        }
        return drawableLoadDrawableFromDelegates;
    }

    public synchronized void onConfigurationChanged(Context context) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
    }

    private static long createCacheKey(TypedValue typedValue) {
        return (((long) typedValue.assetCookie) << 32) | ((long) typedValue.data);
    }

    private Drawable createDrawableIfNeeded(Context context, int i) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        context.getResources().getValue(i, typedValue, true);
        long jCreateCacheKey = createCacheKey(typedValue);
        Drawable cachedDrawable = getCachedDrawable(context, jCreateCacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        if (i == C0001R.drawable.abc_cab_background_top_material) {
            cachedDrawable = new LayerDrawable(new Drawable[]{getDrawable(context, C0001R.drawable.abc_cab_background_internal_bg), getDrawable(context, C0001R.drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (cachedDrawable != null) {
            cachedDrawable.setChangingConfigurations(typedValue.changingConfigurations);
            addDrawableToCache(context, jCreateCacheKey, cachedDrawable);
        }
        return cachedDrawable;
    }

    private Drawable tintDrawable(Context context, int i, boolean z, Drawable drawable) {
        ColorStateList tintList = getTintList(context, i);
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            Drawable drawableWrap = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawableWrap, tintList);
            PorterDuff.Mode tintMode = getTintMode(i);
            if (tintMode == null) {
                return drawableWrap;
            }
            DrawableCompat.setTintMode(drawableWrap, tintMode);
            return drawableWrap;
        }
        if (i == C0001R.drawable.abc_seekbar_track_material) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.background), ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.secondaryProgress), ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.progress), ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        }
        if (i == C0001R.drawable.abc_ratingbar_material || i == C0001R.drawable.abc_ratingbar_indicator_material || i == C0001R.drawable.abc_ratingbar_small_material) {
            LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.background), ThemeUtils.getDisabledThemeAttrColor(context, C0001R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.secondaryProgress), ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.progress), ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        }
        if (tintDrawableUsingColorFilter(context, i, drawable) || !z) {
            return drawable;
        }
        return null;
    }

    private Drawable loadDrawableFromDelegates(Context context, int i) {
        int next;
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap == null || arrayMap.isEmpty()) {
            return null;
        }
        SparseArrayCompat<String> sparseArrayCompat = this.mKnownDrawableIdTags;
        if (sparseArrayCompat != null) {
            String str = sparseArrayCompat.get(i);
            if (SKIP_DRAWABLE_TAG.equals(str) || (str != null && this.mDelegates.get(str) == null)) {
                return null;
            }
        } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        long jCreateCacheKey = createCacheKey(typedValue);
        Drawable cachedDrawable = getCachedDrawable(context, jCreateCacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        if (typedValue.string != null && typedValue.string.toString().endsWith(".xml")) {
            try {
                XmlResourceParser xml = resources.getXml(i);
                AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(xml);
                do {
                    next = xml.next();
                    if (next == 2) {
                        break;
                    }
                } while (next != 1);
                if (next != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String name = xml.getName();
                this.mKnownDrawableIdTags.append(i, name);
                InflateDelegate inflateDelegate = this.mDelegates.get(name);
                if (inflateDelegate != null) {
                    cachedDrawable = inflateDelegate.createFromXmlInner(context, xml, attributeSetAsAttributeSet, context.getTheme());
                }
                if (cachedDrawable != null) {
                    cachedDrawable.setChangingConfigurations(typedValue.changingConfigurations);
                    addDrawableToCache(context, jCreateCacheKey, cachedDrawable);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while inflating drawable", e);
            }
        }
        if (cachedDrawable == null) {
            this.mKnownDrawableIdTags.append(i, SKIP_DRAWABLE_TAG);
        }
        return cachedDrawable;
    }

    private synchronized Drawable getCachedDrawable(Context context, long j) {
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
        if (longSparseArray == null) {
            return null;
        }
        WeakReference<Drawable.ConstantState> weakReference = longSparseArray.get(j);
        if (weakReference != null) {
            Drawable.ConstantState constantState = weakReference.get();
            if (constantState != null) {
                return constantState.newDrawable(context.getResources());
            }
            longSparseArray.delete(j);
        }
        return null;
    }

    private synchronized boolean addDrawableToCache(Context context, long j, Drawable drawable) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        if (constantState == null) {
            return false;
        }
        LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
        if (longSparseArray == null) {
            longSparseArray = new LongSparseArray<>();
            this.mDrawableCaches.put(context, longSparseArray);
        }
        longSparseArray.put(j, new WeakReference<>(constantState));
        return true;
    }

    synchronized Drawable onDrawableLoadedFromResources(Context context, VectorEnabledTintResources vectorEnabledTintResources, int i) {
        Drawable drawableLoadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
        if (drawableLoadDrawableFromDelegates == null) {
            drawableLoadDrawableFromDelegates = vectorEnabledTintResources.superGetDrawable(i);
        }
        if (drawableLoadDrawableFromDelegates == null) {
            return null;
        }
        return tintDrawable(context, i, false, drawableLoadDrawableFromDelegates);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0061 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static boolean tintDrawableUsingColorFilter(Context context, int i, Drawable drawable) {
        boolean z;
        int iRound;
        PorterDuff.Mode mode = DEFAULT_MODE;
        boolean zArrayContains = arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, i);
        int i2 = R.attr.colorBackground;
        if (zArrayContains) {
            i2 = C0001R.attr.colorControlNormal;
        } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, i)) {
            i2 = C0001R.attr.colorControlActivated;
        } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, i)) {
            mode = PorterDuff.Mode.MULTIPLY;
        } else {
            if (i == C0001R.drawable.abc_list_divider_mtrl_alpha) {
                i2 = R.attr.colorForeground;
                iRound = Math.round(40.8f);
                z = true;
            } else if (i != C0001R.drawable.abc_dialog_material_background) {
                z = false;
                iRound = -1;
                i2 = 0;
            }
            if (z) {
                return false;
            }
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            drawable.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, i2), mode));
            if (iRound != -1) {
                drawable.setAlpha(iRound);
            }
            return true;
        }
        z = true;
        iRound = -1;
        if (z) {
        }
    }

    private void addDelegate(String str, InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<>();
        }
        this.mDelegates.put(str, inflateDelegate);
    }

    private void removeDelegate(String str, InflateDelegate inflateDelegate) {
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap == null || arrayMap.get(str) != inflateDelegate) {
            return;
        }
        this.mDelegates.remove(str);
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    static PorterDuff.Mode getTintMode(int i) {
        if (i == C0001R.drawable.abc_switch_thumb_material) {
            return PorterDuff.Mode.MULTIPLY;
        }
        return null;
    }

    synchronized ColorStateList getTintList(Context context, int i) {
        ColorStateList tintListFromCache;
        tintListFromCache = getTintListFromCache(context, i);
        if (tintListFromCache == null) {
            if (i == C0001R.drawable.abc_edit_text_material) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_edittext);
            } else if (i == C0001R.drawable.abc_switch_track_mtrl_alpha) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_switch_track);
            } else if (i == C0001R.drawable.abc_switch_thumb_material) {
                tintListFromCache = createSwitchThumbColorStateList(context);
            } else if (i == C0001R.drawable.abc_btn_default_mtrl_shape) {
                tintListFromCache = createDefaultButtonColorStateList(context);
            } else if (i == C0001R.drawable.abc_btn_borderless_material) {
                tintListFromCache = createBorderlessButtonColorStateList(context);
            } else if (i == C0001R.drawable.abc_btn_colored_material) {
                tintListFromCache = createColoredButtonColorStateList(context);
            } else if (i == C0001R.drawable.abc_spinner_mtrl_am_alpha || i == C0001R.drawable.abc_spinner_textfield_background_material) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_spinner);
            } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, i)) {
                tintListFromCache = ThemeUtils.getThemeAttrColorStateList(context, C0001R.attr.colorControlNormal);
            } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, i)) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_default);
            } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, i)) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_btn_checkable);
            } else if (i == C0001R.drawable.abc_seekbar_thumb_material) {
                tintListFromCache = AppCompatResources.getColorStateList(context, C0001R.color.abc_tint_seek_thumb);
            }
            if (tintListFromCache != null) {
                addTintListToCache(context, i, tintListFromCache);
            }
        }
        return tintListFromCache;
    }

    private ColorStateList getTintListFromCache(Context context, int i) {
        SparseArrayCompat<ColorStateList> sparseArrayCompat;
        WeakHashMap<Context, SparseArrayCompat<ColorStateList>> weakHashMap = this.mTintLists;
        if (weakHashMap == null || (sparseArrayCompat = weakHashMap.get(context)) == null) {
            return null;
        }
        return sparseArrayCompat.get(i);
    }

    private void addTintListToCache(Context context, int i, ColorStateList colorStateList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<>();
        }
        SparseArrayCompat<ColorStateList> sparseArrayCompat = this.mTintLists.get(context);
        if (sparseArrayCompat == null) {
            sparseArrayCompat = new SparseArrayCompat<>();
            this.mTintLists.put(context, sparseArrayCompat);
        }
        sparseArrayCompat.append(i, colorStateList);
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorButtonNormal));
    }

    private ColorStateList createBorderlessButtonColorStateList(Context context) {
        return createButtonColorStateList(context, 0);
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorAccent));
    }

    private ColorStateList createButtonColorStateList(Context context, int i) {
        int themeAttrColor = ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlHighlight);
        return new ColorStateList(new int[][]{ThemeUtils.DISABLED_STATE_SET, ThemeUtils.PRESSED_STATE_SET, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{ThemeUtils.getDisabledThemeAttrColor(context, C0001R.attr.colorButtonNormal), ColorUtils.compositeColors(themeAttrColor, i), ColorUtils.compositeColors(themeAttrColor, i), i});
    }

    private ColorStateList createSwitchThumbColorStateList(Context context) {
        int[][] iArr = new int[3][];
        int[] iArr2 = new int[3];
        ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, C0001R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = themeAttrColorStateList.getColorForState(iArr[0], 0);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = themeAttrColorStateList.getDefaultColor();
        } else {
            iArr[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr2[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0001R.attr.colorSwitchThumbNormal);
            iArr[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr2[1] = ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorControlActivated);
            iArr[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr2[2] = ThemeUtils.getThemeAttrColor(context, C0001R.attr.colorSwitchThumbNormal);
        }
        return new ColorStateList(iArr, iArr2);
    }

    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int i) {
            super(i);
        }

        PorterDuffColorFilter get(int i, PorterDuff.Mode mode) {
            return get(Integer.valueOf(generateCacheKey(i, mode)));
        }

        PorterDuffColorFilter put(int i, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return put(Integer.valueOf(generateCacheKey(i, mode)), porterDuffColorFilter);
        }

        private static int generateCacheKey(int i, PorterDuff.Mode mode) {
            return ((i + 31) * 31) + mode.hashCode();
        }
    }

    static void tintDrawable(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable) && drawable.mutate() != drawable) {
            Log.d(TAG, "Mutated drawable is not the same instance as the input.");
            return;
        }
        if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
            drawable.setColorFilter(createTintFilter(tintInfo.mHasTintList ? tintInfo.mTintList : null, tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE, iArr));
        } else {
            drawable.clearColorFilter();
        }
        if (Build.VERSION.SDK_INT <= 23) {
            drawable.invalidateSelf();
        }
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return getPorterDuffColorFilter(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static synchronized PorterDuffColorFilter getPorterDuffColorFilter(int i, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        porterDuffColorFilter = COLOR_FILTER_CACHE.get(i, mode);
        if (porterDuffColorFilter == null) {
            porterDuffColorFilter = new PorterDuffColorFilter(i, mode);
            COLOR_FILTER_CACHE.put(i, mode, porterDuffColorFilter);
        }
        return porterDuffColorFilter;
    }

    private static void setPorterDuffColorFilter(Drawable drawable, int i, PorterDuff.Mode mode) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawable = drawable.mutate();
        }
        if (mode == null) {
            mode = DEFAULT_MODE;
        }
        drawable.setColorFilter(getPorterDuffColorFilter(i, mode));
    }

    private void checkVectorDrawableSetup(Context context) {
        if (this.mHasCheckedVectorDrawableSetup) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = true;
        Drawable drawable = getDrawable(context, C0001R.drawable.abc_vector_test);
        if (drawable == null || !isVectorDrawable(drawable)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private static boolean isVectorDrawable(Drawable drawable) {
        return (drawable instanceof VectorDrawableCompat) || PLATFORM_VD_CLAZZ.equals(drawable.getClass().getName());
    }

    private static class VdcInflateDelegate implements InflateDelegate {
        VdcInflateDelegate() {
        }

        @Override
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    private static class AvdcInflateDelegate implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        @Override
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }

    static class AsldcInflateDelegate implements InflateDelegate {
        AsldcInflateDelegate() {
        }

        @Override
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return AnimatedStateListDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", e);
                return null;
            }
        }
    }
}
