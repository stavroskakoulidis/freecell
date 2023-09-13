package com.company;

public class MoveTracker {
    public int move;
    public Card card1;
    public Card card2;

    public MoveTracker(int move, Card card1, Card card2) {
        this.move = move;
        this.card1 = card1;
        this.card2 = card2;
    }

    public MoveTracker() {
        this.card1 = new Card();
        this.card2 = new Card();
    }
}
