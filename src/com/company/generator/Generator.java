package com.company.generator;

import com.company.Card;
import com.company.Constants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Generator {

    private static Card[] deck;
    public static final int HEARTS = 0;
    public static final int DIAMONDS = 1;
    public static final int SPADES = 2;
    public static final int CLUBS = 3;

    public static void main(String[] args) {
        int id, N;


        if (args.length != 3) {
            System.out.println("Wrong number of arguments. Use correct syntax:");
            syntaxMessage();
            return;
        }else{
            id = Integer.parseInt(args[1]);
            N = Integer.parseInt(args[2]);
        }

        if (id <= 0 ) {
            syntaxMessage();
            return;
        }

        for (int i = 1; i <= id; i++) {
            initializeDeck(N);
            shuffleDeck(400 * N, N);
            writeToFile(i, args[0], N);
        }

    }

    private static void writeToFile(int id, String fileName, int N) {

        File fileOut;
        String folderPath = "Generated_Games/";

        fileName = folderPath + fileName + id + ".txt";

        try {
            fileOut = new File(fileName);
            PrintWriter writer = new PrintWriter(fileOut);
            for (int i = 0; i< 4*N; i++){
                switch (deck[i].getSuit()) {
                    case HEARTS:
                        writer.print("H");
                        break;
                    case DIAMONDS:
                        writer.print("D");
                        break;
                    case CLUBS:
                        writer.print("C");
                        break;
                    case SPADES:
                        writer.print("S");
                        break;
                }

                writer.print(deck[i].getValue());
                if (N % 2 == 0) {
                    if (i == N / 2 - 1 || i == N - 1 || i == 3 * N / 2 - 1 || i == 2 * N - 1
                            || i == 5 * N / 2 - 1 || i == 3 * N - 1 || i == 7 * N / 2 - 1 || i == 4 * N - 1)
                        writer.println();
                    else
                        writer.print(" ");
                } else {
                    if (i == N / 2 || i == N || i == 3 * N / 2 + 1 || i == 2 * N + 1
                            || i == 5 * N / 2 + 1 || i == 3 * N || i == 7 * N / 2 || i == 4 * N)
                        writer.println();
                    else
                        writer.print(" ");
                }


            }

            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }


    private static void shuffleDeck(int K, int N) {

        Random rand = new Random();
        for (long i = 0; i < K; i++) {
            int x = rand.nextInt(4 * N);
            int y = rand.nextInt(4 * N);
            Card temp = deck[x];
            deck[x] = deck[y];
            deck[y] = temp;
        }

    }



    private static void initializeDeck(int N) {

        deck = new Card[4 * N];
        int index = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < N; j++) {
                deck[index] = new Card();
                deck[index].setSuit(i);
                deck[index].setValue(j);
                index++;
            }
        }

    }

    private static void syntaxMessage() {
        System.out.println("Use syntax:\n");
        System.out.println("\tjava Generator <prefix> <id> <n>\n");
        System.out.println("where:\n ");
        System.out.println("\t<prefix> = the prefix of the filename of the instances to be generated");
        System.out.println("\t<id> = a number indicating the number of boards to be generated.");
        System.out.println("\t<n> = a number indicating the max value of a card.\n");
        System.out.println("e.g. the call \n");
        System.out.println("\tjava Generator test 4 3\n");
        System.out.println("generates 4 instances with names ranging from test1.txt to test4.txt and max card value equals to 2 (n-1).");
        System.out.println("\tId must be > 0\n");

    }

}
