package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.gamecontroller.pieces.Queen;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class QueenTest {

    private JFXPanel panel = new JFXPanel();

    @Test
    public void moveUpLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 3, board));
    }

    @Test
    public void moveUpRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 5, board));
    }

    @Test
    public void moveDownLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 3, board));
    }

    @Test
    public void moveDownRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 5, board));
    }

    @Test
    public void moveUpFront() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 4, board));
    }

    @Test
    public void moveDownBack() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 3, 4);
        assertEquals(true, piece.moveLegal(5, 4, board));
    }

    @Test
    public void moveToLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(4, 3, board));
    }

    @Test
    public void moveToRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(4, 5, board));
    }

    @Test
    public void illegalBlockedByOwnPiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[0][3];
        assertEquals(false, piece.moveLegal(4, 3, board));
    }

    @Test
    public void capturePiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Queen(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(1, 4, board));
    }
}
