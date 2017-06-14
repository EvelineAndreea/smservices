package sm.utils.model;

import static sm.utils.Constants.MAX_SIZE_FOR_GROUP_ELEMENTS;

/**
 * Class needed for instantiating many-to-many type of problems
 */

public class GroupElement extends Element {
    private String[] groupElemIds;
    private int noOfElemInGroup = 0;
    private int matchedNo = 0;

    public GroupElement(String elementId) {
        super(elementId);
        groupElemIds = new String[MAX_SIZE_FOR_GROUP_ELEMENTS];
    }

    public GroupElement(java.util.Set<String> elements){
        StringBuilder name = new StringBuilder("");
        groupElemIds = new String[elements.size()];

        for(String elemName: elements) {
            name.append(elemName);
            addElemInGroup(elemName);
        }

        this.elementId = name.toString();
    }

    public void addElemInGroup(String currentElemId) {
        groupElemIds[noOfElemInGroup++] = currentElemId;
    }

    public String printElemsInGroup() {
        StringBuilder string = new StringBuilder("");
        if (noOfElemInGroup != 0) {
            for (int i = 0; i < noOfElemInGroup - 1; i++)
                string.append(groupElemIds[i]).append(" AND ");

            string.append(groupElemIds[noOfElemInGroup - 1]);
        }
        return string.toString();
    }

    public boolean isAGroup() {
        return noOfElemInGroup != 0;
    }

    public String[] groupElems() {
        return groupElemIds;
    }

    public int noOfElemInGroup() {
        return noOfElemInGroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof GroupElement)) return false;
        GroupElement element = (GroupElement) obj;
        return (this.containsAll(element.groupElems()));
    }

    private boolean containsAll(String[] strings) {
        boolean ok = true;
        for(int i = 0; i < noOfElemInGroup(); i++){
            ok = false;
            for(int j = 0; j < noOfElemInGroup(); j++){
                if(strings[j].equals(groupElemIds[i])) {
                    ok = true;
                    break;
                }
            }
            if (!ok)
                break;
        }
        return ok;
    }


    public boolean contains(String elemId) {
        for (int i = 0; i < noOfElemInGroup; i++) {
            String current = groupElemIds[i];
            if (current.equals(elemId))
                return true;
        }
        return false;
    }


}
