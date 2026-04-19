package com.bumptech.glide.manager;

import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class TargetTracker implements LifecycleListener {
    private final Set<Target<?>> targets = Collections.newSetFromMap(new WeakHashMap());

    public void track(Target<?> target) {
        this.targets.add(target);
    }

    public void untrack(Target<?> target) {
        this.targets.remove(target);
    }

    @Override
    public void onStart() {
        Iterator it = Util.getSnapshot(this.targets).iterator();
        while (it.hasNext()) {
            ((Target) it.next()).onStart();
        }
    }

    @Override
    public void onStop() {
        Iterator it = Util.getSnapshot(this.targets).iterator();
        while (it.hasNext()) {
            ((Target) it.next()).onStop();
        }
    }

    @Override
    public void onDestroy() {
        Iterator it = Util.getSnapshot(this.targets).iterator();
        while (it.hasNext()) {
            ((Target) it.next()).onDestroy();
        }
    }

    public List<Target<?>> getAll() {
        return Util.getSnapshot(this.targets);
    }

    public void clear() {
        this.targets.clear();
    }
}
