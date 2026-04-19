package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import androidx.core.util.LogWriter;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManagerImpl.OpGenerator {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SET_PRIMARY_NAV = 8;
    static final int OP_SHOW = 5;
    static final int OP_UNSET_PRIMARY_NAV = 9;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    final FragmentManagerImpl mManager;
    String mName;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;
    ArrayList<C0144Op> mOps = new ArrayList<>();
    boolean mAllowAddToBackStack = true;
    int mIndex = -1;
    boolean mReorderingAllowed = false;

    static final class C0144Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;

        C0144Op() {
        }

        C0144Op(int i, Fragment fragment) {
            this.cmd = i;
            this.fragment = fragment;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dump(str, printWriter, true);
    }

    public void dump(String str, PrintWriter printWriter, boolean z) {
        String str2;
        if (z) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (this.mOps.isEmpty()) {
            return;
        }
        printWriter.print(str);
        printWriter.println("Operations:");
        String str3 = str + "    ";
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            C0144Op c0144Op = this.mOps.get(i);
            switch (c0144Op.cmd) {
                case 0:
                    str2 = "NULL";
                    break;
                case 1:
                    str2 = "ADD";
                    break;
                case 2:
                    str2 = "REPLACE";
                    break;
                case 3:
                    str2 = "REMOVE";
                    break;
                case 4:
                    str2 = "HIDE";
                    break;
                case 5:
                    str2 = "SHOW";
                    break;
                case 6:
                    str2 = "DETACH";
                    break;
                case 7:
                    str2 = "ATTACH";
                    break;
                case 8:
                    str2 = "SET_PRIMARY_NAV";
                    break;
                case 9:
                    str2 = "UNSET_PRIMARY_NAV";
                    break;
                default:
                    str2 = "cmd=" + c0144Op.cmd;
                    break;
            }
            printWriter.print(str);
            printWriter.print("  Op #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.print(str2);
            printWriter.print(" ");
            printWriter.println(c0144Op.fragment);
            if (z) {
                if (c0144Op.enterAnim != 0 || c0144Op.exitAnim != 0) {
                    printWriter.print(str);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(c0144Op.enterAnim));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(c0144Op.exitAnim));
                }
                if (c0144Op.popEnterAnim != 0 || c0144Op.popExitAnim != 0) {
                    printWriter.print(str);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(c0144Op.popEnterAnim));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(c0144Op.popExitAnim));
                }
            }
        }
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        this.mManager = fragmentManagerImpl;
    }

    @Override
    public int getId() {
        return this.mIndex;
    }

    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    void addOp(C0144Op c0144Op) {
        this.mOps.add(c0144Op);
        c0144Op.enterAnim = this.mEnterAnim;
        c0144Op.exitAnim = this.mExitAnim;
        c0144Op.popEnterAnim = this.mPopEnterAnim;
        c0144Op.popExitAnim = this.mPopExitAnim;
    }

    @Override
    public FragmentTransaction add(Fragment fragment, String str) {
        doAddOp(0, fragment, str, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(int i, Fragment fragment) {
        doAddOp(i, fragment, null, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(int i, Fragment fragment, String str) {
        doAddOp(i, fragment, str, 1);
        return this;
    }

    private void doAddOp(int i, Fragment fragment, String str, int i2) {
        Class<?> cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (cls.isAnonymousClass() || !Modifier.isPublic(modifiers) || (cls.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalStateException("Fragment " + cls.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
        }
        fragment.mFragmentManager = this.mManager;
        if (str != null) {
            if (fragment.mTag != null && !str.equals(fragment.mTag)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + str);
            }
            fragment.mTag = str;
        }
        if (i != 0) {
            if (i == -1) {
                throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + str + " to container view with no id");
            }
            if (fragment.mFragmentId != 0 && fragment.mFragmentId != i) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + i);
            }
            fragment.mFragmentId = i;
            fragment.mContainerId = i;
        }
        addOp(new C0144Op(i2, fragment));
    }

    @Override
    public FragmentTransaction replace(int i, Fragment fragment) {
        return replace(i, fragment, null);
    }

    @Override
    public FragmentTransaction replace(int i, Fragment fragment, String str) {
        if (i == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(i, fragment, str, 2);
        return this;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        addOp(new C0144Op(3, fragment));
        return this;
    }

    @Override
    public FragmentTransaction hide(Fragment fragment) {
        addOp(new C0144Op(4, fragment));
        return this;
    }

    @Override
    public FragmentTransaction show(Fragment fragment) {
        addOp(new C0144Op(5, fragment));
        return this;
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        addOp(new C0144Op(6, fragment));
        return this;
    }

    @Override
    public FragmentTransaction attach(Fragment fragment) {
        addOp(new C0144Op(7, fragment));
        return this;
    }

    @Override
    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        addOp(new C0144Op(8, fragment));
        return this;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int i, int i2) {
        return setCustomAnimations(i, i2, 0, 0);
    }

    @Override
    public FragmentTransaction setCustomAnimations(int i, int i2, int i3, int i4) {
        this.mEnterAnim = i;
        this.mExitAnim = i2;
        this.mPopEnterAnim = i3;
        this.mPopExitAnim = i4;
        return this;
    }

    @Override
    public FragmentTransaction setTransition(int i) {
        this.mTransition = i;
        return this;
    }

    @Override
    public FragmentTransaction addSharedElement(View view, String str) {
        if (FragmentTransition.supportsTransition()) {
            String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList<>();
                this.mSharedElementTargetNames = new ArrayList<>();
            } else {
                if (this.mSharedElementTargetNames.contains(str)) {
                    throw new IllegalArgumentException("A shared element with the target name '" + str + "' has already been added to the transaction.");
                }
                if (this.mSharedElementSourceNames.contains(transitionName)) {
                    throw new IllegalArgumentException("A shared element with the source name '" + transitionName + " has already been added to the transaction.");
                }
            }
            this.mSharedElementSourceNames.add(transitionName);
            this.mSharedElementTargetNames.add(str);
        }
        return this;
    }

    @Override
    public FragmentTransaction setTransitionStyle(int i) {
        this.mTransitionStyle = i;
        return this;
    }

    @Override
    public FragmentTransaction addToBackStack(String str) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        this.mAddToBackStack = true;
        this.mName = str;
        return this;
    }

    @Override
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    @Override
    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(int i) {
        this.mBreadCrumbTitleRes = i;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = charSequence;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(int i) {
        this.mBreadCrumbShortTitleRes = i;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = charSequence;
        return this;
    }

    void bumpBackStackNesting(int i) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v(TAG, "Bump nesting in " + this + " by " + i);
            }
            int size = this.mOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                C0144Op c0144Op = this.mOps.get(i2);
                if (c0144Op.fragment != null) {
                    c0144Op.fragment.mBackStackNesting += i;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v(TAG, "Bump nesting of " + c0144Op.fragment + " to " + c0144Op.fragment.mBackStackNesting);
                    }
                }
            }
        }
    }

    @Override
    public FragmentTransaction runOnCommit(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable cannot be null");
        }
        disallowAddToBackStack();
        if (this.mCommitRunnables == null) {
            this.mCommitRunnables = new ArrayList<>();
        }
        this.mCommitRunnables.add(runnable);
        return this;
    }

    public void runOnCommitRunnables() {
        ArrayList<Runnable> arrayList = this.mCommitRunnables;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
        }
    }

    @Override
    public int commit() {
        return commitInternal(false);
    }

    @Override
    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    @Override
    public void commitNow() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    @Override
    public void commitNowAllowingStateLoss() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    @Override
    public FragmentTransaction setReorderingAllowed(boolean z) {
        this.mReorderingAllowed = z;
        return this;
    }

    @Override
    public FragmentTransaction setAllowOptimization(boolean z) {
        return setReorderingAllowed(z);
    }

    int commitInternal(boolean z) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Commit: " + this);
            PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
            dump("  ", null, printWriter, null);
            printWriter.close();
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, z);
        return this.mIndex;
    }

    @Override
    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(false);
        if (!this.mAddToBackStack) {
            return true;
        }
        this.mManager.addBackStackState(this);
        return true;
    }

    boolean interactsWith(int i) {
        int size = this.mOps.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0144Op c0144Op = this.mOps.get(i2);
            int i3 = c0144Op.fragment != null ? c0144Op.fragment.mContainerId : 0;
            if (i3 != 0 && i3 == i) {
                return true;
            }
        }
        return false;
    }

    boolean interactsWith(ArrayList<BackStackRecord> arrayList, int i, int i2) {
        if (i2 == i) {
            return false;
        }
        int size = this.mOps.size();
        int i3 = -1;
        for (int i4 = 0; i4 < size; i4++) {
            C0144Op c0144Op = this.mOps.get(i4);
            int i5 = c0144Op.fragment != null ? c0144Op.fragment.mContainerId : 0;
            if (i5 != 0 && i5 != i3) {
                for (int i6 = i; i6 < i2; i6++) {
                    BackStackRecord backStackRecord = arrayList.get(i6);
                    int size2 = backStackRecord.mOps.size();
                    for (int i7 = 0; i7 < size2; i7++) {
                        C0144Op c0144Op2 = backStackRecord.mOps.get(i7);
                        if ((c0144Op2.fragment != null ? c0144Op2.fragment.mContainerId : 0) == i5) {
                            return true;
                        }
                    }
                }
                i3 = i5;
            }
        }
        return false;
    }

    void executeOps() {
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            C0144Op c0144Op = this.mOps.get(i);
            Fragment fragment = c0144Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            switch (c0144Op.cmd) {
                case 1:
                    fragment.setNextAnim(c0144Op.enterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + c0144Op.cmd);
                case 3:
                    fragment.setNextAnim(c0144Op.exitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                case 4:
                    fragment.setNextAnim(c0144Op.exitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                case 5:
                    fragment.setNextAnim(c0144Op.enterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                case 6:
                    fragment.setNextAnim(c0144Op.exitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                case 7:
                    fragment.setNextAnim(c0144Op.enterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
            }
            if (!this.mReorderingAllowed && c0144Op.cmd != 1 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (this.mReorderingAllowed) {
            return;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mManager;
        fragmentManagerImpl.moveToState(fragmentManagerImpl.mCurState, true);
    }

    void executePopOps(boolean z) {
        for (int size = this.mOps.size() - 1; size >= 0; size--) {
            C0144Op c0144Op = this.mOps.get(size);
            Fragment fragment = c0144Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            switch (c0144Op.cmd) {
                case 1:
                    fragment.setNextAnim(c0144Op.popExitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + c0144Op.cmd);
                case 3:
                    fragment.setNextAnim(c0144Op.popEnterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                case 4:
                    fragment.setNextAnim(c0144Op.popEnterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                case 5:
                    fragment.setNextAnim(c0144Op.popExitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                case 6:
                    fragment.setNextAnim(c0144Op.popEnterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                case 7:
                    fragment.setNextAnim(c0144Op.popExitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
            }
            if (!this.mReorderingAllowed && c0144Op.cmd != 3 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (this.mReorderingAllowed || !z) {
            return;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mManager;
        fragmentManagerImpl.moveToState(fragmentManagerImpl.mCurState, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    Fragment expandOps(ArrayList<Fragment> arrayList, Fragment fragment) {
        Fragment fragment2 = fragment;
        int i = 0;
        while (i < this.mOps.size()) {
            C0144Op c0144Op = this.mOps.get(i);
            int i2 = c0144Op.cmd;
            if (i2 == 1) {
                arrayList.add(c0144Op.fragment);
            } else if (i2 == 2) {
                Fragment fragment3 = c0144Op.fragment;
                int i3 = fragment3.mContainerId;
                Fragment fragment4 = fragment2;
                int i4 = i;
                boolean z = false;
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    Fragment fragment5 = arrayList.get(size);
                    if (fragment5.mContainerId == i3) {
                        if (fragment5 == fragment3) {
                            z = true;
                        } else {
                            if (fragment5 == fragment4) {
                                this.mOps.add(i4, new C0144Op(9, fragment5));
                                i4++;
                                fragment4 = null;
                            }
                            C0144Op c0144Op2 = new C0144Op(3, fragment5);
                            c0144Op2.enterAnim = c0144Op.enterAnim;
                            c0144Op2.popEnterAnim = c0144Op.popEnterAnim;
                            c0144Op2.exitAnim = c0144Op.exitAnim;
                            c0144Op2.popExitAnim = c0144Op.popExitAnim;
                            this.mOps.add(i4, c0144Op2);
                            arrayList.remove(fragment5);
                            i4++;
                        }
                    }
                }
                if (z) {
                    this.mOps.remove(i4);
                    i4--;
                } else {
                    c0144Op.cmd = 1;
                    arrayList.add(fragment3);
                }
                i = i4;
                fragment2 = fragment4;
            } else if (i2 == 3 || i2 == 6) {
                arrayList.remove(c0144Op.fragment);
                if (c0144Op.fragment == fragment2) {
                    this.mOps.add(i, new C0144Op(9, c0144Op.fragment));
                    i++;
                    fragment2 = null;
                }
            } else if (i2 != 7) {
                if (i2 == 8) {
                    this.mOps.add(i, new C0144Op(9, fragment2));
                    i++;
                    fragment2 = c0144Op.fragment;
                }
            }
            i++;
        }
        return fragment2;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    Fragment trackAddedFragmentsInPop(ArrayList<Fragment> arrayList, Fragment fragment) {
        for (int i = 0; i < this.mOps.size(); i++) {
            C0144Op c0144Op = this.mOps.get(i);
            int i2 = c0144Op.cmd;
            if (i2 == 1) {
                arrayList.remove(c0144Op.fragment);
            } else if (i2 != 3) {
                switch (i2) {
                    case 6:
                        arrayList.add(c0144Op.fragment);
                        break;
                    case 8:
                        fragment = null;
                        break;
                    case 9:
                        fragment = c0144Op.fragment;
                        break;
                }
            }
        }
        return fragment;
    }

    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); i++) {
            if (isFragmentPostponed(this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }

    void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); i++) {
            C0144Op c0144Op = this.mOps.get(i);
            if (isFragmentPostponed(c0144Op)) {
                c0144Op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }

    private static boolean isFragmentPostponed(C0144Op c0144Op) {
        Fragment fragment = c0144Op.fragment;
        return (fragment == null || !fragment.mAdded || fragment.mView == null || fragment.mDetached || fragment.mHidden || !fragment.isPostponed()) ? false : true;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }
}
