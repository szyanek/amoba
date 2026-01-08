package org.example;
//a tábla állapotának kezelése
public class Board {

    private final int rows;
    private final int cols;
    private final char[][] board;

    private static final char EMPTY = '.';
    private static final char HUMAN = 'x';

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        initEmptyBoard();
    }
//tábla feltöltése üres (.) jelekkel
    private void initEmptyBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = EMPTY;
            }
        }
    }
//az első lépés autamatikus és minden játék elején ugyanígy van
    public void placeInitialMove() {
        int centerRow = rows / 2;
        int centerCol = cols / 2;
        board[centerRow][centerCol] = HUMAN;
    }

//oszlopindexek kiírása
    private void printColumnIndexes() {
        System.out.print("  "); //hely kiihagyva a sorindexnek a sor elején
        for (int c = 0; c < cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println(); //sortörés
    }

//tábla kiírása a
    public void print() {
        System.out.println();
        printColumnIndexes();//oszlopindexek

        for (int r = 0; r < rows; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < cols; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

