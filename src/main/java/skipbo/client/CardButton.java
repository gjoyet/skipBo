package skipbo.client;

import javax.swing.*;
import java.util.ArrayList;

public class CardButton extends JButton {

    ArrayList<ImageIcon> icons;
    ArrayList<String> colors = new ArrayList<>();
    ArrayList<Integer> numbers = new ArrayList<>();

    CardButton(Icon icon) {
        super(icon);
    }

    CardButton() {
        super();
    }

    void addCard(String color, int number) {
        colors.add(color);
        numbers.add(number);
    }

    String removeColour() {
        return colors.remove(0);
    }

    int removeNumber() {
        return numbers.remove(0);
    }
}
