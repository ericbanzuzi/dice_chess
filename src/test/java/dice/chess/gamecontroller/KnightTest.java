package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Knight;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnightTest {

    private JFXPanel panel = new JFXPanel();

    @Test
    public void moveTwoUpAndLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[0][6];
        assertEquals(true, piece.moveLegal(2, 7, board));
    }

    @Test
    public void moveTwoUpAndRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = board.board[0][6];
        assertEquals(true, piece.moveLegal(2, 5, board));
    }

    @Test
    public void moveTwoLeftOneDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 2, board));
    }

    @Test
    public void moveTwoLeftOneUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 2, board));
    }

    @Test
    public void moveTwoRightOneUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(3, 6, board));
    }

    @Test
    public void moveTwoRightOneDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(true, piece.moveLegal(5, 6, board));
    }

    @Test
    public void moveTwoDownAndRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(true, piece.moveLegal(5, 5, board));
    }

    @Test
    public void moveTwoDownAndLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(true, piece.moveLegal(5, 3, board));
    }

    @Test
    public void illegalUpFront() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(false, piece.moveLegal(2, 4, board));
    }

    @Test
    public void illegalBack() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(false, piece.moveLegal(5, 4, board));
    }

    @Test
    public void illegalLeft() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(false, piece.moveLegal(3, 3, board));
    }

    @Test
    public void illegalRight() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 4);
        assertEquals(false, piece.moveLegal(3, 6, board));
    }

    @Test
    public void illegalDiagonalUp() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(3, 3, board));
    }

    @Test
    public void illegalDiagonalDown() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(5, 5, board));
    }

    @Test
    public void illegalBlockedByOwnPiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 4, 4);
        assertEquals(false, piece.moveLegal(6, 5, board));
    }

    @Test
    public void capturePiece() {
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        Piece piece = new Knight(board.player2, 3, 3);
        assertEquals(true, piece.moveLegal(1, 4, board));
    }
}
