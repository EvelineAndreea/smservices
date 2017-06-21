package sm.solvers;

import sm.utils.logging.SmLogger;
import sm.utils.model.Element;
import sm.utils.model.Problem;
import sm.utils.model.Set;
import sm.utils.model.matchings.Matching;

import java.util.Arrays;

import static sm.utils.Constants.K_ALGORITHM_NAME;

public class KiralySolver extends AbstractSolver {
    private static final float EPSILON = (float) 0.5;
    private HrGaleShapleySolver gsSolver;

    public KiralySolver(Problem problem) {
        this.gsSolver = new HrGaleShapleySolver(problem);
        this.problem = problem;
        this.algorithmName = K_ALGORITHM_NAME;
        this.solverName = "[KIRALY_SOLVER] ";
    }

    @Override
    public Matching solve() {
        SmLogger.start(true, algorithmName, solverName);

        matching = new Matching(problem.getSets().get(0).getElements());
        setNames();

        Set men = problem.getSets().get(0);

        /* array which holds a boolean that specifies if a man was already re-promoted */
        boolean visited[] = new boolean[problem.getSets().get(0).getSize()];
        Arrays.fill(visited, false);

        boolean finished = false;
        while(!finished){
            finished = true;
            matching = gsSolver.solve();
            int size = men.getSize();

            /* loop through all men */
            for (int i = 0; i < size; i++){
                Element elem = men.getElements().poll();
                men.insertElement(elem);

                /* if the current man has been already promoted or it has a matching we pass over him */
                if (visited[i] || !matching.isFree(elem))
                    continue;

                /* we promote him */
                gsSolver.matching.addFreeElement(problem.element(elem.elemId()));
                visited[i] = true;
                finished = false;

                Set women = problem.getSets().get(1);

                /* loop through women to raise the current man's priority with the value of EPSILON */
                for (int j = 0; j < women.getSize(); j++){
                    Element wElem = women.getElements().poll();
                    women.insertElement(wElem);

                    /* we have to verify if the woman has the current man on her list, and if she does, his priority is raised with EPSILON */
                    if (wElem.accepts(elem)) {
                        wElem.prioritize(elem, EPSILON);
                        wElem.remakeList();
                    }
                }
            }
        }

        SmLogger.start(false, algorithmName, solverName);
        return matching;
    }
}
