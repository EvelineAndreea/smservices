package tests;


import org.xml.sax.SAXException;
import sm.utils.model.Problem;
import sm.utils.parsers.SMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class BaseTest {
    protected Problem problem;

    protected void load(String path){
        SMParser parser = null;
        try {
            parser = new SMParser(path);
            parser.read();

            problem = parser.getProblem();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}
