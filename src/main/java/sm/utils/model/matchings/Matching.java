package sm.utils.model.matchings;

import sm.utils.model.Element;
import sm.utils.model.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static sm.utils.Constants.MAX_SIZE;

public class Matching {
    protected List<Pair> pairs;
    protected Queue<Element> freeElements;
    protected int unmatchedNumberOfElements = 0;
    protected List<String> setNames;

    public Matching() {

    }

    public int getUnmatchedNumberOfElements() {
        return unmatchedNumberOfElements;
    }


    public Matching(Queue<Element> freeElements) {
        this.freeElements = new ArrayDeque<>();
        this.freeElements.addAll(freeElements);
        pairs = new ArrayList<>();
        unmatchedNumberOfElements = freeElements.size();

    }

    public Queue<Element> freeElements() {
        return freeElements;
    }

    public boolean addPair(Pair pair) {
        if (!pairs.contains(pair))
            pairs.add(pair);
        else
            return false;

        return true;
    }

    public String getElementPair(Element preference) {
        Pair selectedPair = null;

        for (int i = 0; i < pairs.size(); i++)
            if (pairs.get(i).contains(preference)) {
                selectedPair = pairs.get(i);
                pairs.remove(i);
            }
        if (selectedPair == null)
            return null;

        if (selectedPair.getFirst().elemId().equals(preference.elemId()))
            return selectedPair.getSecond().elemId();
        return selectedPair.getFirst().elemId();
    }

    public void addFreeElement(Element elementReference) {
        freeElements.add(elementReference);
        unmatchedNumberOfElements++;
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public boolean isFree(Element elem) {
        for (Element element : freeElements)
            if (elem.equals(element))
                return true;
        return false;
    }

    public void removeFreeElement(Element elem) {
        freeElements.remove(elem);
        unmatchedNumberOfElements--;
    }

    public Element getLowestMatch(Element hospital) {
        Element lowest = null;

        Element[] matches = new Element[MAX_SIZE];
        int idx = 0;
        float level = 0;

        for (Pair pair : pairs)
            if (pair.contains(hospital)) {
                if (pair.getFirst().equals(hospital))
                    matches[idx] = pair.getSecond();
                else
                    matches[idx] = pair.getFirst();

                if (hospital.getIndex(matches[idx].elemId()).level() > level) {
                    level = hospital.getIndex(matches[idx].elemId()).level();
                    lowest = matches[idx];
                }
                idx++;
            }

        return lowest;
    }

    public void removePairFor(Element first) {
        for (int i = 0; i < pairs.size(); i++)
            if (pairs.get(i).contains(first)){
                pairs.remove(i);
                break;
            }
    }

    public void removeSpecificPair(Element first, Element second) {
        for (int i = 0; i < pairs.size(); i++)
            if (pairs.get(i).contains(first) && pairs.get(i).contains(second)) {
                pairs.remove(i);
                break;
            }
    }

    public Queue<Element> getFreeElements() {
        return freeElements;
    }

    public int addPairs(Element firm, List<Element> elements) {
        int x = 0;
        for (Element elem: elements)
            if (!addPair(new Pair(firm, elem)))
                x++;
        return x;
    }


    public List<String> getSetNames() {
        return setNames;
    }

    public void setSetNames(List<String> setNames) {
        this.setNames = setNames;
    }
    
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("\nMatch: \n");
        for (Pair pair : pairs)
            string.append(pair.getFirst().elemId()).append(" + ").append(pair.getSecond().elemId()).append("\n");

        string.append("Free elements: ");
        if (freeElements.size() == 0)
            string.append("none.");
        else
            for (Element element : freeElements)
                string.append(element.elemId()).append(" ");
        string.append("\n");

        return string.toString();
    }

}
