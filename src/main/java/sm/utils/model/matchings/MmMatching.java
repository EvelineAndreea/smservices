package sm.utils.model.matchings;

import sm.utils.model.Element;
import sm.utils.model.GroupElement;
import sm.utils.model.Pair;

import java.util.*;

import static sm.utils.Constants.MAX_SIZE;


/**
 * Class intended to extend the definition of the Matching instance, specifically made for many-to-many instances
 */

public class MmMatching extends Matching {
    private Map<String, Integer> matchedElements;
    private List<Pair> removedPairs;

    public MmMatching(Queue<Element> freeElements) {
        super(freeElements);
        matchedElements = new HashMap<>();
        removedPairs = new ArrayList<>();
    }

    public int noOfCurrentMatches(Element worker) {
        return matchedElements.get(worker.elemId());
    }

    public void removeLeastPreferred(Element worker){
        Set<Element> currentMatches = new HashSet<>();
        for (Pair pair: pairs){
            if(!pair.getSecond().equals(worker))
                continue;
            currentMatches.add(pair.getFirst());
        }

        Set<String> elements = new HashSet<>();
        for(Element el: currentMatches)
            elements.add(el.elemId());

        int maxGroupSize = worker.getMaximumSizeOfAGroup();
        Set<Set<String>> groups = com.google.common.collect.Sets.powerSet(elements);
        for (Set<String> set: groups){
            if(set.size() > maxGroupSize)
                continue;
            GroupElement group = new GroupElement(set);
            currentMatches.add(group);
        }

        int maxCapacity = worker.capacity();
        for(Element match : worker.preferences()){
            if (maxCapacity <= 0)
                break;
            if (currentMatches.contains(match)){
                if (match instanceof GroupElement) {
                    int newCapacity = maxCapacity - ((GroupElement) match).noOfElemInGroup();
                    if (newCapacity >= 0) {
                        maxCapacity = newCapacity;
                        for (int i = 0; i< ((GroupElement) match).noOfElemInGroup();i++) {
                            String id = ((GroupElement) match).groupElems()[i];
                            currentMatches.remove(new Element(id));
                        }
                    }
                }
                else
                    maxCapacity--;
                currentMatches.remove(match);
            }
        }

        for (Element toRemove: currentMatches){
            if (toRemove instanceof GroupElement)
                continue;
            removeSpecificPair(new Element(toRemove.elemId()), worker);
            matchedElements.replace(worker.elemId(), matchedElements.get(worker.elemId()) - 1);
            removedPairs.add(new Pair(toRemove, worker));
        }
    }


    public List<String> rejected(Element firm){
        List<String> rejectedBy = new ArrayList<>();

        for (Pair pair: removedPairs){
            if (pair.contains(firm))
                rejectedBy.add(pair.getSecond().elemId());
        }
        return rejectedBy;
    }

    public void clearRemoved() {
        removedPairs.clear();
    }

    public int getMatchedNoOfPairsForFirm(Element firm) {
        int x = 0;
        for (Pair pair: pairs){
            if(pair.contains(firm))
                x++;
        }
        return x;
    }

    public List<Element> getMatchedWorkers() {
        List<Element> matched = new ArrayList<>(MAX_SIZE);
        matchedElements.clear();

        for(Pair pair: pairs) {
            Element current = pair.getSecond();
            if (!matched.contains(current)) {
                matched.add(current);
                matchedElements.put(current.elemId(), 1);
            }
            else {
                matchedElements.replace(current.elemId(), matchedElements.get(current.elemId()) + 1);
            }
        }
        return matched;
    }


    public List<Element> getPairedFor(Element firm){
        List<Element> paired = new ArrayList<>();

        for (Pair pair: pairs)
            if (pair.getFirst().equals(firm))
                paired.add(pair.getSecond());
        return paired;
    }
}
