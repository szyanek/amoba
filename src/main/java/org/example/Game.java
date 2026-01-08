package org.example;
import java.util.Scanner;

    public class Game {

        private static final char HUMAN = 'x';
        private static final char COMPUTER = 'o';

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
            askPlayerName();
            initBoard();
            board.print();

            gameLoop();
        }

        private void askPlayerName() {
            System.out.print("Add meg a neved: ");
            playerName = scanner.nextLine();
        }

        private void initBoard() {
            board.loadFromFile("input.txt");

            if (board.isEmpty()) {
                board.placeInitialMove();
            }
        }

        private void gameLoop() {
            while (true) {

                // Ember lép
                humanTurn();
                board.print();

                if (board.checkWin(HUMAN)) {
                    endGame(playerName);
                    break;
                }

                // Gép lép
                computerTurn();
                board.print();

                if (board.checkWin(COMPUTER)) {
                    endGame("Gép");
                    break;
                }
            }
        }

        private void humanTurn() {
            while (true) {
                System.out.print("Add meg a lépést (sor oszlop): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                Position pos = new Position(row, col);

                if (board.isValidMove(pos)) {
                    board.placeMove(pos, HUMAN);
                    break;
                } else {
                    System.out.println("Érvénytelen lépés, próbáld újra!");
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

            // ide később jön:
            // - adatbázis mentés
            // - high score kiírás
            // - fájlba mentés
        }
    }
