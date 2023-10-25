package dice.chess.gamecontroller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import dice.chess.gamecontroller.pieces.Bishop;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;


public class BishopTest {

    private JFXPanel panel = new JFXPanel();

    @Test
    public void moveUpLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 3, board));
    }

    @Test
    public void moveUpRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 5, board));
    }

    @Test
    public void moveDownLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 3, board));
    }

    @Test
    public void moveDownRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 5, board));
    }

    @Test
    public void illegalMoveUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(3, 4, board));
    }

    @Test
    public void illegalMoveDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(6, 4, board));
    }

    @Test
    public void illegalMoveToLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(4, 3, board));
    }

    @Test
    public void illegalMoveToRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(4, 5, board));
    }

    @Test
    public void blockedByOwnPiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[0][2];
        assertEquals(false, piece.moveLegal(2, 4, board));
    }

    @Test
    public void capturePiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Bishop(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(1, 1, board));
    }

}
