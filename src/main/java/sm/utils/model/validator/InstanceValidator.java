package sm.utils.model.validator;

import sm.utils.model.StableMatchingType;

public class InstanceValidator {
    public static boolean isAValidProblemType(String type){
        for (StableMatchingType smType: StableMatchingType.values())
            if (type.equals(smType.toString()))
                return true;
        return false;
    }

    public static boolean checkSize(float size) {
        return size > 0;
    }

    public static boolean verifyLevel(float level, int maxSize){
        return (level > 0) && (level < (maxSize + 1));
    }
}
