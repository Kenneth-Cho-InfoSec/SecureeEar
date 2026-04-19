package com.i4season.bkCamera.uirelated.other.view.stickygridheaders;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class StickyGridHeadersSimpleAdapterWrapper extends BaseAdapter implements StickyGridHeadersBaseAdapter {
    private StickyGridHeadersSimpleAdapter mDelegate;
    private HeaderData[] mHeaders;

    public StickyGridHeadersSimpleAdapterWrapper(StickyGridHeadersSimpleAdapter stickyGridHeadersSimpleAdapter) {
        this.mDelegate = stickyGridHeadersSimpleAdapter;
        stickyGridHeadersSimpleAdapter.registerDataSetObserver(new DataSetObserverExtension());
        this.mHeaders = generateHeaderList(stickyGridHeadersSimpleAdapter);
    }

    @Override
    public int getCount() {
        return this.mDelegate.getCount();
    }

    @Override
    public int getCountForHeader(int i) {
        return this.mHeaders[i].getCount();
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        return this.mDelegate.getHeaderView(this.mHeaders[i].getRefPosition(), view, viewGroup);
    }

    @Override
    public Object getItem(int i) {
        return this.mDelegate.getItem(i);
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
    public int getNumHeaders() {
        return this.mHeaders.length;
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

    protected HeaderData[] generateHeaderList(StickyGridHeadersSimpleAdapter stickyGridHeadersSimpleAdapter) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < stickyGridHeadersSimpleAdapter.getCount(); i++) {
            long headerId = stickyGridHeadersSimpleAdapter.getHeaderId(i);
            HeaderData headerData = (HeaderData) map.get(Long.valueOf(headerId));
            if (headerData == null) {
                headerData = new HeaderData(i);
                arrayList.add(headerData);
            }
            headerData.incrementCount();
            map.put(Long.valueOf(headerId), headerData);
        }
        return (HeaderData[]) arrayList.toArray(new HeaderData[arrayList.size()]);
    }

    private final class DataSetObserverExtension extends DataSetObserver {
        private DataSetObserverExtension() {
        }

        @Override
        public void onChanged() {
            StickyGridHeadersSimpleAdapterWrapper stickyGridHeadersSimpleAdapterWrapper = StickyGridHeadersSimpleAdapterWrapper.this;
            stickyGridHeadersSimpleAdapterWrapper.mHeaders = stickyGridHeadersSimpleAdapterWrapper.generateHeaderList(stickyGridHeadersSimpleAdapterWrapper.mDelegate);
            StickyGridHeadersSimpleAdapterWrapper.this.notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            StickyGridHeadersSimpleAdapterWrapper stickyGridHeadersSimpleAdapterWrapper = StickyGridHeadersSimpleAdapterWrapper.this;
            stickyGridHeadersSimpleAdapterWrapper.mHeaders = stickyGridHeadersSimpleAdapterWrapper.generateHeaderList(stickyGridHeadersSimpleAdapterWrapper.mDelegate);
            StickyGridHeadersSimpleAdapterWrapper.this.notifyDataSetInvalidated();
        }
    }

    private class HeaderData {
        private int mCount = 0;
        private int mRefPosition;

        public HeaderData(int i) {
            this.mRefPosition = i;
        }

        public int getCount() {
            return this.mCount;
        }

        public int getRefPosition() {
            return this.mRefPosition;
        }

        public void incrementCount() {
            this.mCount++;
        }
    }
}
