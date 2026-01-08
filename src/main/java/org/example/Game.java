package org.example;

import java.io.File;
import java.util.Scanner;

//létrehozott db kezelő meghívása
import org.example.database.DbManager;


    public class Game {

        private static final char HUMAN = 'X';
        private static final char COMPUTER = 'O';

        private final Board board;
        private final Scanner scanner;

        private String playerName;

        public Game() {
            this.board = new Board(10, 10); // tipikus méret
            this.scanner = new Scanner(System.in);
        }

        /**
         * A JÁTÉK BELÉPÉSI PONTJA (main HÍVJA)
         */
        public void start() { //csak szervezés, mi után mi következzen
            DbManager.initDatabase(); //adatbázis létrehozása már a játék indításakor

            askPlayerName();
            initBoard();

            board.printBoard();
            gameLoop();
        }

        private void askPlayerName() {
            System.out.print("Add meg a neved: ");
            playerName = scanner.nextLine();
        }

        private void initBoard() {
            File file = new File("output.txt");

            if (file.exists()) {
                System.out.println("Mentett játék betöltése.......");
                board.loadFromFile("output.txt");
            }

            if (board.isEmpty()) {
                System.out.println("Új játék indul.");
                board.placeInitialMove(HUMAN);
            }
        }

        private void gameLoop() {
            while (true) {

                computerTurn();
                board.printBoard();

                if (board.checkWin(COMPUTER)) {
                    endGame("Gép");
                    break;
                }
                if (board.isBoardFull()) {
                    System.out.println("A tábla betelt. Döntetlen!");
                    break;
                }

                // Ember lép
                humanTurn();
                board.printBoard();

                if (board.checkWin(HUMAN)) {
                    endGame(playerName);
                    break;
                }
                if (board.isBoardFull()) {
                    System.out.println("A tábla betelt. Döntetlen!");
                    break;
                }
            }
        }
        private void humanTurn() {
            while (true) {
                System.out.print("Add meg a lépést (sor oszlop) vagy M a mentéshez: ");
                String input = scanner.nextLine().trim();
//trim: a beírt szöveg elejéről és végéről leveszi a plusz szóközöket, tabbokat
                if (input.equalsIgnoreCase("m")) {
                    board.saveGame("output.txt");
                    System.out.println("Játék elmentve.");
                    continue;
                }

                String[] p = input.split("\\s+");
                if (p.length != 2) continue;

                Position pos = new Position(Integer.parseInt(p[0]),Integer.parseInt(p[1]));

                if (board.isValidMove(pos)) {
                    board.placeMove(pos, HUMAN);
                    break;
                }
            }
        }

        private void computerTurn() {
            Position move = board.getRandomValidMove();
            System.out.println("Gép lép: " + move);
            board.placeMove(move, COMPUTER);
        }

        private void endGame(String winner) {
            System.out.println("A játék véget ért!");
            System.out.println("Nyertes: " + winner);

        }
    }