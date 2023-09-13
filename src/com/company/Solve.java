package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solve {

    public static void main(String[] args) {

        Board brd = new Board();
        LinkedList<TreeNode> queue = new LinkedList<>();

        int method;
        String filePath = args[1];
        String outPutFile = args[2];

        if (args.length !=3){
            System.out.println("Wrong number of arguments. Use correct syntax: ");
            syntax_message();
            return;
        }
        
        method = getMethod(args[0]);
        if (method == -1){
            System.out.println("Wrong method. Use correct syntax:");
            syntax_message();
            return;
        }

        inflateBoard(brd, filePath);

        initializeSearch(brd, queue);

        TreeNode solution = search(queue, method);

        if (solution != null){
            extract_solution(solution, outPutFile);
        }else {
            System.out.println("No solution found!");
        }


    }

    private static void extract_solution(TreeNode solution, String outPutFile) {

        ArrayList<MoveTracker> moves = new ArrayList<>();

        TreeNode tempNode = solution;

        while (tempNode.parent != null){
            moves.add(tempNode.tracker);
            tempNode = tempNode.parent;
        }

        Collections.reverse(moves);

        String filePath = "Generated_Games/" + outPutFile;

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("Solution Length is " + solution.d + ".\n");

            for (MoveTracker move: moves){
                writer.write(extractMove(move) + ".\n");
            }

            writer.close();
        } catch (IOException exception) {

            exception.printStackTrace();
        }



    }

    private static String extractCard(Card card) {
        if (card.getSuit() == 0){
            return "H" + card.getValue();
        }else if (card.getSuit() == 1){
            return "D" + card.getValue();
        }else if (card.getSuit() == 3){
            return "S" + card.getValue();
        }else if (card.getSuit() == 4){
            return "C" + card.getValue();
        }
        return "Error";
    }

    private static String extractMove(MoveTracker move) {

        String moveBuilder = null;

        if (move.move == 1){
            moveBuilder = "FreeCell ";
        }else if (move.move == 2){
            moveBuilder = "Stack ";
        }else if (move.move == 4){
            moveBuilder = "Foundation ";
        }

        moveBuilder += extractCard(move.card1);

        if (move.card2 != null){
            moveBuilder +=  " " + extractCard(move.card2);
        }

        return moveBuilder;
    }

    private static TreeNode search(LinkedList<TreeNode> queue, int method) {
        int k =0;

        while (!queue.isEmpty()) {

            TreeNode currentNode = queue.poll();
            Board currentBoard = currentNode.board;
            /*for (int i = 0; i<8; i++) {
                for (int j = 0; j < currentNode.board.stacks[i].size(); j++) {
                    System.out.println("CARD === >" + currentNode.board.stacks[i].get(j).getValue() + " --- " + currentNode.board.stacks[i].get(j).getSuit());
                }
                System.out.println("---------------------------------------");
            }*/

            if (currentBoard.isGameWon()) {
                return currentNode; // Solution found
            }

            /*if (k==15){
                System.out.println("NEW Children  = " + queue.size());

                break;

            }
            System.out.println("********************************************************* " + currentNode.h +
                    " freeCells === > " + currentNode.board.freeCells.size() + " foundation ==> " + currentNode.board.foundations[0].size());*/

            /*for (int i =0;i<4;i++){
                for (int j=0;j<currentNode.board.foundations[i].size();j++){
                    System.out.println("Foundation "  + currentNode.board.foundations[i].get(j).getValue() + " --- " + currentNode.board.foundations[i].get(j).getSuit());
                }
                System.out.println("---------------------------------------");
            }*/

            findChildren(queue, currentNode, method);
            k++;

        }
        return null;
    }


    private static void findChildren(LinkedList<TreeNode> queue, TreeNode currentNode, int method) {

        ArrayList<Board> childBoards = new ArrayList<>();
        ArrayList<MoveTracker> trackers = new ArrayList<>();
        boolean cardFromStackMoved = false; // if false, that means that a card can only be moved to a freeCell.

        for (int fromStack = 0; fromStack < 8; fromStack++) {
            ArrayList<Card> stackFrom = currentNode.board.stacks[fromStack];
            if (!stackFrom.isEmpty()){
                for (int toStack = 0; toStack < 8; toStack++) {
                    ArrayList<Card> stackTo = currentNode.board.stacks[toStack];
                    if (isValidMoveToStack(stackFrom, stackTo)){
                        MoveTracker tracker = new MoveTracker();
                        tracker.move = Constants.STACK;
                        tracker.card1 = stackFrom.get(stackFrom.size()-1);
                        if (stackTo.size() != 0){
                            tracker.card2 = stackTo.get(stackTo.size()-1);
                        }else{
                            tracker.card2 = null;
                        }
                        trackers.add(tracker);
                        Board newBoard = applyMove(currentNode.board, fromStack, toStack, Constants.STACK);
                        childBoards.add(newBoard);
                        cardFromStackMoved = true;
                    }
                }
                for (int toFoundation = 0; toFoundation < 4; toFoundation++){
                    ArrayList<Card> foundationTo = currentNode.board.foundations[toFoundation];
                    if (isValidMoveToFoundation(stackFrom.get(stackFrom.size() - 1), foundationTo)){
                        MoveTracker tracker = new MoveTracker();
                        tracker.move = Constants.FOUNDATION;
                        tracker.card1 = stackFrom.get(stackFrom.size()-1);
                        tracker.card2 = null;
                        trackers.add(tracker);
                        Board newBoard = applyMove(currentNode.board, fromStack, toFoundation, Constants.FOUNDATION);
                        childBoards.add(newBoard);
                        cardFromStackMoved = true;
                    }
                }
            }
        }

        if (!cardFromStackMoved){
            for (int fromStack = 0; fromStack < 8; fromStack++) {
                ArrayList<Card> stackFrom = currentNode.board.stacks[fromStack];
                if (!stackFrom.isEmpty()){
                    if(currentNode.board.freeCells.size() < 4){
                        MoveTracker tracker = new MoveTracker();
                        tracker.move = Constants.FREECELL;
                        tracker.card1 = stackFrom.get(stackFrom.size()-1);
                        tracker.card2 = null;
                        trackers.add(tracker);
                        Board newBoard = applyMove(currentNode.board, fromStack, -1, Constants.FREECELL);
                        childBoards.add(newBoard);
                    }
                }
            }
        }

        for (Card freeCellCard : currentNode.board.freeCells) {
            for (int toStack = 0; toStack < 8; toStack++) {
                ArrayList<Card> stackTo = currentNode.board.stacks[toStack];
                if (isValidMoveToStack(stackTo, freeCellCard)){
                    MoveTracker tracker = new MoveTracker();
                    tracker.move = Constants.STACK;
                    tracker.card1 = freeCellCard;
                    if (stackTo.size() != 0){
                        tracker.card2 = stackTo.get(stackTo.size()-1);
                    }else{
                        tracker.card2 = null;
                    }
                    trackers.add(tracker);
                    Board newBoard = applyMove(currentNode.board, freeCellCard, toStack, Constants.STACK);
                    childBoards.add(newBoard);

                }
            }
            for (int toFoundation = 0; toFoundation < 4; toFoundation++){
                ArrayList<Card> foundationTo = currentNode.board.foundations[toFoundation];
                if (isValidMoveToFoundation(freeCellCard,foundationTo)){
                    MoveTracker tracker = new MoveTracker();
                    tracker.move = Constants.FOUNDATION;
                    tracker.card1 = freeCellCard;
                    tracker.card2 = null;
                    trackers.add(tracker);
                    Board newBoard = applyMove(currentNode.board, freeCellCard, toFoundation, Constants.FOUNDATION);
                    childBoards.add(newBoard);
                }
            }
        }

        for (int i = 0; i< childBoards.size(); i++){
            TreeNode childNode = new TreeNode(childBoards.get(i), 0, currentNode.d+1, 0, trackers.get(i), currentNode);
            /*childNode.board = childBoards.get(i);
            childNode.parent = currentNode;
            childNode.d = currentNode.d + 1;
            childNode.f = 0;
            childNode.h = 0;
            childNode.tracker = trackers.get(i);*/

            /*System.out.println("CHILDREN ARE === " + i);
            System.out.println("MOVE is === " + trackers.get(i).move);
            System.out.println("card 1 is === " + trackers.get(i).card1.getValue() + " -- "+  trackers.get(i).card1.getSuit());
            if (trackers.get(i).card2 != null){
                System.out.println("card 2 is === " + trackers.get(i).card2.getValue() + " -- "+  trackers.get(i).card2.getSuit());
            }*/

            if (check_with_parents(childNode)){

                if (method == Constants.DEPTH){
                    queue.addFirst(childNode);
                }else if (method == Constants.BREADTH){
                    queue.addLast(childNode);
                }else if (method == Constants.ASTAR){
                    childNode.h = childNode.board.heuristic();
                    childNode.f = childNode.h + childNode.d;
                    int index = findIndex(queue, childNode.f);
                    queue.add(index, childNode);
                }else if (method == Constants.BEST){
                    childNode.h = childNode.board.heuristic();
                    childNode.f = childNode.h;
                    int index = findIndex(queue, childNode.f);
                    queue.add(index, childNode);
                }

            }

        }

        /*for (int i = 0; i<8; i++){
            for (int j=0; j<currentNode.board.stacks[i].size();j++){
                System.out.println("CARD === >" + currentNode.board.stacks[i].get(j).getValue() + " --- " + currentNode.board.stacks[i].get(j).getSuit());
            }
            System.out.println("---------------------------------------");

        }*/


    }

    private static int findIndex(LinkedList<TreeNode> queue, int heuristicValue) {

        int index = 0;

        for (TreeNode node: queue){
            if (node.h < heuristicValue){
                break;
            }
            index++;
        }
        return index;

    }

    private static boolean check_with_parents(TreeNode childNode) {
        TreeNode parent  = childNode.parent;
        while (parent!=null){
            if (childNode.board.equalsBoard(parent.board)){
                return false;
            }
            parent = parent.parent;
        }
        return true;
    }



    private static Board applyMove(Board board, Card freeCellCard, int toStack, int target) {

        Board newBoard = board.copyBoard();

        ArrayList<Card> toPile  = null;

        if (target == Constants.STACK){
            toPile = newBoard.stacks[toStack];
        }else if (target == Constants.FOUNDATION){
            toPile = newBoard.foundations[toStack];
        }
        toPile.add(freeCellCard);
        newBoard.freeCells.remove(freeCellCard);

        return newBoard;

    }

    private static boolean isValidMoveToStack(ArrayList<Card> stack, Card freeCellCard) {

        if (stack.isEmpty()){
            return true;
        }

        Card topCardTo = stack.get(stack.size() - 1);
        if (freeCellCard.getValue() - topCardTo.getValue() == -1 &&
                (freeCellCard.getSuit() - topCardTo.getSuit() > 1 || freeCellCard.getSuit() - topCardTo.getSuit() < -1)){
            return true;
        }
        return false;
    }


    private static boolean isValidMoveToFoundation(Card cardFrom, ArrayList<Card> foundationTo) {

        Card topCardFoundation = foundationTo.get(foundationTo.size()-1);

        if (cardFrom.getSuit() == topCardFoundation.getSuit() && topCardFoundation.getValue() - cardFrom.getValue() == -1){
            return true;
        }
        return false;
    }

    private static Board applyMove(Board board, int from, int to, int target) {

        Board newBoard = board.copyBoard();

        ArrayList<Card> toPile = null;
        ArrayList<Card> fromPile  = newBoard.stacks[from];


        if (target == Constants.STACK){
             toPile = newBoard.stacks[to];
        } else if (target == Constants.FOUNDATION){
            toPile = newBoard.foundations[to];
        }else if (target == Constants.FREECELL){
            toPile = newBoard.freeCells;
        }


        Card movedCard = fromPile.remove(fromPile.size() - 1);
        toPile.add(movedCard);

        return newBoard;

    }

    private static boolean isValidMoveToStack(ArrayList<Card> stackFrom, ArrayList<Card> stackTo) {

        if (stackTo.isEmpty()){
            return true;
        }else {
            Card topCardFrom = stackFrom.get(stackFrom.size()-1);
            Card topCardTo = stackTo.get(stackTo.size()-1);

            if (topCardFrom.getValue() - topCardTo.getValue() == -1 &&
                    (topCardFrom.getSuit() - topCardTo.getSuit() > 1 || topCardFrom.getSuit() - topCardTo.getSuit() < -1)){
                return true;
            }
        }
        return false;
    }


    private static void initializeSearch(Board board, Queue<TreeNode> queue) {

        TreeNode rootNode = new TreeNode();
        MoveTracker tracker = new MoveTracker();

        tracker.card1.setSuit(-1);
        tracker.card1.setValue(-1);
        tracker.card2.setSuit(-1);
        tracker.card2.setValue(-1);
        tracker.move = -1;

        /*rootNode.tracker.card1.setSuit(-1);
        rootNode.tracker.card1.setValue(-1);
        rootNode.tracker.card2.setSuit(-1);
        rootNode.tracker.card2.setValue(-1);
        rootNode.tracker.move = -1;*/
        rootNode.tracker =tracker;
        rootNode.parent = null;
        rootNode.board = board;
        rootNode.d = 0;
        rootNode.f = 0;
        rootNode.h = rootNode.board.heuristic();

        queue.offer(rootNode);

    }


    private static void inflateBoard(Board brd, String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] cards = line.split(" ");
                for (String strCard : cards) {
                    if (strCard.length() == 2){
                        Card card = new Card();
                        createCard(card, strCard.charAt(0), Integer.parseInt(String.valueOf(strCard.charAt(1))));
                        brd.stacks[i].add(card);
                    }else if (strCard.length() == 3){
                        Card card = new Card();
                        createCard(card, strCard.charAt(0), 10+Integer.parseInt(String.valueOf(strCard.charAt(2))));
                        brd.stacks[i].add(card);
                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void createCard(Card card, char c, int number) {

        if (c == 'H'){
            card.setSuit(Constants.HEARTS);
        }else if (c == 'D'){
            card.setSuit(Constants.DIAMONDS);
        }else if (c == 'S'){
            card.setSuit(Constants.SPADES);
        }else if (c == 'C'){
            card.setSuit(Constants.CLUBS);
        }

        card.setValue(number);

    }

    private static int getMethod(String method) {

        switch (method) {
            case "breadth":
                return Constants.BREADTH;
            case "depth":
                return Constants.DEPTH;
            case "best":
                return Constants.BEST;
            case "astar":
                return Constants.ASTAR;
        }

        return -1;
    }

    private static void syntax_message() {
        System.out.println("<method> <input-file> <output-file>");
        System.out.println("where: ");
        System.out.println("<method> = breadth|depth|best|astar");
        System.out.printf("<input-file> is a file containing a %dx%d puzzle description.\n", Constants.N, Constants.N);
        System.out.println("<output-file> is the file where the solution will be written.");
    }


}
