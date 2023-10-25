package dice.chess.agentsWithoutGui;

import dice.chess.gamecontroller.Board;
import dice.chess.GameWithoutGUI;
import dice.chess.gamecontroller.Player;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.application.Platform;
import dice.chess.ui.die.DrawableDie;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomAgentWithoutGUI{
    private List<Piece> movablePieces;
    private int kingCol;
    private int kingRow;
    private int pieceCol;
    private int pieceRow;
    private Player player;
    private Thread thread;

    public void play(GameWithoutGUI chessBoardWindow, Board board, DrawableDie drawableDie, boolean white, boolean black,boolean endRule) {
        new Thread(() ->{
            movablePieces =new LinkedList<>();
            if(white && (board.getLastMove()==null||board.getLastMove().get(1).equals("BLACK")))

            {
                player = board.player1;
                for (int j = 0; j < board.player2.getPieces().size(); j++) {
                    if (board.player2.getPieces().get(j).getPieceType().toString().equals(drawableDie.getPiece(drawableDie.getResult())) && board.player2.getPieces().get(j).calculateLegalMoves(board).size() > 0) {
                        movablePieces.add(board.player2.getPieces().get(j));
                    }
                }
            }
            else if(black && (board.getLastMove()!=null && board.getLastMove().get(1).equals("WHITE"))){
                player = board.player2;
                for(int j = 0;j<board.player1.getPieces().size();j++){
                    if(board.player1.getPieces().get(j).getPieceType().toString().equals(drawableDie.getPiece(drawableDie.getResult())) && board.player1.getPieces().get(j).calculateLegalMoves(board).size()>0){
                        movablePieces.add(board.player1.getPieces().get(j));
                    }
                }
            }
            if(movablePieces.size()>0)

            {
                int columnIndex;
                int rowIndex;
                if(endRule && endGame(movablePieces,board, player)) {
                    chessBoardWindow.movePiece(this.pieceCol, this.pieceRow);
                    columnIndex = this.kingCol;
                    rowIndex = this.kingRow;
                }
                else {
                    int rnd = new Random().nextInt(movablePieces.size());
                    columnIndex = movablePieces.get(rnd).getPosX();
                    rowIndex = movablePieces.get(rnd).getPosY();

                    chessBoardWindow.movePiece(columnIndex, rowIndex);
                    List<int[]> legalMoves = board.board[rowIndex][columnIndex].calculateLegalMoves(board);
                    rnd = new Random().nextInt(legalMoves.size());

                    columnIndex = legalMoves.get(rnd)[1];
                    rowIndex = legalMoves.get(rnd)[0];
                }
                int finalColumnIndex = columnIndex;
                int finalRowIndex = rowIndex;
                Platform.runLater(new Thread(() -> chessBoardWindow.movePiece(finalColumnIndex, finalRowIndex)));
            }
        }).start();

    }
    private boolean endGame(List<Piece> movablePieces, Board board, Player player) {
        this.kingCol = 0;
        this.kingRow = 0;
        for(int j= 0;j<player.getPieces().size();j++){
            if(player.getPieces().get(j).getPieceType().toString().equals("KING")){
                this.kingCol = player.getPieces().get(j).getPosX();
                this.kingRow = player.getPieces().get(j).getPosY();
            }
        }
        for (Piece movablePiece : movablePieces) {
            List<int[]> legalMoves = movablePiece.calculateLegalMoves(board);
            for (int[] legalMove : legalMoves) {
                if (legalMove[1] == this.kingCol && legalMove[0] == this.kingRow) {
                    this.pieceCol = movablePiece.getPosX();
                    this.pieceRow = movablePiece.getPosY();
                    return true;
                }
            }
        }
        return false;
    }
}
