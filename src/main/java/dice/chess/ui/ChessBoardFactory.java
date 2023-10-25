package dice.chess.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class ChessBoardFactory {
    public static Paint color1 = Color.rgb(243, 218, 178);
    public static Paint color2 = Color.rgb(188, 135, 94);

    protected List<Rectangle> createDrawableBoard() {

        List<Rectangle> drawableGrid = new ArrayList<>();
        int rectangleSize = 100;
        int boardSize = 8;
        int count = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Rectangle rectangle = new Rectangle(rectangleSize, rectangleSize, rectangleSize, rectangleSize);
                if (count % 2 == 0) {
                    rectangle.setFill(color1);
                }
                else {
                    rectangle.setFill(color2);
                }
                rectangle.setX(j);
                rectangle.setY(i);
                drawableGrid.add(rectangle);
                count++;
            }
            count++;
        }
        return drawableGrid;
    }

}