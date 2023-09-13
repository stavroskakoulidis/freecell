package com.company;

import com.sun.deploy.security.SelectableSecurityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Board {

    public ArrayList<Card>[] stacks;
    public ArrayList<Card>[] foundations;
    public ArrayList<Card> freeCells;


    public Board() {
            stacks = new ArrayList[8];
            foundations = new ArrayList[4];
            freeCells = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            stacks[i] = new ArrayList<>();
        }

        for (int i = 0; i < 4; i++) {
            Card card = new Card();
            card.setValue(-1);
            if (i<2){
                card.setSuit(i);
            }else{
                card.setSuit(i+1);
            }

            foundations[i] = new ArrayList<>();
            foundations[i].add(card);
        }
    }

    public boolean isGameWon(){

        /*int i=0;

        for (int j=0; j<4; j++){
            if (foundations[Constants.N-1].get(j).getValue() == Constants.N-1){
                i++;
            }
        }
        return i == 4;*/

        for (ArrayList<Card> foundation: foundations) {
            if (foundation.size() != Constants.N+1) {
                return false;
            }
        }
        return true;
    }

    public Board copyBoard(){

        Board copyBoard = new Board();

        // Copy stacks
        copyBoard.stacks = new ArrayList[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            copyBoard.stacks[i] = new ArrayList<>(stacks[i]);
        }

        // Copy foundations
        copyBoard.foundations = new ArrayList[foundations.length];
        for (int i = 0; i < foundations.length; i++) {
            copyBoard.foundations[i] = new ArrayList<>(foundations[i]);
        }

        // Copy free cells
        copyBoard.freeCells = new ArrayList<>(freeCells);

        return copyBoard;
    }


    public boolean equalsBoard(Board board) {
        for (int i = 0; i<8; i++){
            if (this.stacks[i].size() == board.stacks[i].size()){
                for (int j=0; j<board.stacks[i].size();j++){
                    if (this.stacks[i].get(j).getSuit() != board.stacks[i].get(j).getSuit() ||
                            this.stacks[i].get(j).getValue() != board.stacks[i].get(j).getValue()){
                        return false;
                    }
                }
            }else {
                return false;
            }
        }

        for (int i = 0; i<4; i++){
            if (this.foundations[i].size() == board.foundations[i].size()){
                for (int j=0; j<board.foundations[i].size();j++){
                    if (this.foundations[i].get(j).getSuit() != board.foundations[i].get(j).getSuit() ||
                            this.foundations[i].get(j).getValue() != board.foundations[i].get(j).getValue()){
                        return false;
                    }
                }
            }else {
                return false;
            }
        }

        if (this.freeCells.size() == board.freeCells.size()){
            for (int i = 0; i<this.freeCells.size(); i++){
                if (this.freeCells.get(i).getSuit() != board.freeCells.get(i).getSuit() ||
                        this.freeCells.get(i).getValue() != board.freeCells.get(i).getValue()){
                    return false;
                }
            }
        }else {
            return false;
        }
        return true;
    }

    public int heuristic(){

        int heuristicValue = 0;

        // Estimate the number of cards in their final foundation piles
        for (ArrayList<Card> foundation : foundations) {
            for (Card card : foundation) {
                heuristicValue++;
            }
        }

        // Add the number of free freeCells
        heuristicValue+= 4 - freeCells.size();

        for (ArrayList<Card> stack : stacks) {
            if (stack.isEmpty()) {
                heuristicValue++;
            } else {


                // Check dependencies between cards
                int dependencies = countCardDependencies(stack);
                heuristicValue -= dependencies;
            }
        }


        return heuristicValue;

    }

    private int countCardDependencies(ArrayList<Card> stack) {
        int dependencies = 0;

        for (int i = 0; i < stack.size(); i++) {
            Card currentCard = stack.get(i);

            // Check if the card depends on cards above it
            for (int j = i + 1; j < stack.size(); j++) {
                Card upperCard = stack.get(j);

                // Cards of the same suit or higher rank block the current card
                if (currentCard.getSuit() == upperCard.getSuit() ||
                        currentCard.getValue() < upperCard.getValue()) {
                    dependencies++;
                }
            }
        }

        return dependencies;
    }

}
