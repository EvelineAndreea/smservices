package examples;
import org.xml.sax.SAXException;
import sm.solvers.MmSolver;
import sm.solvers.Solver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MmTest {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SMParser parser = new SMParser("D:\\Facultate\\Sem 5\\LICENTA\\Architecture Planning\\mmSample.xml");
        parser.read();

        Problem problem = parser.getProblem();
        System.out.println(problem);

        Solver solver = new MmSolver(problem);
        System.out.println("---MM Solver---");
        System.out.println(solver.solve());
    }

}