package tests;


import org.junit.Test;
import sm.solvers.HrGaleShapleySolver;
import sm.solvers.Solver;
import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.matchings.Matching;

public class HrTest extends BaseTest{

    @Test
    public void hrShouldReturnTheMatching_r1h1_r3h1_r2h2_r4h2_r5h1() throws Exception {
        load("hrTestSample.xml");

        Solver solver = new HrGaleShapleySolver(problem);
        Matching matching = solver.solve();

        Pair pair = new Pair(new Element("r1"), new Element("h1"));
        Pair pair2 = new Pair(new Element("r3"), new Element("h1"));
        Pair pair3 = new Pair(new Element("r2"), new Element("h2"));
        Pair pair4 = new Pair(new Element("r4"), new Element("h2"));
        Pair pair5 = new Pair(new Element("r5"), new Element("h1"));

        assert pair.equals(matching.getPairs().get(0));
        assert pair2.equals(matching.getPairs().get(1));
        assert pair3.equals(matching.getPairs().get(2));
        assert pair4.equals(matching.getPairs().get(3));
        assert pair5.equals(matching.getPairs().get(4));

        assert matching.getFreeElements().size() == 0;
    }
}
