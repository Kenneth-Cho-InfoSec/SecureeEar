package com.bumptech.glide.manager;

class ApplicationLifecycle implements Lifecycle {
    @Override
    public void removeListener(LifecycleListener lifecycleListener) {
    }

    ApplicationLifecycle() {
    }

    @Override
    public void addListener(LifecycleListener lifecycleListener) {
        lifecycleListener.onStart();
    }
}
