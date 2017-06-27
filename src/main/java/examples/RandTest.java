package examples;

import org.xml.sax.SAXException;
import sm.solvers.AbstractSolver;
import sm.solvers.SolverFactory;
import sm.utils.data.ArtificialData;
import sm.utils.data.XMLCreator;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static sm.utils.model.StableMatchingType.HR;


public class RandTest {

    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {
//        ArtificialData data = new ArtificialData(HR);
//        data.createInstance();
//
//        XMLCreator xml = new XMLCreator(16, data.getProblem());
//        xml.createXmlInstance();

        SMParser parser = new SMParser("sample16.xml");
        parser.read();

        Problem problem = parser.getProblem();
        AbstractSolver solver = SolverFactory.getSolver("HR", 1, problem);
        solver.solve();
        Matching matching = solver.getMatching();
        System.out.println(matching);
        System.out.println("DONE!");
    }
}
