package dice.chess.ui;

import dice.chess.gamecontroller.PieceType;
import javafx.scene.image.Image;
import dice.chess.ui.pieces.DrawablePieces;
import java.util.ArrayList;
import java.util.List;


public class ChessPieceFactory {

    private static final Image pawnImageWhite = new Image(getImage("pawn", "white"));
    private static final Image pawnImageBlack = new Image(getImage("pawn", "black"));
    private static final Image rookImageWhite = new Image(getImage("rook", "white"));
    private static final Image rookImageBlack = new Image(getImage("rook", "black"));
    private static final Image bishopImageWhite = new Image(getImage("bishop", "white"));
    private static final Image bishopImageBlack = new Image(getImage("bishop", "black"));
    private static final Image knightImageWhite = new Image(getImage("knight", "white"));
    private static final Image knightImageBlack = new Image(getImage("knight", "black"));
    private static final Image queenImageWhite = new Image(getImage("queen", "white"));
    private static final Image queenImageBlack = new Image(getImage("queen", "black"));
    private static final Image kingImageWhite = new Image(getImage("king", "white"));
    private static final Image kingImageBlack = new Image(getImage("king", "black"));
    private static final String whiteColor = "WHITE";
    private static final String blackColor = "BLACK";
    private final List<DrawablePieces> list = new ArrayList<>();

    public static String getImage(String type,String color)
    {
        return "File:src/main/resources/pieces/" + type + "_" + color+".png";
    }
//public for testing
    public List<DrawablePieces> createDrawablePieces() {

        for (int i = 0; i < 8; i++) {
            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }

        list.add(new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 0, 0, blackColor, false));
        list.add( new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 7, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 1, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 6, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 2, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 5, 0, blackColor, false));
        list.add(new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, 3, 0, blackColor, false));
        list.add(new DrawablePieces(kingImageBlack, PieceType.KING, 85, 85, 4, 0, blackColor, false));

        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 0, 7, whiteColor, false));
        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 7, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 1, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 6, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 2, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 5, 7, whiteColor, false));
        list.add(new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, 3, 7, whiteColor, false));
        list.add(new DrawablePieces(kingImageWhite, PieceType.KING, 85, 85, 4, 7, whiteColor, false));

        return list;
    }

    public List<DrawablePieces> createPiecesChallenge1() {

        for (int i = 0; i < 8; i++) {
            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
//            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }
        for (int i = 0; i < 6; i++) {
//            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }

        list.add(new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 0, 0, blackColor, false));
        list.add( new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 7, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 1, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 6, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 2, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 5, 0, blackColor, false));
        list.add(new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, 3, 0, blackColor, false));
        list.add(new DrawablePieces(kingImageBlack, PieceType.KING, 85, 85, 4, 0, blackColor, false));

        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 0, 7, whiteColor, false));
        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 7, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 1, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 6, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 2, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 5, 7, whiteColor, false));
        list.add(new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, 3, 7, whiteColor, false));
        list.add(new DrawablePieces(kingImageWhite, PieceType.KING, 85, 85, 4, 7, whiteColor, false));

        return list;
    }

    public List<DrawablePieces> createPiecesChallenge2() {

        for (int i = 0; i < 8; i++) {
            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }

        list.add(new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 0, 0, blackColor, false));
        list.add( new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 7, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 1, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 6, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 2, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 5, 0, blackColor, false));
        list.add(new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, 3, 0, blackColor, false));
        list.add(new DrawablePieces(kingImageBlack, PieceType.KING, 85, 85, 4, 0, blackColor, false));

        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 0, 7, whiteColor, false));
        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 7, 7, whiteColor, false));
        //list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 1, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 6, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 2, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 5, 7, whiteColor, false));
        list.add(new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, 3, 7, whiteColor, false));
        list.add(new DrawablePieces(kingImageWhite, PieceType.KING, 85, 85, 4, 7, whiteColor, false));

        return list;
    }

    public List<DrawablePieces> createPiecesChallenge1and2() {

        for (int i = 0; i < 8; i++) {
            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
//            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }
        for (int i = 0; i < 6; i++) {
//            list.add(new DrawablePieces(pawnImageBlack, PieceType.PAWN, 85, 85, i, 1, blackColor, false));
            list.add(new DrawablePieces(pawnImageWhite, PieceType.PAWN, 85, 85, i, 6, whiteColor, false));
        }

        list.add(new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 0, 0, blackColor, false));
        list.add( new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, 7, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 1, 0, blackColor, false));
        list.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, 6, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 2, 0, blackColor, false));
        list.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, 5, 0, blackColor, false));
        list.add(new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, 3, 0, blackColor, false));
        list.add(new DrawablePieces(kingImageBlack, PieceType.KING, 85, 85, 4, 0, blackColor, false));

        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 0, 7, whiteColor, false));
        list.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, 7, 7, whiteColor, false));
        //list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 1, 7, whiteColor, false));
        list.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, 6, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 2, 7, whiteColor, false));
        list.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, 5, 7, whiteColor, false));
        list.add(new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, 3, 7, whiteColor, false));
        list.add(new DrawablePieces(kingImageWhite, PieceType.KING, 85, 85, 4, 7, whiteColor, false));

        return list;
    }

}
