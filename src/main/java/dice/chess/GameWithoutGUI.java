package dice.chess;


import dice.chess.agentsWithoutGui.ExpectiMaxAgentWithoutGUI;
import dice.chess.agentsWithoutGui.ExpectiMaxAgentWithoutGUI4D;
import dice.chess.agentsWithoutGui.MiniMaxAgentWithoutGui;
import dice.chess.agentsWithoutGui.RandomAgentWithoutGUI;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.DiceGame;
import dice.chess.gamecontroller.pieces.King;
import dice.chess.gamecontroller.pieces.Pawn;
import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.ui.ChessPieceFactory;
import dice.chess.ui.die.DrawableDie;
import dice.chess.ui.pieces.DrawablePieces;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class GameWithoutGUI {

    private static final int MAP_HEIGHT = 800;
    private static final int MAP_WIDTH = 800;
    private static String gameMode;
    private int columnIndex;
    private int rowIndex;
    private int columnIndexPiece = -1;
    private int rowIndexPiece = -1;
    private int cb0int;
    private int cb1int;
    private DrawablePieces selectedPiece = null;
    private DrawablePieces pieceToBeTaken = null;
    public final List<DrawablePieces> takenPieces;
    public static CheckBox secondStageTraining;
    private List<DrawablePieces> drawablePieces;
    private final DrawableDie drawableDie;
    private String selectedPieceColor;
    private final DiceGame diceGame;
    private final Board board;
    private boolean end;
    private final RandomAgentWithoutGUI randomAgent;
    private MiniMaxAgentWithoutGui miniMaxAgent;
    private MiniMaxAgentWithoutGui alphaBetaAgent;
    private MiniMaxAgentWithoutGui alphaBetaForQ;
    private ExpectiMaxAgentWithoutGUI expectiMaxAgent;
    
    // Uncomment next three lines for using the 7-D agent and comment out the other three below
//    private ExpectiMaxAgentWithoutGUI expectiMaxAgentQ;
//    private ExpectiMaxAgentWithoutGUI expectiMaxAgentQ3;
//    private ExpectiMaxAgentWithoutGUI expectiMaxAgentQ5;
    private ExpectiMaxAgentWithoutGUI4D expectiMaxAgentQ;
    private ExpectiMaxAgentWithoutGUI4D expectiMaxAgentQ3;
    private ExpectiMaxAgentWithoutGUI4D expectiMaxAgentQ5;
    private String move = "src/main/resources/sound/move.wav";
    private String takes = "src/main/resources/sound/takes.wav";
    private String type;
    private ArrayList boardHistory;
    private int countReplay=0;
    private int[] old;
    private boolean picked;
    private static int numberOfGames;
    private static int whiteWins;
    private static int blackWins;
    private static List<Integer> gameTime = new ArrayList<>();
    private long beforeTime;
    private static int whiteAI;
    private static int blackAI;

    private static Text wonText = null;
    private static Text gameNumberText = null;
    private static Button playButton;

    private static int whiteWon = 0;
    private static int blackWon = 0;
    private static int drawWon = 0;

    private static int gamesToPlay;
    private static int gamesPlayed = 0;


    public static void main(String[] args) {
        JFXPanel jfxPanel = new JFXPanel();
        showGUI();
    }

    /**
     * Add all Shapes to the BoardView and AnchorPane to be rendered
     */
    public GameWithoutGUI() {
        //JFXPanel jfxPanel = new JFXPanel();
        boardHistory = new ArrayList<Board>();
        picked = false;

        randomAgent = new RandomAgentWithoutGUI();
        miniMaxAgent = new MiniMaxAgentWithoutGui(3, false); // choose the max depth of minimax
        alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true); // choose the max depth of minimax

        alphaBetaForQ = new MiniMaxAgentWithoutGui(4, true);
        alphaBetaAgent = new MiniMaxAgentWithoutGui(3, true); // choose the max depth of minimax
        expectiMaxAgent = new ExpectiMaxAgentWithoutGUI(4, true); // choose the max depth of expectiMax
//        expectiMaxAgentQ3 = new ExpectiMaxAgentWithoutGUI(3, true); // uncomment to use 7-D version of the Q-agent
//        expectiMaxAgentQ = new ExpectiMaxAgentWithoutGUI(4, true); // uncomment to use 7-D version of the Q-agent
//        expectiMaxAgentQ5 = new ExpectiMaxAgentWithoutGUI(5, true); // uncomment to use 7-D version of the Q-agent

        expectiMaxAgentQ3 = new ExpectiMaxAgentWithoutGUI4D(3, true); // using the 4-D version of the Q-agent
        expectiMaxAgentQ = new ExpectiMaxAgentWithoutGUI4D(4, true); // using the 4-D version of the Q-agent
        expectiMaxAgentQ5 = new ExpectiMaxAgentWithoutGUI4D(5, true); // using the 4-D version of the Q-agent
        //Create 8x8 black and white board
        
        //Create chess pieces
        ChessPieceFactory chessPieceFactory = new ChessPieceFactory();
        this.drawablePieces = chessPieceFactory.createDrawablePieces();
        this.takenPieces = new ArrayList<>();
        this.drawableDie = new DrawableDie(100,100,100);
        end = false;

        this.diceGame = new DiceGame(drawablePieces);
        this.board = diceGame.getBoard();
        
        boardHistory.add(board.clone());
        newRoll();
    }

    private static void showGUI(){

        Platform.runLater(new Runnable(){
            @Override
            public void run() {

                VBox mainBox = new VBox(10);
                mainBox.setPadding(new Insets(5));
                wonText = new Text("White: "+ whiteWon+ " - Black: "+blackWon);
                wonText.setFont(Font.font("Copperplate", 32));

                gameNumberText = new Text("Games Played: "+ (gamesPlayed));
                gameNumberText.setFont(Font.font("Copperplate", 32));

                HBox numberBox = new HBox(5);
                Label numberLabel = new Label("Number of Games:");
                TextField numberField = new TextField();

                // force the field to be numeric only
                numberField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d*")) {
                            numberField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });
                numberBox.getChildren().addAll(numberLabel, numberField);

                Text cbtext = new Text("White:     ");
                cbtext.setFont(Font.font("Copperplate", 16));
                Text cb1text = new Text("    Black:     ");
                cb1text.setFont(Font.font("Copperplate", 16));

                ComboBox cb = new ComboBox();
                ComboBox cb2 = new ComboBox();
                playButton = new Button("Play!");
                secondStageTraining = new CheckBox("Training (Second Stage)");
                HBox hbox = new HBox(10);
                hbox.getChildren().addAll(cbtext, cb, cb1text, cb2, playButton, secondStageTraining);
                cb.getItems().addAll("Random Agent with rule","Random Agent without rule", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5", "ExpectiMax Depth 4", "Hybrid Agent Depth 3",  "Hybrid Agent Depth 4", "Hybrid Agent Depth 5");
                cb2.getItems().addAll("Random Agent with rule","Random Agent without rule", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5", "ExpectiMax Depth 3", "ExpectiMax Depth 4", "ExpectiMax Depth 5");
                cb.getSelectionModel().selectFirst();
                cb2.getSelectionModel().selectFirst();

                mainBox.getChildren().addAll(numberBox, hbox, wonText, gameNumberText);

                Scene test = new Scene(mainBox);
                Stage stage = new Stage();
                stage.setScene(test);
                stage.show();
                stage.setAlwaysOnTop(true);



                playButton.setOnAction(event -> {
                    playButton.setDisable(true);
                    whiteAI = cb.getSelectionModel().getSelectedIndex();
                    blackAI = cb2.getSelectionModel().getSelectedIndex();
                    whiteWon = 0;
                    blackWon = 0;

                    int numberOfGames = 1;
                    if (numberField.getText() != null){
                        numberOfGames = Integer.parseInt(numberField.getText());
                    }
                    gamesToPlay = numberOfGames;
                    gamesPlayed = 0;
                    GameWithoutGUI gameWithoutGUI = new GameWithoutGUI();
                });

            }
        });

    }



    private void updateWon(){
        if (wonText != null){
            wonText.setText("White: "+whiteWon+ " - "+"Black: "+blackWon);
        }
    }

    private void updateGameNumber(){
        if (gameNumberText != null){
            gameNumberText.setText("Games Played: "+ (gamesPlayed));
        }
    }


    public void movePiece(int columnIndex, int rowIndex) {

        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
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
        this.type = "move";
        // Check if piece is moved to an empty square
        // or taking opponent's piece
        if (this.pieceToBeTaken != null){
            for (DrawablePieces drawablePiece : this.drawablePieces) {
                if (drawablePiece.isSelected()
                        && board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()].moveLegal(this.rowIndex, this.columnIndex, this.board)) {
                    this.drawablePieces.remove(this.pieceToBeTaken);
                    this.type = "takes";
//                    System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
//                            " took " +  this.pieceToBeTaken.getColor() + " " + this.pieceToBeTaken.getPieceName() + " on column " + this.columnIndex + " and row " + this.rowIndex);
                    this.takenPieces.add(this.pieceToBeTaken);
                    renderBoard(drawablePiece);
                    boardHistory.add(board.clone());
                    board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.columnIndex),String.valueOf(this.rowIndex));

                    // Here we check if the pieceToBeTaken is equal to king
                    if (this.pieceToBeTaken.getPieceName().equals("KING")){
                        String wonColor = drawablePiece.getColor();
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
//                        System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
//                                " took " +  this.pieceToBeTaken.getPieceName() + " on column " + this.pieceToBeTaken.getXAsInt() + " and row " + this.pieceToBeTaken.getYAsInt());
                        this.takenPieces.add(this.pieceToBeTaken);
                        renderBoard(drawablePiece);
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
//                        System.out.println("CASTLING for " + drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
//                                " to column " + this.columnIndex + " and row " + this.rowIndex);
                        boardHistory.add(board.clone());
                        renderBoard(drawablePiece);
                        board.setLastMove(drawablePiece.getPieceName(),drawablePiece.getColor(),String.valueOf(this.columnIndex),String.valueOf(this.rowIndex));
                        board.setLastMove("ROOK",drawablePiece.getColor(),String.valueOf(rookCastlingXPosition),String.valueOf(drawablePiece.getYAsInt()));
                        this.newRoll();
                        break;
                    }
                    else {
//                        System.out.println(drawablePiece.getColor() + " " + drawablePiece.getPieceName() +  " from column " + drawablePiece.getXAsInt() + " and row " + drawablePiece.getYAsInt() +
//                                " to column " + this.columnIndex + " and row " + this.rowIndex);

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

    public void endGame(String wonColor, String s) {
        if (wonColor.equalsIgnoreCase("black")){
            blackWon++;
        }else if (wonColor.equalsIgnoreCase("white")){
            whiteWon++;
        }else{
            drawWon++;
        }
        System.out.println(s);
        updateWon();
        gamesPlayed++;
        updateGameNumber();
        if (gamesPlayed < gamesToPlay){
            GameWithoutGUI gameWithoutGUI = new GameWithoutGUI();
        }else{
            playButton.setDisable(false);
        }
    }

    public List<DrawablePieces> getDrawablePieces() {

        return this.drawablePieces;
    }


    private void renderBoard(DrawablePieces drawablePiece) {
        Piece movingPiece = board.board[drawablePiece.getYAsInt()][drawablePiece.getXAsInt()];

        drawablePiece.setX(this.columnIndex);
        drawablePiece.setY(this.rowIndex);

        diceGame.play(movingPiece, this.rowIndex, this.columnIndex);


        for (DrawablePieces piece : this.drawablePieces) {
            if (drawablePiece.isSelected()) {
                piece.setIsSelected(false);
                this.selectedPiece = null;
            }
        }

        drawableDie.resetResult();
    }


    private void newRoll(){
        List<String> movablePieces = diceGame.getPlaying().movablePieces(board);
        drawableDie.roll(movablePieces);
        if(diceGame.getPlaying().getPlayerColor().equals("black")) {
            //cb.getItems().addAll("Random Agent", "Minimax Depth 3", "Minimax Depth 4", "Minimax Depth 5", "Alpha-Beta Depth 3", "Alpha-Beta Depth 4", "Alpha-Beta Depth 5", "Q-Agent");
            switch(blackAI){
                case 0:
                    randomAgent.play(this, this.board, drawableDie, false, true,true);
                    break;
                case 1:
                    randomAgent.play(this, this.board, drawableDie, false, true,false);
                    break;
                case 2:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(3, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 3:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(4, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 4:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(5, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 5:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(3, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 6:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 7:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(5, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 8:
                    expectiMaxAgent = new ExpectiMaxAgentWithoutGUI(3, true); // choose the max depth of expectimax
                    expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 9:
                    expectiMaxAgent = new ExpectiMaxAgentWithoutGUI(4, true); // choose the max depth of expectimax
                    expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 10:
                    expectiMaxAgent = new ExpectiMaxAgentWithoutGUI(5, true); // choose the max depth of expectimax
                    expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
            }
        }else if(diceGame.getPlaying().getPlayerColor().equals("white")) {
            long beforeTime = System.currentTimeMillis();
            switch(whiteAI){
                case 0:
                    randomAgent.play(this, this.board, drawableDie, true, false,true);
                    break;
                case 1:
                    randomAgent.play(this, this.board, drawableDie, true, false,false);
                    break;
                case 2:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(3, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 3:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(4, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 4:
                    miniMaxAgent = new MiniMaxAgentWithoutGui(5, false); // choose the max depth of minimax
                    miniMaxAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 5:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(3, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 6:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 7:
                    alphaBetaAgent = new MiniMaxAgentWithoutGui(5, true); // choose the max depth of minimax
                    alphaBetaAgent.play(this, this.board, this.drawableDie, true, false);
                    break;
                case 8:
                    expectiMaxAgent = new ExpectiMaxAgentWithoutGUI(4, true); // choose the max depth of expectimax
                    expectiMaxAgent.play(this, this.board, this.drawableDie, false, true);
                    break;
                case 9:
                    // This is already initialized in constructor no need to do it again.
                    //alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true);
                    expectiMaxAgentQ3.playQ(this, this.board, this.drawableDie, true, false);
                    break;
                case 10:
                    // This is already initialized in constructor no need to do it again.
                    //alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true);
                    expectiMaxAgentQ.playQ(this, this.board, this.drawableDie, true, false);
                    break;
                case 11:
                    // This is already initialized in constructor no need to do it again.
                    //alphaBetaAgent = new MiniMaxAgentWithoutGui(4, true);
                    expectiMaxAgentQ5.playQ(this, this.board, this.drawableDie, true, false);
                    break;
            }
            long afterTime = System.currentTimeMillis();
            long difference = (afterTime-beforeTime);
            System.out.println("It took "+difference+"ms");
        }else{return;}

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

    public boolean getEnd(){return end;}
}