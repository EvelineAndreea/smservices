package sm.utils.comparators;

import sm.utils.model.Element;

import java.util.Comparator;

public class ElementComparator implements Comparator<Element> {
    @Override
    public int compare(Element first, Element second) {
        if (first.level() > second.level())
            return 1;
        if (first.level() < second.level())
            return -1;
        return 0;
    }
}
