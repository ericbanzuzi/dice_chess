package dice.chess.agents;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.ui.ChessBoardWindow;
import javafx.application.Platform;


public class QAgent implements Serializable {
    
    private Types.States prevState1;
    private Types.States prevState2;
    private Types.States prevState3;
    private Types.States currState1;
    private Types.States currState2;
    private Types.States currState3;
    private int bestMove;
    private int prevAction = 0;
    private int currAction;
    private float epsilon;
    private float gamma;
    private float alpha;
    private int numActions;
    private int numXYStates;
    private int numPieceStates;
    private double[][][][] qTable;
    private int[][][][] alphaTable;
    private Piece piece;
    private Board board;
    private MoveState bestMoves;
    private Random rn;
    
    public QAgent(float gamma, float epsilon, float alpha, int numActions, int numXYStates,
            int numPieceStates, MoveState bestMoves) {
        
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.alpha = alpha;
        this.numActions = numActions;
        this.numXYStates = numXYStates;
        this.numPieceStates = numPieceStates;
        // Comment out next 2 after training for 1 game
        
        boolean trained = true;
        
        if (!trained) {
            this.qTable = new double[numXYStates][numXYStates][numPieceStates][numActions];
            this.alphaTable = new int[numXYStates][numXYStates][numPieceStates][numActions];
        } else {
            deserializeQTable4D();
            deserializeAlphaTable();
        }
        
        this.rn = new Random();
        this.bestMoves = bestMoves;
    }
    
    public HashMap<Piece, Map<Integer, Double>> getMaxValueAction(int state1, int state2, int state3, Piece piece,
            int[] move, List<int[]> moves) {
        
        int stateIndex1 = state1;
        int stateIndex2 = state2;
        int stateIndex3 = state3;
        
        HashMap<Piece, Map<Integer, Double>> pieceActionIndexMap = new HashMap<>();
        List<Integer> actionsToCheck = legalActionsToBeChecked(List.of(move));
        double[] qTablePart = this.qTable[stateIndex1][stateIndex2][stateIndex3];
        int maxIndex = getMaxValue(qTablePart, actionsToCheck);
        
        Map<Integer, Double> maxValueMap = new HashMap();
        maxValueMap.put(maxIndex, this.qTable[stateIndex1][stateIndex2][stateIndex3][maxIndex]);
        
        //update epsilon value
        this.epsilon = (float) (1.0 / (1 + Math.log(
                1 + Arrays.stream(this.alphaTable[stateIndex1][stateIndex2][stateIndex3]).sum())));
        
        pieceActionIndexMap.put(piece, maxValueMap);
        
        return pieceActionIndexMap;
    }
    
    public int getMaxValueAction(int state1, int state2, int state3, List<int[]> moves) {
        
        int stateIndex1 = state1;
        int stateIndex2 = state2;
        int stateIndex3 = state3;
        
        List<Integer> actionsToCheck = legalActionsToBeChecked(moves);
        Map<Integer, int[]> maxIndexMap = new HashMap();
        double[] qTablePart = this.qTable[stateIndex1][stateIndex2][stateIndex3];
        int maxIndex = getMaxValue(qTablePart, actionsToCheck);
        Map<Integer, Double> maxValueMap = new HashMap();
        maxValueMap.put(maxIndex, this.qTable[stateIndex1][stateIndex2][stateIndex3][maxIndex]);
        maxIndex = Collections.max(maxValueMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
        
        double chance = Math.random();
        Random rand = new Random();
        
        //update epsilon value
        this.epsilon = (float) (1.0 / (1 + Math.log(
                1 + Arrays.stream(this.alphaTable[stateIndex1][stateIndex2][stateIndex3]).sum())));
        
        // Random action if chance is smaller than epsilon
        // Otherwise return highest value action from Q-table
        if (chance < this.epsilon) {
            return actionsToCheck.get(rand.nextInt(actionsToCheck.size()));
        } else {
            return maxIndex;
        }
    }
    
    private List<Integer> legalActionsToBeChecked(List<int[]> moves) {
        
        //        List<int[]> legalMoves = this.piece.calculateLegalMoves(this.board);
        Types.Actions[] allActions = Types.Actions.class.getEnumConstants();
        List<Integer> actionsToCheck = new ArrayList<>();
        
        for (Types.Actions actions : allActions) {
            for (int[] move : moves) {
                if (Arrays.equals(actions.value, move)) {
                    actionsToCheck.add(Integer.valueOf(actions.name().replaceAll("[^0-9]", "")));
                }
            }
        }
        return actionsToCheck;
    }
    
    protected void updateQTable(int state1, int state2, Piece piece, int currentAction, float reward, MoveState moves) {
        
        Types.States[] allStates = Types.States.class.getEnumConstants();
        if (this.prevState1 == null && this.prevState2 == null) {
            for (Types.States states : allStates) {
                if (states.name().equalsIgnoreCase("x" + state1)) {
                    this.prevState1 = states;
                }
                if (states.name().equalsIgnoreCase("y" + state2)) {
                    this.prevState2 = states;
                }
                if (piece.getPlayer().getPlayerColor().equalsIgnoreCase("white") && states.name()
                        .equalsIgnoreCase("white" + piece.getPieceType().name())) {
                    this.prevState3 = states;
                }
            }
        }
        
        this.currAction = currentAction;
        
        for (Types.States cStates : allStates) {
            if (state1 == cStates.value) {
                this.currState1 = cStates;
            }
            if (state2 == cStates.value) {
                this.currState2 = cStates;
            }
            if (piece.getPlayer().getPlayerColor().equalsIgnoreCase("white") && cStates.name()
                    .equalsIgnoreCase("white" + piece.getPieceType().name())) {
                this.currState3 = cStates;
            }
        }
        
        //Adaptive alpha
        alphaTable[this.currState1.value][this.currState2.value][this.currState3.value][currentAction]++;
        alpha = (float) (1.0 / Math.log(
                1 + alphaTable[this.currState1.value][this.currState2.value][this.currState3.value][currentAction]));
        
        //Bellman Equation
        double update =
                reward + gamma * findMaxQState(this.currState1.value, this.currState2.value, this.currState3.value,
                        moves)
                        - this.qTable[this.prevState1.value][this.prevState2.value][this.prevState3.value][this.prevAction];
        this.qTable[this.prevState1.value][this.prevState2.value][this.prevState3.value][this.prevAction] =
                this.qTable[this.prevState1.value][this.prevState2.value][this.prevState3.value][this.prevAction]
                        + alpha * update;
        
        this.prevAction = this.currAction;
        this.prevState1 = this.currState1;
        this.prevState2 = this.currState2;
        this.prevState3 = this.currState3;
    }
    
    private double findMaxQState(int stateIndex1, int stateIndex2, int stateIndex3, MoveState moves) {
        
        List<Integer> actionsToCheck = legalActionsToBeChecked(moves.getMoves());
        Map<Integer, int[]> maxIndexMap = new HashMap();
        double[] qTablePart = this.qTable[stateIndex1][stateIndex2][stateIndex3];
        int maxIndex = getMaxValue(qTablePart, actionsToCheck);
        Map<Integer, Double> maxValueMap = new HashMap();
        maxValueMap.put(maxIndex, this.qTable[stateIndex1][stateIndex2][stateIndex3][maxIndex]);
        
        return Collections.max(maxValueMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue();
    }
    
    private int getMaxValue(double[] qTablePart, List<Integer> actionsToCheck) {
        
        int max = 0;
        double maxValue = Integer.MIN_VALUE;
        for (Integer action : actionsToCheck) {
            if (qTablePart[action] > maxValue) {
                max = action;
            }
        }
        return max;
    }
    
    public Piece getWhitePiece() {
        
        return this.piece;
    }
    
    private int getMaxValue(double[] qTablePart) {
        
        int max = 0;
        double maxValue = qTablePart[0];
        for (int i = 0; i < qTablePart.length; i++) {
            if (qTablePart[i] > maxValue) {
                max = i;
            }
        }
        return max;
    }
    
    protected void writeTableToFile4D() {
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.numXYStates; i++) {
            for (int j = 0; j < this.numXYStates; j++) {
                for (int k = 0; k < this.numPieceStates; k++) {
                    for (int l = 0; l < this.numActions; l++) {
                        builder.append(this.qTable[i][j][k][l] + "");
                        if (l < this.numActions - 1) {
                            builder.append(" ");
                        }
                    }
                    builder.append("\n");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/QTable4D.txt"));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getBestMove() {
        
        return this.bestMove;
    }
    
    protected void serializeQTable4D() {
        
        try {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/QTable4D.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.qTable);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    private void deserializeQTable4D() {
        
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/QTable4D.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.qTable = (double[][][][]) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("QTable not found");
            c.printStackTrace();
            return;
        }
    }
    
    protected void serializeAlphaTable() {
        
        try {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/AlphaTable.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.alphaTable);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    private void deserializeAlphaTable() {
        
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/AlphaTable.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.alphaTable = (int[][][][]) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Alpha table not found");
            c.printStackTrace();
        }
    }
    
    public void play(List<Piece> whitePieces, ChessBoardWindow chessBoardWindow, MoveState moves, Board board) {
        
        this.board = board;
        double bestMoveValueQAgent = Double.NEGATIVE_INFINITY;
        this.bestMove = -1;
        Types.States pieceState = null;
        Types.States[] allStates = Types.States.class.getEnumConstants();
        Types.Actions[] allActions = Types.Actions.class.getEnumConstants();
        
        for (Piece whitePiece : whitePieces) {
            for (Types.States tempStates : allStates) {
                if (tempStates.name().equalsIgnoreCase("white" + whitePiece.getPieceType().name())) {
                    pieceState = tempStates;
                    break;
                }
            }
            int moveIndex = whitePieces.indexOf(whitePiece);
            HashMap<Piece, Map<Integer, Double>> moveQAgent =
                    getMaxValueAction(whitePiece.getPosX(), whitePiece.getPosY(), pieceState.value, whitePiece,
                            moves.getMoves().get(moveIndex), moves.getMoves());
            
            if (moveQAgent.get(whitePiece).entrySet().stream().findFirst().get().getValue() > bestMoveValueQAgent) {
                this.piece = whitePiece;
                bestMoveValueQAgent = moveQAgent.get(this.piece).entrySet().stream().findFirst().get().getValue();
                this.bestMove = moveQAgent.get(this.piece).entrySet().stream().findFirst().get().getKey();
            }
        }
        
        double chance = Math.random();
        Random rand = new Random();
        
        // Random action if chance is smaller than epsilon
        // Otherwise return highest value action from Q-table
        if (chance < this.epsilon) {
            int random = rand.nextInt(moves.getMoves().size());
            List<Integer> actionsToCheck = legalActionsToBeChecked(List.of(moves.getMoves().get(random)));
            this.bestMove = actionsToCheck.get(0);
            this.piece = moves.getPieces().get(random);
        }
        
        for (Types.Actions action : allActions) {
            if (action.name().equalsIgnoreCase("action" + this.bestMove)) {
                System.out.println(moves.getMoves().get(moves.getHighestValueMoveIndex())[0] + ", " + moves.getMoves()
                        .get(moves.getHighestValueMoveIndex())[1] + " BEST MOVE AS PER EXPECTIMAX AGENT");
                System.out.println(Arrays.toString(action.value) + " BEST MOVE AS PER Q AGENT");
                chessBoardWindow.movePiece(this.piece.getPosX(), this.piece.getPosY());
                Platform.runLater(new Thread(() -> {
                    chessBoardWindow.movePiece(action.value[1], action.value[0]);
                }));
                break;
            }
        }
    }
    
    // method to use for GUI
    public void play(Piece whitePiece, ChessBoardWindow chessBoardWindow, MoveState moves, Board board) {
        
        this.piece = whitePiece;
        this.board = board;
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Types.States pieceState = null;
            Types.Actions[] allActions = Types.Actions.class.getEnumConstants();
            Types.States[] allStates = Types.States.class.getEnumConstants();
            
            for (Types.States tempStates : allStates) {
                if (tempStates.name().equalsIgnoreCase("white" + this.piece.getPieceType().name())) {
                    pieceState = tempStates;
                    break;
                }
            }
            
            int bestMoveQAgent =
                    getMaxValueAction(this.piece.getPosX(), this.piece.getPosY(), pieceState.value, moves.getMoves());
            
            for (Types.Actions action : allActions) {
                if (action.name().equalsIgnoreCase("action" + bestMoveQAgent)) {
                    System.out.println(
                            moves.getMoves().get(moves.getHighestValueMoveIndex())[0] + ", " + moves.getMoves()
                                    .get(moves.getHighestValueMoveIndex())[1] + " BEST MOVE AS PER EXPECTIMAX AGENT");
                    System.out.println(Arrays.toString(action.value) + " BEST MOVE AS PER Q AGENT");
                    break;
                }
            }
            for (Types.Actions tempActions : allActions) {
                if (Arrays.equals(tempActions.value, moves.getMoves().get(moves.getHighestValueMoveIndex()))) {
                    chessBoardWindow.movePiece(moves.getPieces().get(moves.getHighestValueMoveIndex()).getPosX(),
                            moves.getPieces().get(moves.getHighestValueMoveIndex()).getPosY());
                    Platform.runLater(new Thread(() -> {
                        chessBoardWindow.movePiece(tempActions.value[1], tempActions.value[0]);
                    }));
                    break;
                }
            }
        }).start();
    }
    
    protected void setMoves(MoveState moves) {
        
        this.bestMoves = moves;
    }
    
    public boolean agentToBeCaptured(Board b, String pieceType) {
        
        Piece pieceKing = null;
        
        //Find the piece that is the player's king.
        for (Piece piece : b.player2.getPieces()) {
            if (piece.getPieceType().name().equalsIgnoreCase(pieceType)) {
                pieceKing = piece;
                
            }
        }
        if (pieceKing == null) {
            return false;
        }
        
        for (Piece piece : b.player1.getPieces()) {
            for (int[] positions : piece.calculateLegalMoves(b)) {
                if (pieceKing.getPosX() == positions[1] && pieceKing.getPosY() == positions[0]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean agentCanCapture(Board b, String pieceType) {
        
        Piece pieceKing = null;
        for (Piece piece : b.player1.getPieces()) {
            if (piece.getPieceType().name().equalsIgnoreCase(pieceType)) {
                pieceKing = piece;
            }
        }
        if (pieceKing == null) {
            return false;
        }
        for (Piece piece : b.player2.getPieces()) {
            
            for (int[] positions : piece.calculateLegalMoves(b)) {
                if (pieceKing.getPosX() == positions[1] && pieceKing.getPosY() == positions[0]) {
                    return true;
                }
            }
        }
        return false;
        
    }
    
    protected float getReward(Board board, ChessBoardWindow chessBoardWindow) {
        
        float reward;
        
        //agent is checking the opponent's king
        if (!board.isEnded() && agentCanCapture(board, "king")) {
            reward = 0.4f;
            return reward;
        }
        //agent's king is being checked by opponent
        if (!board.isEnded() && agentToBeCaptured(board, "king")) {
            reward = -1.5f;
            return reward;
        }
        
        if (!board.isEnded() && agentCanCapture(board, "queen")) {
            reward = 0.2f;
            return reward;
        }
        //agent's king is being checked by opponent
        if (!board.isEnded() && agentToBeCaptured(board, "queen")) {
            reward = -1.2f;
            return reward;
        }
        
        if (!board.isEnded() && agentCanCapture(board, "bishop")) {
            reward = 0.1f;
            return reward;
        }
        //agent's king is being checked by opponent
        if (!board.isEnded() && agentToBeCaptured(board, "bishop")) {
            reward = -1.1f;
            return reward;
        }
        
        if (!board.isEnded() && agentCanCapture(board, "rook")) {
            reward = 0.1f;
            return reward;
        }
        //agent's king is being checked by opponent
        if (!board.isEnded() && agentToBeCaptured(board, "rook")) {
            reward = -1.1f;
            return reward;
        }
        
        //agent (white) has won the game
        if (board.isEnded() && board.getLastMove().get(1).equalsIgnoreCase("white")) {
            reward = 2f;
            return reward;
            
        }
        //agent has lost the game
        else if (board.isEnded() && board.getLastMove().get(1).equalsIgnoreCase("black")) {
            reward = -6f;
            return reward;
        }
        //last piece captured was an agent's piece
        if (!chessBoardWindow.takenPieces.isEmpty() && chessBoardWindow.takenPieces.get(
                chessBoardWindow.takenPieces.size() - 1).getColor().equalsIgnoreCase("white")) {
            reward = -2f;
            return reward;
        } else {
            reward = -1f;
        }
        return reward;
    }
    
}
