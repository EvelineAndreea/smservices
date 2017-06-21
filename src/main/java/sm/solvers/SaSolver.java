package sm.solvers;

import sm.utils.model.*;
import sm.utils.model.matchings.Matching;
import sm.utils.model.matchings.SaMatching;

import static sm.utils.Constants.SA_ALGORITHM_NAME;

/**
 * Class made to solve stable-allocation problems
 */

public class SaSolver extends AbstractSolver {

    public SaSolver(Problem problem) {
        this.problem = problem;
        this.algorithmName = SA_ALGORITHM_NAME;
    }

    @Override
    public Matching solve() {
        matching = new SaMatching(problem.getSets().get(0).getElements());
        setNames();

        /* while there are unmatched workers */
        while (!matching.freeElements().isEmpty()) {
            SaElement worker = (SaElement) matching.freeElements().poll();

            /* while the current worker is still available and not all of his favourite employers rejected him */
            while (!worker.saPreferences().isEmpty() && worker.isAvailable()) {
                SaPreference firmPreference = worker.saPreferences().poll();
                SaElement firm = (SaElement) problem.element(firmPreference.elementName());

                System.out.println("\n[SA-SOLVER] " + worker.elemId() + " " + firm.elemId());

                /* does the current firm, the one that the worker wants to be employed at, accepts him? */
                if(!firm.accepts(worker))
                    continue;

                if (!firm.isAvailable()){
                    /* if the firm has no more units to give, verify if it has some worker less desired than the current one asking for the job */
                    SaElement least = leastPreferredFromMatched(firm);

                    if(least.level() > worker.level())
                        continue;

                    least = problem.saElement(least.elemId());
                    int unitsToRelocate = ((SaMatching)matching).getUnitsForPair(least, firm);
                    System.out.println("units to relocate = " +unitsToRelocate);
                    firm.relocateUnits(unitsToRelocate);
                    least.relocateUnits(unitsToRelocate);

                    matching.removeSpecificPair(least, firm);
                    matching.addFreeElement(problem.saElement(least.elemId()));
                    System.out.println(firm.elemId() + " rejected " + least.elemId() + " in favour of " + worker.elemId());
                }

                /* assign the minimum units for both, the firm, and the worker */
                int units = getMinimum(worker.getUnitsLeft(), firmPreference.getMaxunits(), firm.getUnitsLeft());
                firm.addUnits(units);
                worker.addUnits(units);
                matching.addPair(new SaPair(worker, firm, units));

                System.out.println("firm " + firm.elemId() + " has still " + firm.getUnitsLeft());
            }

        }
        return matching;
    }

    private SaElement leastPreferredFromMatched(SaElement firm) {
        float minLevel = 0;
        SaElement toRemove = null;
        for(Pair pair: matching.getPairs()){
            if (pair.getSecond().equals(firm)) {
                SaElement element = firm.getIndex(pair.getFirst().elemId());
                if (element.level() > minLevel) {
                    toRemove = element;
                    minLevel = element.level();
                }
            }
        }
        return toRemove;
    }

    private int getMinimum(int unitsLeft, int maxunits, int unitsLeft1) {
        return Math.min(Math.min(unitsLeft, maxunits), unitsLeft1);
    }
}