package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{

    private boolean hasNotMoved;
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 500;
    private static final int [][] STATE_POSITION_VALUES_WHITE = {
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  5, 10, 10, 10, 10, 10, 10,  5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            {  0,  0,  0,  5,  5,  0,  0,  0 } };
    private static final int [][] STATE_POSITION_VALUES_BLACK = {
            {  0,  0,  0,  5,  5,  0,  0,  0 } ,
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            { -5,  0,  0,  0,  0,  0,  0, -5 },
            {  5, 10, 10, 10, 10, 10, 10,  5 },
            {  0,  0,  0,  0,  0,  0,  0,  0 } };

    public Rook(Player player, int posY, int posX) {
        super(PieceType.ROOK, player, posY, posX);
        hasNotMoved = true;
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

    public boolean getHasNotMoved() {
        return hasNotMoved;
    }

    //moves in a straight line either vertically or horizontally
    public boolean moveLegal(int goalY, int goalX, Board b){
        boolean legal = true;
        
        if(goalX != posX && goalY != posY){
            legal = false;
        } else {
            if (goalX != posX) {
                if (goalX > posX) {
                    for (int i = posX + 1; i < goalX; i++) {
                        if (b.board[posY][i] != null) {
                                legal = false;
                        }
                    }
                }
                if (goalX < posX) {
                    for (int i = goalX + 1; i < posX; i++) {
                        if (b.board[posY][i] != null) {
                                legal = false;
                        }
                    }

                }
                // check capture
                if (legal && b.board[posY][goalX] != null && b.board[posY][goalX].getPlayer().equals(this.getPlayer())) {
                    legal = false;
                }

            } else {
                if (goalY > posY) {
                    for (int i = posY + 1; i < goalY; i++) {
                        if (b.board[i][posX] != null) {
                                legal = false;
                        }
                    }
                }
                if (goalY < posY) {
                    for (int i = goalY + 1; i < posY; i++) {
                        if (b.board[i][posX] != null) {
                                legal = false;
                        }
                    }
                }
                // check capture
                if (legal && b.board[goalY][posX] != null && b.board[goalY][posX].getPlayer().equals(this.getPlayer())) {
                    legal = false;
                }
            }
        }
        return legal;
    }

    @Override
    public void move(int goalY, int goalX) {
        super.move(goalY, goalX);
        hasNotMoved = false;
    }
    
    public int getValue() {
        return VALUE;
    }

    public Piece clone(){
        return new Rook(this.getPlayer(), this.posY, this.posX);
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
