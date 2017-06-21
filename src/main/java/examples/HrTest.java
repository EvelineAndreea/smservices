package examples;
import org.xml.sax.SAXException;
import sm.solvers.HrGaleShapleySolver;
import sm.solvers.Solver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class HrTest {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SMParser parser = new SMParser("sample15.xml");
        parser.read();

        Problem problem = parser.getProblem();
        System.out.println(problem);

        Solver solver = new HrGaleShapleySolver(problem);
        System.out.println(solver.solve());
    }
}