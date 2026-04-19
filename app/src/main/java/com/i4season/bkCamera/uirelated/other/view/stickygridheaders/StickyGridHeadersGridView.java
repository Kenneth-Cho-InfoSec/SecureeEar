package com.i4season.bkCamera.uirelated.other.view.stickygridheaders;

import android.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.i4season.bkCamera.uirelated.other.view.stickygridheaders.StickyGridHeadersBaseAdapterWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class StickyGridHeadersGridView extends GridView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {
    private static final int MATCHED_STICKIED_HEADER = -2;
    private static final int NO_MATCHED_HEADER = -1;
    protected static final int TOUCH_MODE_DONE_WAITING = 2;
    protected static final int TOUCH_MODE_DOWN = 0;
    protected static final int TOUCH_MODE_FINISHED_LONG_PRESS = -2;
    protected static final int TOUCH_MODE_REST = -1;
    protected static final int TOUCH_MODE_TAP = 1;
    protected StickyGridHeadersBaseAdapterWrapper mAdapter;
    private boolean mAreHeadersSticky;
    private boolean mClipToPaddingHasBeenSet;
    private final Rect mClippingRect;
    private boolean mClippingToPadding;
    private int mColumnWidth;
    private long mCurrentHeaderId;
    protected boolean mDataChanged;
    private DataSetObserver mDataSetObserver;
    private int mHeaderBottomPosition;
    boolean mHeaderChildBeingPressed;
    private boolean mHeadersIgnorePadding;
    private int mHorizontalSpacing;
    private boolean mMaskStickyHeaderRegion;
    public int mMotionHeaderPosition;
    private float mMotionY;
    private int mNumColumns;
    private boolean mNumColumnsSet;
    private int mNumMeasuredColumns;
    private OnHeaderClickListener mOnHeaderClickListener;
    private OnHeaderLongClickListener mOnHeaderLongClickListener;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    public CheckForHeaderLongPress mPendingCheckForLongPress;
    public CheckForHeaderTap mPendingCheckForTap;
    private PerformHeaderClick mPerformHeaderClick;
    private AbsListView.OnScrollListener mScrollListener;
    private int mScrollState;
    private View mStickiedHeader;
    protected int mTouchMode;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    private int mVerticalSpacing;
    private static final String ERROR_PLATFORM = "Error supporting platform " + Build.VERSION.SDK_INT + ".";
    static final String TAG = StickyGridHeadersGridView.class.getSimpleName();

    public interface OnHeaderClickListener {
        void onHeaderClick(AdapterView<?> adapterView, View view, long j);
    }

    public interface OnHeaderLongClickListener {
        boolean onHeaderLongClick(AdapterView<?> adapterView, View view, long j);
    }

    private static MotionEvent.PointerCoords[] getPointerCoords(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        MotionEvent.PointerCoords[] pointerCoordsArr = new MotionEvent.PointerCoords[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            pointerCoordsArr[i] = new MotionEvent.PointerCoords();
            motionEvent.getPointerCoords(i, pointerCoordsArr[i]);
        }
        return pointerCoordsArr;
    }

    private static int[] getPointerIds(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        int[] iArr = new int[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            iArr[i] = motionEvent.getPointerId(i);
        }
        return iArr;
    }

    public StickyGridHeadersGridView(Context context) {
        this(context, null);
    }

    public StickyGridHeadersGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.gridViewStyle);
    }

    public StickyGridHeadersGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAreHeadersSticky = true;
        this.mClippingRect = new Rect();
        this.mCurrentHeaderId = -1L;
        this.mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                StickyGridHeadersGridView.this.reset();
            }

            @Override
            public void onInvalidated() {
                StickyGridHeadersGridView.this.reset();
            }
        };
        this.mMaskStickyHeaderRegion = true;
        this.mNumMeasuredColumns = 1;
        this.mScrollState = 0;
        this.mHeaderChildBeingPressed = false;
        super.setOnScrollListener(this);
        setVerticalFadingEdgeEnabled(false);
        if (!this.mNumColumnsSet) {
            this.mNumColumns = -1;
        }
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean areHeadersSticky() {
        return this.mAreHeadersSticky;
    }

    public View getHeaderAt(int i) {
        if (i == -2) {
            return this.mStickiedHeader;
        }
        try {
            return (View) getChildAt(i).getTag();
        } catch (Exception unused) {
            return null;
        }
    }

    public View getStickiedHeader() {
        return this.mStickiedHeader;
    }

    public boolean getStickyHeaderIsTranscluent() {
        return !this.mMaskStickyHeaderRegion;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.mOnItemClickListener.onItemClick(adapterView, view, this.mAdapter.translatePosition(i).mPosition, j);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        return this.mOnItemLongClickListener.onItemLongClick(adapterView, view, this.mAdapter.translatePosition(i).mPosition, j);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        this.mOnItemSelectedListener.onItemSelected(adapterView, view, this.mAdapter.translatePosition(i).mPosition, j);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.mOnItemSelectedListener.onNothingSelected(adapterView);
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mAreHeadersSticky = savedState.areHeadersSticky;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.areHeadersSticky = this.mAreHeadersSticky;
        return savedState;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        AbsListView.OnScrollListener onScrollListener = this.mScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScroll(absListView, i, i2, i3);
        }
        if (Build.VERSION.SDK_INT >= 8) {
            scrollChanged(i);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        AbsListView.OnScrollListener onScrollListener = this.mScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(absListView, i);
        }
        this.mScrollState = i;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        int action = motionEvent.getAction();
        boolean z = this.mHeaderChildBeingPressed;
        if (z) {
            View headerAt = getHeaderAt(this.mMotionHeaderPosition);
            int i2 = this.mMotionHeaderPosition;
            final View childAt = i2 == -2 ? headerAt : getChildAt(i2);
            if (action == 1 || action == 3) {
                this.mHeaderChildBeingPressed = false;
            }
            if (headerAt != null) {
                headerAt.dispatchTouchEvent(transformEvent(motionEvent, this.mMotionHeaderPosition));
                headerAt.invalidate();
                headerAt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StickyGridHeadersGridView.this.invalidate(0, childAt.getTop(), StickyGridHeadersGridView.this.getWidth(), childAt.getTop() + childAt.getHeight());
                    }
                }, ViewConfiguration.getPressedStateDuration());
                invalidate(0, childAt.getTop(), getWidth(), childAt.getTop() + childAt.getHeight());
            }
        }
        int i3 = action & 255;
        if (i3 == 0) {
            if (this.mPendingCheckForTap == null) {
                this.mPendingCheckForTap = new CheckForHeaderTap();
            }
            postDelayed(this.mPendingCheckForTap, ViewConfiguration.getTapTimeout());
            float y = (int) motionEvent.getY();
            this.mMotionY = y;
            this.mMotionHeaderPosition = findMotionHeader(y);
            int i4 = this.mMotionHeaderPosition;
            if (i4 != -1 && this.mScrollState != 2) {
                View headerAt2 = getHeaderAt(i4);
                if (headerAt2 != null) {
                    if (headerAt2.dispatchTouchEvent(transformEvent(motionEvent, this.mMotionHeaderPosition))) {
                        this.mHeaderChildBeingPressed = true;
                        headerAt2.setPressed(true);
                    }
                    headerAt2.invalidate();
                    int i5 = this.mMotionHeaderPosition;
                    if (i5 != -2) {
                        headerAt2 = getChildAt(i5);
                    }
                    invalidate(0, headerAt2.getTop(), getWidth(), headerAt2.getTop() + headerAt2.getHeight());
                }
                this.mTouchMode = 0;
                return true;
            }
        } else if (i3 != 1) {
            if (i3 == 2 && this.mMotionHeaderPosition != -1 && Math.abs(motionEvent.getY() - this.mMotionY) > this.mTouchSlop) {
                this.mTouchMode = -1;
                View headerAt3 = getHeaderAt(this.mMotionHeaderPosition);
                if (headerAt3 != null) {
                    headerAt3.setPressed(false);
                    headerAt3.invalidate();
                }
                Handler handler = getHandler();
                if (handler != null) {
                    handler.removeCallbacks(this.mPendingCheckForLongPress);
                }
                this.mMotionHeaderPosition = -1;
            }
        } else {
            int i6 = this.mTouchMode;
            if (i6 == -2) {
                this.mTouchMode = -1;
                return true;
            }
            if (i6 != -1 && (i = this.mMotionHeaderPosition) != -1) {
                final View headerAt4 = getHeaderAt(i);
                if (!z && headerAt4 != null) {
                    if (this.mTouchMode != 0) {
                        headerAt4.setPressed(false);
                    }
                    if (this.mPerformHeaderClick == null) {
                        this.mPerformHeaderClick = new PerformHeaderClick();
                    }
                    final PerformHeaderClick performHeaderClick = this.mPerformHeaderClick;
                    performHeaderClick.mClickMotionPosition = this.mMotionHeaderPosition;
                    performHeaderClick.rememberWindowAttachCount();
                    int i7 = this.mTouchMode;
                    if (i7 == 0 || i7 == 1) {
                        Handler handler2 = getHandler();
                        if (handler2 != null) {
                            handler2.removeCallbacks(this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress);
                        }
                        if (!this.mDataChanged) {
                            this.mTouchMode = 1;
                            headerAt4.setPressed(true);
                            setPressed(true);
                            Runnable runnable = this.mTouchModeReset;
                            if (runnable != null) {
                                removeCallbacks(runnable);
                            }
                            this.mTouchModeReset = new Runnable() {
                                @Override
                                public void run() {
                                    StickyGridHeadersGridView stickyGridHeadersGridView = StickyGridHeadersGridView.this;
                                    stickyGridHeadersGridView.mMotionHeaderPosition = -1;
                                    stickyGridHeadersGridView.mTouchModeReset = null;
                                    StickyGridHeadersGridView.this.mTouchMode = -1;
                                    headerAt4.setPressed(false);
                                    StickyGridHeadersGridView.this.setPressed(false);
                                    headerAt4.invalidate();
                                    StickyGridHeadersGridView.this.invalidate(0, headerAt4.getTop(), StickyGridHeadersGridView.this.getWidth(), headerAt4.getHeight());
                                    if (StickyGridHeadersGridView.this.mDataChanged) {
                                        return;
                                    }
                                    performHeaderClick.run();
                                }
                            };
                            postDelayed(this.mTouchModeReset, ViewConfiguration.getPressedStateDuration());
                        } else {
                            this.mTouchMode = -1;
                        }
                    } else if (!this.mDataChanged) {
                        performHeaderClick.run();
                    }
                }
                this.mTouchMode = -1;
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean performHeaderClick(View view, long j) {
        if (this.mOnHeaderClickListener == null) {
            return false;
        }
        playSoundEffect(0);
        if (view != null) {
            view.sendAccessibilityEvent(1);
        }
        this.mOnHeaderClickListener.onHeaderClick(this, view, j);
        return true;
    }

    public boolean performHeaderLongPress(View view, long j) {
        OnHeaderLongClickListener onHeaderLongClickListener = this.mOnHeaderLongClickListener;
        boolean zOnHeaderLongClick = onHeaderLongClickListener != null ? onHeaderLongClickListener.onHeaderLongClick(this, view, j) : false;
        if (zOnHeaderLongClick) {
            if (view != null) {
                view.sendAccessibilityEvent(2);
            }
            performHapticFeedback(0);
        }
        return zOnHeaderLongClick;
    }

    @Override
    public void setAdapter(ListAdapter listAdapter) {
        StickyGridHeadersBaseAdapter stickyGridHeadersListAdapterWrapper;
        DataSetObserver dataSetObserver;
        if (listAdapter == null) {
            return;
        }
        StickyGridHeadersBaseAdapterWrapper stickyGridHeadersBaseAdapterWrapper = this.mAdapter;
        if (stickyGridHeadersBaseAdapterWrapper != null && (dataSetObserver = this.mDataSetObserver) != null) {
            stickyGridHeadersBaseAdapterWrapper.unregisterDataSetObserver(dataSetObserver);
        }
        if (!this.mClipToPaddingHasBeenSet) {
            this.mClippingToPadding = true;
        }
        if (listAdapter instanceof StickyGridHeadersBaseAdapter) {
            stickyGridHeadersListAdapterWrapper = (StickyGridHeadersBaseAdapter) listAdapter;
        } else if (listAdapter instanceof StickyGridHeadersSimpleAdapter) {
            stickyGridHeadersListAdapterWrapper = new StickyGridHeadersSimpleAdapterWrapper((StickyGridHeadersSimpleAdapter) listAdapter);
        } else {
            stickyGridHeadersListAdapterWrapper = new StickyGridHeadersListAdapterWrapper(listAdapter);
        }
        this.mAdapter = new StickyGridHeadersBaseAdapterWrapper(getContext(), this, stickyGridHeadersListAdapterWrapper);
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        reset();
        super.setAdapter((ListAdapter) this.mAdapter);
    }

    public void setAreHeadersSticky(boolean z) {
        if (z != this.mAreHeadersSticky) {
            this.mAreHeadersSticky = z;
            requestLayout();
        }
    }

    @Override
    public void setClipToPadding(boolean z) {
        super.setClipToPadding(z);
        this.mClippingToPadding = z;
        this.mClipToPaddingHasBeenSet = true;
    }

    @Override
    public void setColumnWidth(int i) {
        super.setColumnWidth(i);
        this.mColumnWidth = i;
    }

    public void setHeadersIgnorePadding(boolean z) {
        this.mHeadersIgnorePadding = z;
    }

    @Override
    public void setHorizontalSpacing(int i) {
        super.setHorizontalSpacing(i);
        this.mHorizontalSpacing = i;
    }

    @Override
    public void setNumColumns(int i) {
        StickyGridHeadersBaseAdapterWrapper stickyGridHeadersBaseAdapterWrapper;
        super.setNumColumns(i);
        this.mNumColumnsSet = true;
        this.mNumColumns = i;
        if (i == -1 || (stickyGridHeadersBaseAdapterWrapper = this.mAdapter) == null) {
            return;
        }
        stickyGridHeadersBaseAdapterWrapper.setNumColumns(i);
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public void setOnHeaderLongClickListener(OnHeaderLongClickListener onHeaderLongClickListener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        this.mOnHeaderLongClickListener = onHeaderLongClickListener;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        super.setOnItemClickListener(this);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
        super.setOnItemLongClickListener(this);
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
        super.setOnItemSelectedListener(this);
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setStickyHeaderIsTranscluent(boolean z) {
        this.mMaskStickyHeaderRegion = !z;
    }

    @Override
    public void setVerticalSpacing(int i) {
        super.setVerticalSpacing(i);
        this.mVerticalSpacing = i;
    }

    private int findMotionHeader(float f) {
        if (this.mStickiedHeader != null && f <= this.mHeaderBottomPosition) {
            return -2;
        }
        int i = 0;
        int firstVisiblePosition = getFirstVisiblePosition();
        while (firstVisiblePosition <= getLastVisiblePosition()) {
            if (getItemIdAtPosition(firstVisiblePosition) == -1) {
                View childAt = getChildAt(i);
                int bottom = childAt.getBottom();
                int top = childAt.getTop();
                if (f <= bottom && f >= top) {
                    return i;
                }
            }
            int i2 = this.mNumMeasuredColumns;
            firstVisiblePosition += i2;
            i += i2;
        }
        return -1;
    }

    private int getHeaderHeight() {
        View view = this.mStickiedHeader;
        if (view != null) {
            return view.getMeasuredHeight();
        }
        return 0;
    }

    public long headerViewPositionToId(int i) {
        if (i == -2) {
            return this.mCurrentHeaderId;
        }
        StickyGridHeadersBaseAdapterWrapper stickyGridHeadersBaseAdapterWrapper = this.mAdapter;
        if (stickyGridHeadersBaseAdapterWrapper == null) {
            return 0L;
        }
        return stickyGridHeadersBaseAdapterWrapper.getHeaderId(getFirstVisiblePosition() + i);
    }

    private void measureHeader() {
        int iMakeMeasureSpec;
        int iMakeMeasureSpec2;
        if (this.mStickiedHeader == null) {
            return;
        }
        if (this.mHeadersIgnorePadding) {
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
        } else {
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
        }
        ViewGroup.LayoutParams layoutParams = this.mStickiedHeader.getLayoutParams();
        if (layoutParams != null && layoutParams.height > 0) {
            iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
        } else {
            iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        this.mStickiedHeader.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        this.mStickiedHeader.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
        if (this.mHeadersIgnorePadding) {
            this.mStickiedHeader.layout(getLeft(), 0, getRight(), this.mStickiedHeader.getMeasuredHeight());
        } else {
            this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), this.mStickiedHeader.getMeasuredHeight());
        }
    }

    public void reset() {
        this.mHeaderBottomPosition = 0;
        swapStickiedHeader(null);
        this.mCurrentHeaderId = Long.MIN_VALUE;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:83:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void scrollChanged(int i) {
        long headerId;
        long headerId2;
        int i2;
        long headerId3;
        int childCount;
        int paddingTop;
        int top;
        StickyGridHeadersBaseAdapterWrapper stickyGridHeadersBaseAdapterWrapper = this.mAdapter;
        if (stickyGridHeadersBaseAdapterWrapper == null || stickyGridHeadersBaseAdapterWrapper.getCount() == 0 || !this.mAreHeadersSticky || getChildAt(0) == null) {
            return;
        }
        int i3 = i - this.mNumMeasuredColumns;
        if (i3 < 0) {
            i3 = i;
        }
        int i4 = this.mNumMeasuredColumns + i;
        if (i4 >= this.mAdapter.getCount()) {
            i4 = i;
        }
        int i5 = this.mVerticalSpacing;
        if (i5 == 0) {
            headerId = this.mAdapter.getHeaderId(i);
        } else {
            if (i5 < 0) {
                this.mAdapter.getHeaderId(i);
                if (getChildAt(this.mNumMeasuredColumns).getTop() <= 0) {
                    headerId3 = this.mAdapter.getHeaderId(i4);
                } else {
                    headerId3 = this.mAdapter.getHeaderId(i);
                    i4 = i;
                }
                headerId2 = headerId3;
                i2 = i4;
            } else {
                int top2 = getChildAt(0).getTop();
                if (top2 > 0 && top2 < this.mVerticalSpacing) {
                    headerId2 = this.mAdapter.getHeaderId(i3);
                    i2 = i3;
                } else {
                    headerId = this.mAdapter.getHeaderId(i);
                }
            }
            headerId = headerId2;
            if (this.mCurrentHeaderId != headerId) {
                swapStickiedHeader(this.mAdapter.getHeaderView(i2, this.mStickiedHeader, this));
                measureHeader();
                this.mCurrentHeaderId = headerId;
            }
            childCount = getChildCount();
            if (childCount == 0) {
                View view = null;
                int i6 = 0;
                int i7 = 99999;
                while (i6 < childCount) {
                    View childAt = super.getChildAt(i6);
                    if (this.mClippingToPadding) {
                        top = childAt.getTop() - getPaddingTop();
                    } else {
                        top = childAt.getTop();
                    }
                    if (top >= 0 && this.mAdapter.getItemId(getPositionForView(childAt)) == -1 && top < i7) {
                        view = childAt;
                        i7 = top;
                    }
                    i6 += this.mNumMeasuredColumns;
                }
                int headerHeight = getHeaderHeight();
                if (view != null) {
                    if (i == 0 && super.getChildAt(0).getTop() > 0 && !this.mClippingToPadding) {
                        this.mHeaderBottomPosition = 0;
                        return;
                    }
                    if (this.mClippingToPadding) {
                        this.mHeaderBottomPosition = Math.min(view.getTop(), getPaddingTop() + headerHeight);
                        if (this.mHeaderBottomPosition < getPaddingTop()) {
                            paddingTop = headerHeight + getPaddingTop();
                        } else {
                            paddingTop = this.mHeaderBottomPosition;
                        }
                        this.mHeaderBottomPosition = paddingTop;
                        return;
                    }
                    this.mHeaderBottomPosition = Math.min(view.getTop(), headerHeight);
                    int i8 = this.mHeaderBottomPosition;
                    if (i8 < 0) {
                        i8 = headerHeight;
                    }
                    this.mHeaderBottomPosition = i8;
                    return;
                }
                this.mHeaderBottomPosition = headerHeight;
                if (this.mClippingToPadding) {
                    this.mHeaderBottomPosition += getPaddingTop();
                    return;
                }
                return;
            }
            return;
        }
        i2 = i;
        if (this.mCurrentHeaderId != headerId) {
        }
        childCount = getChildCount();
        if (childCount == 0) {
        }
    }

    private void swapStickiedHeader(View view) {
        detachHeader(this.mStickiedHeader);
        attachHeader(view);
        this.mStickiedHeader = view;
    }

    private MotionEvent transformEvent(MotionEvent motionEvent, int i) {
        if (i == -2) {
            return motionEvent;
        }
        long downTime = motionEvent.getDownTime();
        long eventTime = motionEvent.getEventTime();
        int action = motionEvent.getAction();
        int pointerCount = motionEvent.getPointerCount();
        int[] pointerIds = getPointerIds(motionEvent);
        MotionEvent.PointerCoords[] pointerCoords = getPointerCoords(motionEvent);
        int metaState = motionEvent.getMetaState();
        float xPrecision = motionEvent.getXPrecision();
        float yPrecision = motionEvent.getYPrecision();
        int deviceId = motionEvent.getDeviceId();
        int edgeFlags = motionEvent.getEdgeFlags();
        int source = motionEvent.getSource();
        int flags = motionEvent.getFlags();
        View childAt = getChildAt(i);
        int i2 = 0;
        while (i2 < pointerCount) {
            int i3 = source;
            pointerCoords[i2].y -= childAt.getTop();
            i2++;
            source = i3;
            edgeFlags = edgeFlags;
            deviceId = deviceId;
        }
        return MotionEvent.obtain(downTime, eventTime, action, pointerCount, pointerIds, pointerCoords, metaState, xPrecision, yPrecision, deviceId, edgeFlags, source, flags);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int width;
        int iMakeMeasureSpec;
        int iMakeMeasureSpec2;
        Canvas canvas2 = canvas;
        if (Build.VERSION.SDK_INT < 8) {
            scrollChanged(getFirstVisiblePosition());
        }
        View view = this.mStickiedHeader;
        boolean z = view != null && this.mAreHeadersSticky && view.getVisibility() == 0;
        int headerHeight = getHeaderHeight();
        int i = this.mHeaderBottomPosition - headerHeight;
        if (z && this.mMaskStickyHeaderRegion) {
            if (this.mHeadersIgnorePadding) {
                Rect rect = this.mClippingRect;
                rect.left = 0;
                rect.right = getWidth();
            } else {
                this.mClippingRect.left = getPaddingLeft();
                this.mClippingRect.right = getWidth() - getPaddingRight();
            }
            Rect rect2 = this.mClippingRect;
            rect2.top = this.mHeaderBottomPosition;
            rect2.bottom = getHeight();
            canvas.save();
            canvas2.clipRect(this.mClippingRect);
        }
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList arrayList = new ArrayList();
        int firstVisiblePosition = getFirstVisiblePosition();
        int i2 = 0;
        while (firstVisiblePosition <= getLastVisiblePosition()) {
            if (getItemIdAtPosition(firstVisiblePosition) == -1) {
                arrayList.add(Integer.valueOf(i2));
            }
            int i3 = this.mNumMeasuredColumns;
            firstVisiblePosition += i3;
            i2 += i3;
        }
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            View childAt = getChildAt(((Integer) arrayList.get(i4)).intValue());
            try {
                View view2 = (View) childAt.getTag();
                boolean z2 = ((long) ((StickyGridHeadersBaseAdapterWrapper.HeaderFillerView) childAt).getHeaderId()) == this.mCurrentHeaderId && childAt.getTop() < 0 && this.mAreHeadersSticky;
                if (view2.getVisibility() != 0 || z2) {
                    canvas2 = canvas;
                } else {
                    if (this.mHeadersIgnorePadding) {
                        iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
                    } else {
                        iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
                    }
                    int iMakeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(0, 0);
                    view2.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                    view2.measure(iMakeMeasureSpec2, iMakeMeasureSpec3);
                    if (this.mHeadersIgnorePadding) {
                        view2.layout(getLeft(), 0, getRight(), childAt.getHeight());
                    } else {
                        view2.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), childAt.getHeight());
                    }
                    if (this.mHeadersIgnorePadding) {
                        Rect rect3 = this.mClippingRect;
                        rect3.left = 0;
                        rect3.right = getWidth();
                    } else {
                        this.mClippingRect.left = getPaddingLeft();
                        this.mClippingRect.right = getWidth() - getPaddingRight();
                    }
                    this.mClippingRect.bottom = childAt.getBottom();
                    this.mClippingRect.top = childAt.getTop();
                    canvas.save();
                    canvas2 = canvas;
                    canvas2.clipRect(this.mClippingRect);
                    if (this.mHeadersIgnorePadding) {
                        canvas2.translate(0.0f, childAt.getTop());
                    } else {
                        canvas2.translate(getPaddingLeft(), childAt.getTop());
                    }
                    view2.draw(canvas2);
                    canvas.restore();
                }
            } catch (Exception unused) {
                return;
            }
        }
        if (z && this.mMaskStickyHeaderRegion) {
            canvas.restore();
        } else if (!z) {
            return;
        }
        if (this.mHeadersIgnorePadding) {
            width = getWidth();
        } else {
            width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        }
        if (this.mStickiedHeader.getWidth() != width) {
            if (this.mHeadersIgnorePadding) {
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
            } else {
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec((getWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824);
            }
            int iMakeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(0, 0);
            this.mStickiedHeader.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this.mStickiedHeader.measure(iMakeMeasureSpec, iMakeMeasureSpec4);
            if (this.mHeadersIgnorePadding) {
                this.mStickiedHeader.layout(getLeft(), 0, getRight(), this.mStickiedHeader.getHeight());
            } else {
                this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), this.mStickiedHeader.getHeight());
            }
        }
        if (this.mHeadersIgnorePadding) {
            Rect rect4 = this.mClippingRect;
            rect4.left = 0;
            rect4.right = getWidth();
        } else {
            this.mClippingRect.left = getPaddingLeft();
            this.mClippingRect.right = getWidth() - getPaddingRight();
        }
        Rect rect5 = this.mClippingRect;
        rect5.bottom = i + headerHeight;
        if (this.mClippingToPadding) {
            rect5.top = getPaddingTop();
        } else {
            rect5.top = 0;
        }
        canvas.save();
        canvas2.clipRect(this.mClippingRect);
        if (this.mHeadersIgnorePadding) {
            canvas2.translate(0.0f, i);
        } else {
            canvas2.translate(getPaddingLeft(), i);
        }
        if (this.mHeaderBottomPosition != headerHeight) {
            canvas.saveLayerAlpha(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), (this.mHeaderBottomPosition * 255) / headerHeight, 31);
        }
        this.mStickiedHeader.draw(canvas2);
        if (this.mHeaderBottomPosition != headerHeight) {
            canvas.restore();
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4 = this.mNumColumns;
        if (i4 == -1) {
            if (this.mColumnWidth > 0) {
                int iMax = Math.max((View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight(), 0);
                i3 = iMax / this.mColumnWidth;
                if (i3 > 0) {
                    while (i3 != 1 && (this.mColumnWidth * i3) + ((i3 - 1) * this.mHorizontalSpacing) > iMax) {
                        i3--;
                    }
                } else {
                    i3 = 1;
                }
            } else {
                i3 = 2;
            }
            this.mNumMeasuredColumns = i3;
        } else {
            this.mNumMeasuredColumns = i4;
        }
        StickyGridHeadersBaseAdapterWrapper stickyGridHeadersBaseAdapterWrapper = this.mAdapter;
        if (stickyGridHeadersBaseAdapterWrapper != null) {
            stickyGridHeadersBaseAdapterWrapper.setNumColumns(this.mNumMeasuredColumns);
        }
        measureHeader();
        super.onMeasure(i, i2);
    }

    void attachHeader(View view) {
        if (view == null || Build.VERSION.SDK_INT > 28) {
            return;
        }
        try {
            Field declaredField = View.class.getDeclaredField("mAttachInfo");
            declaredField.setAccessible(true);
            Method declaredMethod = View.class.getDeclaredMethod("dispatchAttachedToWindow", Class.forName("android.view.View$AttachInfo"), Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(view, declaredField.get(this), 8);
        } catch (ClassNotFoundException e) {
            throw new RuntimePlatformSupportException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimePlatformSupportException(e2);
        } catch (IllegalArgumentException e3) {
            throw new RuntimePlatformSupportException(e3);
        } catch (NoSuchFieldException e4) {
            throw new RuntimePlatformSupportException(e4);
        } catch (NoSuchMethodException e5) {
            throw new RuntimePlatformSupportException(e5);
        } catch (InvocationTargetException e6) {
            throw new RuntimePlatformSupportException(e6);
        }
    }

    void detachHeader(View view) {
        if (view == null || Build.VERSION.SDK_INT > 28) {
            return;
        }
        try {
            Method declaredMethod = View.class.getDeclaredMethod("dispatchDetachedFromWindow", new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(view, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimePlatformSupportException(e);
        } catch (IllegalArgumentException e2) {
            throw new RuntimePlatformSupportException(e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimePlatformSupportException(e3);
        } catch (InvocationTargetException e4) {
            throw new RuntimePlatformSupportException(e4);
        }
    }

    private class CheckForHeaderLongPress extends WindowRunnable implements Runnable {
        private CheckForHeaderLongPress() {
            super();
        }

        @Override
        public void run() {
            StickyGridHeadersGridView stickyGridHeadersGridView = StickyGridHeadersGridView.this;
            View headerAt = stickyGridHeadersGridView.getHeaderAt(stickyGridHeadersGridView.mMotionHeaderPosition);
            if (headerAt != null) {
                StickyGridHeadersGridView stickyGridHeadersGridView2 = StickyGridHeadersGridView.this;
                if ((!sameWindow() || StickyGridHeadersGridView.this.mDataChanged) ? false : StickyGridHeadersGridView.this.performHeaderLongPress(headerAt, stickyGridHeadersGridView2.headerViewPositionToId(stickyGridHeadersGridView2.mMotionHeaderPosition))) {
                    StickyGridHeadersGridView stickyGridHeadersGridView3 = StickyGridHeadersGridView.this;
                    stickyGridHeadersGridView3.mTouchMode = -2;
                    stickyGridHeadersGridView3.setPressed(false);
                    headerAt.setPressed(false);
                    return;
                }
                StickyGridHeadersGridView.this.mTouchMode = 2;
            }
        }
    }

    private class PerformHeaderClick extends WindowRunnable implements Runnable {
        int mClickMotionPosition;

        private PerformHeaderClick() {
            super();
        }

        @Override
        public void run() {
            int i;
            View headerAt;
            if (StickyGridHeadersGridView.this.mDataChanged || StickyGridHeadersGridView.this.mAdapter == null || StickyGridHeadersGridView.this.mAdapter.getCount() <= 0 || (i = this.mClickMotionPosition) == -1 || i >= StickyGridHeadersGridView.this.mAdapter.getCount() || !sameWindow() || (headerAt = StickyGridHeadersGridView.this.getHeaderAt(this.mClickMotionPosition)) == null) {
                return;
            }
            StickyGridHeadersGridView stickyGridHeadersGridView = StickyGridHeadersGridView.this;
            stickyGridHeadersGridView.performHeaderClick(headerAt, stickyGridHeadersGridView.headerViewPositionToId(this.mClickMotionPosition));
        }
    }

    private class WindowRunnable {
        private int mOriginalAttachCount;

        private WindowRunnable() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = StickyGridHeadersGridView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return StickyGridHeadersGridView.this.hasWindowFocus() && StickyGridHeadersGridView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    final class CheckForHeaderTap implements Runnable {
        CheckForHeaderTap() {
        }

        @Override
        public void run() {
            if (StickyGridHeadersGridView.this.mTouchMode == 0) {
                StickyGridHeadersGridView stickyGridHeadersGridView = StickyGridHeadersGridView.this;
                stickyGridHeadersGridView.mTouchMode = 1;
                View headerAt = stickyGridHeadersGridView.getHeaderAt(stickyGridHeadersGridView.mMotionHeaderPosition);
                if (headerAt == null || StickyGridHeadersGridView.this.mHeaderChildBeingPressed) {
                    return;
                }
                if (!StickyGridHeadersGridView.this.mDataChanged) {
                    headerAt.setPressed(true);
                    StickyGridHeadersGridView.this.setPressed(true);
                    StickyGridHeadersGridView.this.refreshDrawableState();
                    int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    if (StickyGridHeadersGridView.this.isLongClickable()) {
                        if (StickyGridHeadersGridView.this.mPendingCheckForLongPress == null) {
                            StickyGridHeadersGridView stickyGridHeadersGridView2 = StickyGridHeadersGridView.this;
                            stickyGridHeadersGridView2.mPendingCheckForLongPress = new CheckForHeaderLongPress();
                        }
                        StickyGridHeadersGridView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                        StickyGridHeadersGridView stickyGridHeadersGridView3 = StickyGridHeadersGridView.this;
                        stickyGridHeadersGridView3.postDelayed(stickyGridHeadersGridView3.mPendingCheckForLongPress, longPressTimeout);
                        return;
                    }
                    StickyGridHeadersGridView.this.mTouchMode = 2;
                    return;
                }
                StickyGridHeadersGridView.this.mTouchMode = 2;
            }
        }
    }

    class RuntimePlatformSupportException extends RuntimeException {
        private static final long serialVersionUID = -6512098808936536538L;

        public RuntimePlatformSupportException(Exception exc) {
            super(StickyGridHeadersGridView.ERROR_PLATFORM, exc);
        }
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            /* JADX WARN: Can't rename method to resolve collision */
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean areHeadersSticky;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.areHeadersSticky = parcel.readByte() != 0;
        }

        public String toString() {
            return "StickyGridHeadersGridView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " areHeadersSticky=" + this.areHeadersSticky + "}";
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.areHeadersSticky ? (byte) 1 : (byte) 0);
        }
    }
}
