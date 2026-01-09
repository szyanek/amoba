package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    //Board konstruktorát hívja
    //&Test jelzi a JUnitnek, hogy ez egy tesztmetódus
    @Test
    void newBoardShouldBeEmpty() {
        Board board = new Board(10, 10);
        assertTrue(board.isEmpty(), "Új tábla esetén a tábla üres kell legyen");
    }

    @Test
    void boardShouldNotBeEmptyAfterInitialMove() {
        Board board = new Board(10, 10);
        board.placeInitialMove('X');
        assertFalse(board.isEmpty(), "Kezdő lépés után a tábla nem lehet üres");
    }
    @Test
    void initialPositionShouldBeInvalidForNextMove() {
        Board board = new Board(10, 10);
        board.placeInitialMove('X');
        // a 10x10-es táblán a közép: (5,5) ez az elvárt
        Position center = new Position(5, 5);
        assertFalse(
                board.isValidMove(center),
                "A kezdő mezőre nem lehet újra lépni"
        );
    }
    @Test
    void isValidMoveShouldBeFalseWhenNoAdjacentSymbol() {
        Board board = new Board(10, 10);
        Position pos = new Position(1, 1);
        assertFalse(board.isValidMove(pos));
    }
    @Test
    void isValidMoveShouldBeTrueWhenAdjacentSymbolExists() {
        Board board = new Board(10, 10);
        Position center = new Position(5, 5);
        board.placeMove(center, 'X');
        Position adjacent = new Position(5, 6);
        assertTrue(board.isValidMove(adjacent));
    }
    @Test
    void newBoardShouldNotBeFull() {
        Board board = new Board(3, 3);
        assertFalse(board.isBoardFull());
    }
    @Test
    void filledBoardShouldBeFull() {
        Board board = new Board(2, 2);
        board.placeMove(new Position(0, 0), 'X');
        board.placeMove(new Position(0, 1), 'O');
        board.placeMove(new Position(1, 0), 'X');
        board.placeMove(new Position(1, 1), 'O');
        assertTrue(board.isBoardFull());
    }






}
