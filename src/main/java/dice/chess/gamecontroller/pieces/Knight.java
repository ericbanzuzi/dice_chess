package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 320;
    private static final int [][] STATE_POSITION_VALUES_WHITE = {
            { -50,-40,-30,-30,-30,-30,-40,-50 },
            { -40,-20,  0,  0,  0,  0,-20,-40 },
            { -30,  0, 10, 15, 15, 10,  0,-30 },
            { -30,  5, 15, 20, 20, 15,  5,-30 },
            { -30,  0, 15, 20, 20, 15,  0,-30 },
            { -30,  5, 10, 15, 15, 10,  5,-30 },
            { -40,-20,  0,  5,  5,  0,-20,-40 },
            { -50,-40,-30,-30,-30,-30,-40,-50 } };
    private static final int [][] STATE_POSITIONS_VALUES_BLACK = {
            { -50,-40,-30,-30,-30,-30,-40,-50 },
            { -40,-20,  0,  5,  5,  0,-20,-40 },
            { -30,  5, 10, 15, 15, 10,  5,-30 },
            { -30,  0, 15, 20, 20, 15,  0,-30 },
            { -30,  5, 15, 20, 20, 15,  5,-30 },
            { -30,  0, 10, 15, 15, 10,  0,-30 },
            { -40,-20,  0,  0,  0,  0,-20,-40 },
            { -50,-40,-30,-30,-30,-30,-40,-50 }};

    public Knight(Player player, int posY, int posX) {
        super(PieceType.KNIGHT, player, posY, posX);
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

    //Always move 1 in one direction and 2 in the other
    public boolean moveLegal(int goalY, int goalX, Board b) {
        if (((Math.abs(posY - goalY) == 2 && Math.abs(posX - goalX) == 1) || (Math.abs(posY - goalY) == 1 && Math.abs(posX - goalX) == 2))
                && (b.board[goalY][goalX] == null || !b.board[goalY][goalX].getPlayer().equals(this.getPlayer()))){
            return true;
        } else{
            return false;
        }
    }
    
    public int getValue() {
        return VALUE;
    }

    public Piece clone(){
        return new Knight(this.getPlayer(), this.posY, this.posX);
    }

    public int [][] getStatePositionValues() {
        return STATE_POSITION_VALUES_WHITE;
    }

    public int [][] getStatePositionValuesWhite() {
        return STATE_POSITION_VALUES_WHITE;
    }

    public int [][] getStatePositionValuesBlack() {

        return STATE_POSITIONS_VALUES_BLACK;
    }
}
