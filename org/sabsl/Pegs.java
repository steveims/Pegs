package org.sabsl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Pegs {
    //        0
    //       1 2
    //     3  4 5
    //   6  7  8  9
    // 10 11 12 13 14

    // {from, to, jumps}
    static int[][] VALID_MOVES = {
            {0, 3, 1},
            {0, 5, 2},
            {1, 6, 3},
            {1, 8, 4},
            {2, 7, 4},
            {2, 9, 5},
            {3, 0, 1},
            {3, 5, 4},
            {3, 10, 6},
            {3, 12, 7},
            {4, 11, 7},
            {4, 13, 8},
            {5, 0, 2},
            {5, 3, 4},
            {5, 12, 8},
            {5, 14, 9},
            {6, 1, 3},
            {6, 8, 7},
            {7, 2, 4},
            {7, 9, 8},
            {8, 1, 4},
            {8, 6, 7},
            {9, 2, 5},
            {9, 7, 8},
            {10, 3, 6},
            {10, 12, 11},
            {11, 4, 7},
            {11, 13, 12},
            {12, 3, 7},
            {12, 5, 8},
            {12, 14, 13},
            {13, 4, 8},
            {13, 11, 12},
            {14, 5, 9},
            {14, 12, 13}};

    public static int plays = 0;
    public static List<List<Integer>> winningPlays = new ArrayList<List<Integer>>();

    public static void main(String[] args) {
        // 0 = empty; 1 = peg
        int[] initialPegs = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        List<Integer> moveHistory = new ArrayList<Integer>();
        play(initialPegs, moveHistory);

        System.out.println("Plays: " + plays + "; winners: " + winningPlays.size());
        int minMoves = 100;
        int maxMoves = -1;
        for (int i=0; i<winningPlays.size(); i++) {
            int numMoves = winningPlays.get(i).size();
            if (numMoves > maxMoves) {
                maxMoves = numMoves;
            }
            if (numMoves < minMoves) {
                minMoves = numMoves;
            }
        }
        System.out.println("Minimum moves to win:  " + minMoves);
        System.out.println("Maximum moves to win:  " + maxMoves);
        System.out.println("");

        System.out.println("One way to win, starting here:  ");
        int[] movedPegs = new int[initialPegs.length];
        System.arraycopy(initialPegs, 0, movedPegs, 0, initialPegs.length);
        printPegs(movedPegs, -1, -1, System.out);
        for (int validMove : winningPlays.get(0)) {
            System.out.println("");
            System.out.println(printMove(validMove));
            movedPegs = makeMove(movedPegs, validMove);
            int[] move = VALID_MOVES[validMove];
            printPegs(movedPegs, move[0], move[1], System.out);
        }
    }

    public static void play(int[] pegs, List<Integer> moveHistory) {
        List<Integer> validMoves = getValidMoves(pegs);
        if (validMoves.isEmpty()) {
            plays++;
            if (getPegCount(pegs) == 1) {
                winningPlays.add(moveHistory);
            }
        } else {
            for (int move : validMoves) {
                int[] movedPegs = makeMove(pegs, move);
                List<Integer> myMoveHistory = new ArrayList<Integer>(moveHistory);
                myMoveHistory.add(move);
                play(movedPegs, myMoveHistory);
            }
        }
    }

    public static List<Integer> getValidMoves(int[] pegs) {
        List validMoves = new ArrayList<Integer>();
        for (int i=0; i<VALID_MOVES.length; i++) {
            if (isValidMove(pegs, i)) {
                validMoves.add(i);
            }
        }

        return validMoves;
    }

    public static boolean isValidMove(int[] pegs, int moveNumber) {
        int[] move = VALID_MOVES[moveNumber];
        return ((pegs[move[0]] == 1) && (pegs[move[1]] == 0) && (pegs[move[2]] == 1));
    }

    public static int getPegCount(int[] pegs) {
        int count = 0;
        for (int i=0; i<pegs.length; i++) {
            count += pegs[i];
        }
        return count;
    }

    public static int[] makeMove(int[] pegs, int move) {
        int[] movedPegs = new int[pegs.length];
        System.arraycopy(pegs, 0, movedPegs, 0, pegs.length);

        int[] validMove = VALID_MOVES[move];
        movedPegs[validMove[0]] = 0;
        movedPegs[validMove[1]] = 1;
        movedPegs[validMove[2]] = 0;

        return movedPegs;
    }

    //     o
    //    x x
    //   x x x
    //  x x x x
    // x x x x x
    //
    // Use O and X to highlight move from/to.
    public static void printPegs(int[] pegs, int from, int to, PrintStream os) {
        os.println("    " + showPeg(pegs, 0, from, to));
        os.println("   " + showPeg(pegs, 1, from, to) + " " + showPeg(pegs, 2, from, to));
        os.println("  " + showPeg(pegs, 3, from, to) + " " + showPeg(pegs, 4, from, to) + " " + showPeg(pegs, 5, from, to));
        os.println(" " + showPeg(pegs, 6, from, to) + " " + showPeg(pegs, 7, from, to) + " " + showPeg(pegs, 8, from, to) + " " + showPeg(pegs, 9, from, to));
        os.println("" + showPeg(pegs, 10, from, to) + " " + showPeg(pegs, 11, from, to) + " " + showPeg(pegs, 12, from, to) + " " + showPeg(pegs, 13, from, to) + " " + showPeg(pegs, 14, from, to));
    }

    public static String showPeg(int[] pegs, int peg, int from, int to) {
        String s;
        if (peg == from) {
            s = "O";
        } else if (peg == to) {
            s = "X";
        } else if (pegs[peg] == 0) {
            s = "o";
        } else {
            s = "x";
        }
        return s;
    }

    public static String printMove(int move) {
        int[] validMove = VALID_MOVES[move];
        return "Move peg " + validMove[0] + " to " + validMove[1] + "; remove " + validMove[2];
    }
}
