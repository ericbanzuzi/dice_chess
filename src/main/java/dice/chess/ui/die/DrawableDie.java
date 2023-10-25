package dice.chess.ui.die;

import javafx.scene.shape.Box;

import java.util.List;
import java.util.Random;

public class DrawableDie {
    private int result;
    private Box box;

    public DrawableDie(int depth, int height, int width){
        this.box = new Box();
        this.box.setDepth(depth);
        this.box.setHeight(height);
        this.box.setWidth(width);
    }

    public void roll(List<String> pieces){
        int[] draw = new int[pieces.size()];
        for(int i = 0; i < pieces.size(); i++){
            draw[i] = getPieceID(pieces.get(i));
        }
        int rnd = new Random().nextInt(draw.length);
        this.result = draw[rnd];
    }

    public int getResult(){
        return this.result;
    }

    public void resetResult(){
        this.result = 0;
    }

    public Box getDie(){
        return this.box;
    }

    public int getPieceID(String piece){
        int n = 0;
        switch(piece){
            case "PAWN":
                n = 1;
                break;
            case "KNIGHT":
                n = 2;
                break;
            case "BISHOP":
                n = 3;
                break;
            case "ROOK":
                n = 4;
                break;
            case "QUEEN":
                n = 5;
                break;
            case "KING":
                n = 6;
                break;
        }
        return n;
    }

    public String getPiece(int nbr){
        String string = "";
        switch(nbr){
            case 1:
                string = "PAWN";
                break;
            case 2:
                string = "KNIGHT";
                break;
            case 3:
                string = "BISHOP";
                break;
            case 4:
                string = "ROOK";
                break;
            case 5:
                string = "QUEEN";
                break;
            case 6:
                string = "KING";
                break;
        }
        return string;
    }
}
