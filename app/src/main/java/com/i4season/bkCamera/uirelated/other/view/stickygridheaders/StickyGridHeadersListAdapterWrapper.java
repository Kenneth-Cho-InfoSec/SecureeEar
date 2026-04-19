package com.i4season.bkCamera.uirelated.other.view.stickygridheaders;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class StickyGridHeadersListAdapterWrapper extends BaseAdapter implements StickyGridHeadersBaseAdapter {
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            StickyGridHeadersListAdapterWrapper.this.notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            StickyGridHeadersListAdapterWrapper.this.notifyDataSetInvalidated();
        }
    };
    private ListAdapter mDelegate;

    @Override
    public int getCountForHeader(int i) {
        return 0;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getNumHeaders() {
        return 0;
    }

    public StickyGridHeadersListAdapterWrapper(ListAdapter listAdapter) {
        this.mDelegate = listAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.mDataSetObserver);
        }
    }

    @Override
    public int getCount() {
        ListAdapter listAdapter = this.mDelegate;
        if (listAdapter == null) {
            return 0;
        }
        return listAdapter.getCount();
    }

    @Override
    public Object getItem(int i) {
        ListAdapter listAdapter = this.mDelegate;
        if (listAdapter == null) {
            return null;
        }
        return listAdapter.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return this.mDelegate.getItemId(i);
    }

    @Override
    public int getItemViewType(int i) {
        return this.mDelegate.getItemViewType(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return this.mDelegate.getView(i, view, viewGroup);
    }

    @Override
    public int getViewTypeCount() {
        return this.mDelegate.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return this.mDelegate.hasStableIds();
    }
}
