package sm.solvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSolver implements Solver {
    Problem problem;
    Matching matching;
    String algorithmName;
    String solverName;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract Matching solve();
    public Matching getMatching() {
        return matching;
    }
    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setNames(){
        List<String> setNames = new ArrayList<>();
        for (int i = 0; i < problem.getSets().size(); i++)
            setNames.add(problem.getSets().get(i).getSetName());

        this.matching.setSetNames(setNames);
    }
}
