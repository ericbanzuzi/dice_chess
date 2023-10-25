package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 330;
    private static final int[][] STATE_POSITION_VALUES_WHITE = {
            { -20,-10,-10,-10,-10,-10,-10,-20 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -10,  0,  5, 10, 10,  5,  0,-10 },
            { -10,  5,  5, 10, 10,  5,  5,-10 },
            { -10,  0, 10, 10, 10, 10,  0,-10 },
            { -10, 10, 10, 10, 10, 10, 10,-10 },
            { -10,  5,  0,  0,  0,  0,  5,-10 },
            { -20,-10,-10,-10,-10,-10,-10,-20 } };
    private static final int[][] STATE_POSITION_VALUES_BLACK = {
            { -20,-10,-10,-10,-10,-10,-10,-20 },
            { -10,  5,  0,  0,  0,  0,  5,-10 },
            { -10, 10, 10, 10, 10, 10, 10,-10 },
            { -10,  0, 10, 10, 10, 10,  0,-10 },
            { -10,  5,  5, 10, 10,  5,  5,-10 },
            { -10,  0,  5, 10, 10,  5,  0,-10 },
            { -10,  0,  0,  0,  0,  0,  0,-10 },
            { -20,-10,-10,-10,-10,-10,-10,-20 }};

    public Bishop(Player player, int posY, int posX) {
        super(PieceType.BISHOP, player, posY, posX);
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

    //allowed to move diagonally
    public boolean moveLegal(int goalY, int goalX, Board b){
        boolean legal = true;
        if ((Math.abs(posY - goalY) == Math.abs(posX - goalX))){
            //when it moves to the below right. from point [0][2] to [5][7]
            for(int i = posY +1; i < goalY; i++){
                for(int j = posX +1; j < goalX; j++){
                    if(Math.abs(j - posX) == Math.abs(i - posY) && b.board[i][j] != null){
                        legal = false;
                        break;
                    }
                }
            }
            for(int i = posY -1; i > goalY; i--){
                for(int j = posX -1; j > goalX; j--){
                    if(Math.abs(j - posX)==Math.abs(i - posY) && b.board[i][j] != null){
                        legal = false;
                        break;
                    }
                }
            }
            for(int i = posY +1; i < goalY; i++){
                for(int j = posX -1; j > goalX; j--){
                    if(Math.abs(j - posX) == Math.abs(i - posY) && b.board[i][j] != null){
                        legal = false;
                        break;
                    }
                }
            }
            for(int i = posY -1; i > goalY; i--){
                for(int j = posX +1; j < goalX; j++){
                    if(Math.abs(j - posX)== Math.abs(i - posY) && b.board[i][j] != null){
                        legal = false;
                        break;
                    }
                }
            }
            //check capture
            if(legal && b.board[goalY][goalX] != null && b.board[goalY][goalX].getPlayer().equals(this.getPlayer())){
                legal = false;
            }

        } else{
            legal = false;
        }
        return legal;
    }
    
    public int getValue() {
        return VALUE;
    }
    public Piece clone(){
        return new Bishop(this.getPlayer(), this.posY, this.posX);
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
