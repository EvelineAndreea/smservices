package sm.utils.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sm.utils.model.Problem;
import sm.utils.model.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class made for generating an XML file of an instance of a problem
 */

public class XMLCreator {
    private Problem problem;
    private int noOfSample;

    public XMLCreator(int number, Problem problem) {
        this.noOfSample = number;
        this.problem = problem;
    }

    public void createXmlInstance() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        /* creating the root node */
        Element rootElement = doc.createElement("problem");
        rootElement.setAttribute("name", problem.getProblemName());
        rootElement.setAttribute("description", problem.getProblemDescription());
        doc.appendChild(rootElement);

        List<Element> preferences = new ArrayList<Element>();

        /* creating the set nodes */
        List<Set> sets = problem.getSets();

        for (Set set: sets) {
            Element setElement = doc.createElement("set");
            setElement.setAttribute("name", set.getSetName());
            setElement.setAttribute("size", Integer.toString(set.getSize()));

            StringBuilder setValue = new StringBuilder("");
            for (sm.utils.model.Element element : set.getElements()) {

                if (problem.getProblemName().equals("HR") && !set.getSetName().equals(problem.getSets().get(0).getSetName())) {
                    Element current = doc.createElement("element");
                    current.setAttribute("name", element.elemId());
                    current.setAttribute("capacity", Integer.toString(element.capacity()));

                    setElement.appendChild(current);
                }
                else
                    setValue.append(element.elemId()).append(",");

                Element preference = doc.createElement("preferences");
                preference.setAttribute("name", element.elemId());

                /* creating the preferences nodes */
                for (sm.utils.model.Element pref : element.preferences()){
                    Element elementNode = doc.createElement("element");
                    elementNode.setAttribute("name", pref.elemId());
                    elementNode.setAttribute("level", Float.toString(pref.level()));
                    elementNode.setAttribute("tie", "false");

                    preference.appendChild(elementNode);
                }
                preferences.add(preference);
            }
            if (!setValue.toString().equals("")) {
                setValue.deleteCharAt(setValue.length() - 1);
                setElement.setTextContent(setValue.toString());
            }
            rootElement.appendChild(setElement);
        }

        for (Element preferenceElement : preferences)
            rootElement.appendChild(preferenceElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("sample" + Integer.toString(noOfSample) + ".xml"));

        transformer.transform(source, result);
    }
}
