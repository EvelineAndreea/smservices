package sm.solvers;

import sm.utils.model.Problem;

public class SolverFactory {

    public static AbstractSolver getSolver(String problemType, int algorithm, Problem problem) {
        switch (problemType.toLowerCase()) {
            case "sm":
                return new KiralySolver(problem);
            case "hr":
                return new HrGaleShapleySolver(problem);
            case "mm":
                return new MmSolver(problem);
            case "sa":
                return new SaSolver(problem);
            default:
                return new GaleShapleySolver(problem);
        }
    }
}
