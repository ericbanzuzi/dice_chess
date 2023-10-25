package dice.chess.ui;

import dice.chess.agents.ExpectiMaxAgent;
import dice.chess.agents.MiniMaxAgent;
import dice.chess.agents.MoveState;
import dice.chess.agents.RandomAgent;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.DiceGame;
import dice.chess.gamecontroller.pieces.King;
import dice.chess.gamecontroller.pieces.Pawn;
import dice.chess.gamecontroller.pieces.Piece;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import dice.chess.ui.die.DrawableDie;
import dice.chess.ui.pieces.DrawablePieces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ChessBoardWindow extends Group{

    public static boolean training = false;
    private static int whiteWon = 0;
    private static int blackWon = 0;

    private static final int MAP_HEIGHT = 800;
    private static final int MAP_WIDTH = 800;
    private static String gameMode;
    private int columnIndex;
    private int rowIndex;
    private int columnIndexPiece = -1;
    private int rowIndexPiece = -1;
    private static int cb0int;
    private static int cb1int;
    private DrawablePieces selectedPiece = null;
    private DrawablePieces pieceToBeTaken = null;
    public final List<DrawablePieces> takenPieces;
    private List<DrawablePieces> drawablePieces;
    private List<Rectangle> drawableGrid;
    private final DrawableDie drawableDie;
    private Text lastMoveText;
    private Text gameWonText;
    private String selectedPieceColor;
    private final DiceGame diceGame;
    private final Board board;
    private boolean end;
    private RandomAgent randomAgent;
    private MiniMaxAgent miniMaxAgent;
    private MiniMaxAgent alphaBetaAgent;
    private ExpectiMaxAgent expectiMaxAgent;
    private ExpectiMaxAgent expectiMaxPruningAgent;
    private ImageView rollImage;
    private String move = "src/main/resources/sound/move.wav";
    private String takes = "src/main/resources/sound/takes.wav";
    private String type;
    private ListView moveHistory;
    private ArrayList boardHistory;
    private int countReplay=0;
    private int[] old;
    private DrawablePieces oldPiece;
    private TranslateTransition tt = new TranslateTransition();
    private static boolean picked;


    private Media moveSound = new Media(new File(move).toURI().toString());
    private Media takesSound = new Media(new File(takes).toURI().toString());
    private MediaPlayer moveSoundPlayer = new MediaPlayer(moveSound);
    private MediaPlayer takesSoundPlayer = new MediaPlayer(takesSound);

    @FXML
    public GridPane mainPane;
    public BorderPane borderPane;
    public BorderPane borderPaneTakenPieces;

    /**
     * Add all Shapes to the BoardView and AnchorPane to be rendered
     */
    public ChessBoardWindow() {


        this.prefHeight(MAP_HEIGHT);
        this.prefWidth(MAP_WIDTH);

        this.mainPane = new GridPane();
        this.borderPane = new BorderPane();
        this.borderPaneTakenPieces = new BorderPane();
        moveHistory = new ListView();
        boardHistory = new ArrayList<Board>();

        randomAgent = new RandomAgent();
        if (!training){
            picked = false;
            miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
            alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
            expectiMaxAgent = new ExpectiMaxAgent(4, false);
            expectiMaxPruningAgent = new ExpectiMaxAgent(4, true);
        }else{
            this.initAI();
        }
        //Create 8x8 black and white board

        //Create 8x8 ChessBoardFactory.color1 and ChessBoardFactory.color2 board
        ChessBoardFactory chessBoardFactory = new ChessBoardFactory();
        this.drawableGrid = chessBoardFactory.createDrawableBoard();

        //Create chess pieces
        ChessPieceFactory chessPieceFactory = new ChessPieceFactory();
        if(Settings.isChallenge1() && Settings.isChallenge2())
        {
            this.drawablePieces = chessPieceFactory.createPiecesChallenge1and2();

        }
        else if (Settings.isChallenge1())
        {
            this.drawablePieces = chessPieceFactory.createPiecesChallenge1();
        }
        else if(Settings.isChallenge2())
        {
            this.drawablePieces = chessPieceFactory.createPiecesChallenge2();
        }
        else
        {
            this.drawablePieces = chessPieceFactory.createDrawablePieces();
        }

        this.takenPieces = new ArrayList<>();
        this.drawableDie = new DrawableDie(100,100,100);
        end = false;

        this.diceGame = new DiceGame(drawablePieces);
        this.board = diceGame.getBoard();

        boardHistory.add(board.clone());
        renderInitialBoard();
        renderDie();
        newRoll();
    }

    protected static void display(String title, String mode) {
        gameMode = mode;
        try {
            // Load the FXML for the chess board
            Parent root;
            URL boardUrl = new File("src/main/java/dice/chess/ui/board.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(boardUrl);
            root = loader.load();
            Scene scene = new Scene(root,1300, 850);
            URL cssUrl = new File("src/main/java/dice/chess/ui/application.css").toURI().toURL();
            scene.getStylesheets().add(cssUrl.toString());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            root.getChildrenUnmodifiable();
            root.requestFocus();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected static void display(String title, String mode, int[] chosenAI){
        picked = true;
        cb0int = chosenAI[0];
        cb1int = chosenAI[1];
        display(title, mode);
    }

    public void movePiece(int columnIndex, int rowIndex) {

        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        if(gameMode.equals("mode1") && Settings.isAnimateMoves())
            this.update();
        // Check if a board piece was clicked on
        // If yes - mark it as selected
        for (DrawablePieces drawablePiece : this.drawablePieces) {
            if (drawablePiece.getXAsInt() == this.columnIndex && drawablePiece.getYAsInt() == this.rowIndex
                    && this.selectedPiece == null && drawablePiece.getPieceName().equals(drawableDie.getPiece(drawableDie.getResult()))
                    && drawablePiece.getColor().equalsIgnoreCase(diceGame.getPlaying().getPlayerColor())) {
                this.columnIndexPiece = columnIndex;
                this.rowIndexPiece = rowIndex;
                drawablePiece.setIsSelected(true);
                this.selectedPiece = drawablePiece;
            }
            else if (drawablePiece.getXAsInt() == this.columnIndex && drawablePiece.getYAsInt() == this.rowIndex
                    && this.selectedPiece != null && this.selectedPiece.getColor().equalsIgnoreCase(drawablePiece.getColor())
                    && drawablePiece.getPieceName().equals(drawableDie.getPiece(drawableDie.getResult()))
                    && drawablePiece.getColor().equalsIgnoreCase(diceGame.getPlaying().getPlayerColor())){
                this.selectedPiece.setIsSelected(false);
                this.columnIndexPiece = columnIndex;
                this.rowIndexPiece = rowIndex;
                drawablePiece.setIsSelected(true);
                this.selectedPiece = drawablePiece;
            }
            else if (drawablePiece.getXAsInt() == this.columnIndex && drawablePiece.getYAsInt() == this.rowIndex
                    && this.selectedPiece != null && !this.selectedPiece.getColor().equalsIgnoreCase(drawablePiece.getColor())
                    && this.selectedPiece.getPieceName().equals(drawableDie.getPiece(drawableDie.getResult()))
                    && this.selectedPiece.getColor().equalsIgnoreCase(diceGame.getPlaying().getPlayerColor())){
                this.pieceToBeTaken = drawablePiece;
                this.columnIndexPiece = columnIndex;
                this.rowIndexPiece = rowIndex;
            }
        }

        // Highlight the selected piece
        for (Rectangle rectangle : this.drawableGrid) {
            highlightSelectedPiece(rectangle);
        }

        if(Settings.isHighlightPieceMoves()){
            highlightSelectedPieceMoves();
        }
        this.type = "move";
        // Check if piece is moved to an empty square
        // or taking opponent's piece
        if (this.pieceToBeTaken != null){
            for (DrawablePieces drawablePiece : this.drawablePieces) {
                if (drawablePiece.isSelected()
                        && board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()].moveLegal(this.rowIndex, this.columnIndex, this.board)) {
                    this.drawablePieces.remove(this.pieceToBeTaken);
                    this.type = "takes";
                    System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                            " took " +  this.pieceToBeTaken.getColor() + " " + this.pieceToBeTaken.getPieceName() + " on column " + this.columnIndex + " and row " + this.rowIndex);
                    renderText(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                            " took " +  this.pieceToBeTaken.getColor() + " " + this.pieceToBeTaken.getPieceName() + " on column " + this.columnIndex + " and row " + this.rowIndex);
                    addMoveHistory(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                            " took " +  this.pieceToBeTaken.getColor() + " " + this.pieceToBeTaken.getPieceName() + " on column " + this.columnIndex + " and row " + this.rowIndex);
                    this.takenPieces.add(this.pieceToBeTaken);
                    renderTakenPieces();
                    renderBoard(drawablePiece);
                    boardHistory.add(board.clone());
                    board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.columnIndex),String.valueOf(this.rowIndex));

                    // Here we check if the pieceToBeTaken is equal to king
                    if (this.pieceToBeTaken.getPieceName().equals("KING")){
                        String wonColor = drawablePiece.getColor();
                        if (training){
                            Piece movingPiece = board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()];
                            this.expectiMaxAgent.updateQAgentAfterMove(this, this.board, new MoveState(movingPiece, new int []{this.rowIndex, this.columnIndex}, 0));
                        }
                        end = true;
                        endGame(wonColor,wonColor + " player won the game!");
                        this.diceGame.finishGame();
                    }
                    else if (this.isDraw()){
                        end = true;
                        endGame(null,"DRAW!");
                        this.diceGame.finishGame();
                    }
                    if(!end) {
                        this.newRoll();
                    }
                    break;
                }
            }
            this.pieceToBeTaken = null;
        }
        else{
            for (DrawablePieces drawablePiece : this.drawablePieces) {
                if ((this.columnIndexPiece != this.columnIndex || this.rowIndexPiece != this.rowIndex) && drawablePiece.isSelected()
                        && board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()].moveLegal(this.rowIndex, this.columnIndex, this.board)) {

                    if(this.selectedPiece.getPieceName().equals("PAWN")
                            && ((Pawn) board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()]).enPassant(this.rowIndex, this.columnIndex, this.board)) {
                        for (DrawablePieces drawablePiece2 : this.drawablePieces) {
                            if (!drawablePiece.getColor().equalsIgnoreCase(drawablePiece2.getColor()) && diceGame.getPlaying().getPlayerColor().equals("black") && drawablePiece2.getY() == this.rowIndex - 1
                                    && drawablePiece2.getX() == this.columnIndex) {
                                this.pieceToBeTaken = drawablePiece2;
                                break;
                            } else if (!drawablePiece.getColor().equalsIgnoreCase(drawablePiece2.getColor()) && diceGame.getPlaying().getPlayerColor().equals("white") && drawablePiece2.getY() == this.rowIndex + 1
                                    && drawablePiece2.getX() == this.columnIndex) {
                                this.pieceToBeTaken = drawablePiece2;
                                break;
                            }
                        }
                        this.drawablePieces.remove(this.pieceToBeTaken);
                        this.type = "takes";
                        System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " took " +  this.pieceToBeTaken.getPieceName() + " on column " + this.pieceToBeTaken.getXAsInt() + " and row " + this.pieceToBeTaken.getYAsInt());
                        renderText(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " took " +  this.pieceToBeTaken.getPieceName() + " on column " + this.pieceToBeTaken.getXAsInt() + " and row " + this.pieceToBeTaken.getYAsInt());
                        this.takenPieces.add(this.pieceToBeTaken);
                        renderTakenPieces();
                        renderBoard(drawablePiece);
                        addMoveHistory(drawablePiece.getColor() + " " + drawablePiece.getPieceName() + " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " took " + this.pieceToBeTaken.getPieceName() + " on column " + this.pieceToBeTaken.getXAsInt() + " and row " + this.pieceToBeTaken.getYAsInt());
                        boardHistory.add(board.clone());
                        board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.pieceToBeTaken.getXAsInt()),String.valueOf(this.pieceToBeTaken.getYAsInt()));
                        this.newRoll();
                        break;
                    }
                    else if (this.selectedPiece.getPieceName().equals("KING")
                            && ((King) board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()]).castling(this.columnIndex, this.board)) {
                        int rookCastlingXPosition = 0;
                        for (DrawablePieces rookToBeMoved : this.drawablePieces) {
                            if (this.selectedPiece.getColor().equalsIgnoreCase(rookToBeMoved.getColor()) && rookToBeMoved.getYAsInt() == this.selectedPiece.getYAsInt()
                                    && this.columnIndex == 2 && rookToBeMoved.getXAsInt() == 0) {
                                Piece movingPiece = board.board[rookToBeMoved.getYAsInt()][rookToBeMoved.getXAsInt()];
                                diceGame.playCastlingRook(movingPiece, this.rowIndex, 3);
                                rookToBeMoved.setX(3);
                                rookCastlingXPosition = 3;
                                break;
                            } else if (this.selectedPiece.getColor().equalsIgnoreCase(rookToBeMoved.getColor()) && rookToBeMoved.getYAsInt() == this.selectedPiece.getYAsInt()
                                    && this.columnIndex == 6 && rookToBeMoved.getXAsInt() == 7) {
                                Piece movingPiece = board.board[rookToBeMoved.getYAsInt()][rookToBeMoved.getXAsInt()];
                                diceGame.playCastlingRook(movingPiece, this.rowIndex, 5);
                                rookToBeMoved.setX(5);
                                rookCastlingXPosition = 5;
                                break;
                            }
                        }
                        System.out.println("CASTLING for " + drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        renderText("CASTLING for " + drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        addMoveHistory("CASTLING for " + drawablePiece.getColor() + " " + drawablePiece.getPieceName() + " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        boardHistory.add(board.clone());
                        renderBoard(drawablePiece);
                        board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.columnIndex),String.valueOf(this.rowIndex));
                        board.setLastMove("ROOK",drawablePiece.getColor(),String.valueOf(rookCastlingXPosition),String.valueOf(drawablePiece.getYAsInt()));
                        this.newRoll();
                        break;
                    }
                    else {
                        System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        renderText(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        addMoveHistory(drawablePiece.getColor() + " " + drawablePiece.getPieceName() + " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        renderBoard(drawablePiece);
                        boardHistory.add(board.clone());
                        board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.columnIndex),String.valueOf(this.rowIndex));
                        this.newRoll();
                        break;
                    }
                }
            }
            this.pieceToBeTaken = null;
        }
    }

    public List<DrawablePieces> getDrawablePieces() {

        return this.drawablePieces;
    }

    protected void setGridCoordinates(MouseEvent event) {
        if (!this.diceGame.isActive()){
            return;
        }
        this.columnIndex = GridPane.getColumnIndex((Node) event.getSource());
        this.rowIndex = GridPane.getRowIndex((Node) event.getSource());
        movePiece(this.columnIndex, this.rowIndex);
    }

    protected void setPieceCoordinates(MouseEvent event) {

        columnIndexPiece = GridPane.getColumnIndex((Node) event.getSource());
        rowIndexPiece = GridPane.getRowIndex((Node) event.getSource());
    }

    protected List<Integer> getGridCoordinates() {

        return Arrays.asList(columnIndex, rowIndex);
    }

    private void renderInitialBoard() {
        for (Rectangle rectangle : this.drawableGrid) {
            rectangle.setOnMousePressed(this::setGridCoordinates);
            this.mainPane.add(rectangle, (int) rectangle.getX(), (int) rectangle.getY());
        }

        for (DrawablePieces piece : this.drawablePieces) {
            piece.setOnMousePressed(this::setGridCoordinates);
            this.mainPane.add(piece, piece.getXAsInt(), piece.getYAsInt());
            GridPane.setHalignment(piece, HPos.CENTER);
            GridPane.setValignment(piece, VPos.CENTER);
        }
        this.getChildren().add(this.mainPane);
    }

    private void update(){
        if (this.old!= null) {
            //reverseAnim(oldPiece,old[0],old[1]);
            this.tt.stop();
            this.oldPiece.setX(this.old[0]);
            this.oldPiece.setY(this.old[1]);

            GridPane.setHalignment(this.oldPiece, HPos.CENTER);
            GridPane.setValignment(this.oldPiece, VPos.CENTER);
            this.oldPiece.setTranslateY(this.old[0]);
            this.oldPiece.setTranslateX(this.old[1]);
            if(!(this.oldPiece.getPieceName().equals("PAWN") && (this.oldPiece.getY()==0 || this.oldPiece.getY()==7))) {
                this.mainPane.getChildren().remove(this.oldPiece);

                this.mainPane.add(this.oldPiece, this.oldPiece.getXAsInt(), this.oldPiece.getYAsInt());
            }
        }
    }

    private void renderBoard(DrawablePieces drawablePiece) {
        Piece movingPiece = board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()];
        if(gameMode.equals("mode1") && Settings.isAnimateMoves()) {
            this.old = animateMove(drawablePiece);
            this.oldPiece = drawablePiece;
        }else{
            drawablePiece.setX(this.columnIndex);
            drawablePiece.setY(this.rowIndex);
        }
        diceGame.play(movingPiece, this.rowIndex, this.columnIndex);
        this.getChildren().remove(this.mainPane);
        this.mainPane.getChildren().clear();
        this.mainPane = new GridPane();
        for (Rectangle rectangle : this.drawableGrid) {
            // Return the default color for the chess board
            if ((rectangle.getX() + rectangle.getY()) % 2 == 0){
                rectangle.setFill(ChessBoardFactory.color1);
            } else{
                rectangle.setFill(ChessBoardFactory.color2);
            }
            this.mainPane.add(rectangle, (int) rectangle.getX(), (int) rectangle.getY());
        }

        for (DrawablePieces piece : this.drawablePieces) {
            if(gameMode.equals("mode1") && Settings.isAnimateMoves() && ((piece.getY() == 0 && piece.getColor().equals("WHITE") && oldPiece.getPieceName().equals("PAWN") && oldPiece.getY() == 1)
                    ||(piece.getY() == 7 && piece.getColor().equals("BLACK") && oldPiece.getPieceName().equals("PAWN") && oldPiece.getY() == 6))){
                this.oldPiece = piece;
            }
            if (drawablePiece.isSelected()) {
                GridPane.setHalignment(piece, HPos.CENTER);
                GridPane.setValignment(piece, VPos.CENTER);
                piece.setIsSelected(false);
                this.selectedPiece = null;
                this.mainPane.add(piece, piece.getXAsInt(), piece.getYAsInt());
            } else {
                GridPane.setHalignment(piece, HPos.CENTER);
                GridPane.setValignment(piece, VPos.CENTER);
                this.mainPane.add(piece, piece.getXAsInt(), piece.getYAsInt());
            }
        }

        this.getChildren().add(this.mainPane);
        drawableDie.resetResult();
    }
    private int[] animateMove(DrawablePieces drawablePiece){

        int changeX = ((this.columnIndex - drawablePiece.getXAsInt()))*100;
        int changeY = (this.rowIndex - drawablePiece.getYAsInt())*100;

        this.tt = new TranslateTransition();
        this.tt.setDuration(Duration.seconds(2));
        this.tt.setNode(drawablePiece);
        this.tt.setFromX(drawablePiece.getXAsInt());
        this.tt.setFromY(drawablePiece.getYAsInt());
        this.tt.setToX(drawablePiece.getXAsInt() + changeX);
        this.tt.setToY(drawablePiece.getYAsInt() + changeY);
        this.tt.play();
        return new int[]{this.columnIndex, this.rowIndex};

    }


    private void renderDie(){

//        StackPane left = new StackPane();
//        Button button = new Button();
//        FlowPane flowPane = new FlowPane();
//        flowPane.getChildren().addAll(button, drawableDie.getDie());
//        left.setPrefWidth(850);
//        button.setText("Roll");
//        button.setFont(Font.font("Copperplate", 16));
//        button.getStyleClass().add("dieButton");
//        flowPane.setTranslateY(130);
        Label label = new Label("Next Piece");
        label.setFont(Font.font("Copperplate", 19));
        label.setTextFill(Color.rgb(243, 218, 178));
        label.setTranslateY(320);
        label.setTranslateX(850);
        this.borderPane.setRight(drawableDie.getDie());
        this.borderPane.setTranslateX(850);
        this.borderPane.setTranslateY(350);
        this.getChildren().addAll(this.borderPane, label);

        //        button.setOnAction(value ->  {
//            label.setText("Clicked!");
//        });
//
//        button.setOnAction(event -> {
//            // the player hasn't moved yet
//
//        });

    }

    private void renderTakenPieces(){
        this.getChildren().remove(this.borderPaneTakenPieces);
        this.borderPaneTakenPieces = new BorderPane();
        this.borderPaneTakenPieces.getStyleClass().add("borderPaneTakenPieces");
        this.borderPaneTakenPieces.setPrefHeight(700);
        this.borderPaneTakenPieces.setMaxWidth(400);
        this.borderPaneTakenPieces.setTranslateX(850);
        this.borderPaneTakenPieces.setTranslateY(50);

        FlowPane flowPaneTakenPiecesBlack = new FlowPane();
        FlowPane flowPaneTakenPiecesWhite = new FlowPane();

        flowPaneTakenPiecesBlack.setPadding(new Insets(5, 0, 5, 0));
        flowPaneTakenPiecesBlack.setVgap(4);
        flowPaneTakenPiecesBlack.setHgap(4);
        flowPaneTakenPiecesBlack.setAlignment(Pos.CENTER);
        flowPaneTakenPiecesBlack.setPrefWrapLength(370);

        flowPaneTakenPiecesWhite.setPadding(new Insets(5, 0, 5, 0));
        flowPaneTakenPiecesWhite.setVgap(4);
        flowPaneTakenPiecesWhite.setHgap(4);
        flowPaneTakenPiecesWhite.setAlignment(Pos.CENTER);
        flowPaneTakenPiecesWhite.setPrefWrapLength(370);

        for (DrawablePieces drawablePiece : this.takenPieces) {
            drawablePiece.setFitHeight(70);
            drawablePiece.setFitWidth(70);
        }

        flowPaneTakenPiecesBlack.getChildren().addAll(this.takenPieces.stream().filter(p -> p.getColor().equalsIgnoreCase("black")).collect(
                Collectors.toList()));
        flowPaneTakenPiecesWhite.getChildren().addAll(this.takenPieces.stream().filter(p -> p.getColor().equalsIgnoreCase("white")).collect(
                Collectors.toList()));

        this.borderPaneTakenPieces.setBottom(flowPaneTakenPiecesBlack);
        this.borderPaneTakenPieces.setTop(flowPaneTakenPiecesWhite);
        this.getChildren().add(this.borderPaneTakenPieces);
    }

    private void renderGamesWon(){
        this.getChildren().remove(this.gameWonText);
        this.gameWonText = new Text();
        this.gameWonText.setX(1000);
        this.gameWonText.setY(400);
        this.gameWonText.setFont(Font.font("Copperplate", 30));
        this.gameWonText.setFill(Color.DARKGRAY);
        this.getChildren().add(this.gameWonText);
        this.gameWonText.setText("White: "+whiteWon+ " - "+"Black: "+blackWon);
    }
    
    private void renderButtons(){
        Button resetButton = new Button();
        resetButton.setText("Reset Game");
        resetButton.setFont(Font.font("Copperplate", 16));
        resetButton.setTranslateX(845);
        resetButton.setTranslateY(475);
        resetButton.getStyleClass().add("resetButton");
        this.getChildren().add(resetButton);
        resetButton.setOnAction(event -> {
            ((Stage) this.getScene().getWindow()).close();
            if (training){
                display("Chess Board" + " (restarted with same game mode) ", gameMode, new int[]{cb0int, cb1int});
            }else{
                display("Chess Board" + " (restarted with same game mode) ", gameMode);
            }

        });

        Button history = new Button("Move history");
        history.setFont(Font.font("Copperplate", 16));
        history.setTranslateX(839);
        history.setTranslateY(515);
        VBox hbox = new VBox(moveHistory);
        Scene historyScene = new Scene(hbox, 500,500);
        Stage historyStage = new Stage();
        historyStage.setScene(historyScene);
        historyStage.setAlwaysOnTop(true);
        history.setFont(Font.font("Copperplate", 16));
        history.getStyleClass().add("historyButton");

        EventHandler<ActionEvent> event =
                new EventHandler<>() {

                    public void handle(ActionEvent e) {
                        historyStage.show();
                    }
                };
        history.setOnAction(event);
        moveHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    displayTempBoard((Board) boardHistory.get(moveHistory.getSelectionModel().getSelectedIndex()+1));
                }
            }
        });
        this.getChildren().add(history);
    }

    private void displayTempBoard(Board board){
        GridPane tempPane = new GridPane();
        ChessBoardFactory cbf = new ChessBoardFactory();
        List<Rectangle> grid = cbf.createDrawableBoard();
        Scene sc = new Scene(tempPane);
        Stage st = new Stage();
        st.setScene(sc);

        for (Rectangle rectangle : grid) {
            // Return the default color for the chess board
            if ((rectangle.getX() + rectangle.getY()) % 2 == 0){
                rectangle.setFill(ChessBoardFactory.color1);
            } else{
                rectangle.setFill(ChessBoardFactory.color2);
            }
            tempPane.add(rectangle, (int) rectangle.getX(), (int) rectangle.getY());
        }
        st.show();
        st.setAlwaysOnTop(true);

        for (DrawablePieces piece : board.drawablePieces) {
            GridPane.setHalignment(piece, HPos.CENTER);
            GridPane.setValignment(piece, VPos.CENTER);
            tempPane.add(piece, piece.getXAsInt(), piece.getYAsInt());
        }
    }
    private void selectGameMode(){

    }
    private void newRoll(){
        try {
            List<String> movablePieces = diceGame.getPlaying().movablePieces(board);
            drawableDie.roll(movablePieces);
            this.moveSoundPlayer.stop();
            this.takesSoundPlayer.stop();
            if(Settings.isAddSoundEffects() && this.type!= null) {
                if (this.type.equals("move")) {
                    this.moveSoundPlayer.play();
                } else {
                    this.takesSoundPlayer.play();
                }
            }

            animateDie();
            renderButtons();
            if (training){
                renderGamesWon();
            }


            //Argument 4 : true for ai,false for human (white pieces)
            //Argument 5: true for ai, false for human (black pieces)
            if (gameMode.equalsIgnoreCase("mode2")){
                if(!picked) {
                    System.out.println("test");
                    picked=true;
                    Text cbtext = new Text("Play against:    ");
                    cbtext.setFont(Font.font("Copperplate", 16));
                    ComboBox cb = new ComboBox();
                    cb.getItems().addAll("Random Agent", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5","ExpectiMax Depth 3","ExpectiMax Depth 4","ExpectiMaxPruning", "Hybrid Agent Depth 3", "Hybrid Agent Depth 4");
                    Button button = new Button("Play!");
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll(cbtext, cb, button);
                    cb.getSelectionModel().selectFirst();
                    Scene test = new Scene(hbox);
                    Stage stage = new Stage();
                    stage.setScene(test);
                    stage.show();
                    stage.setAlwaysOnTop(true);
                    button.setOnAction(event -> {
                        cb0int = cb.getSelectionModel().getSelectedIndex();
                        System.out.println(cb.getSelectionModel().getSelectedIndex() + " " + cb.getSelectionModel().getSelectedItem());
                        switch(cb.getSelectionModel().getSelectedIndex()){
                            case 0:
                                randomAgent.play(this, this.board, drawableDie, false, true);
                                stage.hide();
                                break;
                            case 1:
                                miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 2:
                                miniMaxAgent = new MiniMaxAgent(4, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 3:
                                miniMaxAgent = new MiniMaxAgent(5, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 4:
                                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 5:
                                alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 6:
                                alphaBetaAgent = new MiniMaxAgent(5, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 7:
                                expectiMaxAgent = new ExpectiMaxAgent(3, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 8:
                                expectiMaxAgent = new ExpectiMaxAgent(4, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 9:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(4, true);
                                expectiMaxPruningAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 10:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(3, true); // choose the max depth of minimax
                                expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 11:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(4, true); // choose the max depth of minimax
                                expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                        }
                    });
                }
                else {
                    switch (cb0int) {
                        case 0:
                            randomAgent.play(this, this.board, drawableDie, false, true);
                            break;
                        case 1:
                        case 2:
                        case 3:
                            miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 4:
                        case 5:
                        case 6:
                            alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 7:
                        case 8:
                            expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 9:
                            expectiMaxPruningAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 10:
                            expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                            break;
                        case 11:
                            expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                            break;
                    }
                }
            }
            else if (gameMode.equalsIgnoreCase("mode3")){
                if(!picked) {
                    picked = true;
                    Text cbtext = new Text("White:     ");
                    cbtext.setFont(Font.font("Copperplate", 16));
                    Text cb1text = new Text("    Black:     ");
                    cb1text.setFont(Font.font("Copperplate", 16));
                    ComboBox cb = new ComboBox();
                    ComboBox cb2 = new ComboBox();
                    Button button = new Button("Play!");
                    CheckBox trainMode = new CheckBox("Train Mode");
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll(cbtext, cb, cb1text, cb2, button);
                    cb.getItems().addAll("Random Agent", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5","ExpectiMax Depth 3", "ExpectiMax depth 4","ExpectiMaxPruning", "Hybrid Agent depth 3", "Hybrid Agent Depth 4");
                    cb2.getItems().addAll("Random Agent", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5", "ExpectiMax Depth 3","ExpectiMax Depth 4","ExpectiMaxPruning");
                    cb.getSelectionModel().selectFirst();
                    cb2.getSelectionModel().selectFirst();

                    Scene test = new Scene(hbox);
                    Stage stage = new Stage();
                    stage.setScene(test);
                    stage.show();
                    stage.setAlwaysOnTop(true);
                    button.setOnAction(event -> {
                        cb0int = cb.getSelectionModel().getSelectedIndex();
                        cb1int = cb2.getSelectionModel().getSelectedIndex();
                        training = trainMode.isSelected();
                        switch(cb.getSelectionModel().getSelectedIndex()){
                            case 0:
                                randomAgent.play(this, this.board, drawableDie, true, false);
                                stage.hide();
                                break;
                            case 1:
                                miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                                stage.hide();
                                break;
                            case 2:
                                miniMaxAgent = new MiniMaxAgent(4, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                                stage.hide();
                                break;
                            case 3:
                                miniMaxAgent = new MiniMaxAgent(5, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                                stage.hide();
                                break;
                            case 4:
                                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 5:
                                alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 6:
                                alphaBetaAgent = new MiniMaxAgent(5, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 7:
                                expectiMaxAgent = new ExpectiMaxAgent(3, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 8:
                                expectiMaxAgent = new ExpectiMaxAgent(4, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 9:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(4, true);
                                expectiMaxPruningAgent.play(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 10:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(3, true); // choose the max depth of minimax
                                expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                            case 11:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(4, true); // choose the max depth of minimax
                                expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                                stage.hide();
                                break;
                        }
                        switch(cb2.getSelectionModel().getSelectedIndex()){
                            case 0:
                                randomAgent.play(this, this.board, drawableDie, false, true);
                                stage.hide();
                                break;
                            case 1:
                                miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 2:
                                miniMaxAgent = new MiniMaxAgent(4, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 3:
                                miniMaxAgent = new MiniMaxAgent(5, false); // choose the max depth of minimax
                                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 4:
                                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 5:
                                alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 6:
                                alphaBetaAgent = new MiniMaxAgent(5, true); // choose the max depth of minimax
                                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 7:
                                expectiMaxAgent = new ExpectiMaxAgent(3, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 8:
                                expectiMaxAgent = new ExpectiMaxAgent(4, false); // choose the max depth of expectiMax
                                expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                            case 9:
                                expectiMaxPruningAgent = new ExpectiMaxAgent(4, true);
                                expectiMaxPruningAgent.play(this, this.board, this.drawableDie, false, true);
                                stage.hide();
                                break;
                        }

                    });
                }
                else{
                    switch(cb0int){
                        case 0:
                            randomAgent.play(this, this.board, drawableDie, true, false);
                            break;
                        case 1:
                        case 2:
                        case 3:
                            miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                            break;
                        case 4:
                        case 5:
                        case 6:
                            alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                            break;
                        case 7:
                        case 8:
                            expectiMaxAgent.play(this, this.board, this.drawableDie, true, false);
                            break;
                        case 9:
                            expectiMaxPruningAgent.play(this, this.board, this.drawableDie, true, false);
                            break;
                        case 10:
                            expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                            break;
                        case 11:
                            expectiMaxPruningAgent.playQ(this, this.board, this.drawableDie, true, false);
                            break;
                    }
                    switch(cb1int){
                        case 0:
                            randomAgent.play(this, this.board, drawableDie, false, true);
                            break;
                        case 1:
                        case 2:
                        case 3:
                            miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 4:
                        case 5:
                        case 6:
                            alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 7:
                        case 8:
                            expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                        case 9:
                            expectiMaxPruningAgent.play(this, this.board, this.drawableDie, false, true);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initAI(){
        switch(cb0int){
            case 0:
                randomAgent.play(this, this.board, drawableDie, true, false);
                break;
            case 1:
                miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                break;
            case 2:
                miniMaxAgent = new MiniMaxAgent(4, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                break;
            case 3:
                miniMaxAgent = new MiniMaxAgent(5, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, true,false);
                break;
            case 4:
                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                break;
            case 5:
                alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                break;
            case 6:
                alphaBetaAgent = new MiniMaxAgent(5, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                break;
            case 7:
                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
//                alphaBetaAgent.playQ(this, this.board, this.drawableDie, true, false);
                break;
        }
        switch(cb1int){
            case 0:
                randomAgent.play(this, this.board, drawableDie, false, true);
                break;
            case 1:
                miniMaxAgent = new MiniMaxAgent(3, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                break;
            case 2:
                miniMaxAgent = new MiniMaxAgent(4, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                break;
            case 3:
                miniMaxAgent = new MiniMaxAgent(5, false); // choose the max depth of minimax
                miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                break;
            case 4:
                alphaBetaAgent = new MiniMaxAgent(3, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                break;
            case 5:
                alphaBetaAgent = new MiniMaxAgent(4, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                break;
            case 6:
                alphaBetaAgent = new MiniMaxAgent(5, true); // choose the max depth of minimax
                alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                break;
        }
    }


    private void animateDie() throws FileNotFoundException, InterruptedException {
        this.borderPane.getChildren().remove(rollImage);
        rollImage = new ImageView();
        rollImage.setFitHeight(100);
        rollImage.setFitWidth(100);
        this.borderPane.getChildren().add(rollImage);
        // imageView.setImage(new Image(new FileInputStream("src/main/Images/"+drawableDie.getPiece(drawableDie.getResult()) + "_w"+ ".png")));
        rollImage.setImage(new Image(new FileInputStream("src/main/resources/pieces/"+drawableDie.getPiece(drawableDie.getResult()) + "_"+diceGame.getPlaying().getPlayerColor()+ ".png")));
    }

    private void addMoveHistory(String move){
        moveHistory.getItems().add(move);
    }


    private void renderText(String lastMove){
        this.borderPane.getChildren().remove(this.lastMoveText);
        this.lastMoveText = new Text();
        this.lastMoveText.setX(-850);
        this.lastMoveText.setY(475);
        this.lastMoveText.setFont(Font.font("Copperplate", 20));
        this.lastMoveText.setFill(Color.DARKGRAY);
        this.borderPane.getChildren().add(this.lastMoveText);
        this.lastMoveText.setText(lastMove);
    }

    private void highlightSelectedPiece(Rectangle rectangle) {
        if (gameMode.equalsIgnoreCase("mode1") || (gameMode.equalsIgnoreCase("mode2") && diceGame.getPlaying().getPlayerColor().equalsIgnoreCase("white") )) {
            if ((this.selectedPiece != null && this.selectedPiece.isSelected()) &&
                    (rectangle.getX() == this.selectedPiece.getX() && rectangle.getY() == this.selectedPiece.getY())) {
                rectangle.setFill(Color.KHAKI);
            } else {
                if ((rectangle.getX() + rectangle.getY()) % 2 == 0) {
                    rectangle.setFill(ChessBoardFactory.color1);
                } else {
                    rectangle.setFill(ChessBoardFactory.color2);
                }
            }
        }
    }

    private void highlightSelectedPieceMoves() {
        if (gameMode.equalsIgnoreCase("mode1") || (gameMode.equalsIgnoreCase("mode2") && diceGame.getPlaying().getPlayerColor().equalsIgnoreCase("white") )) {

            if (this.selectedPiece != null && this.selectedPiece.isSelected()) {
                List<int[]> legalMoves = board.board[this.selectedPiece.getYAsInt()][this.selectedPiece.getXAsInt()].calculateLegalMoves(board);
                for (Rectangle rectangle : this.drawableGrid) {
                    for (int i = 0; i < legalMoves.size(); i++) {
                        if (legalMoves.get(i)[0] == rectangle.getY() && legalMoves.get(i)[1] == rectangle.getX()) {
                            rectangle.setFill(Color.KHAKI);
                        }
                    }
                }
            }
        }
    }

    protected void setColumnIndex(int currColumnIndex) {

        this.columnIndex = currColumnIndex;
    }

    protected void setRowIndex(int currRowIndex) {

        this.rowIndex = currRowIndex;
    }

    protected void setPieceColor(List<Integer> pieceCoordinates) {

        for (DrawablePieces drawablePiece : this.drawablePieces) {
            if (drawablePiece.getX() == pieceCoordinates.get(0) && drawablePiece.getY() == pieceCoordinates.get(1)) {
                this.selectedPieceColor = drawablePiece.getColor();
            }
        }
    }

    protected String getPieceColor() {
        return this.selectedPieceColor;
    }

    public boolean isDraw() {
        if (this.drawablePieces.size() != 2) {
            return false;
        }
        // If there are only 2 pieces left
        // check if those are kings

        if (this.drawablePieces.get(0).getPieceName().equals("KING") && this.drawablePieces.get(1).getPieceName().equals("KING")) {
            return true;
        }
        return false;
    }

    public void endGame(String color, String message) {


        if (training){
            if (color.equalsIgnoreCase("black")){
                blackWon++;
            }else{
                whiteWon++;
            }
            System.out.println("White: "+whiteWon+" - Black: "+blackWon);
            ((Stage) this.getScene().getWindow()).close();
            display("Chess Board" + " (Training Mode) ", gameMode, new int[]{cb0int, cb1int});
            return;
        }


        Stage window = new Stage();
        window.setMinWidth(300);
        window.setMinHeight(250);
        window.initModality(Modality.APPLICATION_MODAL); // block events on other windows while open

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);

        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            ((Stage) this.getScene().getWindow()).close();
            window.close();
        });

        Button restart = new Button("Restart");
        restart.setOnAction(e -> {
            ((Stage) this.getScene().getWindow()).close();
            display("Chess Board" + " (restarted with same game mode) ", gameMode);
            window.close();
        });

        Button watchReplay = new Button("Watch replay");
        watchReplay.setOnAction(e -> {
//            ((Stage) this.getScene().getWindow()).close();
            try {
                getReplay();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            //            window.close();
        });

        // layout of the window
        GridPane.setConstraints(restart, 0, 0);
        GridPane.setConstraints(quit, 1, 0);
        GridPane.setConstraints(watchReplay, 2, 0);
        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(restart, quit, watchReplay);

        VBox vbox = new VBox(4);
        if(!message.equals("DRAW!")) {
            ImageView kingImage = new ImageView();
            kingImage.setFitHeight(100);
            kingImage.setFitWidth(100);
            kingImage.setImage(new Image("File:src/main/resources/pieces/king_" + color + ".png"));
            vbox.getChildren().addAll(kingImage, new Label(message), grid);
        } else {
            vbox.getChildren().addAll(new Label(message), grid);
        }
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
    public boolean getEnd(){return end;}

    public void getReplay() throws MalformedURLException {
        GridPane tempPane = new GridPane();
        ChessBoardFactory cbf = new ChessBoardFactory();
        List<Rectangle> grid = cbf.createDrawableBoard();
        Scene asd = new Scene(tempPane, 1000, 850);
        URL cssUrl = new File("src/main/java/dice/chess/ui/application.css").toURI().toURL();
        asd.getStylesheets().add(cssUrl.toString());
        Stage test = new Stage();
        test.setScene(asd);
        Board bo = (Board) boardHistory.get(countReplay);
        Button next = new Button("Next");
        Button previous = new Button("Previous");
        next.setFont(Font.font("Copperplate", 16));
        next.getStyleClass().add("resetButton");
        previous.setFont(Font.font("Copperplate", 16));
        previous.getStyleClass().add("resetButton");

        next.setTranslateX(850);
        previous.setTranslateX(850);
        previous.setTranslateY(50);
        next.setOnAction(event -> {
            if(countReplay < boardHistory.size()-1) {
                test.hide();
                countReplay++;
                try {
                    getReplay();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else{
            }
        });
        previous.setOnAction(event -> {
            if(countReplay > 0){
                test.hide();
                countReplay--;
                try {
                    getReplay();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
        });

        for (Rectangle rectangle : grid) {
            // Return the default color for the chess board
            if ((rectangle.getX() + rectangle.getY()) % 2 == 0){
                rectangle.setFill(ChessBoardFactory.color1);
            } else{
                rectangle.setFill(ChessBoardFactory.color2);
            }
            tempPane.add(rectangle, (int) rectangle.getX(), (int) rectangle.getY());
        }
        test.show();
        test.setAlwaysOnTop(true);

        for (DrawablePieces piece : bo.drawablePieces) {
            GridPane.setHalignment(piece, HPos.CENTER);
            GridPane.setValignment(piece, VPos.CENTER);
            tempPane.add(piece, piece.getXAsInt(), piece.getYAsInt());
        }
        tempPane.getChildren().addAll(next,previous);
    }



}