package androidx.constraintlayout.solver;

public class GoalRow extends ArrayRow {
    public GoalRow(Cache cache) {
        super(cache);
    }

    @Override
    public void addError(SolverVariable solverVariable) {
        super.addError(solverVariable);
        solverVariable.usageInRowCount--;
    }
}
