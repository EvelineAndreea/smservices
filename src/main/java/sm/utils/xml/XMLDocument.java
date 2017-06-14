package sm.utils.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sm.utils.model.Pair;
import sm.utils.model.matchings.Matching;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Class destined to build the XML of the result for the matching instance given
 */

public class XMLDocument {
    private String fileName;
    private boolean success;
    private Document xml;
    private Matching matching;
    private String message;
    private String algorithm;

    public XMLDocument(String fileName, boolean type, Matching currentMatching, String errorMessage, String algorithm) {
        this.fileName = fileName;
        this.success = type;
        this.algorithm = algorithm;

        if(type)
            matching = currentMatching;
        else
            message = errorMessage;
    }

    public void buildXMLFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            xml = docBuilder.newDocument();

            /* success = true => there is a solution for the problem, else there is an error */
            if(!success)
                createErrorXML();
            else
                createResultXML();
        } catch (ParserConfigurationException e) {
            message = "Server error, try again later!";
            createErrorXML();
            //TODO logging
//            e.printStackTrace();
        }
        finally{
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(xml);
                StreamResult result = new StreamResult(new File(fileName));

                transformer.transform(source, result);
            } catch (TransformerException e) {
                //TODO logging
//                e.printStackTrace();
            }
        }
    }

    private void createErrorXML() {
        Element rootElement = xml.createElement("sm-result");
        xml.appendChild(rootElement);

        Element errorElement = xml.createElement("error");
        errorElement.setAttribute("message", message);
        rootElement.appendChild(errorElement);
    }

    private void createResultXML() {
        Element rootElement = xml.createElement("result");
        rootElement.setAttribute("description", "Matching result for the instance proposed");
        rootElement.setAttribute("algorithm", algorithm);
        xml.appendChild(rootElement);

        for(Pair pair: matching.getPairs())
        {
            Element pairElement = xml.createElement("match");
            Element element0 = xml.createElement("element");
            element0.setTextContent(pair.getFirst().elemId());
            element0.setAttribute("set", matching.getSetNames().get(0));

            Element element1 = xml.createElement("element");
            element1.setTextContent(pair.getSecond().elemId());
            element1.setAttribute("set", matching.getSetNames().get(1));

            pairElement.appendChild(element0);
            pairElement.appendChild(element1);

            rootElement.appendChild(pairElement);
        }

        Element freeElements = xml.createElement("unmatched");
        freeElements.setAttribute("number", String.valueOf(matching.getUnmatchedNumberOfElements()));

        for(sm.utils.model.Element element : matching.getFreeElements())
        {
            Element elementNode = xml.createElement("element");
            elementNode.setTextContent(element.elemId());
            freeElements.appendChild(elementNode);
        }

        rootElement.appendChild(freeElements);
    }

    public Document getXml() {
        return xml;
    }
}
