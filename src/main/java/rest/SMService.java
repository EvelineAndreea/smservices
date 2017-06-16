package rest;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import rest.model.*;
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

import static sm.utils.Constants.XML_DESCRIPTION;

public class SMService {
    private String algorithmName;

    public ResponseXml manage(String problemType, String documentContent){
        SolvedXml xmlOk = null;
        ErrorXml xmlError = null;
        try {
            Matching matching = interpretInstance(problemType, documentContent);
            xmlOk = map(matching, algorithmName, XML_DESCRIPTION ,matching.getSetNames().get(0), matching.getSetNames().get(1));
        } catch (ValidationException e) {
            xmlError = new ErrorXml();
            xmlError.setAlgorithm(algorithmName);
            xmlError.setMessage(e.getMessage());
        }

        return xmlError == null ? xmlOk : xmlError;
    }

    private Matching interpretInstance(String problemType, String documentContent){
        Matching matching = new Matching();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(documentContent));

        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(is);

            SMParser parser = new SMParser(document);
            parser.read();

            Problem problem = parser.getProblem();
            AbstractSolver solver = SolverFactory.getSolver(problemType, 1, problem);
            matching = solver.solve();

            this.algorithmName = solver.getAlgorithmName();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return matching;
    }

    public SolvedXml map(Matching matching, String algorithm, String description, String firstSet, String secondSet){
        SolvedXml response = new SolvedXml();
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
