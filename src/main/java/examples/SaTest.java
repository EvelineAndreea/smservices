package examples;

import org.xml.sax.SAXException;
import sm.solvers.SaSolver;
import sm.solvers.Solver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SaTest {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SMParser parser = new SMParser("D:\\Facultate\\Sem 5\\LICENTA\\Architecture Planning\\saSample2.xml");
        parser.read();

        Problem problem = parser.getProblem();
        System.out.println(problem);

        Solver solver = new SaSolver(problem);
        System.out.println("---SA Solver---");
        System.out.println(solver.solve());
    }

}