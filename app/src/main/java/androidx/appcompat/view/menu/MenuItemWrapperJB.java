package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.view.ActionProvider;

class MenuItemWrapperJB extends MenuItemWrapperICS {
    MenuItemWrapperJB(Context context, SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }

    @Override
    MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider actionProvider) {
        return new ActionProviderWrapperJB(this.mContext, actionProvider);
    }

    class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper implements ActionProvider.VisibilityListener {
        ActionProvider.VisibilityListener mListener;

        public ActionProviderWrapperJB(Context context, android.view.ActionProvider actionProvider) {
            super(context, actionProvider);
        }

        @Override
        public View onCreateActionView(MenuItem menuItem) {
            return this.mInner.onCreateActionView(menuItem);
        }

        @Override
        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        @Override
        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        @Override
        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        @Override
        public void setVisibilityListener(ActionProvider.VisibilityListener visibilityListener) {
            this.mListener = visibilityListener;
            this.mInner.setVisibilityListener(visibilityListener != null ? this : null);
        }

        @Override
        public void onActionProviderVisibilityChanged(boolean z) {
            ActionProvider.VisibilityListener visibilityListener = this.mListener;
            if (visibilityListener != null) {
                visibilityListener.onActionProviderVisibilityChanged(z);
            }
        }
    }
}
