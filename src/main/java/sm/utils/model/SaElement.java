package sm.utils.model;

import sm.utils.comparators.PreferenceComparator;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Stable Allocation Element class definition
 */

public class SaElement extends Element {
    private int units;
    private Queue<SaPreference> saPreferences;
    private java.util.Set<SaPreference> saPartners;
    private int unitsLeft;

    public SaElement(String elementId) {
        super(elementId);
        saPreferences = new PriorityQueue<>(new PreferenceComparator());
        saPartners = new HashSet<>();
    }

    public SaElement(String name, float units) {
        super(name);
        this.units = (int) units;
        saPreferences = new PriorityQueue<>(new PreferenceComparator());
        saPartners = new HashSet<>();
        unitsLeft = this.units;
    }

    public int getUnitsLeft(){
        return unitsLeft;
    }

    public void addUnits(int unitsToAdd){
        unitsLeft -= unitsToAdd;
        if (unitsLeft == 0)
            availability = false;
    }

    public void addPreference(Element element, int maxunits) {
        SaPreference preference = new SaPreference(element.elemId(), maxunits, element.level());
        saPreferences.add(preference);
        saPartners.add(preference);
        partnerIdx++;
    }

    public boolean accepts(SaElement element) {
        for(SaPreference pref: saPartners)
            if (pref.equals(new SaPreference(element.elemId())))
                return true;

        return false;
    }

    public Queue<SaPreference> saPreferences() {
        return saPreferences;
    }

    public void setSaPreferences(Queue<SaPreference> saPreferences) {
        this.saPreferences = saPreferences;
        saPartners.addAll(saPreferences);
    }


    public SaElement getIndex(String currentMatchName) {
        for (SaPreference preference : saPreferences) {
            if (preference.elementName().equals(currentMatchName)) {
                SaElement pref = new SaElement(preference.elementName());
                pref.setLevel(preference.level());
                return pref;
            }
        }
        return new SaElement(currentMatchName);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("SaElement ").append(elementId);
        if (units != 0)
            string.append(" with ").append(units).append(" units");
        string.append(" has the following preferences: ");

        for (SaPreference current : saPreferences)
            string.append(current.elementName()).append(" (").append(current.level()).append(" maxunits - ").append(current.getMaxunits()).append(") ");
        string.append("\n");
        return string.toString();
    }

    public void relocateUnits(int unitsToRelocate) {
        unitsLeft += unitsToRelocate;
        if(unitsLeft > 0)
            availability = true;
    }
}
