package sm.utils.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sm.exceptions.ValidationException;
import sm.utils.model.GroupElement;
import sm.utils.model.Problem;
import sm.utils.model.SaElement;
import sm.utils.model.Set;
import sm.utils.model.validator.InstanceValidator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static sm.utils.Constants.*;

public class SMParser {
    protected Problem problem;
    protected Document document;
    protected File xmlFile;

    public SMParser(String fileName) throws ParserConfigurationException, IOException, SAXException {
        xmlFile = new File(fileName);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        document = docBuilder.parse(xmlFile);
    }

    public SMParser(Document xmlDocument) {
        document = xmlDocument;
    }

    public Problem getProblem() {
        return problem;
    }

    public void read() throws IOException, SAXException, ParserConfigurationException, ValidationException {
        document.getDocumentElement().normalize();
        instantiateProblem();
    }

    private void setProblemDetails(Element element) throws ValidationException {
        String problemName = element.getAttribute("name");
        if (!InstanceValidator.isAValidProblemType(problemName))
            throw new ValidationException();

        problem = new Problem();
        problem.setProblemName(problemName);
        problem.setProblemDescription(element.getAttribute("description"));
    }

    public void instantiateProblem() {
        Element rootElement = document.getDocumentElement();
        setProblemDetails(rootElement);

        NodeList nodeList = rootElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentNode;
                String elementName = currentElement.getTagName().trim();

                switch (elementName.toLowerCase()) {
                    case "set":
                        createSet(currentElement);
                        break;
                    case "preferences":
                        addPreferenceList(currentElement);
                        break;
                    case "group":
                        addGroupToTheInstance(currentElement);
                        break;
                }
            }
        }
    }

    private void addPreferenceList(Element element) {
        String elementName = element.getAttribute("name").trim();
        verifyExistenceOfElement(elementName, ERROR_NO_ELEMENT_FOUND);

        sm.utils.model.Element currentElement;

        if(problem.needsUnitsSpecified())
            currentElement = new SaElement(elementName);
        else
            currentElement = new sm.utils.model.Element(elementName);

        NodeList nodes = element.getElementsByTagName("element");
        for(int i = 0; i < nodes.getLength(); i++){
            if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element preference = (Element) nodes.item(i);
            boolean tie = Boolean.parseBoolean(preference.getAttribute("tie").trim());
            float level = verifyNumberValue(preference.getAttribute("level"), ERROR_INVALID_LEVEL, null);

            if(tie)
                if (currentElement.getClass().equals(SaElement.class))
                    parseTieElement(preference, (SaElement)currentElement, level);
                else
                    parseTieElement(preference, currentElement, level);
            else {
                NodeList childNodes = preference.getChildNodes();

                /* has a group element */
                if (childNodes.getLength() > 1)
                    addGroups(preference, currentElement, level);
                else {
                    String name = preference.getAttribute("name");
                    verifyExistenceOfElement(name, ERROR_NO_ELEMENT_FOUND);

                    sm.utils.model.Element preferenceElement = new sm.utils.model.Element(name);
                    preferenceElement.setLevel(level);

                    if (currentElement.getClass().equals(SaElement.class)) {
                        int maxunits = (int) Math.floor(verifyNumberValue(preference.getAttribute("maxunits"), ERROR_INVALID_UNITS_VALUE, name));
                        ((SaElement) currentElement).addPreference(preferenceElement, maxunits);
                    }
                    else
                        currentElement.addPreference(preferenceElement);
                }
            }
        }
        sm.utils.model.Element elem = problem.element(elementName);

        if (!elem.groupMemberId().equals("NULL")) {
            sm.utils.model.Element member = problem.element(elem.groupMemberId());
            if (!member.preferences().isEmpty())
                throw new ValidationException(ERROR_INVALID_PREFERENCE_LIST + elem.elemId() + ", " + elem.groupMemberId());

            currentElement.setElementId(member.elemId());
            problem.updateElementPreferenceList(currentElement);
            currentElement.setElementId(elementName);
        }
        if (currentElement.getClass().equals(SaElement.class))
            problem.updateElementPreferenceList((SaElement) currentElement);
        else
            problem.updateElementPreferenceList(currentElement);

    }

    private void parseTieElement(Element tieElement, SaElement currentElement, float level){
        String[] names = tieElement.getTextContent().split(",");
        int maxunits = (int) Math.floor(verifyNumberValue(tieElement.getAttribute("maxunits"), ERROR_INVALID_MAXUNITS_VALUE, null));

        for(String name: names){
            name = name.trim();
            verifyExistenceOfElement(name, ERROR_NO_ELEMENT_FOUND);
            sm.utils.model.Element preference = new sm.utils.model.Element(name);
            preference.setLevel(level);

            currentElement.addPreference(preference, maxunits);
        }
    }

    private void parseTieElement(Element tieElement, sm.utils.model.Element currentElement, float level) {
        NodeList groups = tieElement.getElementsByTagName("group");
        addGroups(tieElement, currentElement, level);

        if (groups.getLength() < 2) {
            addSimpleElements(tieElement, currentElement, level);
        }
    }

    /* simple elements */
    private void addSimpleElements(Element tieElement, sm.utils.model.Element currentElement, float level) {
        String[] elementNames = tieElement.getTextContent().trim().split(",");
        for (String name: elementNames) {
            name = name.trim();
            verifyExistenceOfElement(name, ERROR_NO_ELEMENT_FOUND + name);

            sm.utils.model.Element preference = new sm.utils.model.Element(name);
            preference.setLevel(level);
            currentElement.addPreference(preference);
        }
    }

    /* groups */
    private void addGroups(Element xmlElement, sm.utils.model.Element currentElement, float level){
        NodeList groups = xmlElement.getElementsByTagName("group");

        for (int i = 0; i < groups.getLength(); i++){
            if (groups.item(i).getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element groupElement = (Element) groups.item(i);
            String groupName = xmlElement.getAttribute("name");
            if(groupName.length() == 0)
                groupName = groupElement.getAttribute("name");

            GroupElement group = new GroupElement(groupName);
            String[] names = groupElement.getTextContent().split(",");
            for (String name: names){
                name = name.trim();
                verifyExistenceOfElement(name, ERROR_NO_ELEMENT_FOUND + name);

                group.addElemInGroup(name);
            }
            group.setLevel(level);
            currentElement.addPreference(group);
        }
    }

    private void createSet(Element setElement) {
        String setName = setElement.getAttribute("name");
        int size = (int) Math.floor(verifyNumberValue(setElement.getAttribute("size"), ERROR_INVALID_SIZE, setName));

        Set set = new Set(setName);
        NodeList nodes = setElement.getChildNodes();

        if(nodes.getLength() == 1){
            String[] elements = setElement.getTextContent().split(",");
            for(String elementName: elements) {
                verifyIfElementAlreadyExists(elementName.trim(), set);
                set.insertElement(new sm.utils.model.Element(elementName.trim()));
            }
        }
        else {
            for (int i = 0; i < nodes.getLength(); i++){
                if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element element = (Element) nodes.item(i);
                float capacity, units;
                String name = element.getAttribute("name");

                if(name == null)
                    throw new ValidationException(ERROR_ELEMENT_NAME);

                if (problem.needsCapacitySpecified()) {
                    capacity = verifyNumberValue(element.getAttribute("capacity"), ERROR_INVALID_CAPACITY, name);
                    verifyIfElementAlreadyExists(name, set);
                    set.insertElement(new sm.utils.model.Element(name, capacity));
                }

                if (problem.needsUnitsSpecified()) {
                    units = verifyNumberValue(element.getAttribute("units"), ERROR_INVALID_UNITS_VALUE, name);
                    verifyIfElementAlreadyExists(name, set);
                    set.insertElement(new sm.utils.model.SaElement(name, units));
                }
            }
        }

        if (size != set.getSize())
            throw new ValidationException("Invalid size for " + setName + ".");
        problem.insertSet(set);
    }

    private void addGroupToTheInstance(Element currentElement) {
        NodeList nodeElements = currentElement.getChildNodes();

        for (int i = 0; i < nodeElements.getLength(); i++) {
            if (nodeElements.item(i).getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element element = (Element) nodeElements.item(i);
            if (!element.getTagName().toLowerCase().equals("couple"))
                continue;

            String[] names = element.getTextContent().split(",");
            int setIdx = problem.getSetOfElement(names[0].trim());

            if (setIdx < 0)
                throw new ValidationException(ERROR_NO_ELEMENT_FOUND + names[0]);

            sm.utils.model.Element elem = problem.element(names[0].trim());
            for (int j = 1; j < names.length; j++) {
                sm.utils.model.Element member = problem.element(names[j].trim());
                if (problem.getSetOfElement(names[j].trim()) != setIdx)
                    throw new ValidationException(ERROR_COUPLE_DEFINITION + names[0] + ", " + names[j]);
                elem.assignGroupMember(names[j].trim());
                member.assignGroupMember(names[0].trim());
            }
        }
    }


    private void verifyIfElementAlreadyExists(String name, Set set){
        String error = ERROR_NAME_TAKEN + name;
        if (set.contains(name))
            throw new ValidationException(error);
    }

    private Float verifyNumberValue(String attributeValue, String errorMessage, String parameterName){
        String error = errorMessage;
        if (parameterName != null)
            error += " " + parameterName + ".";

        if (attributeValue.isEmpty())
            throw new ValidationException(error);

        Float value = Float.parseFloat(attributeValue);
        if (!InstanceValidator.checkSize(value))
            throw new ValidationException(error);

        return value;
    }

    private void verifyExistenceOfElement(String name, String errorMessage){
        if (name == null)
            throw new ValidationException(ERROR_NO_NAME);
        if (problem.getSetOfElement(name) == -1)
            throw new ValidationException(errorMessage + " " + name);
    }
}
