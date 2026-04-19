package com.bumptech.glide.request;

public final class ErrorRequestCoordinator implements RequestCoordinator, Request {
    private Request error;
    private final RequestCoordinator parent;
    private Request primary;

    public ErrorRequestCoordinator(RequestCoordinator requestCoordinator) {
        this.parent = requestCoordinator;
    }

    public void setRequests(Request request, Request request2) {
        this.primary = request;
        this.error = request2;
    }

    @Override
    public void begin() {
        if (this.primary.isRunning()) {
            return;
        }
        this.primary.begin();
    }

    @Override
    public void clear() {
        this.primary.clear();
        if (this.error.isRunning()) {
            this.error.clear();
        }
    }

    @Override
    public boolean isRunning() {
        return (this.primary.isFailed() ? this.error : this.primary).isRunning();
    }

    @Override
    public boolean isComplete() {
        return (this.primary.isFailed() ? this.error : this.primary).isComplete();
    }

    @Override
    public boolean isResourceSet() {
        return (this.primary.isFailed() ? this.error : this.primary).isResourceSet();
    }

    @Override
    public boolean isCleared() {
        return (this.primary.isFailed() ? this.error : this.primary).isCleared();
    }

    @Override
    public boolean isFailed() {
        return this.primary.isFailed() && this.error.isFailed();
    }

    @Override
    public void recycle() {
        this.primary.recycle();
        this.error.recycle();
    }

    @Override
    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ErrorRequestCoordinator)) {
            return false;
        }
        ErrorRequestCoordinator errorRequestCoordinator = (ErrorRequestCoordinator) request;
        return this.primary.isEquivalentTo(errorRequestCoordinator.primary) && this.error.isEquivalentTo(errorRequestCoordinator.error);
    }

    @Override
    public boolean canSetImage(Request request) {
        return parentCanSetImage() && isValidRequest(request);
    }

    private boolean parentCanSetImage() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canSetImage(this);
    }

    @Override
    public boolean canNotifyStatusChanged(Request request) {
        return parentCanNotifyStatusChanged() && isValidRequest(request);
    }

    @Override
    public boolean canNotifyCleared(Request request) {
        return parentCanNotifyCleared() && isValidRequest(request);
    }

    private boolean parentCanNotifyCleared() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
    }

    private boolean parentCanNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isValidRequest(Request request) {
        return request.equals(this.primary) || (this.primary.isFailed() && request.equals(this.error));
    }

    @Override
    public boolean isAnyResourceSet() {
        return parentIsAnyResourceSet() || isResourceSet();
    }

    private boolean parentIsAnyResourceSet() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator != null && requestCoordinator.isAnyResourceSet();
    }

    @Override
    public void onRequestSuccess(Request request) {
        RequestCoordinator requestCoordinator = this.parent;
        if (requestCoordinator != null) {
            requestCoordinator.onRequestSuccess(this);
        }
    }

    @Override
    public void onRequestFailed(Request request) {
        if (!request.equals(this.error)) {
            if (this.error.isRunning()) {
                return;
            }
            this.error.begin();
        } else {
            RequestCoordinator requestCoordinator = this.parent;
            if (requestCoordinator != null) {
                requestCoordinator.onRequestFailed(this);
            }
        }
    }
}
