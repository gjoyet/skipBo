package skipbo.client;

import javax.swing.*;
import java.util.ArrayList;

public class CardButton extends JButton {

    private ArrayList<ImageIcon> icons;
    private ArrayList<String> colors = new ArrayList<>();
    private ArrayList<Integer> numbers = new ArrayList<>();

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
}
