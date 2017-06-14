package sm.representation;

import java.applet.Applet;
import java.awt.*;

public class DrawMan extends Applet {
    public void paint(Graphics page) {
        setBackground(Color.white);

    }

    public void drawPerson(Graphics page, int formerX, int formerY, int humanNumber) {
        setForeground(Color.red);

        // the head
        page.drawOval(90,60,20,20);
//        page.drawOval(formerX + humanNumber * 10, formerY + humanNumber * 10, 20, 20);

        // the body
        page.drawLine(100, 80, 100, 120);
//        page.drawLine(formerX + 10 + humanNumber * 10, );

        // the hands
        page.drawLine(100, 100, 80, 100);
        page.drawLine(100, 100, 120, 100);

        // the legs
        page.drawLine(100, 120, 85, 135);
        page.drawLine(100, 120, 115, 135);
    }
}