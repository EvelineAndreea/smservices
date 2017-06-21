package sm.solvers;

import sm.utils.model.Element;
import sm.utils.model.GroupElement;
import sm.utils.model.Pair;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;
import sm.utils.model.matchings.MmMatching;

import java.util.ArrayList;
import java.util.List;

import static sm.utils.Constants.MM_ALGORITHM_NAME;

/**
 * Class that solves many-to-many instance problems
 */

public class MmSolver extends AbstractSolver {
    public MmSolver(Problem problem) {
        this.problem = problem;
        this.algorithmName = MM_ALGORITHM_NAME;
    }

    @Override
    public Matching solve() {
        matching = new MmMatching(problem.getSets().get(0).getElements());
        setNames();
        int l = 0;
        do {
//            System.out.println("Phase " + l);

            while (!matching.freeElements().isEmpty()) {
                Element firm = matching.freeElements().poll();

                /* while the firm still has preferences in its list or has some more available places */
                while (!firm.preferences().isEmpty() && firm.isAvailable()) {
                    Element worker = firm.preferences().poll();
//                    System.out.println("[MM-SOLVER] " + firm.elemId() + " " + worker.elemId());

                    if (worker instanceof GroupElement) {
                        GroupElement gworker = (GroupElement) worker;
                        int size = gworker.noOfElemInGroup();

                        List<Element> elements = new ArrayList<>(size);
                        for (int i = 0; i < size; i++) {
                            String name = gworker.groupElems()[i];
                            Element el = problem.element(name);

                        /* verify if the current worker accepts the firm that's making an offer */
                            if (!el.accepts(firm))
                                break;
                            elements.add(el);
                        }

                    /* one/many worker/(s) from the group rejected the firm, so we have to move on to the next preference */
                        if (elements.size() != gworker.noOfElemInGroup())
                            continue;

                        List<Element> currentPaired = ((MmMatching) matching).getPairedFor(firm);

                        /* if every worker accepted the firm, we add the pairs to the matching */
                        int x = matching.addPairs(firm, elements);

                        if (firm.capacity() < size - x)
                            for (Element elem : elements) {
                                if (!currentPaired.contains(elem))
                                    matching.removeSpecificPair(firm, elem);
                            }
                        else
                            firm.decCapacity(size - x);
                    } else {
                        worker = problem.element(worker.elemId());
                        if (!worker.accepts(firm))
                            continue;

                        matching.addPair(new Pair(firm, worker));
                        firm.decCapacity(1);
                    }
                }
                matching.removeFreeElement(firm);
            }

            for (Element worker : ((MmMatching) matching).getMatchedWorkers()) {
                Element wElement = problem.element(worker.elemId());
//                System.out.println(worker.elemId() + " has the capacity " + ((MmMatching) matching).noOfCurrentMatches(worker));

                if (((MmMatching) matching).noOfCurrentMatches(worker) > wElement.capacity())
                    ((MmMatching) matching).removeLeastPreferred(worker);
            }
            for (Element firm : problem.getSets().get(0).getElements()) {
                List<String> workerIds = ((MmMatching) matching).rejected(firm);
                if (workerIds.isEmpty())
                    continue;

                for (String workerId : workerIds) {
//                    System.out.println(firm.elemId() + " has been rejected by " + workerId);
                    firm.rejectedBy(workerId);
                }
                if (!firm.preferences().isEmpty())
                    matching.addFreeElement(firm);
            }
            l++;
            ((MmMatching) matching).clearRemoved();
        } while (!matching.freeElements().isEmpty());

        return matching;
    }
}