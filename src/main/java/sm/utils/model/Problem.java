package sm.utils.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Problem {
    private String problemName;
    private String problemDescription;
    private List<Set> sets;
    private StableMatchingType type;

    public Problem() {
        sets = new ArrayList<>();
    }

    public void insertSet(Set set){
        sets.add(set);
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(Set ... sets) {
        this.sets = new ArrayList<>();
        Collections.addAll(this.sets, sets);
    }

    public int getSetOfElement(String elementId){
        for (int i = 0; i < sets.size(); i++)
            if (sets.get(i).contains(elementId))
                return i;
        return -1;
    }

    public Element element(String elementId) {
        return sets.get(getSetOfElement(elementId)).getElementByName(elementId);
    }

    public SaElement saElement(String elementId) {
        return (SaElement) sets.get(getSetOfElement(elementId)).getElementByName(elementId);
    }

    public void updateElementPreferenceList(Element newElement){
        element(newElement.elemId()).setPreferences(newElement.preferences());
    }

    public void updateElementPreferenceList(SaElement newElement){
        saElement(newElement.elemId()).setSaPreferences(newElement.saPreferences());
    }

    public int getSetSizeForElement(String elementId){
        return sets.get(getSetOfElement(elementId)).getSize();
    }

    public boolean isAvailable(String elementId) {
        return element(elementId).isAvailable();
    }

    public String getProblemName() {
        return problemName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Problem ").append(problemName).append(" ").append(problemDescription).append("\n");
        for (Set i: sets)
            string.append(i).append("\n");
        return string.toString();
    }

    public boolean needsCapacitySpecified() {
        return problemName.equalsIgnoreCase("HR") || problemName.equalsIgnoreCase("MM");
    }


    public boolean needsUnitsSpecified() {
        return problemName.equalsIgnoreCase("SA");
    }
}
