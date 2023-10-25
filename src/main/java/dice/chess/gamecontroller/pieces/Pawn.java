package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{

    private boolean firstMove;
    private boolean wasFirstMove;
    //Source for table values - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private static final int VALUE = 100;
    private static final int [][] STATE_POSITION_VALUES_WHITE = {
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
            {  5,  5, 10, 25, 25, 10,  5,  5 },
            {  0,  0,  0, 20, 20,  0,  0,  0 },
            {  5, -5,-10,  0,  0,-10, -5,  5 },
            {  5, 10, 10,-20,-20, 10, 10,  5 },
            {  0,  0,  0,  0,  0,  0,  0,  0 } };
    private static final int [][] STATE_POSITION_VALUES_BLACK = {
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  5, 10, 10,-20,-20, 10, 10,  5 },
            {  5, -5,-10,  0,  0,-10, -5,  5 },
            {  0,  0,  0, 20, 20,  0,  0,  0 },
            {  5,  5, 10, 25, 25, 10,  5,  5 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            {  0,  0,  0,  0,  0,  0,  0,  0 } };

    public Pawn(Player player, int posY, int posX) {
        super(PieceType.PAWN, player, posY, posX);
        firstMove = true;
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

    //On first move allowed to move one or two forward
    //After allowed to move one square only NEVER BACKWARDS.
    //Capture using diagonal move.
    public boolean moveLegal(int goalY, int goalX, Board b){
        boolean legal = true;

        if(goalX == posX && goalY == posY)
            return false;

        if(getPlayer().getPlayerColor().equals("black")) {
            if (firstMove) {
                //If current Y - end Y is not smaller or equal to 2, OR, moving backward move not allowed
                if (((goalY - posY > 2) || goalX != posX || goalY < posY || b.board[posY +1][posX] != null ||
                        ((goalY - posY) == 2 && b.board[posY +2][posX] != null)) && !capture(goalY, goalX, b)) {
                    legal = false;
                }
            } else {
                if (((goalY - posY > 1) || goalX != posX || goalY < posY || b.board[posY +1][posX] != null)
                        && !capture(goalY, goalX, b) && !enPassant(goalY, goalX, b)) {
                    legal = false;
                }

            }
        } else{
            if (firstMove) {
                //If current Y - end Y is not smaller or equal to 2, OR, moving backward move not allowed
                if ((( posY - goalY > 2) || goalX != posX || goalY > posY || b.board[posY -1][posX] != null ||
                        ((posY - goalY) == 2 && b.board[posY -2][posX] != null)) && !capture(goalY, goalX, b)) {
                    legal = false;
                }
            } else {
                if (((posY - goalY > 1) || goalX != posX || goalY > posY || b.board[posY -1][posX] != null)
                        && !capture(goalY, goalX, b) && !enPassant(goalY, goalX, b)) {
                    legal = false;
                }
            }
        }

//        System.out.println("FIRST MOVE: " + firstMove);
//        System.out.println("LEGAL: " + legal);
//        System.out.println(posY + " " + posX);
        return legal;
    }

    private boolean capture(int goalY, int goalX, Board b) {
        if (getPlayer().getPlayerColor().equals("black")) {
            if ((goalY - posY < 2) && Math.abs(goalX - posX) == 1 && goalY > posY && b.board[goalY][goalX] != null){
                if (!b.board[goalY][goalX].getPlayer().equals(this.getPlayer())) {
                    return true;
                }
            }
        } else {
            if (((posY - goalY < 2) && Math.abs(posX - goalX) == 1 && goalY < posY && b.board[goalY][goalX] != null)) {
                if (!b.board[goalY][goalX].getPlayer().equals(this.getPlayer())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean enPassant(int goalY, int goalX, Board b) {
        //Check if the last piece that was moved was a pawn
        if(b.getLastMove()!= null && !b.getLastMove().get(0).equals("PAWN")){
//            System.out.println("Pawn was not the last piece to be moved, en passant is not allowed");
            return false;
        }
        if (getPlayer().getPlayerColor().equals("black")) {
            if ((goalY - posY < 2) && Math.abs(goalX - posX) == 1 && goalY > posY && b.board[goalY][goalX] == null){
                if (b.board[posY][goalX] != null && b.board[posY][goalX].getPieceType().name().equals("PAWN")
                        && !b.board[posY][goalX].getPlayer().equals(this.getPlayer()) && ((Pawn)b.board[posY][goalX]).wasFirstMove()
                        && Math.abs(Integer.parseInt(b.getLastMove().get(2)) - posX) == 1 && posY == Integer.parseInt(b.getLastMove().get(3))
                        && Integer.parseInt(b.getLastMove().get(2)) == goalX && goalY-1 == Integer.parseInt(b.getLastMove().get(3))
                        && (6-Integer.parseInt(b.getLastMove().get(3)) == 2)) {
//                    System.out.println("Black is allowed to take white pawn at position: "+goalX + " "+goalY);
                    return true;
                }
            }
        } else {
            if (((posY - goalY < 2) && Math.abs(posX - goalX) == 1 && goalY < posY && b.board[goalY][goalX] == null)) {
                if (b.board[posY][goalX] != null && b.board[posY][goalX].getPieceType().name().equals("PAWN")
                        && !b.board[posY][goalX].getPlayer().equals(this.getPlayer()) && ((Pawn)b.board[posY][goalX]).wasFirstMove()
                        && Math.abs(Integer.parseInt(b.getLastMove().get(2)) - posX) == 1 && posY == Integer.parseInt(b.getLastMove().get(3))
                        && Integer.parseInt(b.getLastMove().get(2)) == goalX && goalY+1 == Integer.parseInt(b.getLastMove().get(3))
                        && (Integer.parseInt(b.getLastMove().get(3))-1 == 2)) {
//                    System.out.println("White is allowed to take Black pawn at position: "+goalX + " "+goalY);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean wasFirstMove() {
        return wasFirstMove;
    }

    @Override
    public void move(int goalY, int goalX) {
        super.move(goalY, goalX);
        if(firstMove){
            wasFirstMove = true;
        } else {
            wasFirstMove = false;
        }
        firstMove = false;
    }
    
    public int getValue() {
        return VALUE;
    }

    public Piece clone(){
        return new Pawn(this.getPlayer(), this.posY, this.posX);
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
