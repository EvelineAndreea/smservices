package sm.solvers;

import sm.utils.logging.SmLogger;
import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;

import static sm.utils.Constants.GS_ALGORITHM_NAME;

public class GaleShapleySolver extends AbstractSolver {

    public GaleShapleySolver(Problem problem) {
        this.problem = problem;
        this.matching = new Matching(problem.getSets().get(0).getElements());
        this.algorithmName = GS_ALGORITHM_NAME;
        this.solverName = "[GS_SOLVER] ";
        setNames();
    }

    @Override
    public Matching solve() {

        SmLogger.start(true, algorithmName, solverName);
        /* while there are elements that aren't matched yet */
        while (!matching.freeElements().isEmpty()) {
            /* get first element from the queue */
            Element element = problem.element(matching.freeElements().poll().elemId());

            /* get its preference element: looping through its list => by the level value */
            while (!element.preferences().isEmpty()) {
                Element pref = problem.element(element.preferences().poll().elemId());
                SmLogger.logAction("proposal", element.elemId(), pref.elemId());

                /* check if is an acceptable partner for current preference */
                if (!pref.accepts(element)) {
                    SmLogger.logAction("rejection", element.elemId(), pref.elemId());
                    continue;
                }

                /* verify the availability of its current preference */
                if (problem.isAvailable(pref.elemId())) {
                    problem.element(pref.elemId()).setAvailability(false);
                    problem.element(element.elemId()).setAvailability(false);
                    matching.addPair(new Pair(element, pref));
                    SmLogger.logAction("paired", element.elemId(), pref.elemId());
                    break;
                }
                else {
                    /* if the preference is already taken, get the best one between current element and its current match */
                    String currMatchName = matching.getElementPair(pref);
                    Element currentMatch = pref.getIndex(currMatchName);
                    Element elem = pref.getIndex(element.elemId());

                    /* when current element is better than the current match */
                    if (currentMatch.level() > elem.level()){
                        if (matching.isFree(elem))
                            matching.removeFreeElement(elem);

                        problem.element(pref.elemId()).setAvailability(false);
                        problem.element(element.elemId()).setAvailability(false);
                        problem.element(currentMatch.elemId()).setAvailability(true);

                        /* add the current pair to the Matching and the old match for the woman to the free list */
                        SmLogger.logAction("paired", element.elemId(), pref.elemId());
                        matching.addPair(new Pair(problem.element(element.elemId()), problem.element(pref.elemId())));

                        matching.addFreeElement(problem.element(currMatchName));
                        SmLogger.logAction("unmatched", currMatchName, pref.elemId());
                        break;
                    }
                    else {
                        matching.addPair(new Pair(currentMatch, pref));
                        SmLogger.logAction("paired", currentMatch.elemId(), pref.elemId());
                    }
                }
            }
            /* when man was rejected by all its preferences */
            if (element.preferences().isEmpty())
                element.setAvailability(true);
        }
        problem.getSets().get(0).getElements().stream().filter(Element::isAvailable).forEach(Element::remakeList);
        problem.getSets().get(0).getElements().stream().filter(Element::isAvailable).forEach(element -> matching.addFreeElement(problem.element(element.elemId())));

        SmLogger.start(false, algorithmName, solverName);
        return matching;
    }
}
