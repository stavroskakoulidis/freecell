package com.company;

public class Card {
    private int suit;
    private  int value;

    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public Card() {
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
