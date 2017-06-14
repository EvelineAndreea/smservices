package rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "unmatched")
public class UnmatchedXml {
    private List<ElementXml> unmatched;

    @XmlElement(name = "element")
    public List<ElementXml> getUnmatched() {
        return unmatched;
    }

    public void setUnmatched(List<ElementXml> unmatched) {
        this.unmatched = unmatched;
    }
}
