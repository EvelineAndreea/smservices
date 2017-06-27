package tests;

import org.junit.Test;
import org.xml.sax.SAXException;
import sm.solvers.AbstractSolver;
import sm.solvers.GaleShapleySolver;
import sm.solvers.HrGaleShapleySolver;
import sm.solvers.Solver;
import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.Problem;
import sm.utils.model.Set;
import sm.utils.model.matchings.Matching;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SmTest extends BaseTest {

    @Test
    public void smShouldReturnTheMatching_w2m3_w3m1_w1m2() throws Exception {
        load("smTestSample.xml");

        Solver solver = new GaleShapleySolver(problem);
        System.out.println(problem.toString());

        Matching matching = solver.solve();

        System.out.println(matching.toString());

        Pair pair = new Pair(new Element("w2"), new Element("m3"));
        Pair pair2 = new Pair(new Element("w3"), new Element("m1"));
        Pair pair3 = new Pair(new Element("w1"), new Element("m2"));

        assert pair.equals(matching.getPairs().get(0));
        assert pair2.equals(matching.getPairs().get(1));
        assert pair3.equals(matching.getPairs().get(2));
        assert matching.getFreeElements().size() == 0;
    }

}