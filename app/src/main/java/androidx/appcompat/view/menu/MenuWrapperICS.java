package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.core.internal.view.SupportMenu;

class MenuWrapperICS extends BaseMenuWrapper<SupportMenu> implements Menu {
    MenuWrapperICS(Context context, SupportMenu supportMenu) {
        super(context, supportMenu);
    }

    @Override
    public MenuItem add(CharSequence charSequence) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).add(charSequence));
    }

    @Override
    public MenuItem add(int i) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).add(i));
    }

    @Override
    public MenuItem add(int i, int i2, int i3, CharSequence charSequence) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).add(i, i2, i3, charSequence));
    }

    @Override
    public MenuItem add(int i, int i2, int i3, int i4) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).add(i, i2, i3, i4));
    }

    @Override
    public SubMenu addSubMenu(CharSequence charSequence) {
        return getSubMenuWrapper(((SupportMenu) this.mWrappedObject).addSubMenu(charSequence));
    }

    @Override
    public SubMenu addSubMenu(int i) {
        return getSubMenuWrapper(((SupportMenu) this.mWrappedObject).addSubMenu(i));
    }

    @Override
    public SubMenu addSubMenu(int i, int i2, int i3, CharSequence charSequence) {
        return getSubMenuWrapper(((SupportMenu) this.mWrappedObject).addSubMenu(i, i2, i3, charSequence));
    }

    @Override
    public SubMenu addSubMenu(int i, int i2, int i3, int i4) {
        return getSubMenuWrapper(((SupportMenu) this.mWrappedObject).addSubMenu(i, i2, i3, i4));
    }

    @Override
    public int addIntentOptions(int i, int i2, int i3, ComponentName componentName, Intent[] intentArr, Intent intent, int i4, MenuItem[] menuItemArr) {
        MenuItem[] menuItemArr2 = menuItemArr != null ? new MenuItem[menuItemArr.length] : null;
        int iAddIntentOptions = ((SupportMenu) this.mWrappedObject).addIntentOptions(i, i2, i3, componentName, intentArr, intent, i4, menuItemArr2);
        if (menuItemArr2 != null) {
            int length = menuItemArr2.length;
            for (int i5 = 0; i5 < length; i5++) {
                menuItemArr[i5] = getMenuItemWrapper(menuItemArr2[i5]);
            }
        }
        return iAddIntentOptions;
    }

    @Override
    public void removeItem(int i) {
        internalRemoveItem(i);
        ((SupportMenu) this.mWrappedObject).removeItem(i);
    }

    @Override
    public void removeGroup(int i) {
        internalRemoveGroup(i);
        ((SupportMenu) this.mWrappedObject).removeGroup(i);
    }

    @Override
    public void clear() {
        internalClear();
        ((SupportMenu) this.mWrappedObject).clear();
    }

    @Override
    public void setGroupCheckable(int i, boolean z, boolean z2) {
        ((SupportMenu) this.mWrappedObject).setGroupCheckable(i, z, z2);
    }

    @Override
    public void setGroupVisible(int i, boolean z) {
        ((SupportMenu) this.mWrappedObject).setGroupVisible(i, z);
    }

    @Override
    public void setGroupEnabled(int i, boolean z) {
        ((SupportMenu) this.mWrappedObject).setGroupEnabled(i, z);
    }

    @Override
    public boolean hasVisibleItems() {
        return ((SupportMenu) this.mWrappedObject).hasVisibleItems();
    }

    @Override
    public MenuItem findItem(int i) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).findItem(i));
    }

    @Override
    public int size() {
        return ((SupportMenu) this.mWrappedObject).size();
    }

    @Override
    public MenuItem getItem(int i) {
        return getMenuItemWrapper(((SupportMenu) this.mWrappedObject).getItem(i));
    }

    @Override
    public void close() {
        ((SupportMenu) this.mWrappedObject).close();
    }

    @Override
    public boolean performShortcut(int i, KeyEvent keyEvent, int i2) {
        return ((SupportMenu) this.mWrappedObject).performShortcut(i, keyEvent, i2);
    }

    @Override
    public boolean isShortcutKey(int i, KeyEvent keyEvent) {
        return ((SupportMenu) this.mWrappedObject).isShortcutKey(i, keyEvent);
    }

    @Override
    public boolean performIdentifierAction(int i, int i2) {
        return ((SupportMenu) this.mWrappedObject).performIdentifierAction(i, i2);
    }

    @Override
    public void setQwertyMode(boolean z) {
        ((SupportMenu) this.mWrappedObject).setQwertyMode(z);
    }
}
