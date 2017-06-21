package examples;


import org.xml.sax.SAXException;
import sm.solvers.AbstractSolver;
import sm.solvers.GaleShapleySolver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SmTest {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SMParser parser = new SMParser("D:\\Facultate\\Sem 5\\LICENTA\\Architecture Planning\\kiralySample.xml");
        parser.read();

        Problem problem = parser.getProblem();

        AbstractSolver solver = new GaleShapleySolver(problem);
        System.out.println(problem.toString());

        System.out.println(solver.solve().toString());
    }
}