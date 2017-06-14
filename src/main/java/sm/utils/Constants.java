package sm.utils;

public final class Constants {
    public static final String XML_ERROR_PREFIX = "[XML][ERROR] ";

    public static final String GS_ALGORITHM_NAME = "Gale Shapley for one-to-one instance";
    public static final String HR_ALGORITHM_NAME = "Gale Shapley for one-to-many instance";
    public static final String MM_ALGORITHM_NAME = "Deferred-Acceptance algorithm for many-to-many instance";
    public static final String K_ALGORITHM_NAME = "Kiraly for the Stable Matching instance";
    public static final String SA_ALGORITHM_NAME = "Stable - Allocation solver";

    public static final int MAX_SIZE = 200;
    public static final int MAX_SIZE_FOR_GROUP_ELEMENTS = 1000;
    public static final int MAX_LEVEL_VALUE = 1000000000;

    public static final String ERROR_INVALID_PROBLEM_TYPE = "There is no algorithm for solving this type of problem";
    public static final String ERROR_NO_ELEMENT_FOUND = "There is no element defined in the set tag as:";
    public static final String ERROR_INVALID_MAXUNITS_VALUE = "Invalid maxunits value for";
    public static final String ERROR_INVALID_SIZE = "Invalid size for set";
    public static final String ERROR_INVALID_CAPACITY = "Invalid capacity for";
    public static final String ERROR_INVALID_UNITS_VALUE = "Invalid units value for";
    public static final String ERROR_ELEMENT_NAME = "Every element needs a proper name.";
    public static final String ERROR_COUPLE_DEFINITION = "The following elements are not defined in the same set to be a couple:";
    public static final String ERROR_NAME_TAKEN = "Elements should have different ids. Already defined element ";
    public static final String ERROR_NO_NAME = "No name specified";
    public static final String ERROR_INVALID_LEVEL = "Invalid level value";
    public static final String ERROR_WITH_LOGGER = "Problems with making the logger file";
    public static final String ERROR_INVALID_PREFERENCE_LIST = "A group MUST have only one set of preferences. The following elements are both defined to have list of preferences but they are in the same group:";
}
