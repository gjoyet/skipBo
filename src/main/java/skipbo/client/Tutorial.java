package skipbo.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tutorial extends GameGraphic implements ActionListener {

    int moveNumber = 0;
    JTextArea instruction = new JTextArea();
    Font arrowFont = new Font(DEFAULTFONT.getName(), Font.BOLD, 35);
    Font tiltedArrowFont = new Font(DEFAULTFONT.getName(), Font.BOLD, 55);
    JLabel leftArrow = new JLabel("\u2B05");
    JLabel downArrow = new JLabel("\u2B07");
    JLabel upArrow = new JLabel("\u2B06");
    JLabel northEast = new JLabel("\u2B08");
    JLabel northWest = new JLabel("\u2B09");
    JLabel southEast = new JLabel("\u2B0A");
    JLabel southWest = new JLabel("\u2B0B");

    Tutorial() {
        super();
        actionListener = this;
        setArrowFonts();
        appendDecks();
        adjustGameGraphic();
        firstMove();
    }

    private void adjustGameGraphic() {
        layeredPane.add(instruction);
        e1.setText("Bob");
        e2.setText("Alice");
        setOpponentVisible(0);
        setOpponentVisible(1);
        yourTurnLabel.setVisible(true);
        initialNumStockCards = 3;
        numOfStockCards.setText(initialNumStockCards + " cards left");
        oppNumStockCards[0].setText(String.valueOf(initialNumStockCards));
        oppNumStockCards[1].setText(String.valueOf(initialNumStockCards));
        layeredPane.add(oppNumStockCards[0]);
        layeredPane.add(oppNumStockCards[1]);

        hand[0].setIcon(cardIcons.getIcon("R",10, CardIcons.MEDIUM));
        hand[1].setIcon(cardIcons.getIcon("B", 1, CardIcons.MEDIUM));
        hand[2].setIcon(cardIcons.getIcon("B",7, CardIcons.MEDIUM));
        hand[3].setIcon(cardIcons.getIcon("G", 10, CardIcons.MEDIUM));
        hand[4].setIcon(cardIcons.getIcon("R",12, CardIcons.MEDIUM));
        stock.setIcon(cardIcons.getIcon("R",2, CardIcons.LARGE));

        e1_stock.setIcon(cardIcons.getIcon("B",12, CardIcons.SMALL));
        e2_stock.setIcon(cardIcons.getIcon("G",8, CardIcons.SMALL));

        instruction.setBackground(Color.orange);
        instruction.setFont(new Font(DEFAULTFONT.getName(), Font.BOLD, DEFAULTFONT.getSize()+3));
        instruction.setForeground(ChatGraphic.DARKGREEN);
        instruction.setEditable(false);

    }

    private void firstMove() {
        upArrow.setBounds(400, 495, 50, 50);
        layeredPane.add(upArrow);
        instruction.setBounds(440, 500, 300, 100);
        instruction.setText("Your goal is to play all of\nyour stock cards on the build piles.");
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    private void setArrowFonts() {
        leftArrow.setFont(arrowFont);
        upArrow.setFont(arrowFont);
        downArrow.setFont(arrowFont);
        northEast.setFont(tiltedArrowFont);
        northWest.setFont(tiltedArrowFont);
        southEast.setFont(tiltedArrowFont);
        southWest.setFont(tiltedArrowFont);

        leftArrow.setForeground(ChatGraphic.DARKGREEN);
        upArrow.setForeground(ChatGraphic.DARKGREEN);
        downArrow.setForeground(ChatGraphic.DARKGREEN);
        northEast.setForeground(ChatGraphic.DARKGREEN);
        northWest.setForeground(ChatGraphic.DARKGREEN);
        southEast.setForeground(ChatGraphic.DARKGREEN);
        southWest.setForeground(ChatGraphic.DARKGREEN);
    }
}
