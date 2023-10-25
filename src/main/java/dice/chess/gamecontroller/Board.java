package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.*;
import javafx.scene.image.Image;
import dice.chess.ui.Settings;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.*;

import static dice.chess.ui.ChessPieceFactory.getImage;

public class Board {
    public Player player1 = new Player("black");
    public Player player2 = new Player("white");
    public Piece[][] board = new Piece[8][8];
    private List<String> lastMove;
    public final  List<DrawablePieces> drawablePieces;
    private boolean ended = false;

    private static final Image rookImageWhite = new Image(getImage("rook", "white"));
    private static final Image rookImageBlack = new Image(getImage("rook", "black"));
    private static final Image bishopImageWhite = new Image(getImage("bishop", "white"));
    private static final Image bishopImageBlack = new Image(getImage("bishop", "black"));
    private static final Image knightImageWhite = new Image(getImage("knight", "white"));
    private static final Image knightImageBlack = new Image(getImage("knight", "black"));
    private static final Image queenImageWhite = new Image(getImage("queen", "white"));
    private static final Image queenImageBlack = new Image(getImage("queen", "black"));
    private static final String whiteColor = "WHITE";
    private static final String blackColor = "BLACK";
    private Random rand = new Random();


    public Board(List<DrawablePieces> drawablePieces){
        this.drawablePieces = drawablePieces;

        if(Settings.isChallenge1()&& Settings.isChallenge2()) {
            setUpChallengeMode1and2();
        }
        else if(Settings.isChallenge1())
        {
            setUpChallengeMode1();
        }
        else if(Settings.isChallenge2())
        {
            setUpChallengeMode2();
        }
        else {
            setUpBoard();
        }

        //rooks in the corners
        board[0][0] = new Rook(player1,0,0);
        board[0][7] = new Rook(player1,0,7);
        player1.getPieces().add(board[0][0]);
        player1.getPieces().add(board[0][7]);
        board[7][0] = new Rook(player2,7,0);
        board[7][7] = new Rook(player2,7,7);
        player2.getPieces().add(board[7][0]);
        player2.getPieces().add(board[7][7]);

        //bishops next to knights
        board[0][2] = new Bishop(player1,0,2);
        board[0][5] = new Bishop(player1,0,5);
        player1.getPieces().add(board[0][2]);
        player1.getPieces().add(board[0][5]);
        board[7][2] = new Bishop(player2,7,2);
        board[7][5] = new Bishop(player2,7,5);
        player2.getPieces().add(board[7][2]);
        player2.getPieces().add(board[7][5]);

        //queen goes on the right of left-side
        board[0][3] = new Queen(player1,0,3);
        player1.getPieces().add(board[0][3]);
        board[7][3] = new Queen(player2,7,3);
        player2.getPieces().add(board[7][3]);

        //King goes in last remaining spot
        board[0][4] = new King(player1,0,4);
        player1.getPieces().add(board[0][4]);
        board[7][4] = new King(player2,7,4);
        player2.getPieces().add(board[7][4]);

    }


    public void setUpBoard()
    {
        for(int i=0; i < board.length; i++){
            board[1][i] = new Pawn(player1, 1, i);
            board[6][i] = new Pawn(player2, 6, i);
            player1.getPieces().add(board[1][i]);
            player2.getPieces().add(board[6][i]);
        }

        //knights next to rook
        board[0][1] = new Knight(player1, 0,1);
        board[0][6] = new Knight(player1, 0,6);
        player1.getPieces().add(board[0][1]);
        player1.getPieces().add(board[0][6]);
        board[7][1] = new Knight(player2, 7,1);
        board[7][6] = new Knight(player2, 7,6);
        player2.getPieces().add(board[7][1]);
        player2.getPieces().add(board[7][6]);

    }

    public void setUpChallengeMode1()
    {
        for(int i=0; i < board.length; i++){
            board[1][i] = new Pawn(player1, 1, i);
            player1.getPieces().add(board[1][i]);
        }

        for(int i=0; i < board.length-2; i++){
            board[6][i] = new Pawn(player2, 6, i);
            player2.getPieces().add(board[6][i]);
        }
        //knights next to rook
        board[0][1] = new Knight(player1, 0,1);
        board[0][6] = new Knight(player1, 0,6);
        player1.getPieces().add(board[0][1]);
        player1.getPieces().add(board[0][6]);
        board[7][1] = new Knight(player2, 7,1);
        board[7][6] = new Knight(player2, 7,6);
        player2.getPieces().add(board[7][1]);
        player2.getPieces().add(board[7][6]);

    }

    public void setUpChallengeMode2()
    {
        for(int i=0; i < board.length; i++){
            board[1][i] = new Pawn(player1, 1, i);
            board[6][i] = new Pawn(player2, 6, i);
            player1.getPieces().add(board[1][i]);
            player2.getPieces().add(board[6][i]);
        }

        //knights next to rook
        board[0][1] = new Knight(player1, 0,1);
        board[0][6] = new Knight(player1, 0,6);
        player1.getPieces().add(board[0][1]);
        player1.getPieces().add(board[0][6]);
        //board[7][1] = new Knight(player2, 7,1);
        board[7][6] = new Knight(player2, 7,6);
        //player2.getPieces().add(board[7][1]);
        player2.getPieces().add(board[7][6]);



    }
    private void setUpChallengeMode1and2() {

        for(int i=0; i < board.length; i++){
            board[1][i] = new Pawn(player1, 1, i);
            player1.getPieces().add(board[1][i]);
        }

        for(int i=0; i < board.length-2; i++){
            board[6][i] = new Pawn(player2, 6, i);
            player2.getPieces().add(board[6][i]);
        }

        //knights next to rook
        board[0][1] = new Knight(player1, 0,1);
        board[0][6] = new Knight(player1, 0,6);
        player1.getPieces().add(board[0][1]);
        player1.getPieces().add(board[0][6]);
        //board[7][1] = new Knight(player2, 7,1);
        board[7][6] = new Knight(player2, 7,6);
        //player2.getPieces().add(board[7][1]);
        player2.getPieces().add(board[7][6]);

    }


    public void movePiece(Piece piece, int posY, int posX){

        if(board[posY][posX] != null && !piece.getPlayer().equals(board[posY][posX].getPlayer())){
            if(board[posY][posX].getPieceType().name().equals("KING")){
                ended = true;
            }
            if(piece.getPlayer().getPlayerColor().equals("black")){
                player2.getPieces().remove(board[posY][posX]);
            } else {
                player1.getPieces().remove(board[posY][posX]);
            }
//            board[posY][posX].getPlayer().getPieces().remove(board[posY][posX]);
        } else if(board[posY][posX] == null && piece.getPieceType().name().equals("PAWN") && ((Pawn)piece).enPassant(posY, posX, this)){
            if(piece.getPlayer().getPlayerColor().equals("black")){
                player2.getPieces().remove(board[posY-1][posX]);
                board[posY-1][posX] = null;
            } else {
                player1.getPieces().remove(board[posY+1][posX]);
                board[posY+1][posX] = null;
            }
        } else if (board[posY][posX] == null && piece.getPieceType().name().equals("KING") && ((King)piece).castling(posX, this)){
            if (posX == 2 && piece.getPosX() == 4 && (piece.getPosY() == posY)){
                Piece rook = board[posY][0];
                board[posY][0] = null;
                rook.move(posY,3);
                board[posY][0] = rook;
            }
            else if (posX == 6 && piece.getPosX() == 4 && (piece.getPosY() == posY)){
                Piece rook = board[posY][7];
                board[posY][7] = null;
                rook.move(posY,5);
                board[posY][7] = rook;
            }
        }
        board[piece.getPosY()][piece.getPosX()] = null;
        piece.move(posY, posX);
        if(piece.getPieceType().name().equals("PAWN") && piece.getPlayer().getPlayerColor().equals("white") && posY == 0){

            for(int i = 0; i < drawablePieces.size(); i++) {
                if(drawablePieces.get(i).isSelected()){
                    drawablePieces.remove(i);
                    break;
                }
            }
            player2.getPieces().remove(piece);

            int next = rand.nextInt(4);

            if(next==0) {
                piece = new Rook(piece.getPlayer(), posY, posX);
                //NEW PIECE CREATED NOW NEED TO ADD IT TO THE BOARD
//                ChessPieceFactory.list.add((new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, posX, posY, whiteColor, false)));
                drawablePieces.add(new DrawablePieces(rookImageWhite, PieceType.ROOK, 85, 85, posX, posY, whiteColor, false));
                player2.getPieces().add(piece);
//                System.out.println("WHITE PAWN upgraded to WHITE ROOK");
            }
            if(next==1){
                piece = new Bishop(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, posX, posY, whiteColor, false)));
                drawablePieces.add(new DrawablePieces(bishopImageWhite, PieceType.BISHOP, 85, 85, posX, posY, whiteColor, false));
                player2.getPieces().add(piece);
//                System.out.println("WHITE PAWN upgraded to WHITE BISHOP");

            }
            if(next==2){
                piece = new Knight(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, posX, posY, whiteColor, false)));
                drawablePieces.add(new DrawablePieces(knightImageWhite, PieceType.KNIGHT, 85, 85, posX, posY, whiteColor, false));
                player2.getPieces().add(piece);
//                System.out.println("WHITE PAWN upgraded to WHITE KNIGHT");

            }
            if(next==3){
                piece = new Queen(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, posX, posY, whiteColor, false)));
                drawablePieces.add(new DrawablePieces(queenImageWhite, PieceType.QUEEN, 85, 85, posX, posY, whiteColor, false));
                player2.getPieces().add(piece);
//                System.out.println("WHITE PAWN upgraded to WHITE QUEEN");
            }
        } else if(piece.getPieceType().name().equals("PAWN") && piece.getPlayer().getPlayerColor().equals("black") && posY == 7){
            for(int i =0; i < drawablePieces.size(); i++){
                if(drawablePieces.get(i).isSelected()){
                    drawablePieces.remove(i);
                    break;
                }
            }
//            drawablePieces.remove(piece);
            player1.getPieces().remove(piece);

            int next = rand.nextInt(4);

            if(next==0) {
                piece = new Rook(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, posX, posY, blackColor, false)));
                drawablePieces.add(new DrawablePieces(rookImageBlack, PieceType.ROOK, 85, 85, posX, posY, blackColor, false));
                player1.getPieces().add(piece);
//                System.out.println("BLACK PAWN upgraded to BLACK ROOK");

            }
            if(next==1){
                piece = new Bishop(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, posX, posY, blackColor, false)));
                drawablePieces.add(new DrawablePieces(bishopImageBlack, PieceType.BISHOP, 85, 85, posX, posY, blackColor, false));
                player1.getPieces().add(piece);
//                System.out.println("BLACK PAWN upgraded to BLACK BISHOP");
            }
            if(next==2){
                piece = new Knight(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, posX, posY, blackColor, false)));
                drawablePieces.add(new DrawablePieces(knightImageBlack, PieceType.KNIGHT, 85, 85, posX, posY, blackColor, false));
                player1.getPieces().add(piece);
//                System.out.println("BLACK PAWN upgraded to BLACK KNIGHT");
            }
            if(next==3){
                piece = new Queen(piece.getPlayer(), posY, posX);
//                ChessPieceFactory.list.add((new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, posX, posY, blackColor, false)));
                drawablePieces.add(new DrawablePieces(queenImageBlack, PieceType.QUEEN, 85, 85, posX, posY, blackColor, false));
                player1.getPieces().add(piece);
//                System.out.println("BLACK PAWN upgraded to BLACK QUEEN");
            }
        }
        board[posY][posX] = piece;
    }
    public void setLastMove(String name,String playerColor, String posX, String posY){
        lastMove = new LinkedList<>();
        lastMove.add(name);
        lastMove.add(playerColor);
        lastMove.add(posX);
        lastMove.add(posY);
    }
    public List<String> getLastMove(){
        return lastMove;
    }

    public boolean isEnded(){
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Board clone() {

        ArrayList<DrawablePieces> listClone = new ArrayList<>();
        Iterator<DrawablePieces> iterator = drawablePieces.iterator();

        while(iterator.hasNext()) {
            //Add the object clones
            listClone.add(iterator.next().clone());
        }
        Board board = new Board(listClone);
        for(int i=0; i < this.board.length; i++){
            for(int j=0; j<this.board.length; j++){
                if(this.board[i][j] == null){
                    board.board[i][j] = null;
                }else {
                    board.board[i][j] = this.board[i][j].clone();
                }
            }
        }

        board.player1.setPieces(player1.clonePieces());
        board.player2.setPieces(player2.clonePieces());

        if(lastMove != null){
            board.setLastMove(this.lastMove.get(0),this.lastMove.get(1), this.lastMove.get(2), this.lastMove.get(3));
        }
        if(ended){
            board.setEnded(true);
        }
        return board;
    }

}


