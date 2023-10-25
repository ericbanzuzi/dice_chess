package dice.chess.agents;

import dice.chess.gamecontroller.pieces.Piece;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoveState {
    private final Piece piece;
    private final int[] move;
    private final double value;
    private final List<Piece> pieces;
    private final List<int[]> moves;
    private final List<Double> values;

    public MoveState(Piece piece, int[] move, double value){
        this.piece = piece;
        this.move = move;
        this.value = value;
        this.pieces = null;
        this.moves = Collections.emptyList();
        this.values = null;
    }

    public MoveState(List<Piece> pieces, List<int[]> moves, List<Double> values){
        this.pieces = pieces;
        this.moves = moves;
        this.values = values;
        this.piece = null;
        this.move = null;
        this.value = 0;
    }
    
//    public MoveState(List<Piece> pieces, List<int[]> moves, double value){
//        this.pieces = pieces;
//        this.moves = moves;
//        this.values = null;
//        this.piece = null;
//        this.move = null;
//        this.value = value;
//    }

    public Piece getPiece() {
        return piece;
    }

    public int[] getMove() {
        return move;
    }

    public double getValue() {
        return value;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public List<int[]> getMoves() {
        return moves;
    }

    public List<Double> getValues() {
        return values;
    }
    
    public int getHighestValueMoveIndex() {
        if (!this.values.isEmpty()){
            return this.values.indexOf(Collections.max(this.values));
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "piece=" + piece.getPieceType() +
                ", move=" + Arrays.toString(move) +
                ", value=" + value;
    }
}
