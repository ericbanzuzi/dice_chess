package dice.chess.ui.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import dice.chess.gamecontroller.PieceType;


public class DrawablePieces extends ImageView {
    
    private boolean selected = false;
    private final String color;
    private final String name;
    private final PieceType pieceType;

    
    public DrawablePieces(Image image, PieceType pieceType, double height, double width, int centerX, int centerY,
            String color,  boolean selected) {
        super(image);
        
        this.setX(centerX);
        this.setY(centerY);
        this.name = pieceType.name();
        this.color = color;
        this.pieceType = pieceType;
        this.setFitHeight(height);
        this.setFitWidth(width);
        this.setPreserveRatio(true);
        this.setIsSelected(selected);
    }
    
    public void setIsSelected(boolean selected) {
        
        this.selected = selected;
    }
    
    public boolean isSelected() {
        
        return this.selected;
    }
    
    public String getColor() {
        
        return this.color;
    }
    
    public String getPieceName(){
        
        return this.name;
    }
    
    public int getXAsInt(){
        
        return (int) this.getX();
    }
    
    public int getYAsInt(){
        
        return (int) this.getY();
    }

    public DrawablePieces clone(){
        return new DrawablePieces(super.getImage(), this.pieceType, this.getFitHeight(), this.getFitWidth(),
                this.getXAsInt(), this.getYAsInt(), this.color, this.selected);
    }
}
