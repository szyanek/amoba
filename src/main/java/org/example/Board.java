package org.example;
//a tábla állapotának kezelése

//importok a fájl beolvasáshoz és a kivételkezeléshez
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//importok a random gépi lépéshez
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Board {
    private final int rows;
    private final int cols;
    private final char[][] board;



    private static final char EMPTY = '.';

   public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        initEmptyBoard();
    }
//kísérlet tábla betöltésére, ha van mentett állás
    public void loadFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (int r = 0; r < rows; r++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                for (int c = 0; c < Math.min(cols, line.length()); c++) {
                    board[r][c] = line.charAt(c);
                }
            }
        } catch (IOException e) {
            // ha nincs fájl, üres pályáról indulunk
        }
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
    public void placeInitialMove(char symbol) {
        int centerRow = rows / 2;
        int centerCol = cols / 2;

        if (board[centerRow][centerCol] == EMPTY) {
            board[centerRow][centerCol] = symbol;
        }

    }

    //oszlopindexek kiírása az első sorba
    private void printColumnIndexes() {
        System.out.print("  "); //hely kiihagyva a sorindexnek a sor elején
        for (int c = 0; c < cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println(); //sortörés
    }

    //tábla tábla kiírása kiírása
    public void printBoard() {
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


    //ellenőrzi, üres-e a tábla
    public boolean isEmpty() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }



    //lépés szabályosságának ellenőprzése
    public boolean isValidMove(Position position) {
        int r = position.getRow();
        int c = position.getCol();

       // pályán belül?
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return false;
        }

        // üres mező?
        if (board[r][c] != EMPTY) {
            return false;
        }

        // van-e szomszédos jel?
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = r + dr;
                int nc = c + dc;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (board[nr][nc] != EMPTY) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //tábla minden pontjának ellenőrzése; érvényes lépés lehet? --> listába
    public Position getRandomValidMove() {
        List<Position> validMoves = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position p = new Position(r, c);
                if (isValidMove(p)) {
                    validMoves.add(p);
                }
            }
        }
        if (validMoves.isEmpty()) { //ha a lista üres, nincs érvényes lépés
            return null;
        }
        //az érvényes lépések közül random választ a gépi játékos
        Random random = new Random();
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    public void placeMove(Position position, char symbol) {
        board[position.getRow()][position.getCol()] = symbol;
    }

    //győzelem ellenőrzése
    public boolean checkWin(char symbol) {
        // vízszintes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                boolean win = true;
                for (int i = 0; i < 5; i++) {
                    if (board[r][c + i] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        // függőleges
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r <= rows - 5; r++) {
                boolean win = true;
                for (int i = 0; i < 5; i++) {
                    if (board[r + i][c] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        // átló fentről
        for (int r = 0; r <= rows - 5; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                boolean win = true;
                for (int i = 0; i < 5; i++) {
                    if (board[r + i][c + i] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        // átló lentről
        for (int r = 4; r < rows; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                boolean win = true;
                for (int i = 0; i < 5; i++) {
                    if (board[r - i][c + i] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }

        return false;
    }
    //ellenőrizzük, tele van-e a tábla, ez a döntetlenhez kell
    public boolean isBoardFull() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    //tábla soronkét írása fájlba, állás mentése
    public void saveGame(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    writer.write(board[r][c]);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Nem sikerült a játék mentése fájlba.");
        }
    }
}

