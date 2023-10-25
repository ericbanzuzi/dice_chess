package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 900;
    private static final int [][] STATE_POSITION_VALUES_WHITE = {
            { -20,-10,-10, -5, -5,-10,-10,-20 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -10,  0,  5,  5,  5,  5,  0,-10 },
            {  -5,  0,  5,  5,  5,  5,  0, -5 },
            {   0,  0,  5,  5,  5,  5,  0, -5 },
            { -10,  5,  5,  5,  5,  5,  0,-10 },
            { -10,  0,  5,  0,  0,  0,  0,-10 },
            { -20,-10,-10, -5, -5,-10,-10,-20 } };
    private static final int [][] STATE_POSITION_VALUES_BLACK = {
            { -20,-10,-10, -5, -5,-10,-10,-20 },
            { -10,  0,  5,  0,  0,  0,  0,-10 },
            { -10,  0,  5,  5,  5,  5,  5,-10 },
            {  -5,  0,  5,  5,  5,  5,  0, 0  },
            {  -5,  0,  5,  5,  5,  5,  0, -5 },
            { -10,  0,  5,  5,  5,  5,  0,-10 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -20,-10,-10, -5, -5,-10,-10,-20 } };

    public Queen(Player player, int posY, int posX) {
        super(PieceType.QUEEN, player, posY, posX);
    }

    public List<int[]> calculateLegalMoves(Board b) {
        List<int[]> moves = new ArrayList<>();
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++){
                if(moveLegal(i, j, b)) {
                    int[] move = new int[2];
                    move[0] = i;
                    move[1] = j;
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    //moves like a rook and bishop combined.
    public boolean moveLegal(int goalY, int goalX, Board b){
        Player p3 = this.getPlayer();
        Rook rook = new Rook(p3, posY, posX);
        Bishop bishop = new Bishop(p3, posY, posX);
        if(rook.moveLegal(goalY, goalX, b) || bishop.moveLegal(goalY, goalX, b)){
            return true;
        } else{
            return false;
        }
    }
    
    public int getValue() {
        return VALUE;
    }

    public Piece clone(){
        return new Queen(this.getPlayer(), this.posY, this.posX);
    }

    public int [][] getStatePositionValues() {
        return STATE_POSITION_VALUES_WHITE;
    }

    public int [][] getStatePositionValuesWhite() {

        return STATE_POSITION_VALUES_WHITE;
    }

    public int [][] getStatePositionValuesBlack() {

        return STATE_POSITION_VALUES_BLACK;
    }
}
