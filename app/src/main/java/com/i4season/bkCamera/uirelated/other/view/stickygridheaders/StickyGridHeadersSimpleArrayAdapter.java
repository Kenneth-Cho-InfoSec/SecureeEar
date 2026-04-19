package com.i4season.bkCamera.uirelated.other.view.stickygridheaders;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

public class StickyGridHeadersSimpleArrayAdapter<T> extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class.getSimpleName();
    private int mHeaderResId;
    private LayoutInflater mInflater;
    private int mItemResId;
    private List<T> mItems;

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public StickyGridHeadersSimpleArrayAdapter(Context context, List<T> list, int i, int i2) {
        init(context, list, i, i2);
    }

    public StickyGridHeadersSimpleArrayAdapter(Context context, T[] tArr, int i, int i2) {
        init(context, Arrays.asList(tArr), i, i2);
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public long getHeaderId(int i) {
        CharSequence string;
        T item = getItem(i);
        if (item instanceof CharSequence) {
            string = (CharSequence) item;
        } else {
            string = item.toString();
        }
        return string.subSequence(0, 1).charAt(0);
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        HeaderViewHolder headerViewHolder;
        CharSequence string;
        if (view == null) {
            view = this.mInflater.inflate(this.mHeaderResId, viewGroup, false);
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.textView = (TextView) view.findViewById(R.id.text1);
            view.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }
        T item = getItem(i);
        if (item instanceof CharSequence) {
            string = (CharSequence) item;
        } else {
            string = item.toString();
        }
        headerViewHolder.textView.setText(string.subSequence(0, 1));
        return view;
    }

    @Override
    public T getItem(int i) {
        return this.mItems.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(this.mItemResId, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        T item = getItem(i);
        if (item instanceof CharSequence) {
            viewHolder.textView.setText((CharSequence) item);
        } else {
            viewHolder.textView.setText(item.toString());
        }
        return view;
    }

    private void init(Context context, List<T> list, int i, int i2) {
        this.mItems = list;
        this.mHeaderResId = i;
        this.mItemResId = i2;
        this.mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;

        protected HeaderViewHolder() {
        }
    }

    protected class ViewHolder {
        public TextView textView;

        protected ViewHolder() {
        }
    }
}
