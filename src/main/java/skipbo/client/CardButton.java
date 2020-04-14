package skipbo.client;

import javax.swing.*;
import java.util.ArrayList;

import static skipbo.client.SBClient.clientLog;

public class CardButton extends JButton {

    private ArrayList<ImageIcon> icons;
    private ArrayList<String> colors;
    private ArrayList<Integer> numbers;

    CardButton() {
        super();
        colors = new ArrayList<>();
        numbers = new ArrayList<>();
    }

    void addCard(String color, int number) {
        clientLog.debug("(CardButton) Color to be added: " + color);
        clientLog.debug("(CardButton) Number to be added: " + number);
        colors.add(color);
        numbers.add(number);
    }

    int getTopNumber() {
        if (numbers.size() > 0) {
            return numbers.get(numbers.size()-1);
        } else {
            return -1;
        }
    }

    String getTopColour() {
        if (colors.size() > 0) {
            return colors.get(colors.size()-1);
        } else {
            return null;
        }
    }

    String removeColour() {
        return colors.remove(0);
    }

    int removeNumber() {
        return numbers.remove(0);
    }

    void resetCards() {
        colors.clear();
        numbers.clear();
    }
}
