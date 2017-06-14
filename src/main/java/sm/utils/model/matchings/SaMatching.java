package sm.utils.model.matchings;

import sm.utils.model.Element;
import sm.utils.model.Pair;
import sm.utils.model.SaElement;
import sm.utils.model.SaPair;

import java.util.Queue;

public class SaMatching extends Matching {

    public SaMatching(Queue<Element> freeElements) {
        super(freeElements);
    }

    public int getUnitsForPair(SaElement least, SaElement firm) {
        for (Pair pair: pairs){
            if(pair.getFirst().equals(least) && pair.getSecond().equals(firm))
                return ((SaPair) pair).getUnitsAllocated();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("\nMatch: \n");
        for (Pair pair : pairs)
            string.append(pair.getFirst().elemId()).append(" + ").append(pair.getSecond().elemId()).append(" => ").append(((SaPair)pair).getUnitsAllocated()).append("\n");

        string.append("Free elements: ");
        for (Element element : freeElements)
            string.append(element.elemId()).append(" ");
        string.append("\n");

        return string.toString();
    }
}
