package dice.chess.agentsWithoutGui;


import dice.chess.GameWithoutGUI;
import dice.chess.agents.MoveState;
import dice.chess.agents.QAgent;
import dice.chess.agents.Types;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.Player;
import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.ui.ChessBoardWindow;
import javafx.application.Platform;
import dice.chess.ui.die.DrawableDie;

import java.util.*;


public class MiniMaxAgentWithoutGui {

    private int maxDepth;
    private boolean alphaBeta;
    private QAgentWithoutGUI qAgent;
    private Piece blackPiece;
    private Piece whitePiece;

    public MiniMaxAgentWithoutGui(int maxDepth, boolean alphaBeta){
        this.maxDepth = maxDepth;
        this.alphaBeta = alphaBeta;
    }

    // public for dice.chess.tests
    public MoveState minimax(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor){

        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null, new int[0], evaluate(board, maxColor));
        }

        Board clone = board.clone();
        Player max;
        Player min;
        if (maxColor.equals(board.player1.getPlayerColor())){
            max = board.player1;
            min = board.player2;
        } else {
            max = board.player2;
            min = board.player1;
        }

        // for the first call of minimax method where we know the piece type that moves
        if(movablePieces != null){
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
            int[] bestMove = randomMove(movingPiece, board);
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for(Piece piece : movablePieces) {
                List<int[]> moves = piece.calculateLegalMoves(board);
                Piece pieceCopy = piece.clone();
                for (int[] move : moves) {
                    clone.movePiece(pieceCopy, move[0], move[1]);
                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                    double current = minimax(clone,  null, depth - 1, false, max.getPlayerColor()).getValue();
                    clone = board.clone(); // unmake the move
                    if (current > bestValue) {
                        movingPiece = piece;
                        bestMove = move;
                        bestValue = current;
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        }

        if (maximizing) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
            int[] bestMove = randomMove(movingPiece, board);
//            Piece movingPiece = null;
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = minimax(clone,  null, depth-1, false, max.getPlayerColor()).getValue();
                        clone = board.clone(); // unmake the move
                        if(current > bestValue){
                            movingPiece = piece;
                            bestMove = move;
                            bestValue = current;
                        }
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        } else {
            // minimizing player
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
            int[] bestMove = randomMove(movingPiece, board);
//            Piece movingPiece = null;
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for(String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for(int[] move: moves){
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = minimax(clone,  null, depth-1, true, max.getPlayerColor()).getValue();
                        clone = board.clone(); // unmake the move
                        if(current < bestValue){
                            movingPiece = piece;
                            bestMove = move;
                            bestValue = current;
                        }
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        }
    }

    // public for dice.chess.tests
    public MoveState alphaBeta(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor, double alpha, double beta){

        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null, new int[0], evaluate(board, maxColor));
        }

        Board clone = board.clone();
        Player max;
        Player min;
        if (maxColor.equals(board.player1.getPlayerColor())){
            max = board.player1;
            min = board.player2;
        } else {
            max = board.player2;
            min = board.player1;
        }

        // for the first call of minimax method where we know the piece type that moves
        if(movablePieces != null){
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
            int[] bestMove = randomMove(movingPiece, board);
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for(Piece piece : movablePieces) {
                List<int[]> moves = piece.calculateLegalMoves(board);
                Piece pieceCopy = piece.clone();
                for (int[] move : moves) {
                    clone.movePiece(pieceCopy, move[0], move[1]);
                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                    double current = alphaBeta(clone,  null, depth - 1, false, max.getPlayerColor(), alpha, beta).getValue();
                    clone = board.clone(); // unmake the move
                    if (current > bestValue) {
                        movingPiece = piece;
                        bestMove = move;
                        bestValue = current;
                    }
                    alpha = Math.max(alpha, current);
                    if(beta <= alpha){
                        break;
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        }

        if (maximizing) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
            int[] bestMove = randomMove(movingPiece, board);
//            Piece movingPiece = null;
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = alphaBeta(clone,  null, depth-1, false, max.getPlayerColor(), alpha, beta).getValue();
                        clone = board.clone(); // unmake the move
                        if(current > bestValue){
                            movingPiece = piece;
                            bestMove = move;
                            bestValue = current;
                        }
                        alpha = Math.max(alpha, current);
                        if(beta <= alpha){
                            break;
                        }
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        } else {
            // minimizing player
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
            int[] bestMove = randomMove(movingPiece, board);
//            Piece movingPiece = null;
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            for(String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for(int[] move: moves){
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = alphaBeta(clone,  null, depth-1, true, max.getPlayerColor(), alpha, beta).getValue();
                        clone = board.clone(); // unmake the move
                        if(current < bestValue){
                            movingPiece = piece;
                            bestMove = move;
                            bestValue = current;
                        }
                        beta = Math.min(beta, current);
                        if(beta <= alpha){
                            break;
                        }
                    }
                }
            }
            return new MoveState(movingPiece, bestMove, bestValue);
        }
    }

    // method to use for GUI
    public void play(GameWithoutGUI chessBoardWindow, Board board, DrawableDie drawableDie, boolean white, boolean black) {
        new Thread(() ->{

            Player max;
            if(white && (board.getLastMove() == null|| board.getLastMove().get(1).equals("BLACK"))){
                max = board.player2;
            } else if (black && (board.getLastMove()!=null && board.getLastMove().get(1).equals("WHITE"))){
                max = board.player1;
            } else {
                return;
            }

            List<Piece> movablePieces = new ArrayList<>();
            for(Piece piece : max.getPieces()) {
                if(piece.getPieceType().toString().equals(drawableDie.getPiece(drawableDie.getResult()))
                        && piece.calculateLegalMoves(board).size() > 0) {
                    movablePieces.add(piece);
                }
            }
            MoveState move;
            if(alphaBeta){
                move = alphaBeta(board, movablePieces, maxDepth, true, max.getPlayerColor(), Integer.MIN_VALUE, Integer.MAX_VALUE);
            }else {
                move = minimax(board, movablePieces, maxDepth, true, max.getPlayerColor());
            }
            chessBoardWindow.movePiece(move.getPiece().getPosX(), move.getPiece().getPosY());
            Platform.runLater(new Thread(() ->{
                chessBoardWindow.movePiece(move.getMove()[1], move.getMove()[0]);
            }));

//            System.out.print(move.getMove()[1]+ " move x ");
//            System.out.print(move.getMove()[0] + " move y ");
//            System.out.print(move.getValue()+ " val ");
//            System.out.println(move.getPiece().getPieceType().name());
//            System.out.println("initial x " + move.getPiece().getPosX() + " initial y "+ move.getPiece().getPosY());

        }).start();
    }

    // returns a list of movable pieces of certain piece type
    private List<Piece> getMovablePiecesOfType(Player player, String type, Board board){
        List<Piece> movablePieces = new ArrayList<>();
        for(Piece piece : player.getPieces()) {
            if(piece.getPieceType().name().equals(type) && piece.calculateLegalMoves(board).size() > 0)
                movablePieces.add(piece);
        }
        return movablePieces;
    }

    // returns a random movable piece
    private Piece randomMovingPiece(List<String> movableTypes, Player player, Board board) {
        int rnd = new Random().nextInt(movableTypes.size());
        List<Piece> pieces = getMovablePiecesOfType(player, movableTypes.get(rnd), board);
        rnd = new Random().nextInt(pieces.size());
        return pieces.get(rnd);
    }

    // returns a random move for a piece
    private int[] randomMove(Piece piece, Board board){
        List<int[]> moves = piece.calculateLegalMoves(board);
        int rnd = new Random().nextInt(moves.size());
        return moves.get(rnd);
    }

    // reformatted (best one I think) - https://www.chessprogramming.org/Simplified_Evaluation_Function
    private double evaluate(Board board, String maxColor){

        double score = 0;
        // if the max player wins with the move return inf, if it loses return -inf
        if (board.isEnded()){
            if (board.getLastMove().get(1).equalsIgnoreCase(maxColor)){
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }
        else{
            for (Piece[] pieces : board.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if(maxColor.equalsIgnoreCase("white")) {
                            if (piece.getPlayer().getPlayerColor().equalsIgnoreCase(maxColor)) {
                                score += piece.getValue() + piece.getStatePositionValuesWhite()[piece.getPosY()][piece.getPosX()];
                            } else {
                                score -= (piece.getValue() + piece.getStatePositionValuesBlack()[piece.getPosY()][piece.getPosX()]);
                            }
                        } else {
                            if (piece.getPlayer().getPlayerColor().equalsIgnoreCase(maxColor)) {
                                score += piece.getValue() + piece.getStatePositionValuesBlack()[piece.getPosY()][piece.getPosX()];
                            } else {
                                score -= (piece.getValue() + piece.getStatePositionValuesWhite()[piece.getPosY()][piece.getPosX()]);
                            }
                        }
                    }
                }
            }
        }
        return score;
    }

    // reformatted Vlad's function
    private double evaluate(Board board, int depth, String maxColor){
        //TODO: Finish evaluation func (Vlad)
        double score = 0;

        if (depth == 0){
            Piece pieceToBeMoved = board.board[Integer.parseInt(board.getLastMove().get(3))][Integer.parseInt(board.getLastMove().get(2))];
            List<int[]> moves = pieceToBeMoved.calculateLegalMoves(board);
            for (int[] move : moves){
                if (pieceToBeMoved.getPlayer().getPlayerColor().equalsIgnoreCase(maxColor)){
                    score += pieceToBeMoved.getValue() + pieceToBeMoved.getStatePositionValues()[move[0]][move[1]];
                }
                else{
                    score -= pieceToBeMoved.getValue() + pieceToBeMoved.getStatePositionValues()[move[0]][move[1]];
                }

            }
        }
        else{
            for (Piece[] pieces : board.board) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        List<int[]> moves = piece.calculateLegalMoves(board);
                        for (int[] move : moves) {
                            if (piece.getPlayer().getPlayerColor().equalsIgnoreCase(maxColor)) {
                                score += piece.getValue() + piece.getStatePositionValues()[move[0]][move[1]];
                            } else {
                                score -= piece.getValue() + piece.getStatePositionValues()[move[0]][move[1]];
                            }
                        }
                    }
                }
            }
        }

        return score;
    }

    // original
    private double evaluate(Board board, Piece pieceToBeMoved, int depth){
        //TODO: Finish evaluation func (Vlad)
        double score = 0;

        if (depth == 0){
            List<int[]> moves = pieceToBeMoved.calculateLegalMoves(board);
            for (int[] move : moves){
                if (pieceToBeMoved.getPlayer().getPlayerColor().equalsIgnoreCase("white")){
                    score += pieceToBeMoved.getValue() + pieceToBeMoved.getStatePositionValues()[move[0]][move[1]];
                }
                else{
                    score -= pieceToBeMoved.getValue() + pieceToBeMoved.getStatePositionValues()[move[0]][move[1]];
                }
            }
        }
        else{
            for (Piece[] pieces : board.board){
                for (Piece piece : pieces){
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    for (int[] move : moves){
                        if (piece.getPlayer().getPlayerColor().equalsIgnoreCase("white")){
                            score += piece.getValue() + piece.getStatePositionValues()[move[0]][move[1]];
                        }
                        else{
                            score -= piece.getValue() + piece.getStatePositionValues()[move[0]][move[1]];
                        }
                    }
                }
            }
        }

        return score;
    }

    // for QAgent
//    public MoveState alphaBetaQ(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor,
//                                double alpha, double beta) {
//
//        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1
//                || board.player1.movablePieces(board).size() < 1) {
//            return new MoveState(null, Collections.emptyList(), evaluate(board, maxColor));
//        }
//
//        Board clone = board.clone();
//        Player max;
//        Player min;
//        if (maxColor.equals(board.player1.getPlayerColor())) {
//            max = board.player1;
//            min = board.player2;
//        } else {
//            max = board.player2;
//            min = board.player1;
//        }
//
//        // for the first call of minimax method where we know the piece type that moves
//        if (movablePieces != null) {
//            double bestValue = Integer.MIN_VALUE;
//            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
//            List<int[]> bestMoves = new ArrayList<>();
//            int[] bestMove = randomMove(movingPiece, board);
//            bestMoves.add(bestMove);
//            // go through all moves of all pieces that can be moved
//            for (Piece piece : movablePieces) {
//                List<int[]> moves = piece.calculateLegalMoves(board);
//                Piece pieceCopy = piece.clone();
//                for (int[] move : moves) {
//                    clone.movePiece(pieceCopy, move[0], move[1]);
//                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), "" + move[1] + "",
//                            "" + move[0] + "");
//                    double current =
//                            alphaBeta(clone, null, depth - 1, false, max.getPlayerColor(), alpha, beta).getValue();
//                    clone = board.clone(); // unmake the move
//                    if (current > bestValue) {
//                        movingPiece = piece;
//                        bestMove = move;
//                        bestMoves.add(bestMove);
//                        bestValue = current;
//                    }
//                    alpha = Math.max(alpha, current);
//                    if (beta <= alpha) {
//                        break;
//                    }
//                }
//            }
//            return new MoveState(movingPiece, bestMoves, bestValue);
//        }
//
//        if (maximizing) {
//            double bestValue = Integer.MIN_VALUE;
//            List<String> movableTypes = max.movablePieces(board);
//            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
//            List<int[]> bestMoves = new ArrayList<>();
//            int[] bestMove = randomMove(movingPiece, board);
//            bestMoves.add(bestMove);
//            // go through all moves of all pieces that can be moved
//            for (String type : movableTypes) {
//                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
//                for (Piece piece : movablePiecesOfType) {
//                    List<int[]> moves = piece.calculateLegalMoves(board);
//                    Piece pieceCopy = piece.clone();
//                    for (int[] move : moves) {
//                        clone.movePiece(pieceCopy, move[0], move[1]);
//                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), "" + move[1] + "",
//                                "" + move[0] + "");
//                        double current =
//                                alphaBeta(clone, null, depth - 1, false, max.getPlayerColor(), alpha, beta).getValue();
//                        clone = board.clone(); // unmake the move
//                        if (current > bestValue) {
//                            movingPiece = piece;
//                            bestMove = move;
//                            bestMoves.add(bestMove);
//                            bestValue = current;
//                        }
//                        alpha = Math.max(alpha, current);
//                        if (beta <= alpha) {
//                            break;
//                        }
//                    }
//                }
//            }
//            return new MoveState(movingPiece, bestMoves, bestValue);
//        } else {
//            // minimizing player
//            double bestValue = Integer.MAX_VALUE;
//            List<String> movableTypes = min.movablePieces(board);
//            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
//            List<int[]> bestMoves = new ArrayList<>();
//            int[] bestMove = randomMove(movingPiece, board);
//            bestMoves.add(bestMove);
//            // go through all moves of all pieces that can be moved
//            for (String type : movableTypes) {
//                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
//                for (Piece piece : movablePiecesOfType) {
//                    List<int[]> moves = piece.calculateLegalMoves(board);
//                    Piece pieceCopy = piece.clone();
//                    for (int[] move : moves) {
//                        clone.movePiece(pieceCopy, move[0], move[1]);
//                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), "" + move[1] + "",
//                                "" + move[0] + "");
//                        double current =
//                                alphaBeta(clone, null, depth - 1, true, max.getPlayerColor(), alpha, beta).getValue();
//                        clone = board.clone(); // unmake the move
//                        if (current < bestValue) {
//                            movingPiece = piece;
//                            bestMove = move;
//                            bestMoves.add(bestMove);
//                            bestValue = current;
//                        }
//                        beta = Math.min(beta, current);
//                        if (beta <= alpha) {
//                            break;
//                        }
//                    }
//                }
//            }
//            return new MoveState(movingPiece, bestMoves, bestValue);
//        }
//    }

    // method to use for GUI
//    public void playQ(GameWithoutGUI chessBoardWindow, Board board, DrawableDie drawableDie, boolean white,
//                      boolean black) {
//
//        Player max;
//
//        if (board == null){
//            return;
//        }
//
//        if (white && (board.getLastMove() == null || board.getLastMove().get(1).equals("BLACK"))) {
//            max = board.player2;
//        } else if (black && (board.getLastMove() != null && board.getLastMove().get(1).equals("WHITE"))) {
//            max = board.player1;
//        } else {
//            return;
//        }
//
//        List<Piece> movablePieces = new ArrayList<>();
//        for (Piece piece : max.getPieces()) {
//            if (piece.getPieceType().toString().equals(drawableDie.getPiece(drawableDie.getResult()))
//                    && piece.calculateLegalMoves(board).size() > 0) {
//                movablePieces.add(piece);
//            }
//        }
//
//        MoveState moves = alphaBetaQ(board, movablePieces, maxDepth, true, max.getPlayerColor(), Integer.MIN_VALUE,
//                Integer.MAX_VALUE);
//        float gamma = 0.94f;
//        float epsilon = 0.1f;
//        float alpha = 0.15f;
//        if (qAgent == null) {
//            this.qAgent = new QAgentWithoutGUI(gamma, epsilon, alpha, 64, 8, 12, moves);
//        } else {
//            qAgent.setMoves(moves);
//        }
//
////        if (this.whitePiece == null){
////            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
////            randomMove(movingPiece, board);
////            this.whitePiece = movingPiece;
////        }
//        if (this.whitePiece == null){
//            this.whitePiece = moves.getPiece();
//        }
//        else {
//            List<Piece> whitePieces = board.player2.getPieces();
//            for (Piece piece : whitePieces) {
//                if (board.getLastMove().get(1).equalsIgnoreCase("white") && piece.getPosX() == Integer.parseInt(
//                        board.getLastMove().get(2)) && piece.getPosY() == Integer.parseInt(board.getLastMove().get(3))
//                        && board.getLastMove().get(0).equalsIgnoreCase(piece.getPieceType().name())) {
//                    this.whitePiece = piece;
//                    break;
//                }
//            }
//        }
//
//        qAgent.play(this.whitePiece, chessBoardWindow, moves, board);
//        updateQAgentAfterMove(chessBoardWindow, board, moves);
//    }
//
//    public void updateQAgentAfterMove(GameWithoutGUI chessBoardWindow, Board board, MoveState moves) {
//
//        Types.Actions[] allActions = Types.Actions.class.getEnumConstants();
//
//        float reward = qAgent.getReward(board, chessBoardWindow);
//        List<Piece> blackPieces = board.player1.getPieces();
//        Types.States[] allStates = Types.States.class.getEnumConstants();
//        int stateX = 0;
//        int stateY = 0;
//        for (Types.States tempStates : allStates) {
//            if (tempStates.value == moves.getMove()[1]) {
//                stateX = tempStates.value;
//            }
//            if (tempStates.value == moves.getMove()[0]) {
//                stateY = tempStates.value;
//            }
//        }
//
//        if (this.blackPiece != null) {
//            for (Piece piece : blackPieces) {
//                if (board.getLastMove().get(1).equalsIgnoreCase("black") && piece.getPosX() == Integer.parseInt(
//                        board.getLastMove().get(2)) && piece.getPosY() == Integer.parseInt(board.getLastMove().get(3))
//                        && board.getLastMove().get(0).equalsIgnoreCase(piece.getPieceType().name())) {
//                    this.blackPiece = piece;
//                    break;
//                }
//            }
//            for (Types.Actions tempActions : allActions) {
//                if (Arrays.equals(tempActions.value, moves.getMove())) {
//                    String numbersOnly = tempActions.name().replaceAll("[^0-9]", "");
//                    qAgent.updateQTable(stateX, stateY, this.whitePiece, this.blackPiece.getPosX(),
//                            this.blackPiece.getPosY(), this.blackPiece, Integer.parseInt(numbersOnly), reward, moves);
//                }
//            }
//        } else {
//            for (Piece blackPiece : blackPieces) {
//                for (Types.Actions tempActions : allActions) {
//                    if (Arrays.equals(tempActions.value, moves.getMove())) {
//                        String numbersOnly = tempActions.name().replaceAll("[^0-9]", "");
//                        qAgent.updateQTable(stateX, stateY, this.whitePiece, blackPiece.getPosX(),
//                                blackPiece.getPosY(), blackPiece, Integer.parseInt(numbersOnly), reward, moves);
//                    }
//                }
//                this.blackPiece = blackPiece;
//            }
//        }
////        qAgent.writeTableToFile7D();
//        qAgent.serializeQTable7D();
//        qAgent.serializeAlphaTable();
//    }


}
