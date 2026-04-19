package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
    private Request full;
    private boolean isRunning;
    private final RequestCoordinator parent;
    private Request thumb;

    ThumbnailRequestCoordinator() {
        this(null);
    }

    public ThumbnailRequestCoordinator(RequestCoordinator requestCoordinator) {
        this.parent = requestCoordinator;
    }

    public void setRequests(Request request, Request request2) {
        this.full = request;
        this.thumb = request2;
    }

    @Override
    public boolean canSetImage(Request request) {
        return parentCanSetImage() && (request.equals(this.full) || !this.full.isResourceSet());
    }

    private boolean parentCanSetImage() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canSetImage(this);
    }

    @Override
    public boolean canNotifyStatusChanged(Request request) {
        return parentCanNotifyStatusChanged() && request.equals(this.full) && !isAnyResourceSet();
    }

    @Override
    public boolean canNotifyCleared(Request request) {
        return parentCanNotifyCleared() && request.equals(this.full);
    }

    private boolean parentCanNotifyCleared() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
    }

    private boolean parentCanNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
    }

    @Override
    public boolean isAnyResourceSet() {
        return parentIsAnyResourceSet() || isResourceSet();
    }

    @Override
    public void onRequestSuccess(Request request) {
        if (request.equals(this.thumb)) {
            return;
        }
        RequestCoordinator requestCoordinator = this.parent;
        if (requestCoordinator != null) {
            requestCoordinator.onRequestSuccess(this);
        }
        if (this.thumb.isComplete()) {
            return;
        }
        this.thumb.clear();
    }

    @Override
    public void onRequestFailed(Request request) {
        RequestCoordinator requestCoordinator;
        if (request.equals(this.full) && (requestCoordinator = this.parent) != null) {
            requestCoordinator.onRequestFailed(this);
        }
    }

    private boolean parentIsAnyResourceSet() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator != null && requestCoordinator.isAnyResourceSet();
    }

    @Override
    public void begin() {
        this.isRunning = true;
        if (!this.full.isComplete() && !this.thumb.isRunning()) {
            this.thumb.begin();
        }
        if (!this.isRunning || this.full.isRunning()) {
            return;
        }
        this.full.begin();
    }

    @Override
    public void clear() {
        this.isRunning = false;
        this.thumb.clear();
        this.full.clear();
    }

    @Override
    public boolean isRunning() {
        return this.full.isRunning();
    }

    @Override
    public boolean isComplete() {
        return this.full.isComplete() || this.thumb.isComplete();
    }

    @Override
    public boolean isResourceSet() {
        return this.full.isResourceSet() || this.thumb.isResourceSet();
    }

    @Override
    public boolean isCleared() {
        return this.full.isCleared();
    }

    @Override
    public boolean isFailed() {
        return this.full.isFailed();
    }

    @Override
    public void recycle() {
        this.full.recycle();
        this.thumb.recycle();
    }

    @Override
    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ThumbnailRequestCoordinator)) {
            return false;
        }
        ThumbnailRequestCoordinator thumbnailRequestCoordinator = (ThumbnailRequestCoordinator) request;
        Request request2 = this.full;
        if (request2 == null) {
            if (thumbnailRequestCoordinator.full != null) {
                return false;
            }
        } else if (!request2.isEquivalentTo(thumbnailRequestCoordinator.full)) {
            return false;
        }
        Request request3 = this.thumb;
        Request request4 = thumbnailRequestCoordinator.thumb;
        if (request3 == null) {
            if (request4 != null) {
                return false;
            }
        } else if (!request3.isEquivalentTo(request4)) {
            return false;
        }
        return true;
    }
}
