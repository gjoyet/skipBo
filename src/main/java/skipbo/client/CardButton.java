package skipbo.client;

import javax.swing.*;
import java.util.ArrayList;

public class CardButton extends JButton {

    ArrayList<ImageIcon> icons;

    CardButton(Icon icon) {
        super(icon);
    }

    CardButton() {
        super();
    }
}
