package sm.utils.data;

import sm.utils.comparators.ElementComparator;
import sm.utils.model.Element;
import sm.utils.model.GroupElement;
import sm.utils.model.Problem;
import sm.utils.model.StableMatchingType;

import java.util.*;

import static java.lang.StrictMath.abs;
import static sm.utils.model.StableMatchingType.*;

public class ArtificialData {
    private Problem problem;
    private Set<String> firstSet;
    private Set<String> secondSet;
    private StableMatchingType type;
    private Random rand;

    public ArtificialData(StableMatchingType type) {
        this.type = type;
        rand = new Random();
    }

    public void generateSets() {
        int setSize = rand.nextInt(10) + 8;
        int secondSetSize = abs(setSize - rand.nextInt(10) - 1);
        if (setSize < secondSetSize)
        {
            int aux = setSize;
            setSize = secondSetSize;
            secondSetSize = aux;
        }
        System.out.println(setSize + " ---- " + secondSetSize);
        firstSet = new HashSet<>();
        secondSet = new HashSet<>();

        if (type.equals(SM))
            secondSetSize = setSize;

        for (int i = 0; i < setSize; i++) {
            String n1, n2;
            n1 = type.equals(HR) ? "w" : "f";
            n2 = type.equals(HR) ? "m" : "w";

            if (i < secondSetSize)
                firstSet.add(n1 + Integer.toString(i));
            secondSet.add(n2 + Integer.toString(i));
        }

        if (!type.equals(MM))
            return;

        int sz = secondSetSize < setSize ? secondSetSize : setSize;
        int maxGroupSize = (int) abs(Math.sqrt(rand.nextInt(sz) - 1)) + 2;
        String[] fSet = new String[firstSet.size()];
        int slen = 0, flen = 0;
        for (String f : firstSet) {
            fSet[flen++] = f;
        }
        String[] sSet = new String[secondSet.size()];
        for (String s : secondSet) {
            sSet[slen++] = s;
        }

        for (int i = 0; i < fSet.length; i++) {
            for (int j = i + 1; j < fSet.length - 1; j++) {
                StringBuilder gname = new StringBuilder("G").append(fSet[i]).append(" ");
                while (j < i + maxGroupSize) {
                    gname.append(fSet[j]).append(" ");
                    j++;
                }
                secondSet.add(gname.toString());
            }
        }

        for (int i = 0; i < sSet.length; i++) {
            for (int j = i + 1; j < sSet.length - 1; j++) {
                StringBuilder gname = new StringBuilder("G").append(sSet[i]).append(" ");
                while (j < i + maxGroupSize) {
                    gname.append(sSet[j]).append(" ");
                    j++;
                }
                firstSet.add(gname.toString());
            }
        }
    }

    public Element generatePreferences(String elementId, boolean flag) {
        int capacity;
        int preferenceListSize;
        List<String> otherSet = new ArrayList<>();

        if (flag)
            otherSet.addAll(firstSet);
        else
            otherSet.addAll(secondSet);


        /* If it's a normal SM set or if it's an element from a HR instance but of the set of the residents, the capacity should be 1 */
        if (type.equals(SM) || (type.equals(HR) && flag))
            capacity = 1;
        else
            capacity = abs(rand.nextInt(otherSet.size())) + 1;

        preferenceListSize = rand.nextInt(otherSet.size()) + 1;
        Element element = new Element(elementId, capacity);

        Collections.shuffle(otherSet);
        float level = 0;
        Queue<Element> prefs = new PriorityQueue<>(new ElementComparator());

        for (int i = 0; i < preferenceListSize; i++) {
            level += 1;
            String name = otherSet.get(i);
            if (!type.equals(MM) && !(name.charAt(0) == 'G')) {
                Element preference = new Element(name);
                preference.setLevel(level);
                prefs.add(preference);
            } else {
                GroupElement preference = new GroupElement(name);
                String[] names = name.substring(1, name.length() - 1).split(" ");
                for (String gname : names)
                    preference.addElemInGroup(gname);
                preference.setLevel(level);
                prefs.add(preference);
            }
            boolean tie = rand.nextBoolean();
            if (tie) {
                int noOfTieElements = rand.nextInt(preferenceListSize - i);
                noOfTieElements = (int) Math.ceil(noOfTieElements / 2);

                for (int j = 0; j < noOfTieElements; j++) {
                    i++;
                    Element otherPreference = new Element(otherSet.get(i));
                    otherPreference.setLevel(level);
                    prefs.add(otherPreference);
                }
            }

            element.setPreferences(prefs);
        }
        return element;
    }

    public void createInstance() {
        problem = new Problem();
        problem.setProblemName(type.toString());
        generateSets();

        sm.utils.model.Set set2 = new sm.utils.model.Set("second-set");
        for (String elem : secondSet) {
            if (elem.charAt(0) == 'G')
                continue;
            set2.insertElement(generatePreferences(elem, true));
        }
        problem.insertSet(set2);

        sm.utils.model.Set set1 = new sm.utils.model.Set("first-set");
        for (String elem : firstSet) {
            if (elem.charAt(0) == 'G')
                continue;
            set1.insertElement(generatePreferences(elem, false));
        }
        problem.insertSet(set1);
    }

    public Problem getProblem() {
        return problem;
    }

}
