package rest.model;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "result")
public class SolvedXml extends ResponseXml{

    private List<MatchXml> matches;
    private UnmatchedXml unmatched;


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
