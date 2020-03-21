package ch.unibas.game;//package ch.unibas;
import java.awt.Color;
//import

public class Card {

    public int number;
    public Color col;
    public String special;

    Card (int num, Color col){ // Constructor for a normal card with enum color, and a number
        number = num;
        this.col = col;
    }

    Card (Color col){ // Constructor for the special card (Skip Bo)
        this.number = 13;
        this.col = col;
    }



}
