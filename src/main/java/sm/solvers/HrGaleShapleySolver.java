package sm.solvers;

import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;

import static sm.utils.Constants.HR_ALGORITHM_NAME;
import static sm.utils.logging.SmLogger.logAction;
import static sm.utils.logging.SmLogger.start;

/**
 * Class that solves HR (ont-to-many) instances
 */

public class HrGaleShapleySolver extends AbstractSolver {

    public HrGaleShapleySolver(Problem problem) {
        this.problem = problem;
        this.matching = new Matching(problem.getSets().get(0).getElements());
        this.algorithmName = HR_ALGORITHM_NAME;
        this.solverName = "[HR_SOLVER] ";
        setNames();
    }

    @Override
    public Matching solve() {

        logger.info(start(true, algorithmName, solverName));

        while (!matching.freeElements().isEmpty()) {
            Element resident = matching.freeElements().poll();

            while (!resident.preferences().isEmpty()) {
                Element hospital = problem.element(resident.preferences().poll().elemId());
                logger.info(logAction("proposal", resident.elemId(), hospital.elemId()));

                if (!hospital.accepts(resident)) {
                    logger.info(logAction("rejection", resident.elemId(), hospital.elemId()));
                    continue;
                }

                if (!resident.groupMemberId().equals("NULL")) {
                    /* when the resident has a partner */
                    Element partner = problem.element(resident.groupMemberId());

                    if (!hospital.accepts(partner))
                        continue;

                    if (hospital.capacity() >= 2) {
                        matching.addPair(new Pair(resident, hospital));
                        matching.addPair(new Pair(partner, hospital));

                        logger.info(logAction("paired", resident.elemId(), hospital.elemId()));
                        logger.info(logAction("paired", partner.elemId(), hospital.elemId()));

                        resident.setAvailability(false);
                        partner.setAvailability(false);
                        hospital.decCapacity(2);

                        matching.removeFreeElement(partner);
                        break;
                    }
                    else {
                        Element lowestMatch1, lowestMatch2 = null;

                        if (hospital.capacity() == 0)
                            lowestMatch2 = matching.getLowestMatch(hospital);
                        lowestMatch1 = matching.getLowestMatch(hospital);

                        if (lowestMatch2 != null)
                            if (lowestMatch2.level() < hospital.getIndex(resident.elemId()).level() || lowestMatch2.level() < hospital.getIndex(partner.elemId()).level())
                                continue;

                        if (lowestMatch1.level() < hospital.getIndex(resident.elemId()).level() || lowestMatch1.level() < hospital.getIndex(partner.elemId()).level())
                            continue;

                        matching.addPair(new Pair(resident, hospital));
                        matching.addPair(new Pair(partner, hospital));
                        matching.removeFreeElement(partner);

                        logger.info(logAction("paired", resident.elemId(), hospital.elemId()));
                        logger.info(logAction("paired", partner.elemId(), hospital.elemId()));

                        problem.element(lowestMatch1.elemId()).setAvailability(true);
                        matching.addFreeElement(lowestMatch1);
                        matching.removePairFor(lowestMatch1);
                        logger.info(logAction("rejection", lowestMatch1.elemId(), hospital.elemId()));

                        if (lowestMatch2 != null) {
                            logger.info(logAction("rejection", lowestMatch2.elemId(), hospital.elemId()));
                            problem.element(lowestMatch2.elemId()).setAvailability(true);
                            matching.addFreeElement(lowestMatch2);
                            matching.removePairFor(lowestMatch2);
                        }
                        break;
                    }
                }
                else {
                    if (hospital.isAvailable()) {
                    /* when the hospital is available and the resident is single */
                        matching.addPair(new Pair(resident, hospital));
                        resident.setAvailability(false);
                        hospital.decCapacity(1);
                        break;
                    }
                    /* when the hospital is not available and the resident is single */
                    Element lowestMatch = matching.getLowestMatch(hospital);

                    if (hospital.getIndex(lowestMatch.elemId()).level() < hospital.getIndex(resident.elemId()).level())
                        continue;

                    matching.addFreeElement(lowestMatch);
                    matching.removePairFor(lowestMatch);
                    problem.element(lowestMatch.elemId()).setAvailability(true);

                    matching.addPair(new Pair(resident, hospital));
                    resident.setAvailability(false);
                    break;
                }
            }
        }

        problem.getSets().get(0).getElements().stream().filter(Element::isAvailable).forEach(elem -> matching.addFreeElement(problem.element(elem.elemId())));
        logger.info(start(false, algorithmName, solverName));
        return matching;
    }
}