package dice.chess.gamecontroller.pieces;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.PieceType;
import dice.chess.gamecontroller.Player;

import java.util.List;

public abstract class Piece {

    private final PieceType pieceType;
    private final Player player;
    protected int posY;
    protected int posX;
    protected int value;
    protected int [][] statePositionValues;

    public Piece(PieceType pieceType, Player player, int posY, int posX) {
        this.pieceType = pieceType;
        this.player = player;
        this.posY = posY;
        this.posX = posX;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int y) {
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int x) {
        this.posX = x;
    }

    public abstract boolean moveLegal(int goalY, int goalX, Board b);

    public abstract List<int[]> calculateLegalMoves(Board b);

    public abstract int getValue();

    public abstract Piece clone();

    public abstract int [][] getStatePositionValues();

    public abstract int [][] getStatePositionValuesWhite();

    public abstract int [][] getStatePositionValuesBlack();

    public void move(int goalY, int goalX) {
        this.posY = goalY;
        this.posX = goalX;
    }

}
