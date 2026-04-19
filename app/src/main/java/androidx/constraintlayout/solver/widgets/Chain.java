package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i4;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
                }
            } else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:155:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x004a A[PHI: r8 r14
  0x004a: PHI (r8v4 boolean) = (r8v2 boolean), (r8v43 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]
  0x004a: PHI (r14v4 boolean) = (r14v2 boolean), (r14v19 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x004c A[PHI: r8 r14
  0x004c: PHI (r8v40 boolean) = (r8v2 boolean), (r8v43 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]
  0x004c: PHI (r14v16 boolean) = (r14v2 boolean), (r14v19 boolean) binds: [B:28:0x0048, B:17:0x0035] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0158  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        boolean z3;
        int i3;
        ConstraintWidget constraintWidget;
        int i4;
        ConstraintAnchor constraintAnchor;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        ConstraintWidget constraintWidget2;
        ConstraintAnchor constraintAnchor2;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        ConstraintWidget constraintWidget3;
        float f;
        int size;
        ArrayList<ConstraintWidget> arrayList;
        int i5;
        float f2;
        int i6;
        boolean z4;
        ConstraintWidget constraintWidget4;
        boolean z5;
        int i7;
        ConstraintWidget constraintWidget5 = chainHead.mFirst;
        ConstraintWidget constraintWidget6 = chainHead.mLast;
        ConstraintWidget constraintWidget7 = chainHead.mFirstVisibleWidget;
        ConstraintWidget constraintWidget8 = chainHead.mLastVisibleWidget;
        ConstraintWidget constraintWidget9 = chainHead.mHead;
        float f3 = chainHead.mTotalWeight;
        ConstraintWidget constraintWidget10 = chainHead.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget11 = chainHead.mLastMatchConstraintWidget;
        boolean z6 = constraintWidgetContainer.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            z = constraintWidget9.mHorizontalChainStyle == 0;
            z2 = constraintWidget9.mHorizontalChainStyle == 1;
            z3 = constraintWidget9.mHorizontalChainStyle == 2;
        } else {
            z = constraintWidget9.mVerticalChainStyle == 0;
            z2 = constraintWidget9.mVerticalChainStyle == 1;
            if (constraintWidget9.mVerticalChainStyle == 2) {
            }
        }
        boolean z7 = z;
        ConstraintWidget constraintWidget12 = constraintWidget5;
        boolean z8 = z2;
        boolean z9 = z3;
        boolean z10 = false;
        while (true) {
            ConstraintWidget constraintWidget13 = null;
            if (z10) {
                break;
            }
            ConstraintAnchor constraintAnchor3 = constraintWidget12.mListAnchors[i2];
            int i8 = (z6 || z9) ? 1 : 4;
            int margin = constraintAnchor3.getMargin();
            if (constraintAnchor3.mTarget != null && constraintWidget12 != constraintWidget5) {
                margin += constraintAnchor3.mTarget.getMargin();
            }
            int i9 = margin;
            if (z9 && constraintWidget12 != constraintWidget5 && constraintWidget12 != constraintWidget7) {
                f2 = f3;
                z4 = z10;
                i6 = 6;
            } else if (z7 && z6) {
                f2 = f3;
                z4 = z10;
                i6 = 4;
            } else {
                f2 = f3;
                i6 = i8;
                z4 = z10;
            }
            if (constraintAnchor3.mTarget != null) {
                if (constraintWidget12 == constraintWidget7) {
                    z5 = z7;
                    constraintWidget4 = constraintWidget9;
                    linearSystem.addGreaterThan(constraintAnchor3.mSolverVariable, constraintAnchor3.mTarget.mSolverVariable, i9, 5);
                } else {
                    constraintWidget4 = constraintWidget9;
                    z5 = z7;
                    linearSystem.addGreaterThan(constraintAnchor3.mSolverVariable, constraintAnchor3.mTarget.mSolverVariable, i9, 6);
                }
                linearSystem.addEquality(constraintAnchor3.mSolverVariable, constraintAnchor3.mTarget.mSolverVariable, i9, i6);
            } else {
                constraintWidget4 = constraintWidget9;
                z5 = z7;
            }
            if (z6) {
                if (constraintWidget12.getVisibility() == 8 || constraintWidget12.mListDimensionBehaviors[i] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    i7 = 0;
                } else {
                    i7 = 0;
                    linearSystem.addGreaterThan(constraintWidget12.mListAnchors[i2 + 1].mSolverVariable, constraintWidget12.mListAnchors[i2].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget12.mListAnchors[i2].mSolverVariable, constraintWidgetContainer.mListAnchors[i2].mSolverVariable, i7, 6);
            }
            ConstraintAnchor constraintAnchor4 = constraintWidget12.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor4 != null) {
                ConstraintWidget constraintWidget14 = constraintAnchor4.mOwner;
                if (constraintWidget14.mListAnchors[i2].mTarget != null && constraintWidget14.mListAnchors[i2].mTarget.mOwner == constraintWidget12) {
                    constraintWidget13 = constraintWidget14;
                }
            }
            if (constraintWidget13 != null) {
                constraintWidget12 = constraintWidget13;
                z10 = z4;
            } else {
                z10 = true;
            }
            f3 = f2;
            z7 = z5;
            constraintWidget9 = constraintWidget4;
        }
        ConstraintWidget constraintWidget15 = constraintWidget9;
        float f4 = f3;
        boolean z11 = z7;
        if (constraintWidget8 != null) {
            int i10 = i2 + 1;
            if (constraintWidget6.mListAnchors[i10].mTarget != null) {
                ConstraintAnchor constraintAnchor5 = constraintWidget8.mListAnchors[i10];
                linearSystem.addLowerThan(constraintAnchor5.mSolverVariable, constraintWidget6.mListAnchors[i10].mTarget.mSolverVariable, -constraintAnchor5.getMargin(), 5);
            }
        }
        if (z6) {
            int i11 = i2 + 1;
            linearSystem.addGreaterThan(constraintWidgetContainer.mListAnchors[i11].mSolverVariable, constraintWidget6.mListAnchors[i11].mSolverVariable, constraintWidget6.mListAnchors[i11].getMargin(), 6);
        }
        ArrayList<ConstraintWidget> arrayList2 = chainHead.mWeightedMatchConstraintsWidgets;
        if (arrayList2 != null && (size = arrayList2.size()) > 1) {
            float f5 = (!chainHead.mHasUndefinedWeights || chainHead.mHasComplexMatchWeights) ? f4 : chainHead.mWidgetsMatchCount;
            float f6 = 0.0f;
            ConstraintWidget constraintWidget16 = null;
            int i12 = 0;
            float f7 = 0.0f;
            while (i12 < size) {
                ConstraintWidget constraintWidget17 = arrayList2.get(i12);
                float f8 = constraintWidget17.mWeight[i];
                if (f8 < f6) {
                    if (chainHead.mHasComplexMatchWeights) {
                        linearSystem.addEquality(constraintWidget17.mListAnchors[i2 + 1].mSolverVariable, constraintWidget17.mListAnchors[i2].mSolverVariable, 0, 4);
                        arrayList = arrayList2;
                        i5 = size;
                        i12++;
                        size = i5;
                        arrayList2 = arrayList;
                        f6 = 0.0f;
                    } else {
                        f8 = 1.0f;
                    }
                }
                if (f8 == 0.0f) {
                    linearSystem.addEquality(constraintWidget17.mListAnchors[i2 + 1].mSolverVariable, constraintWidget17.mListAnchors[i2].mSolverVariable, 0, 6);
                    arrayList = arrayList2;
                    i5 = size;
                    i12++;
                    size = i5;
                    arrayList2 = arrayList;
                    f6 = 0.0f;
                } else {
                    if (constraintWidget16 != null) {
                        SolverVariable solverVariable5 = constraintWidget16.mListAnchors[i2].mSolverVariable;
                        int i13 = i2 + 1;
                        SolverVariable solverVariable6 = constraintWidget16.mListAnchors[i13].mSolverVariable;
                        SolverVariable solverVariable7 = constraintWidget17.mListAnchors[i2].mSolverVariable;
                        arrayList = arrayList2;
                        SolverVariable solverVariable8 = constraintWidget17.mListAnchors[i13].mSolverVariable;
                        i5 = size;
                        ArrayRow arrayRowCreateRow = linearSystem.createRow();
                        arrayRowCreateRow.createRowEqualMatchDimensions(f7, f5, f8, solverVariable5, solverVariable6, solverVariable7, solverVariable8);
                        linearSystem.addConstraint(arrayRowCreateRow);
                    } else {
                        arrayList = arrayList2;
                        i5 = size;
                    }
                    f7 = f8;
                    constraintWidget16 = constraintWidget17;
                    i12++;
                    size = i5;
                    arrayList2 = arrayList;
                    f6 = 0.0f;
                }
            }
        }
        if (constraintWidget7 != null && (constraintWidget7 == constraintWidget8 || z9)) {
            ConstraintAnchor constraintAnchor6 = constraintWidget5.mListAnchors[i2];
            int i14 = i2 + 1;
            ConstraintAnchor constraintAnchor7 = constraintWidget6.mListAnchors[i14];
            SolverVariable solverVariable9 = constraintWidget5.mListAnchors[i2].mTarget != null ? constraintWidget5.mListAnchors[i2].mTarget.mSolverVariable : null;
            SolverVariable solverVariable10 = constraintWidget6.mListAnchors[i14].mTarget != null ? constraintWidget6.mListAnchors[i14].mTarget.mSolverVariable : null;
            if (constraintWidget7 == constraintWidget8) {
                constraintAnchor6 = constraintWidget7.mListAnchors[i2];
                constraintAnchor7 = constraintWidget7.mListAnchors[i14];
            }
            if (solverVariable9 != null && solverVariable10 != null) {
                if (i == 0) {
                    f = constraintWidget15.mHorizontalBiasPercent;
                } else {
                    f = constraintWidget15.mVerticalBiasPercent;
                }
                linearSystem.addCentering(constraintAnchor6.mSolverVariable, solverVariable9, constraintAnchor6.getMargin(), f, solverVariable10, constraintAnchor7.mSolverVariable, constraintAnchor7.getMargin(), 5);
            }
        } else if (!z11 || constraintWidget7 == null) {
            int i15 = 8;
            if (z8 && constraintWidget7 != null) {
                boolean z12 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
                ConstraintWidget constraintWidget18 = constraintWidget7;
                ConstraintWidget constraintWidget19 = constraintWidget18;
                while (constraintWidget18 != null) {
                    ConstraintWidget constraintWidget20 = constraintWidget18.mNextChainWidget[i];
                    while (constraintWidget20 != null && constraintWidget20.getVisibility() == i15) {
                        constraintWidget20 = constraintWidget20.mNextChainWidget[i];
                    }
                    if (constraintWidget18 == constraintWidget7 || constraintWidget18 == constraintWidget8 || constraintWidget20 == null) {
                        constraintWidget = constraintWidget19;
                        i4 = 8;
                    } else {
                        ConstraintWidget constraintWidget21 = constraintWidget20 == constraintWidget8 ? null : constraintWidget20;
                        ConstraintAnchor constraintAnchor8 = constraintWidget18.mListAnchors[i2];
                        SolverVariable solverVariable11 = constraintAnchor8.mSolverVariable;
                        if (constraintAnchor8.mTarget != null) {
                            SolverVariable solverVariable12 = constraintAnchor8.mTarget.mSolverVariable;
                        }
                        int i16 = i2 + 1;
                        SolverVariable solverVariable13 = constraintWidget19.mListAnchors[i16].mSolverVariable;
                        int margin2 = constraintAnchor8.getMargin();
                        int margin3 = constraintWidget18.mListAnchors[i16].getMargin();
                        if (constraintWidget21 != null) {
                            constraintAnchor = constraintWidget21.mListAnchors[i2];
                            solverVariable = constraintAnchor.mSolverVariable;
                            solverVariable2 = constraintAnchor.mTarget != null ? constraintAnchor.mTarget.mSolverVariable : null;
                        } else {
                            constraintAnchor = constraintWidget18.mListAnchors[i16].mTarget;
                            solverVariable = constraintAnchor != null ? constraintAnchor.mSolverVariable : null;
                            solverVariable2 = constraintWidget18.mListAnchors[i16].mSolverVariable;
                        }
                        if (constraintAnchor != null) {
                            margin3 += constraintAnchor.getMargin();
                        }
                        int i17 = margin3;
                        if (constraintWidget19 != null) {
                            margin2 += constraintWidget19.mListAnchors[i16].getMargin();
                        }
                        int i18 = margin2;
                        int i19 = z12 ? 6 : 4;
                        if (solverVariable11 == null || solverVariable13 == null || solverVariable == null || solverVariable2 == null) {
                            constraintWidget2 = constraintWidget21;
                            constraintWidget = constraintWidget19;
                            i4 = 8;
                        } else {
                            constraintWidget2 = constraintWidget21;
                            constraintWidget = constraintWidget19;
                            i4 = 8;
                            linearSystem.addCentering(solverVariable11, solverVariable13, i18, 0.5f, solverVariable, solverVariable2, i17, i19);
                        }
                        constraintWidget20 = constraintWidget2;
                    }
                    if (constraintWidget18.getVisibility() == i4) {
                        constraintWidget18 = constraintWidget;
                    }
                    constraintWidget19 = constraintWidget18;
                    i15 = 8;
                    constraintWidget18 = constraintWidget20;
                }
                ConstraintAnchor constraintAnchor9 = constraintWidget7.mListAnchors[i2];
                ConstraintAnchor constraintAnchor10 = constraintWidget5.mListAnchors[i2].mTarget;
                int i20 = i2 + 1;
                ConstraintAnchor constraintAnchor11 = constraintWidget8.mListAnchors[i20];
                ConstraintAnchor constraintAnchor12 = constraintWidget6.mListAnchors[i20].mTarget;
                if (constraintAnchor10 == null) {
                    i3 = 5;
                } else if (constraintWidget7 != constraintWidget8) {
                    i3 = 5;
                    linearSystem.addEquality(constraintAnchor9.mSolverVariable, constraintAnchor10.mSolverVariable, constraintAnchor9.getMargin(), 5);
                } else {
                    i3 = 5;
                    if (constraintAnchor12 != null) {
                        linearSystem.addCentering(constraintAnchor9.mSolverVariable, constraintAnchor10.mSolverVariable, constraintAnchor9.getMargin(), 0.5f, constraintAnchor11.mSolverVariable, constraintAnchor12.mSolverVariable, constraintAnchor11.getMargin(), 5);
                    }
                }
                if (constraintAnchor12 != null && constraintWidget7 != constraintWidget8) {
                    linearSystem.addEquality(constraintAnchor11.mSolverVariable, constraintAnchor12.mSolverVariable, -constraintAnchor11.getMargin(), i3);
                }
            }
        } else {
            boolean z13 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
            ConstraintWidget constraintWidget22 = constraintWidget7;
            ConstraintWidget constraintWidget23 = constraintWidget22;
            while (constraintWidget22 != null) {
                ConstraintWidget constraintWidget24 = constraintWidget22.mNextChainWidget[i];
                while (constraintWidget24 != null && constraintWidget24.getVisibility() == 8) {
                    constraintWidget24 = constraintWidget24.mNextChainWidget[i];
                }
                if (constraintWidget24 != null || constraintWidget22 == constraintWidget8) {
                    ConstraintAnchor constraintAnchor13 = constraintWidget22.mListAnchors[i2];
                    SolverVariable solverVariable14 = constraintAnchor13.mSolverVariable;
                    SolverVariable solverVariable15 = constraintAnchor13.mTarget != null ? constraintAnchor13.mTarget.mSolverVariable : null;
                    if (constraintWidget23 != constraintWidget22) {
                        solverVariable15 = constraintWidget23.mListAnchors[i2 + 1].mSolverVariable;
                    } else if (constraintWidget22 == constraintWidget7 && constraintWidget23 == constraintWidget22) {
                        solverVariable15 = constraintWidget5.mListAnchors[i2].mTarget != null ? constraintWidget5.mListAnchors[i2].mTarget.mSolverVariable : null;
                    }
                    int margin4 = constraintAnchor13.getMargin();
                    int i21 = i2 + 1;
                    int margin5 = constraintWidget22.mListAnchors[i21].getMargin();
                    if (constraintWidget24 != null) {
                        constraintAnchor2 = constraintWidget24.mListAnchors[i2];
                        solverVariable3 = constraintAnchor2.mSolverVariable;
                        solverVariable4 = constraintWidget22.mListAnchors[i21].mSolverVariable;
                    } else {
                        constraintAnchor2 = constraintWidget6.mListAnchors[i21].mTarget;
                        solverVariable3 = constraintAnchor2 != null ? constraintAnchor2.mSolverVariable : null;
                        solverVariable4 = constraintWidget22.mListAnchors[i21].mSolverVariable;
                    }
                    if (constraintAnchor2 != null) {
                        margin5 += constraintAnchor2.getMargin();
                    }
                    if (constraintWidget23 != null) {
                        margin4 += constraintWidget23.mListAnchors[i21].getMargin();
                    }
                    if (solverVariable14 == null || solverVariable15 == null || solverVariable3 == null || solverVariable4 == null) {
                        constraintWidget3 = constraintWidget24;
                    } else {
                        if (constraintWidget22 == constraintWidget7) {
                            margin4 = constraintWidget7.mListAnchors[i2].getMargin();
                        }
                        int i22 = margin4;
                        constraintWidget3 = constraintWidget24;
                        linearSystem.addCentering(solverVariable14, solverVariable15, i22, 0.5f, solverVariable3, solverVariable4, constraintWidget22 == constraintWidget8 ? constraintWidget8.mListAnchors[i21].getMargin() : margin5, z13 ? 6 : 4);
                    }
                }
                if (constraintWidget22.getVisibility() != 8) {
                    constraintWidget23 = constraintWidget22;
                }
                constraintWidget22 = constraintWidget3;
            }
        }
        if ((z11 || z8) && constraintWidget7 != null) {
            ConstraintAnchor constraintAnchor14 = constraintWidget7.mListAnchors[i2];
            int i23 = i2 + 1;
            ConstraintAnchor constraintAnchor15 = constraintWidget8.mListAnchors[i23];
            SolverVariable solverVariable16 = constraintAnchor14.mTarget != null ? constraintAnchor14.mTarget.mSolverVariable : null;
            SolverVariable solverVariable17 = constraintAnchor15.mTarget != null ? constraintAnchor15.mTarget.mSolverVariable : null;
            if (constraintWidget6 != constraintWidget8) {
                ConstraintAnchor constraintAnchor16 = constraintWidget6.mListAnchors[i23];
                solverVariable17 = constraintAnchor16.mTarget != null ? constraintAnchor16.mTarget.mSolverVariable : null;
            }
            SolverVariable solverVariable18 = solverVariable17;
            if (constraintWidget7 == constraintWidget8) {
                constraintAnchor14 = constraintWidget7.mListAnchors[i2];
                constraintAnchor15 = constraintWidget7.mListAnchors[i23];
            }
            if (solverVariable16 == null || solverVariable18 == null) {
                return;
            }
            int margin6 = constraintAnchor14.getMargin();
            if (constraintWidget8 != null) {
                constraintWidget6 = constraintWidget8;
            }
            linearSystem.addCentering(constraintAnchor14.mSolverVariable, solverVariable16, margin6, 0.5f, solverVariable18, constraintAnchor15.mSolverVariable, constraintWidget6.mListAnchors[i23].getMargin(), 5);
        }
    }
}
