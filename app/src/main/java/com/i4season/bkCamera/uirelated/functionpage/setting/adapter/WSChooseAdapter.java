package com.i4season.bkCamera.uirelated.functionpage.setting.adapter;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.i4season.bkCamera.uirelated.functionpage.setting.bean.WSChooseBean;
import com.i4season.i4season_camera.C0413R;
import java.util.ArrayList;
import java.util.List;

public class WSChooseAdapter extends ArrayAdapter<WSChooseBean> {
    private List<WSChooseBean> mObjects;

    private class GridHolder {
        ImageView appWs_listchoose_item_selectiv;
        TextView appWs_listchoose_item_titletv;

        private GridHolder() {
        }
    }

    public WSChooseAdapter(Context context, List<WSChooseBean> list, Handler handler) {
        super(context, C0413R.layout.ws_listchoose_item, R.id.text1, list);
        this.mObjects = list;
    }

    @Override
    public int getCount() {
        List<WSChooseBean> list = this.mObjects;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelect(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mObjects);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (i2 == i) {
                ((WSChooseBean) arrayList.get(i)).setChSelect(true);
            } else {
                ((WSChooseBean) arrayList.get(i2)).setChSelect(false);
            }
        }
        this.mObjects.clear();
        this.mObjects.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int size;
        View viewInflate;
        GridHolder gridHolder;
        List<WSChooseBean> list = this.mObjects;
        if (list == null || (size = list.size()) == 0 || size <= i) {
            return view;
        }
        if (view == null) {
            gridHolder = new GridHolder();
            viewInflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C0413R.layout.ws_listchoose_item, viewGroup, false);
            gridHolder.appWs_listchoose_item_titletv = (TextView) viewInflate.findViewById(C0413R.id.ws_listchoose_item_titletv);
            gridHolder.appWs_listchoose_item_selectiv = (ImageView) viewInflate.findViewById(C0413R.id.ws_listchoose_item_selectiv);
            viewInflate.setTag(gridHolder);
        } else {
            viewInflate = view;
            gridHolder = (GridHolder) view.getTag();
        }
        WSChooseBean wSChooseBean = this.mObjects.get(i);
        if (wSChooseBean != null) {
            gridHolder.appWs_listchoose_item_titletv.setText(wSChooseBean.getWSChTitle());
            if (wSChooseBean.isChSelect()) {
                gridHolder.appWs_listchoose_item_selectiv.setVisibility(0);
            } else {
                gridHolder.appWs_listchoose_item_selectiv.setVisibility(8);
            }
        }
        return viewInflate;
    }
}
