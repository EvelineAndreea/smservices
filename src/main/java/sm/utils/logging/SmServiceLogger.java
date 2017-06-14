package sm.utils.logging;

import java.io.IOException;
import java.util.logging.*;

public class SmServiceLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static public void setup(String fileName) throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler)
            rootLogger.removeHandler(handlers[0]);

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler(fileName + " - Logging.txt");

        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}