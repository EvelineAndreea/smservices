package sm.utils.comparators;

import sm.utils.model.SaPreference;

import java.util.Comparator;

public class PreferenceComparator implements Comparator<SaPreference> {
    @Override
    public int compare(SaPreference first, SaPreference second) {
        if (first.level() > second.level())
            return 1;
        if (first.level() < second.level())
            return -1;
        return 0;
    }
}