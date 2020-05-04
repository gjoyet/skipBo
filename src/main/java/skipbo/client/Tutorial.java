package skipbo.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tutorial extends GameGraphic implements ActionListener {

    Tutorial() {
        super();
        actionListener = this;
        appendDecks();
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
