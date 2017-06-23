package rest.model;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "result")
public class SolvedXml extends ResponseXml{
    protected String description;

    private List<MatchXml> matches;
    private UnmatchedXml unmatched;


    @XmlAttribute(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "unmatched")
    public UnmatchedXml getUnmatched() {
        return unmatched;
    }

    public void setUnmatched(UnmatchedXml unmatched) {
        this.unmatched = unmatched;
    }

    @XmlElement(name = "match")
    public List<MatchXml> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchXml> matches) {
        this.matches = matches;
    }
}


