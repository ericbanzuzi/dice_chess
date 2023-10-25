 package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 public class Player {

    private List<Piece> pieces;
    private String playerColor;
    private int timeLeft;

    public Player(String color){
        this.timeLeft = 600;
        this.playerColor = color;
        pieces = new ArrayList<>();
    }

     public String getPlayerColor() {
         return playerColor;
     }

     public int getTimeLeft(){
        return this.timeLeft;
    }

     public List<Piece> getPieces() {
         return pieces;
     }

     public void setPieces(List<Piece> pieces){
        this.pieces = pieces;
     }

     public List<String> movablePieces(Board b) {
         List<String> pieceTypes = new ArrayList<>();
        for(int i = 0; i < pieces.size(); i++){
            List<int[]> moves = pieces.get(i).calculateLegalMoves(b);
            if(moves.size() > 0 && !pieceTypes.contains(pieces.get(i).getPieceType().name())){
                pieceTypes.add(pieces.get(i).getPieceType().name());
            }
        }
        return pieceTypes;
     }

     public void decreaseTime() { this.timeLeft--; }

     public List<Piece> clonePieces(){
         ArrayList<Piece> listClone = new ArrayList<>();
         Iterator<Piece> iterator = pieces.iterator();
         while(iterator.hasNext()) {
             //Add the object clones
             listClone.add(iterator.next().clone());
         }
         return listClone;
     }
}
