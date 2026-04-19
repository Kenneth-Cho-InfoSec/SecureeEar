package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionProvider;
import android.view.CollapsibleActionView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.internal.view.SupportMenuItem;
import java.lang.reflect.Method;

public class MenuItemWrapperICS extends BaseMenuWrapper<SupportMenuItem> implements MenuItem {
    static final String LOG_TAG = "MenuItemWrapper";
    private Method mSetExclusiveCheckableMethod;

    MenuItemWrapperICS(Context context, SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }

    @Override
    public int getItemId() {
        return ((SupportMenuItem) this.mWrappedObject).getItemId();
    }

    @Override
    public int getGroupId() {
        return ((SupportMenuItem) this.mWrappedObject).getGroupId();
    }

    @Override
    public int getOrder() {
        return ((SupportMenuItem) this.mWrappedObject).getOrder();
    }

    @Override
    public MenuItem setTitle(CharSequence charSequence) {
        ((SupportMenuItem) this.mWrappedObject).setTitle(charSequence);
        return this;
    }

    @Override
    public MenuItem setTitle(int i) {
        ((SupportMenuItem) this.mWrappedObject).setTitle(i);
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return ((SupportMenuItem) this.mWrappedObject).getTitle();
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        ((SupportMenuItem) this.mWrappedObject).setTitleCondensed(charSequence);
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return ((SupportMenuItem) this.mWrappedObject).getTitleCondensed();
    }

    @Override
    public MenuItem setIcon(Drawable drawable) {
        ((SupportMenuItem) this.mWrappedObject).setIcon(drawable);
        return this;
    }

    @Override
    public MenuItem setIcon(int i) {
        ((SupportMenuItem) this.mWrappedObject).setIcon(i);
        return this;
    }

    @Override
    public Drawable getIcon() {
        return ((SupportMenuItem) this.mWrappedObject).getIcon();
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        ((SupportMenuItem) this.mWrappedObject).setIntent(intent);
        return this;
    }

    @Override
    public Intent getIntent() {
        return ((SupportMenuItem) this.mWrappedObject).getIntent();
    }

    @Override
    public MenuItem setShortcut(char c, char c2) {
        ((SupportMenuItem) this.mWrappedObject).setShortcut(c, c2);
        return this;
    }

    @Override
    public MenuItem setShortcut(char c, char c2, int i, int i2) {
        ((SupportMenuItem) this.mWrappedObject).setShortcut(c, c2, i, i2);
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char c) {
        ((SupportMenuItem) this.mWrappedObject).setNumericShortcut(c);
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char c, int i) {
        ((SupportMenuItem) this.mWrappedObject).setNumericShortcut(c, i);
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return ((SupportMenuItem) this.mWrappedObject).getNumericShortcut();
    }

    @Override
    public int getNumericModifiers() {
        return ((SupportMenuItem) this.mWrappedObject).getNumericModifiers();
    }

    @Override
    public MenuItem setAlphabeticShortcut(char c) {
        ((SupportMenuItem) this.mWrappedObject).setAlphabeticShortcut(c);
        return this;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char c, int i) {
        ((SupportMenuItem) this.mWrappedObject).setAlphabeticShortcut(c, i);
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return ((SupportMenuItem) this.mWrappedObject).getAlphabeticShortcut();
    }

    @Override
    public int getAlphabeticModifiers() {
        return ((SupportMenuItem) this.mWrappedObject).getAlphabeticModifiers();
    }

    @Override
    public MenuItem setCheckable(boolean z) {
        ((SupportMenuItem) this.mWrappedObject).setCheckable(z);
        return this;
    }

    @Override
    public boolean isCheckable() {
        return ((SupportMenuItem) this.mWrappedObject).isCheckable();
    }

    @Override
    public MenuItem setChecked(boolean z) {
        ((SupportMenuItem) this.mWrappedObject).setChecked(z);
        return this;
    }

    @Override
    public boolean isChecked() {
        return ((SupportMenuItem) this.mWrappedObject).isChecked();
    }

    @Override
    public MenuItem setVisible(boolean z) {
        return ((SupportMenuItem) this.mWrappedObject).setVisible(z);
    }

    @Override
    public boolean isVisible() {
        return ((SupportMenuItem) this.mWrappedObject).isVisible();
    }

    @Override
    public MenuItem setEnabled(boolean z) {
        ((SupportMenuItem) this.mWrappedObject).setEnabled(z);
        return this;
    }

    @Override
    public boolean isEnabled() {
        return ((SupportMenuItem) this.mWrappedObject).isEnabled();
    }

    @Override
    public boolean hasSubMenu() {
        return ((SupportMenuItem) this.mWrappedObject).hasSubMenu();
    }

    @Override
    public SubMenu getSubMenu() {
        return getSubMenuWrapper(((SupportMenuItem) this.mWrappedObject).getSubMenu());
    }

    @Override
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        ((SupportMenuItem) this.mWrappedObject).setOnMenuItemClickListener(onMenuItemClickListener != null ? new OnMenuItemClickListenerWrapper(onMenuItemClickListener) : null);
        return this;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return ((SupportMenuItem) this.mWrappedObject).getMenuInfo();
    }

    @Override
    public void setShowAsAction(int i) {
        ((SupportMenuItem) this.mWrappedObject).setShowAsAction(i);
    }

    @Override
    public MenuItem setShowAsActionFlags(int i) {
        ((SupportMenuItem) this.mWrappedObject).setShowAsActionFlags(i);
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        if (view instanceof CollapsibleActionView) {
            view = new CollapsibleActionViewWrapper(view);
        }
        ((SupportMenuItem) this.mWrappedObject).setActionView(view);
        return this;
    }

    @Override
    public MenuItem setActionView(int i) {
        ((SupportMenuItem) this.mWrappedObject).setActionView(i);
        View actionView = ((SupportMenuItem) this.mWrappedObject).getActionView();
        if (actionView instanceof CollapsibleActionView) {
            ((SupportMenuItem) this.mWrappedObject).setActionView(new CollapsibleActionViewWrapper(actionView));
        }
        return this;
    }

    @Override
    public View getActionView() {
        View actionView = ((SupportMenuItem) this.mWrappedObject).getActionView();
        return actionView instanceof CollapsibleActionViewWrapper ? ((CollapsibleActionViewWrapper) actionView).getWrappedView() : actionView;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        ((SupportMenuItem) this.mWrappedObject).setSupportActionProvider(actionProvider != null ? createActionProviderWrapper(actionProvider) : null);
        return this;
    }

    @Override
    public ActionProvider getActionProvider() {
        androidx.core.view.ActionProvider supportActionProvider = ((SupportMenuItem) this.mWrappedObject).getSupportActionProvider();
        if (supportActionProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper) supportActionProvider).mInner;
        }
        return null;
    }

    @Override
    public boolean expandActionView() {
        return ((SupportMenuItem) this.mWrappedObject).expandActionView();
    }

    @Override
    public boolean collapseActionView() {
        return ((SupportMenuItem) this.mWrappedObject).collapseActionView();
    }

    @Override
    public boolean isActionViewExpanded() {
        return ((SupportMenuItem) this.mWrappedObject).isActionViewExpanded();
    }

    @Override
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        ((SupportMenuItem) this.mWrappedObject).setOnActionExpandListener(onActionExpandListener != null ? new OnActionExpandListenerWrapper(onActionExpandListener) : null);
        return this;
    }

    @Override
    public MenuItem setContentDescription(CharSequence charSequence) {
        ((SupportMenuItem) this.mWrappedObject).setContentDescription(charSequence);
        return this;
    }

    @Override
    public CharSequence getContentDescription() {
        return ((SupportMenuItem) this.mWrappedObject).getContentDescription();
    }

    @Override
    public MenuItem setTooltipText(CharSequence charSequence) {
        ((SupportMenuItem) this.mWrappedObject).setTooltipText(charSequence);
        return this;
    }

    @Override
    public CharSequence getTooltipText() {
        return ((SupportMenuItem) this.mWrappedObject).getTooltipText();
    }

    @Override
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        ((SupportMenuItem) this.mWrappedObject).setIconTintList(colorStateList);
        return this;
    }

    @Override
    public ColorStateList getIconTintList() {
        return ((SupportMenuItem) this.mWrappedObject).getIconTintList();
    }

    @Override
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        ((SupportMenuItem) this.mWrappedObject).setIconTintMode(mode);
        return this;
    }

    @Override
    public PorterDuff.Mode getIconTintMode() {
        return ((SupportMenuItem) this.mWrappedObject).getIconTintMode();
    }

    public void setExclusiveCheckable(boolean z) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = ((SupportMenuItem) this.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, Boolean.valueOf(z));
        } catch (Exception e) {
            Log.w(LOG_TAG, "Error while calling setExclusiveCheckable", e);
        }
    }

    ActionProviderWrapper createActionProviderWrapper(ActionProvider actionProvider) {
        return new ActionProviderWrapper(this.mContext, actionProvider);
    }

    private class OnMenuItemClickListenerWrapper extends BaseWrapper<MenuItem.OnMenuItemClickListener> implements MenuItem.OnMenuItemClickListener {
        OnMenuItemClickListenerWrapper(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
            super(onMenuItemClickListener);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return ((MenuItem.OnMenuItemClickListener) this.mWrappedObject).onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

    private class OnActionExpandListenerWrapper extends BaseWrapper<MenuItem.OnActionExpandListener> implements MenuItem.OnActionExpandListener {
        OnActionExpandListenerWrapper(MenuItem.OnActionExpandListener onActionExpandListener) {
            super(onActionExpandListener);
        }

        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return ((MenuItem.OnActionExpandListener) this.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            return ((MenuItem.OnActionExpandListener) this.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

    class ActionProviderWrapper extends androidx.core.view.ActionProvider {
        final ActionProvider mInner;

        public ActionProviderWrapper(Context context, ActionProvider actionProvider) {
            super(context);
            this.mInner = actionProvider;
        }

        @Override
        public View onCreateActionView() {
            return this.mInner.onCreateActionView();
        }

        @Override
        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }

        @Override
        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }

        @Override
        public void onPrepareSubMenu(SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(subMenu));
        }
    }

    static class CollapsibleActionViewWrapper extends FrameLayout implements androidx.appcompat.view.CollapsibleActionView {
        final CollapsibleActionView mWrappedView;

        /* JADX WARN: Multi-variable type inference failed */
        CollapsibleActionViewWrapper(View view) {
            super(view.getContext());
            this.mWrappedView = (CollapsibleActionView) view;
            addView(view);
        }

        @Override
        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }

        @Override
        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }

        View getWrappedView() {
            return (View) this.mWrappedView;
        }
    }
}
