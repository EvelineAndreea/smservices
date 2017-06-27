package examples;


import org.xml.sax.SAXException;
import sm.solvers.AbstractSolver;
import sm.solvers.GaleShapleySolver;
import sm.solvers.Solver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SmTest {

    public static void main(String[] args)  {
        SMParser parser = null;
        try {
            parser = new SMParser("D:\\Facultate\\Sem 5\\LICENTA\\Architecture Planning\\kiralySample.xml");
            parser.read();

            Problem problem = parser.getProblem();

            Solver solver = new GaleShapleySolver(problem);
            System.out.println(problem.toString());

            System.out.println(solver.solve().toString());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}