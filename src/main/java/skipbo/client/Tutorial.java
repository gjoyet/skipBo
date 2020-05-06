package skipbo.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Tutorial extends GameGraphic implements ActionListener {

    ChatGraphic chatGraphic;
    int moveNumber = 1;
    boolean allowMove = false;
    JTextArea instruction = new JTextArea();
    CardButton chosenBuildButton;

    Font arrowFont = new Font(DEFAULTFONT.getName(), Font.BOLD, 35);
    Font tiltedArrowFont = new Font(DEFAULTFONT.getName(), Font.BOLD, 55);

    JLabel leftArrow = new JLabel("\u2B05");
    JLabel downArrow = new JLabel("\u2B07");
    JLabel upArrow = new JLabel("\u2B06");
    JLabel northEast = new JLabel("\u2B08");
    JLabel northWest = new JLabel("\u2B09");
    JLabel southEast = new JLabel("\u2B0A");
    JLabel southWest = new JLabel("\u2B0B");

    Tutorial(ChatGraphic chatGraphic) {
        super();
        this.chatGraphic = chatGraphic;
        actionListener = this;
        setArrowFonts();
        appendDecks();
        adjustGameGraphic();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                firstMove();
            }
        };
        timer.schedule(task,1000);
    }

    private void adjustGameGraphic() {
        yourTurnLabel.setBounds(657, 0, 600,100);
        layeredPane.add(instruction, Integer.valueOf(-1));
        e1.setText("Bob");
        e2.setText("Alice");
        setOpponentVisible(0);
        setOpponentVisible(1);
        yourTurnLabel.setVisible(true);
        initialNumStockCards = 2;
        numOfStockCards.setText(initialNumStockCards + " cards left");
        oppNumStockCards[0].setText(String.valueOf(initialNumStockCards));
        oppNumStockCards[1].setText(String.valueOf(initialNumStockCards));
        layeredPane.add(oppNumStockCards[0]);
        layeredPane.add(oppNumStockCards[1]);

        hand[0].setIcon(cardIcons.getIcon("R",10, CardIcons.MEDIUM));
        hand[1].setIcon(cardIcons.getIcon("B",7, CardIcons.MEDIUM));
        hand[2].setIcon(cardIcons.getIcon("G", 10, CardIcons.MEDIUM));
        hand[3].setIcon(cardIcons.getIcon("B", 1, CardIcons.MEDIUM));
        hand[4].setIcon(cardIcons.getIcon("R",12, CardIcons.MEDIUM));
        stock.setIcon(cardIcons.getIcon("R",2, CardIcons.LARGE));

        e1_stock.setIcon(cardIcons.getIcon("B",12, CardIcons.SMALL));
        e2_stock.setIcon(cardIcons.getIcon("G",8, CardIcons.SMALL));

        instruction.setBackground(Color.orange);
        instruction.setFont(new Font(DEFAULTFONT.getName(), Font.BOLD, DEFAULTFONT.getSize()+3));
        instruction.setForeground(ChatGraphic.DARKGREEN);
        instruction.setEditable(false);

    }

    /**
     * Introduces the player to the game. Asks to click on hand card 2.
     */
    private void firstMove() {
        try {
            upArrow.setBounds(400, 495, 50, 50);
            layeredPane.add(upArrow);
            instruction.setBounds(440, 500, 300, 100);
            instruction.setText("Your goal is to play all of\nyour stock cards on the build piles.");
            Thread.sleep(1000); //5000
            layeredPane.remove(upArrow);
            layeredPane.repaint();
            downArrow.setBounds(831, 575, 50, 50);
            layeredPane.add(downArrow, Integer.valueOf(-1));
            instruction.setBounds(861, 580, 300, 100);
            instruction.setText("Click on your hand card to grab it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allowMove = true;
    }

    /**
     *Asks player to click on a build pile.
     */
    private void secondMove() {
        moveNumber = 2;
        layeredPane.remove(downArrow);
        layeredPane.repaint();
        downArrow.setBounds(680, 75, 50, 50);
        layeredPane.add(downArrow);
        instruction.setBounds(710, 80, 400, 100);
        instruction.setText("Click on any build pile to put play the one.");
        allowMove = true;
    }

    /**
     *Puts the one on the build pile and asks player to click on stock pile.
     */
    private void thirdMove() {
        moveNumber = 3;
        chosenBuildButton.setIcon(cardIcons.getIcon("B", 1, CardIcons.LARGE));
        hand[3].setIcon(hand[4].getIcon());
        hand[4].setIcon(null);
        layeredPane.remove(downArrow);
        layeredPane.repaint();
        upArrow.setBounds(400, 495, 50, 50);
        layeredPane.add(upArrow);
        instruction.setBounds(440, 500, 400, 100);
        instruction.setText("Now you can play your first stock card\non the build pile.");
        allowMove = true;
    }

    private void fourthMove() {
        moveNumber = 4;
        layeredPane.remove(upArrow);
        layeredPane.repaint();
        instruction.setText("Put the stock card on the build pile.");
        if (chosenBuildButton == build[0]) {
            downArrow.setBounds(chosenBuildButton.getX()+65,75,50,50);
            instruction.setBounds(chosenBuildButton.getX()+95,80,400,100);
        } else {
            downArrow.setBounds(chosenBuildButton.getX()+20,75,50,50);
            instruction.setBounds(chosenBuildButton.getX()+50,80,400,100);
        }
        layeredPane.add(downArrow);
        allowMove = true;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!allowMove) {
            return;
        }
        CardButton buttonPressed = (CardButton) actionEvent.getSource();
        if (moveNumber == 1 && buttonPressed != hand[3]) {
            return;
        } else if (moveNumber == 1) {
            allowMove = false;
            buttonPressed.setBorder(clickedBorder);
            changeButtonStates(buttonPressed, false);
            secondMove();
        }
        if (moveNumber == 2 && !isBuildButton(buttonPressed)) {
            return;
        } else if (moveNumber == 2) {
            allowMove = false;
            chosenBuildButton = buttonPressed;
            hand[3].setBorder(defaultBorder);
            changeButtonStates(hand[3], true);
            thirdMove();
        }
        if (moveNumber == 3 && buttonPressed != stock) {
            return;
        } else if (moveNumber == 3) {
            allowMove = false;
            buttonPressed.setBorder(clickedBorder);
            changeButtonStates(buttonPressed, false);
            fourthMove();
        }
        //chatGraphic.endGame("You");
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

    private boolean isBuildButton(CardButton button) {
        for (int i = 0; i < build.length; i++) {
            if (button == build[i]) {
                return true;
            }
        }
        return false;
    }
}
