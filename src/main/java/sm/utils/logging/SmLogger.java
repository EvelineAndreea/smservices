package sm.utils.logging;

import java.io.IOException;
import java.util.logging.Logger;

import static sm.utils.Constants.ERROR_WITH_LOGGER;

public class SmLogger {
    private static String solverName;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);



    public static String start(boolean flag, String algorithmName, String solver) {
        solverName = solver;
        String message = "\n-------------" + algorithmName + " Matching Algorithm ";
        if (flag) {
            return message + "STARTED-------------\n";
        }
        else
            return message + "FINISHED-------------\n";
    }

    public static String logAction(String action, String man, String woman) {
        String message = solverName + man;
        switch (action.toLowerCase()) {
            case "proposal":
                message += " proposes to " + woman;
                break;
            case "rejection":
                message += " was rejected by " + woman;
                break;
            case "paired":
                message += " was paired with " + woman;
                break;
            case "unmatched":
                message += " is no longer matched with " + woman;
                break;
            default:
                message = solverName + " LOGGING PROBLEM";
        }
        return message;
    }


    public static void setupLogger(){
        try {
            SmServiceLogger.setup(solverName.substring(1,3));
        } catch (IOException e) {
            throw new RuntimeException(ERROR_WITH_LOGGER);
        }
    }
}
