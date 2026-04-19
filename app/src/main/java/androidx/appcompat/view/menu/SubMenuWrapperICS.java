package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.internal.view.SupportSubMenu;

class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu {
    SubMenuWrapperICS(Context context, SupportSubMenu supportSubMenu) {
        super(context, supportSubMenu);
    }

    @Override
    public SupportSubMenu getWrappedObject() {
        return (SupportSubMenu) this.mWrappedObject;
    }

    @Override
    public SubMenu setHeaderTitle(int i) {
        getWrappedObject().setHeaderTitle(i);
        return this;
    }

    @Override
    public SubMenu setHeaderTitle(CharSequence charSequence) {
        getWrappedObject().setHeaderTitle(charSequence);
        return this;
    }

    @Override
    public SubMenu setHeaderIcon(int i) {
        getWrappedObject().setHeaderIcon(i);
        return this;
    }

    @Override
    public SubMenu setHeaderIcon(Drawable drawable) {
        getWrappedObject().setHeaderIcon(drawable);
        return this;
    }

    @Override
    public SubMenu setHeaderView(View view) {
        getWrappedObject().setHeaderView(view);
        return this;
    }

    @Override
    public void clearHeader() {
        getWrappedObject().clearHeader();
    }

    @Override
    public SubMenu setIcon(int i) {
        getWrappedObject().setIcon(i);
        return this;
    }

    @Override
    public SubMenu setIcon(Drawable drawable) {
        getWrappedObject().setIcon(drawable);
        return this;
    }

    @Override
    public MenuItem getItem() {
        return getMenuItemWrapper(getWrappedObject().getItem());
    }
}
