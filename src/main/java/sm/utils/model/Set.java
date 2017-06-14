package sm.utils.model;

import java.util.ArrayDeque;
import java.util.Queue;

public class Set {
    private String setName;
    private int setSize;
    private Queue<sm.utils.model.Element> elements;

    public Set() {}

    public Set(String setName) {
        setSetName(setName);
        elements = new ArrayDeque<>();
    }

    public String getSetName() {
        return setName;
    }

    public void insertElement(sm.utils.model.Element element) {
        elements.add(element);
        setSize++;
    }


    public boolean contains(String elementName) {
        return elements.contains(new Element(elementName));
    }

    public Queue<Element> getElements() {
        return elements;
    }

    public void setElements(Queue<Element> elements) {
        this.elements = elements;
    }

    public Element getElementByName(String elementId) {
        Element newElement = new Element(elementId);
        for (Element e : elements)
            if (e.elemId().equals(newElement.elemId()))
                return e;
        return null;
    }

    public int getSize() {
        return setSize;
    }

    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Set ").append(setName).append(" contains:\n");
        for (Element element : elements)
            string.append(element.toString());
        return string.toString();
    }
}
