package sm.utils.model;


public class SaPair extends Pair {
    private int unitsAllocated;

    public SaPair(Element firstElement, Element secondElement, int unitsAllocated) {
        super(firstElement, secondElement);
        this.unitsAllocated = unitsAllocated;
    }

    public int getUnitsAllocated() {
        return unitsAllocated;
    }

    public void setUnitsAllocated(int unitsAllocated) {
        this.unitsAllocated = unitsAllocated;
    }
}
