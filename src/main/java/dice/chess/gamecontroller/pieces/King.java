package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{

    private boolean hasNotMoved;
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 20000;
    private static final int [][] STATE_POSITION_VALUES_WHITE = {
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -20,-30,-30,-40,-40,-30,-30,-20 },
            { -10,-20,-20,-20,-20,-20,-20,-10 },
            {  20, 20,  0,  0,  0,  0, 20, 20 },
            {  20, 30, 10,  0,  0, 10, 30, 20 } };
    private static final int [][] STATE_POSITION_VALUES_BLACK = {
            {  20, 30, 10,  0,  0, 10, 30, 20 },
            {  20, 20,  0,  0,  0,  0, 20, 20 },
            { -10,-20,-20,-20,-20,-20,-20,-10 },
            { -20,-30,-30,-40,-40,-30,-30,-20 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 },
            { -30,-40,-40,-50,-50,-40,-40,-30 } };

    public King(Player player, int posY, int posX) {
        super(PieceType.KING, player, posY, posX);
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

    //May move one spot every in any direction around it.
    public boolean moveLegal(int goalY, int goalX, Board b){
        if ((checkLeftMostEmptyPositions(b) && checkRightMostEmptyPositions(b)) && hasNotMoved){
            return (Math.abs(goalY - posY) < 2 && Math.abs(goalX - posX) < 2 && (b.board[goalY][goalX] == null
                    || !b.board[goalY][goalX].getPlayer().equals(this.getPlayer())))
                    || (goalY == this.getPosY() && ((goalX == 2 && b.board[posY][0] != null && b.board[posY][0].getPieceType().equals(PieceType.ROOK) && ((Rook)b.board[posY][0]).getHasNotMoved())
                    || (goalX == 6 && b.board[posY][7] != null && b.board[posY][7].getPieceType().equals(PieceType.ROOK) &&  ((Rook)b.board[posY][7]).getHasNotMoved())));
        }
        else if ((checkLeftMostEmptyPositions(b) && !checkRightMostEmptyPositions(b)) && hasNotMoved){
            return (Math.abs(goalY - posY) < 2 && Math.abs(goalX - posX) < 2 && (b.board[goalY][goalX] == null
                    || !b.board[goalY][goalX].getPlayer().equals(this.getPlayer())))
                    || (goalY == this.getPosY() && b.board[posY][0] != null && (goalX == 2 && b.board[posY][0].getPieceType().equals(PieceType.ROOK) &&  ((Rook)b.board[posY][0]).getHasNotMoved()));
        }
        else if ((!checkLeftMostEmptyPositions(b) && checkRightMostEmptyPositions(b)) && hasNotMoved){
            return (Math.abs(goalY - posY) < 2 && Math.abs(goalX - posX) < 2 && (b.board[goalY][goalX] == null
                    || !b.board[goalY][goalX].getPlayer().equals(this.getPlayer())))
                    || (goalY == this.getPosY() && b.board[posY][7] != null && (goalX == 6 && b.board[posY][7].getPieceType().equals(PieceType.ROOK) &&  ((Rook)b.board[posY][7]).getHasNotMoved()));
        }
        else{
            return (Math.abs(goalY - posY) < 2 && Math.abs(goalX - posX) < 2 && (b.board[goalY][goalX] == null
                    || !b.board[goalY][goalX].getPlayer().equals(this.getPlayer())));
        }
    }

    @Override
    public void move(int goalY, int goalX) {
        super.move(goalY, goalX);
        hasNotMoved = false;
    }
    
    public boolean castling(int goalX, Board b) {
        if(!hasNotMoved) {
            return false;
        }
        
        boolean canCastle = false;
        if(b.board[posY][4] != null && b.board[posY][4].getPlayer().equals(this.getPlayer())) {
            if (goalX == 2 && posX == 4) {
                return checkLeftMostEmptyPositions(b);
            }
            else if(goalX == 6 && posX == 4) {
                return checkRightMostEmptyPositions(b);
            }
        }
        return canCastle;
    }
    
    private boolean checkLeftMostEmptyPositions(Board b){
        for (int i = posX - 1; i > 0; i--) {
            if (b.board[posY][i] != null) {
                return false;
            }
        }
        return true;
    }
    private boolean checkRightMostEmptyPositions(Board b){
        for(int i = posX + 1; i < 7; i++) {
            if (b.board[posY][i] != null) {
                return false;
            }
        }
        return true;
    }
    
    public int getValue() {
        return VALUE;
    }

    public Piece clone(){
        return new King(this.getPlayer(), this.posY, this.posX);
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
