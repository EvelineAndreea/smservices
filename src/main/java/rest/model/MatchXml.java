package rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "match")
public class MatchXml {
    private List<ElementXml> elements;
    private int unitsAllocated;

    @XmlElement(name = "element")
    public List<ElementXml> getElements() {
        return elements;
    }

    public void setElements(List<ElementXml> elements) {
        this.elements = elements;
    }

    @XmlElement(name = "unitsAllocated")
    public int getUnitsAllocated() {
        return unitsAllocated;
    }

    public void setUnitsAllocated(int unitsAllocated) {
        this.unitsAllocated = unitsAllocated;
    }
}
