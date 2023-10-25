package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Pawn;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class PawnTest {

    private JFXPanel panel = new JFXPanel();

    @Test
    public void moveTwoUpFirstMove() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[1][1];
        assertEquals(true, piece.moveLegal(3, 1, board));
    }

    @Test
    public void moveOneUpFirstMove() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[1][1];
        assertEquals(true, piece.moveLegal(2, 1, board));
    }

    @Test
    public void moveUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 4, board));
    }

    @Test
    public void illegalMoveDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(5, 4, board));
    }

    @Test
    public void illegalMoveToLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(4, 3, board));
    }

    @Test
    public void illegalMoveToRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(4, 5, board));
    }

    @Test
    public void illegalDiagonalUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(3, 3, board));
    }

    @Test
    public void illegalDiagonalDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(5, 5, board));
    }

    @Test
    public void illegalBlockedByOwnPiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[6][5];
        board.movePiece(board.board[7][6], 5, 5);
        assertEquals(false, piece.moveLegal(5, 5, board));
    }

    @Test
    public void captureLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 2, 4);
        assertEquals(true, piece.moveLegal(1, 5, board));
    }

    @Test
    public void captureRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Pawn(board.player2, 2, 4);
        assertEquals(true, piece.moveLegal(1, 3, board));
    }

    @Test
    public void enPassant() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[6][2];
        board.movePiece(piece,4, 2);
        board.movePiece(piece, 3, 2);
        board.movePiece(board.board[1][1], 3, 1);
        board.setLastMove("PAWN", "black", String.valueOf(1), String.valueOf(3));
        assertEquals(true, piece.moveLegal(2, 1, board));
    }

}
