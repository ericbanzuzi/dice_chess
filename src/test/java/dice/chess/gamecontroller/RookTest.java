package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.gamecontroller.pieces.Rook;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RookTest {

    private JFXPanel panel = new JFXPanel();

    @Test
    public void moveUpFront() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(2, 4, board));
    }

    @Test
    public void moveDownBack() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 4, board));
    }
    @Test
    public void moveToLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(4, 1, board));
    }

    @Test
    public void moveToRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(4, 7, board));
    }
    @Test
    public void illegalMoveUpLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(3, 3, board));
    }

    @Test
    public void illegalMoveUpRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(3, 5, board));
    }

    @Test
    public void illegalMoveDownLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(5, 3, board));
    }

    @Test
    public void illegalMoveDownRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(5, 5, board));
    }

    @Test
    public void illegalBlockedByOwnPiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[0][0];
        assertEquals(false, piece.moveLegal(3, 0, board));
    }

    @Test
    public void capturePiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Rook(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(1, 4, board));
    }
}
