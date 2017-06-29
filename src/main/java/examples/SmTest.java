package examples;


import org.xml.sax.SAXException;
import sm.solvers.KiralySolver;
import sm.solvers.Solver;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SmTest {

    public static void main(String[] args)  {
        SMParser parser = null;
        try {
            parser = new SMParser("kiralySample.xml");
            parser.read();

            Problem problem = parser.getProblem();

            Solver solver = new KiralySolver(problem);
            System.out.println(problem.toString());

            System.out.println(solver.solve().toString());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}