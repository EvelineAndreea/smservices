package sm;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import rest.model.ElementXml;
import rest.model.MatchXml;
import rest.model.ResponseXml;
import rest.model.UnmatchedXml;
import sm.exceptions.ValidationException;
import sm.solvers.AbstractSolver;
import sm.solvers.SolverFactory;
import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.Problem;
import sm.utils.model.matchings.Matching;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SMService {
    private String type;
    private int code;

    public SMService(String type){
        this.type = type;
    }

    public ResponseXml manage(String problemType, String documentContent){
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(documentContent));

        ResponseXml xml;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(is);

            SMParser parser = new SMParser(document);
            parser.read();

            Problem problem = parser.getProblem();
            AbstractSolver solver = SolverFactory.getSolver(problemType, 1, problem);
            Matching matching = solver.solve();

            xml = map(matching, solver.getAlgorithmName(),"Matching result for the instance proposed" ,matching.getSetNames().get(0), matching.getSetNames().get(1));
            code = 200;

        } catch (IOException | ParserConfigurationException | SAXException | ValidationException e) {
            xml = new ResponseXml();
        }

        return xml;
    }

    public int getCode() {
        return code;
    }


    public ResponseXml map(Matching matching, String algorithm, String description, String firstSet, String secondSet){
        ResponseXml response = new ResponseXml();
        response.setAlgorithm(algorithm);
        response.setDescription(description);

        MatchXml match = new MatchXml();
        List<MatchXml> matches = new ArrayList<>();
        UnmatchedXml unmatched = new UnmatchedXml();
        List<ElementXml> unmatchedElements = new ArrayList<>();

        for(Pair pair: matching.getPairs()){
            ElementXml elem0 = new ElementXml();
            elem0.setName(pair.getFirst().elemId());
            elem0.setSet(firstSet);

            ElementXml elem1 = new ElementXml();
            elem1.setName(pair.getSecond().elemId());
            elem1.setSet(secondSet);

            List<ElementXml> elems = new ArrayList<>();
            elems.add(elem0);
            elems.add(elem1);

            match.setElements(elems);
            matches.add(match);
        }

        response.setMatches(matches);

        for (Element element: matching.getFreeElements()){
            ElementXml elementXml = new ElementXml();
            elementXml.setName(element.elemId());

            unmatchedElements.add(elementXml);
        }

        unmatched.setUnmatched(unmatchedElements);
        response.setUnmatched(unmatched);

        return response;
    }
}
