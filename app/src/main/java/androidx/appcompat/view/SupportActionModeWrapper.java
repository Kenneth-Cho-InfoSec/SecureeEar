package androidx.appcompat.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuWrapperFactory;
import androidx.collection.SimpleArrayMap;
import androidx.core.internal.view.SupportMenu;
import androidx.core.internal.view.SupportMenuItem;
import java.util.ArrayList;

public class SupportActionModeWrapper extends android.view.ActionMode {
    final Context mContext;
    final ActionMode mWrappedObject;

    public SupportActionModeWrapper(Context context, ActionMode actionMode) {
        this.mContext = context;
        this.mWrappedObject = actionMode;
    }

    @Override
    public Object getTag() {
        return this.mWrappedObject.getTag();
    }

    @Override
    public void setTag(Object obj) {
        this.mWrappedObject.setTag(obj);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mWrappedObject.setTitle(charSequence);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mWrappedObject.setSubtitle(charSequence);
    }

    @Override
    public void invalidate() {
        this.mWrappedObject.invalidate();
    }

    @Override
    public void finish() {
        this.mWrappedObject.finish();
    }

    @Override
    public Menu getMenu() {
        return MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu) this.mWrappedObject.getMenu());
    }

    @Override
    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }

    @Override
    public void setTitle(int i) {
        this.mWrappedObject.setTitle(i);
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mWrappedObject.getSubtitle();
    }

    @Override
    public void setSubtitle(int i) {
        this.mWrappedObject.setSubtitle(i);
    }

    @Override
    public View getCustomView() {
        return this.mWrappedObject.getCustomView();
    }

    @Override
    public void setCustomView(View view) {
        this.mWrappedObject.setCustomView(view);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return this.mWrappedObject.getMenuInflater();
    }

    @Override
    public boolean getTitleOptionalHint() {
        return this.mWrappedObject.getTitleOptionalHint();
    }

    @Override
    public void setTitleOptionalHint(boolean z) {
        this.mWrappedObject.setTitleOptionalHint(z);
    }

    @Override
    public boolean isTitleOptional() {
        return this.mWrappedObject.isTitleOptional();
    }

    public static class CallbackWrapper implements ActionMode.Callback {
        final Context mContext;
        final ActionMode.Callback mWrappedCallback;
        final ArrayList<SupportActionModeWrapper> mActionModes = new ArrayList<>();
        final SimpleArrayMap<Menu, Menu> mMenus = new SimpleArrayMap<>();

        public CallbackWrapper(Context context, ActionMode.Callback callback) {
            this.mContext = context;
            this.mWrappedCallback = callback;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrappedCallback.onCreateActionMode(getActionModeWrapper(actionMode), getMenuWrapper(menu));
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrappedCallback.onPrepareActionMode(getActionModeWrapper(actionMode), getMenuWrapper(menu));
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrappedCallback.onActionItemClicked(getActionModeWrapper(actionMode), MenuWrapperFactory.wrapSupportMenuItem(this.mContext, (SupportMenuItem) menuItem));
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrappedCallback.onDestroyActionMode(getActionModeWrapper(actionMode));
        }

        private Menu getMenuWrapper(Menu menu) {
            Menu menu2 = this.mMenus.get(menu);
            if (menu2 != null) {
                return menu2;
            }
            Menu menuWrapSupportMenu = MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu) menu);
            this.mMenus.put(menu, menuWrapSupportMenu);
            return menuWrapSupportMenu;
        }

        public android.view.ActionMode getActionModeWrapper(ActionMode actionMode) {
            int size = this.mActionModes.size();
            for (int i = 0; i < size; i++) {
                SupportActionModeWrapper supportActionModeWrapper = this.mActionModes.get(i);
                if (supportActionModeWrapper != null && supportActionModeWrapper.mWrappedObject == actionMode) {
                    return supportActionModeWrapper;
                }
            }
            SupportActionModeWrapper supportActionModeWrapper2 = new SupportActionModeWrapper(this.mContext, actionMode);
            this.mActionModes.add(supportActionModeWrapper2);
            return supportActionModeWrapper2;
        }
    }
}
