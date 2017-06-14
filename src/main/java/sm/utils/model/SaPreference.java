package sm.utils.model;

public class SaPreference {
    private String elementName;
    private int maxunits;
    private float level;

    public SaPreference(String elementName) {
        this.elementName = elementName;
    }

    public SaPreference(String name, int units, float level) {
        elementName = name;
        maxunits = units;
        this.level = level;
    }

    public String elementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public int getMaxunits() {
        return maxunits;
    }

    public void setMaxunits(int maxunits) {
        this.maxunits = maxunits;
    }

    public float level(){
        return level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof SaPreference)) return false;
        SaPreference preference = (SaPreference) obj;
        return (this.elementName.equals(preference.elementName()));
    }
}
