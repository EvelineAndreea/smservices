package sm.utils.model;

import sm.utils.comparators.ElementComparator;

import java.util.*;
import java.util.Set;

import static sm.utils.Constants.MAX_LEVEL_VALUE;

public class Element {

    protected String elementId;
    protected Queue<Element> preferences;
    protected float level = MAX_LEVEL_VALUE;
    protected int capacity;
    protected boolean availability = true;

    private Set<Element> partners;
    protected int partnerIdx = 0;

    /* useful for HR problem, when there exists couples */
    private String partnerName = "NULL";
    public Element(){}

    public Element(String elementId) {
        this.elementId = elementId;
        preferences = new PriorityQueue<>(new ElementComparator());
        partners = new HashSet<>();
    }

    public Element(String elementId, float capacity) {
        this.elementId = elementId;
        preferences = new PriorityQueue<>(new ElementComparator());
        partners = new HashSet<>();
        this.capacity = (int)capacity;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void addPreference(Element element) {
        preferences.add(element);
        partners.add(element);
        partnerIdx++;
    }

    public String elemId() {
        return elementId;
    }

    public void remakeList() {
        preferences.clear();
        preferences.addAll(partners);
    }

    public void prioritize(Element elem, float value) {
        partners.stream().filter(element -> element.equals(elem)).forEach(element -> element.level -= value);
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Queue<Element> preferences() {
        return preferences;
    }

    public void setPreferences(Queue<Element> preferences) {
        this.preferences = preferences;
        partners.addAll(preferences);
    }

    public float level() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public int capacity() {
        return capacity;
    }

    public boolean accepts(Element element) {
        return partners.contains(element);
    }

    public Element getIndex(String currentMatchName) {
        for (Element element : preferences) {
            if (element.elemId().equals(currentMatchName))
                return element;
        }
        return new Element(currentMatchName);
    }

    public void assignGroupMember(String name) {
        partnerName = name;
    }

    public String groupMemberId() {
        return partnerName;
    }

    public boolean isFull() {
        if (capacity == 0)
            return true;

        capacity--;
        return (capacity == 0);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (this.elementId != null ? this.elementId.hashCode() : 0);
        return hash;
    }

    public void decCapacity(int i) {
        capacity -= i;
        if (capacity < 1)
            availability = false;
    }

    public void incCapacity(int i) {
        capacity += i;
        if (capacity > 0)
            availability = true;
    }

    public void rejectedBy(String workerId) {
        Element el = new Element(workerId);
        List<Element> toBeRemoved = new ArrayList<>();

        for (Element pref : preferences) {
            if (pref instanceof GroupElement) {
                if (!((GroupElement) pref).contains(workerId))
                    continue;
            }
            else if (!pref.equals(el))
                continue;

            toBeRemoved.add(pref);
        }

        for(Element element: toBeRemoved)
            preferences.remove(element);

        incCapacity(1);
    }

    public int getMaximumSizeOfAGroup() {
        int max = 0;
        for (Element preference : preferences){
            if (!(preference instanceof GroupElement))
                continue;
            if (max < ((GroupElement) preference).noOfElemInGroup())
                max = ((GroupElement) preference).noOfElemInGroup();
        }

        return max;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Element)) return false;
        Element element = (Element) obj;
        return (this.elementId.equals(element.elemId()));
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Element ").append(elementId);
        if (capacity != 0)
            string.append(" with ").append(capacity).append(" capacity");
        string.append(" has the following preferences: ");

        for (Element current : preferences)
            string.append(current.elemId()).append(" (").append(current.level()).append(") ");

        string.append("\n");
        return string.toString();
    }
}
