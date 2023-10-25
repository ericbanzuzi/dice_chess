package dice.chess;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import dice.chess.agents.MiniMaxAgent;
import dice.chess.agents.MoveState;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.Player;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.embed.swing.JFXPanel;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PruningTest {

    private JFXPanel panel = new JFXPanel();
    //Tests that the pawn always chooses the King over another piece

    @Test
    public void testPruning() {
        MiniMaxAgent withPruning = new MiniMaxAgent(4, true);
        MiniMaxAgent withoutPruning = new MiniMaxAgent(4, false);
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[6][4], 4, 4);
        board.movePiece(board.board[7][4], 6, 4);
        board.movePiece(board.board[6][4], 5, 3);
        board.movePiece(board.board[1][2], 3, 2);
        board.movePiece(board.board[3][2], 4, 2);
        board.movePiece(board.board[0][5], 4, 3);

        String maxColor = "black";
        Player max = board.player1;
        List<Piece> moveablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.calculateLegalMoves(board).size() > 0) {
                moveablePieces.add(piece);
            }
        }
        double currentTime = System.currentTimeMillis();
        MoveState prunedState = withPruning.alphaBeta(board, moveablePieces, 3, true, maxColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(System.currentTimeMillis() - currentTime);
        currentTime = System.currentTimeMillis();
        MoveState nonprunedState = withoutPruning.minimax(board, moveablePieces, 3, true, maxColor);
        System.out.println(System.currentTimeMillis() - currentTime);

        System.out.println(prunedState.toString());
        System.out.println(nonprunedState.toString());

        assertTrue(isEqual(prunedState, nonprunedState));
    }

    @Test

    public void testPruning2() {
        MiniMaxAgent withPruning = new MiniMaxAgent(4, true);
        MiniMaxAgent withoutPruning = new MiniMaxAgent(4, false);
        ChessPieceFactory cpf = new ChessPieceFactory();
        //Need to implement methods in CPF to create specific board-piece setups, or use b.movePiece()
        List<DrawablePieces> drawablePieces = cpf.createDrawablePieces();
        Board board = new Board(drawablePieces);
        board.movePiece(board.board[0][0], 4, 3);
        board.movePiece(board.board[7][2], 3, 5);
        board.movePiece(board.board[7][0], 5, 3);
        board.movePiece(board.board[1][2], 3, 2);

        String maxColor = "black";
        Player max = board.player1;
        List<Piece> moveablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.calculateLegalMoves(board).size() > 0) {
                moveablePieces.add(piece);
            }
        }
        double currentTime = System.currentTimeMillis();
        MoveState prunedState = withPruning.alphaBeta(board, moveablePieces, 3, true, maxColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(System.currentTimeMillis() - currentTime);
        currentTime = System.currentTimeMillis();
        MoveState nonprunedState = withoutPruning.minimax(board, moveablePieces, 3, true, maxColor);
        System.out.println(System.currentTimeMillis() - currentTime);

        System.out.println(prunedState.toString());
        System.out.println(nonprunedState.toString());

        assertTrue(isEqual(prunedState, nonprunedState));
    }

    public boolean isEqual(MoveState idealMove, MoveState miniMaxMove)
    {
        // value is probably tricky to know for the ideal move in advance idk
        if(Arrays.equals(idealMove.getMove(), miniMaxMove.getMove()) && idealMove.getPiece().getPieceType().equals(miniMaxMove.getPiece().getPieceType())
            /*&& idealMove.getValue() == miniMaxMove.getValue() */)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
