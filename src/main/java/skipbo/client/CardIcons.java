package skipbo.client;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

class CardIcons {

    private ImageIcon[][] icons;

    CardIcons(int widthSmall, int heightSmall, int widthMedium, int heightMedium) {

        ImageIcon[] rL = new ImageIcon[12];
        ImageIcon[] gL = new ImageIcon[12];
        ImageIcon[] bL = new ImageIcon[12];

        ImageIcon[] rM = new ImageIcon[12];
        ImageIcon[] gM = new ImageIcon[12];
        ImageIcon[] bM = new ImageIcon[12];

        ImageIcon[] rS = new ImageIcon[12];
        ImageIcon[] gS = new ImageIcon[12];
        ImageIcon[] bS = new ImageIcon[12];

        ImageIcon[] skipbo = new ImageIcon[3];

        Image image;
        for (int i = 1; i <= 12; i++) {
            rL[i-1] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("R" + i + ".png")));
            image = rL[i-1].getImage().getScaledInstance(widthMedium, heightMedium, Image.SCALE_SMOOTH);
            rM[i-1] = new ImageIcon(image);
            image = rL[i-1].getImage().getScaledInstance(widthSmall, heightSmall, Image.SCALE_SMOOTH);
            rS[i-1] = new ImageIcon(image);

            gL[i-1] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("G" + i + ".png")));
            image = gL[i-1].getImage().getScaledInstance(widthMedium, heightMedium, Image.SCALE_SMOOTH);
            gM[i-1] = new ImageIcon(image);
            image = gL[i-1].getImage().getScaledInstance(widthSmall, heightSmall, Image.SCALE_SMOOTH);
            gS[i-1] = new ImageIcon(image);

            bL[i-1] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("B" + i + ".png")));
            image = bL[i-1].getImage().getScaledInstance(widthMedium, heightMedium, Image.SCALE_SMOOTH);
            bM[i-1] = new ImageIcon(image);
            image = bL[i-1].getImage().getScaledInstance(widthSmall, heightSmall, Image.SCALE_SMOOTH);
            bS[i-1] = new ImageIcon(image);

        }

        skipbo[0] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("skipbo.png")));
        image = skipbo[0].getImage().getScaledInstance(widthMedium, heightMedium, Image.SCALE_SMOOTH);
        skipbo[1] = new ImageIcon(image);
        image = skipbo[0].getImage().getScaledInstance(widthSmall, heightSmall, Image.SCALE_SMOOTH);
        skipbo[2] = new ImageIcon(image);

        icons = new ImageIcon[][]{rL, rM, rS, gL, gM, gS, bL, bM, bS, skipbo};

    }

    ImageIcon getIcon(String color, int number, String size) {

        if (color == null || number == -1) {
            return null;
        }

        int i = 0;

        switch (color) {
            case "R":
                i = 0;
                break;
            case "G":
                i = 3;
                break;
            case "B":
                i = 6;
                break;
        }

        if (size.equals("M")) {
            i++;
        } else if (size.equals("S")) {
            i = i + 2;
        }

        if (number == 13) {
            number = ++i;
            i = 9;
        }

        return icons[i][number-1];
    }
}
