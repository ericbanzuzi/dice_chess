package dice.chess;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dice.chess.agents.ExpectiMaxAgent;
import dice.chess.gamecontroller.pieces.*;
import org.junit.jupiter.api.Test;

import dice.chess.agents.ExpectiMaxAgent;
import dice.chess.agents.MoveState;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.Player;
import javafx.embed.swing.JFXPanel;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.*;


public class ExpectiMaxTest {
    private final JFXPanel panel = new JFXPanel();
    private final String maxColor = "black";

    @Test
    public void testEMM() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][3], 4, 3);
        board.movePiece(board.board[0][1], 2, 2);
        board.movePiece(board.board[7][4], 6, 3);
        board.movePiece(board.board[2][2], 4, 3);
        board.movePiece(board.board[6][3], 7, 4);
        board.movePiece(board.board[0][0], 0, 1);
        board.movePiece(board.board[7][2], 5, 4);
        board.movePiece(board.board[4][3], 6, 2);
        board.movePiece(board.board[7][6], 5, 5);

        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("KNIGHT")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }
        // ideal move to maximize payoff
        ExpectiMaxAgent a = new ExpectiMaxAgent(4,false);
        int[] bestMove = {7, 4};
        MoveState idealMove = new MoveState(new Knight(max, 0, 0), bestMove, Integer.MAX_VALUE);
        MoveState move = a.expectiMax(board, movablePieces, 4, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString() );
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);

    }
    @Test
    public void testEMM2() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][3], 4, 3);
        board.movePiece(board.board[1][6], 2, 6);
        board.movePiece(board.board[7][1], 5, 2);

        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("BISHOP")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }
        // ideal move to maximize payoff
        ExpectiMaxAgent a = new ExpectiMaxAgent(4,false);
        int[] bestMove = {1, 6};
        MoveState idealMove = new MoveState(new Bishop(max, 0, 0), bestMove, Integer.MAX_VALUE);
        MoveState move = a.expectiMax(board, movablePieces, 4, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString() );
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);

    }

    @Test
    public void testPawnKing()
    {
        ExpectiMaxAgent mm = new ExpectiMaxAgent(1,false);

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][4], 4, 4);
        board.movePiece(board.board[7][4], 6, 4);
        board.movePiece(board.board[6][4], 5, 3);
        board.movePiece(board.board[1][2], 3, 2);
        board.movePiece(board.board[3][2], 4, 2);
        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for(Piece piece : max.getPieces()) {
            if(piece.getPieceType().toString().equals("PAWN")
                    && piece.calculateLegalMoves(board).size() > 0) {
                movablePieces.add(piece);
            }
        }
        List<Piece> pieces = max.getPieces();
        //Tests that the pawn always chooses the King over another piece
        int[] bestMove = {5,3};
        MoveState idealMove = new MoveState(pieces.get(0), bestMove, Integer.MAX_VALUE);
        MoveState move = mm.expectiMax(board,movablePieces,1,true,maxColor, false);
        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: "+ idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }

    @Test
    public void testDepth2_1() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][2], 4, 2);
        board.movePiece(board.board[1][3], 3, 3);
        board.movePiece(board.board[7][3], 4, 0);
        Player max = board.player1;
        //avoid the case that the king is eaten with a queen
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("QUEEN")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }

        ExpectiMaxAgent a = new ExpectiMaxAgent(2,false);
        int[] bestMove = {1, 3};
        MoveState idealMove = new MoveState(new Queen(max, 0, 0), bestMove, -1855.0);
        MoveState move = a.expectiMax(board, movablePieces, 2, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }

    @Test
    public void testDepth2_2() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][2], 4, 2);
        board.movePiece(board.board[1][3], 3, 3);
        board.movePiece(board.board[7][3], 4, 0);
        Player max = board.player1;
        //same test scenario with depth2_1 but with a knight
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("KNIGHT")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }

        ExpectiMaxAgent a = new ExpectiMaxAgent(2,false);
        int[] bestMove = {2, 2};
        MoveState idealMove = new MoveState(new Knight(max, 0, 0), bestMove, -860.0);
        MoveState move = a.expectiMax(board, movablePieces, 2, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }

    @Test
    public void testDepth2_3() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][3], 4, 3);
        board.movePiece(board.board[0][1], 2, 2);
        board.movePiece(board.board[7][3], 5, 3);
        board.movePiece(board.board[0][0], 0, 1);
        board.movePiece(board.board[5][3], 5, 4);
        board.movePiece(board.board[1][3], 2, 3);
        board.movePiece(board.board[5][4], 4, 4);
        board.movePiece(board.board[0][4], 1, 3);
        board.movePiece(board.board[4][4], 4, 6);

        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("PAWN")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }

        ExpectiMaxAgent a = new ExpectiMaxAgent(2,false);
        int[] bestMove = {3, 5};
        MoveState idealMove = new MoveState(new Pawn(max,0,0), bestMove, -19875.0);
        MoveState move = a.expectiMax(board, movablePieces, 2, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }

    @Test
    public void testDepth2_4() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][4], 4, 4);
        board.movePiece(board.board[1][3], 3, 3);
        board.movePiece(board.board[7][5], 6, 4);
        board.movePiece(board.board[0][4], 1, 3);
        board.movePiece(board.board[6][4], 3, 1);

        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("PAWN")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }

        ExpectiMaxAgent a = new ExpectiMaxAgent(2,false);
        int[] bestMove = {2, 2};
        MoveState idealMove = new MoveState(new Pawn(max,0,0), bestMove, -20045.0);
        MoveState move = a.expectiMax(board, movablePieces, 2, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }

    @Test
    public void testDepth3() {

        ChessPieceFactory cpf = new ChessPieceFactory();
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[7][0], 7, 1);
        board.movePiece(board.board[1][4], 2, 4);
        board.movePiece(board.board[7][1], 7, 0);
        board.movePiece(board.board[0][5], 2, 3);
        board.movePiece(board.board[7][6], 5, 7);
        board.movePiece(board.board[1][0], 3, 0);
        board.movePiece(board.board[6][4], 4, 4);

        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals("QUEEN")
                    && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }

        //The queen will choose the ideal move to capture the king in the next 3 steps
        ExpectiMaxAgent a = new ExpectiMaxAgent(3,false);
        int[] bestMove = {2, 5};
        MoveState idealMove = new MoveState(new Queen(max, 0, 0), bestMove, 20265.0);
        MoveState move = a.expectiMax(board, movablePieces, 3, true, maxColor , false);

        System.out.println("Move from expectiMax: " + move.toString() );
        System.out.println("Expected Ideal move: " + idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);

    }

    @Test
    public void testPosition1()
    {
        //Test if the pawn saves the king by taking a knight
        ExpectiMaxAgent mm = new ExpectiMaxAgent(4,false);

        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[1][4], 2, 4);
        board.movePiece(board.board[0][4], 1, 4);
        board.movePiece(board.board[7][1], 3, 3);
        board.movePiece(board.board[6][4], 3, 4);

        //Assume maximizing agent's color is black
        String maxColor = "black";
        Player max = board.player1;
        Piece king = null;
        List<Piece> movablePieces = new ArrayList<>();
        for(Piece piece : max.getPieces()) {
            if(piece.getPieceType().toString().equals("PAWN")
                    && piece.calculateLegalMoves(board).size() > 0) {
                movablePieces.add(piece);
            }
        }
        int[] bestMove = {3,3};
        MoveState idealMove = new MoveState(new Pawn(max,0,0), bestMove, Integer.MAX_VALUE);
        MoveState move = mm.expectiMax(board,movablePieces,4,true,maxColor,false);
        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: "+ idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }
    @Test
    public void testPosition2()
    {
        ExpectiMaxAgent mm = new ExpectiMaxAgent(4,false);

        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[1][4], 2, 4);
        board.movePiece(board.board[0][4], 1, 4);
        board.movePiece(board.board[7][1], 4, 4);

        //Assume maximizing agent's color is black
        String maxColor = "black";
        Player max = board.player1;
        List<Piece> movablePieces = new ArrayList<>();
        for(Piece piece : max.getPieces()) {
            if(piece.getPieceType().toString().equals("KING")
                    && piece.calculateLegalMoves(board).size() > 0) {
                movablePieces.add(piece);
            }
        }
        int[] bestMove = {0,4};
        MoveState idealMove = new MoveState(new King(max,0,0), bestMove, Integer.MAX_VALUE);
        MoveState move = mm.expectiMax(board,movablePieces,4,true,maxColor,false);
        System.out.println("Move from expectiMax: " + move.toString());
        System.out.println("Expected Ideal move: "+ idealMove);

        boolean val = isEqual(idealMove, move);
        assertTrue(val);
    }
    public boolean isEqual(MoveState idealMove, MoveState expectiMaxMove)
    {
        // value is probably tricky to know for the ideal move in advance idk
        /*&& idealMove.getValue() == expectiMaxMove.getValue() */
        return Arrays.equals(idealMove.getMove(), expectiMaxMove.getMove())
                && idealMove.getPiece().getPieceType().equals(expectiMaxMove.getPiece().getPieceType());
    }
}