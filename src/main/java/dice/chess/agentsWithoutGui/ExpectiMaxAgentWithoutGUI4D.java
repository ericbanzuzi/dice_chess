package dice.chess.agentsWithoutGui;

import dice.chess.GameWithoutGUI;
import dice.chess.agents.MoveState;
import dice.chess.agents.Types;
import dice.chess.gamecontroller.Board;
import dice.chess.gamecontroller.Player;
import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.ui.die.DrawableDie;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpectiMaxAgentWithoutGUI4D {

    private int maxDepth;
    private boolean pruning;
    private QAgentWithoutGUI4D qAgent;
    private Piece whitePiece;

    public ExpectiMaxAgentWithoutGUI4D(int maxDepth, boolean pruning){
        this.maxDepth = maxDepth;
        this.pruning = pruning;
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
            if(pruning){
                move = modifiedPruning(board, movablePieces, maxDepth, true, max.getPlayerColor(),false);
            }else {
                move = expectiMax(board, movablePieces, maxDepth, true, max.getPlayerColor(), false);
            }
            chessBoardWindow.movePiece(move.getPiece().getPosX(), move.getPiece().getPosY());
            Platform.runLater(new Thread(() ->{
                chessBoardWindow.movePiece(move.getMove()[1], move.getMove()[0]);
            }));

        }).start();
    }


    public MoveState expectiMax(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor, boolean chanceNode) {

        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null,  new int[0], evaluate(board, maxColor));
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

        // for the first call of expectiMax method where we know the piece type that moves and it is thus determined
        if(movablePieces != null){
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for(Piece piece : movablePieces) {
                List<int[]> moves = piece.calculateLegalMoves(board);
                Piece pieceCopy = piece.clone();
                for (int[] move : moves) {
                    clone.movePiece(pieceCopy, move[0], move[1]);
                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                    double current = expectiMax(clone,  null, depth - 1, false, max.getPlayerColor(), true).getValue();
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

        // consider chance node for the rest
        if (maximizing && !chanceNode) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, true, max.getPlayerColor(),true).getValue();
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

        } else if(!maximizing && !chanceNode){
            // minimizing player
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for(String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for(int[] move: moves){
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, false, max.getPlayerColor(),true).getValue();
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

        } else if(maximizing){
            // chance node for maximizing player
            List<String> movableTypes = max.movablePieces(board);
            // go through all moves of all pieces that can be moved
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();
            // for all piece types take the best move and add it multiplied by dice probability to get the average of all the best moves
            for (String type : movableTypes) {
                double bestValue = Integer.MIN_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                // search for the best move of a piece type
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, false, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move
                        if (current > bestValue) {
                            bestValue = current;
                        }
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null,  new int[0], averageBestValue);

        } else {
            // chance node for minimizing player
            List<String> movableTypes = min.movablePieces(board);
            // go through all moves of all pieces that can be moved
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();
            // for all piece types take the best move and add it multiplied by dice probability to get the average of all the best moves
            for (String type : movableTypes) {
                double bestValue = Integer.MAX_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, true, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move
                        if (current < bestValue) {
                            bestValue = current;
                        }
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null,  new int[0], averageBestValue);
        }
    }

    private MoveState expectiMaxPruning(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor, boolean chanceNode) {

        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null,  new int[0], evaluate(board, maxColor));
        }

        Board clone = board.clone();
        Player max;
        Player min;
        if (maxColor.equals(board.player1.getPlayerColor())) {
            max = board.player1;
            min = board.player2;
        } else {
            max = board.player2;
            min = board.player1;
        }

        // for the first call of minimax method where we know the piece type that moves and it is thus determined
        // So there should be no need to calculate diceProb (?)
        if (movablePieces != null) {
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
            int[] bestMove = randomMove(movingPiece, board);
//            int[] bestMove = null;
            // go through all moves of all pieces that can be moved
            List<String> movableTypes = max.movablePieces(board);
            // probability from dice
            double diceProb = (double) 1 / movableTypes.size();
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                double pieceProb = diceProb * ((double) 1 / movablePiecesOfType.size()); // probability from dice and piece

                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        double s = 0;
                        double p = 0;
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), "" + move[1] + "", "" + move[0] + "");
                        double current = expectiMaxPruning(clone, null, depth - 1, true, max.getPlayerColor(), false).getValue();
                        // move along the same "maximizing" to keep the right player, I think
                        clone = board.clone(); // unmake the move

                        double weighted_average = (pieceProb * current) / moves.size();
                        double v_max = Math.max(bestValue, weighted_average);
                        s += (pieceProb * current); //expected value
                        p += pieceProb;
                        double alpha = s + (1 - p) * v_max;
                        if (alpha <= bestValue) {
                            break;
                        }
                        bestValue = Math.max(alpha, bestValue);
                    }
                }
                return new MoveState(movingPiece, bestMove, bestValue);
            }
        }

        // gamma-A
        // max-layer of the tree is followed by a Chance node-layer
        if (maximizing && chanceNode) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            // probability from dice
            double diceProb = (double) 1 / movableTypes.size();
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                double pieceProb = diceProb * ((double) 1 / movablePiecesOfType.size()); // probability from dice and piece

                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        double s = 0;
                        double p = 0;
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), "" + move[1] + "", "" + move[0] + "");
                        double current = expectiMaxPruning(clone, null, depth - 1, true, max.getPlayerColor(), true).getValue();
                        // move along the same "maximizing" to keep the right player, I think
                        clone = board.clone(); // unmake the move

                        double weighted_average = (pieceProb * current) / moves.size();
                        double v_max = Math.max(bestValue, weighted_average);
                        s += (pieceProb * current); //expected value
                        p += pieceProb;
                        double alpha = s + (1 - p) * v_max;
                        if (alpha <= bestValue) {
                            break;
                        }
                        bestValue = Math.max(alpha, bestValue);
                    }
                }
            }
            return new MoveState(null,  new int[0], bestValue);

        } else {
            // gamma-B
            // min-layer of the tree is followed by a Chance node-layer
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            // probability from dice
            double diceProb = (double) 1 / movableTypes.size();
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                double pieceProb = diceProb * ((double) 1 / movablePiecesOfType.size()); // probability from dice and piece

                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();

                    for (int[] move : moves) {
                        double s = 0;
                        double p = 0;
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), "" + move[1] + "", "" + move[0] + "");
                        double current = expectiMaxPruning(clone, null, depth - 1, false, max.getPlayerColor(), true).getValue(); // move along the same "maximizing" to keep the right player, I think
                        clone = board.clone(); // unmake the move

                        double weighted_average = (pieceProb * current) / moves.size();
                        double v_min = Math.max(bestValue, weighted_average);
                        s += (pieceProb * current); //expected value
                        p += pieceProb;
                        double beta = s + (1 - p) * v_min;
                        if (beta >= bestValue) {
                            break;
                        }
                        bestValue = Math.min(beta, bestValue);
                    }
                }
            }
            return new MoveState(null,  new int[0], bestValue);
        }
    }


    //New Ver 08.01
    public MoveState modifiedPruning(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor, boolean chanceNode)
    {
        //Finishing condition
        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null,  new int[0], evaluate(board, maxColor));
        }

        //M-E-P
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

        // for the first call of expectiMax method where we know the piece type that moves and it is thus determined
        //So there should be no need to calculate diceProb (Question?)
        if(movablePieces != null){
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));

            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for(Piece piece : movablePieces) {
                List<int[]> moves = piece.calculateLegalMoves(board);
                Piece pieceCopy = piece.clone();
                for (int[] move : moves) {
                    clone.movePiece(pieceCopy, move[0], move[1]);
                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                    double current = modifiedPruning(clone,  null, depth - 1, false, max.getPlayerColor(), true).getValue();
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

        // "Gamma-A"
        // This is NOT the chanceNode, so EMM works as normal
        if (maximizing && !chanceNode) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = modifiedPruning(clone,  null, depth-1, true, max.getPlayerColor(),true).getValue();
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

        }
        // Minimizing player, also works as normal
        else if(!maximizing && !chanceNode){
            // minimizing player
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for(String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for(int[] move: moves){
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = modifiedPruning(clone,  null, depth-1, false, max.getPlayerColor(),true).getValue();
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
        // Chance Node for MAX
        else if(maximizing){
            List<String> movableTypes = max.movablePieces(board);
            // && Calculating expected utilities : average of best moves of all pieces
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();
            for (String type : movableTypes) {
                double bestValue = Integer.MIN_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                double pieceProb = diceProb * ((double) 1 / movablePiecesOfType.size());
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        // Implementing from old pruning method
                        double s = 0;
                        double p = 0;

                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = modifiedPruning(clone,  null, depth-1, false, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move

                        // FROM old method: Calculate weighted average? or use averageBestValue?
                        // TODO: How is the weightedAverage computed? Is diceProb or pieceProb necessary here?
                        double weightedAverage = (pieceProb * current) / moves.size();
                        double vMax = Math.max(bestValue, weightedAverage);
                        s+= pieceProb*current;
                        p+= pieceProb;
                        double alpha = s + (1-p) * vMax;
                        if(alpha <= bestValue)
                        {
                            break;
                        }
                        if (current > bestValue) {
                            bestValue = current;
                        }
                        bestValue = Math.max(alpha, bestValue);
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null,  new int[0], averageBestValue);

        }
        // "Gamma-B"
        else
        {
            List<String> movableTypes = min.movablePieces(board);
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();

            for (String type : movableTypes) {
                double bestValue = Integer.MAX_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                double pieceProb = diceProb * ((double) 1 / movablePiecesOfType.size());

                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();

                    for (int[] move : moves) {
                        double s = 0;
                        double p = 0;

                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = modifiedPruning(clone,  null, depth-1, true, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move

                        double weightedAverage = (pieceProb * current) / moves.size();
                        double vMin = Math.min(bestValue, weightedAverage);
                        s+= pieceProb*current;
                        p+= pieceProb;
                        double beta = s + (1-p) * vMin;
                        if(beta >= bestValue)
                        {
                            break;
                        }

                        if (current < bestValue) {
                            bestValue = current;
                        }
                        bestValue = Math.min(beta,bestValue);
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null,  new int[0], averageBestValue);
        }
    }

    //Method for the QAgent, returns 5 best moves at current board
    public MoveState expectiQ(Board board, List<Piece> movablePieces, int depth, boolean maximizing, String maxColor, boolean chanceNode)
    {
        if (depth == 0 || board.isEnded() || board.player2.movablePieces(board).size() < 1 || board.player1.movablePieces(board).size() < 1) {
            return new MoveState(null, null, evaluate(board, maxColor));
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

        // for the first call of expectiMax method where we know the piece type that moves and it is thus determined
        if(movablePieces != null){
            double bestValue = Integer.MIN_VALUE;
            Piece movingPiece = movablePieces.get(new Random().nextInt(movablePieces.size()));
            int[] bestMove = randomMove(movingPiece, board);
            List<int[]> bestMoves = new ArrayList<>();
            List<Piece> pieces = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            // go through all moves of all pieces that can be moved
            for(Piece piece : movablePieces) {
                List<int[]> moves = piece.calculateLegalMoves(board);
                Piece pieceCopy = piece.clone();
                for (int[] move : moves) {
                    clone.movePiece(pieceCopy, move[0], move[1]);
                    clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                    double current = expectiMax(clone,  null, depth - 1, false, max.getPlayerColor(), true).getValue();
                    clone = board.clone(); // unmake the move

                    // to get some moves (not always 5) but at max 5 moves
//                    if (current > bestValue) {
//                        if(bestMoves.size() < 5) {
//                            pieces.add(piece);
//                            bestMoves.add(move);
//                            values.add(current);
//                        } else {
//                            MoveState sorted = sort(pieces, bestMoves, values);
//                            sorted.getPieces().set(4,piece);
//                            sorted.getMoves().set(4, move);
//                            sorted.getValues().set(4, current);
//                            // switch to updated lists
//                            pieces = sorted.getPieces();
//                            bestMoves = sorted.getMoves();
//                            values = sorted.getValues();
//                        }
//                    }

                    // to get 5 best moves whenever it's possible
                    if(bestMoves.size() < 5) {
                        pieces.add(piece);
                        bestMoves.add(move);
                        values.add(current);
                    } else {
                        MoveState sorted = sort(pieces, bestMoves, values);
                        sorted.getPieces().set(4,piece);
                        sorted.getMoves().set(4, move);
                        sorted.getValues().set(4, current);
                        // switch to updated lists
                        pieces = sorted.getPieces();
                        bestMoves = sorted.getMoves();
                        values = sorted.getValues();
                    }
                }
            }
            return new MoveState(pieces, bestMoves, values);
        }

        // consider chance node for the rest
        if (maximizing && !chanceNode) {
            double bestValue = Integer.MIN_VALUE;
            List<String> movableTypes = max.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, max, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for (String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, true, max.getPlayerColor(),true).getValue();
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

        } else if(!maximizing && !chanceNode){
            // minimizing player
            double bestValue = Integer.MAX_VALUE;
            List<String> movableTypes = min.movablePieces(board);
            Piece movingPiece = randomMovingPiece(movableTypes, min, board);
            int[] bestMove = randomMove(movingPiece, board);
            // go through all moves of all pieces that can be moved
            for(String type : movableTypes) {
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for(int[] move: moves){
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, false, max.getPlayerColor(),true).getValue();
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

        } else if(maximizing){
            // chance node for maximizing player
            List<String> movableTypes = max.movablePieces(board);
            // go through all moves of all pieces that can be moved
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();
            // for all piece types take the best move and add it multiplied by dice probability to get the average of all the best moves
            for (String type : movableTypes) {
                double bestValue = Integer.MIN_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(max, type, board);
                // search for the best move of a piece type
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), max.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, false, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move
                        if (current > bestValue) {
                            bestValue = current;
                        }
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null, null, averageBestValue);

        } else {
            // chance node for minimizing player
            List<String> movableTypes = min.movablePieces(board);
            // go through all moves of all pieces that can be moved
            int averageBestValue = 0;
            double diceProb = (double) 1/movableTypes.size();
            // for all piece types take the best move and add it multiplied by dice probability to get the average of all the best moves
            for (String type : movableTypes) {
                double bestValue = Integer.MAX_VALUE;
                List<Piece> movablePiecesOfType = getMovablePiecesOfType(min, type, board);
                for (Piece piece : movablePiecesOfType) {
                    List<int[]> moves = piece.calculateLegalMoves(board);
                    Piece pieceCopy = piece.clone();
                    for (int[] move : moves) {
                        clone.movePiece(pieceCopy, move[0], move[1]);
                        clone.setLastMove(piece.getPieceType().name(), min.getPlayerColor(), ""+move[1]+"", ""+move[0]+"");
                        double current = expectiMax(clone,  null, depth-1, true, max.getPlayerColor(),false).getValue();
                        clone = board.clone(); // unmake the move
                        if (current < bestValue) {
                            bestValue = current;
                        }
                    }
                }
                averageBestValue += diceProb*bestValue;
            }
            return new MoveState(null, null, averageBestValue);
        }
    }
    
    public void playQ(GameWithoutGUI chessBoardWindow, Board board, DrawableDie drawableDie, boolean white,
            boolean black) {
        
        Player max;
        
        if (board == null){
            return;
        }
        
        if (white && (board.getLastMove() == null || board.getLastMove().get(1).equals("BLACK"))) {
            max = board.player2;
        } else if (black && (board.getLastMove() != null && board.getLastMove().get(1).equals("WHITE"))) {
            max = board.player1;
        } else {
            return;
        }
        
        List<Piece> movablePieces = new ArrayList<>();
        for (Piece piece : max.getPieces()) {
            if (piece.getPieceType().toString().equals(drawableDie.getPiece(drawableDie.getResult()))
                    && piece.calculateLegalMoves(board).size() > 0) {
                movablePieces.add(piece);
            }
        }
        
        MoveState moves = expectiQ(board, movablePieces, maxDepth, true, max.getPlayerColor(), false);
        float gamma = 0.85f;
        float epsilon = 0.1f;
        float alpha = 0.3f;
        if (qAgent == null) {
            this.qAgent = new QAgentWithoutGUI4D(gamma, epsilon, alpha, 64, 8, 12, moves);
        } else {
            qAgent.setMoves(moves);
        }
        
        if (chessBoardWindow.secondStageTraining.isSelected()){
            qAgent.play(moves.getPieces(), chessBoardWindow, moves, board);
            this.whitePiece = qAgent.getWhitePiece();
            updateQAgentAfterMove(chessBoardWindow, board, moves);
        }
        else{
            this.whitePiece = moves.getPieces().get(moves.getHighestValueMoveIndex());
            qAgent.play(this.whitePiece, chessBoardWindow, moves, board);
            updateQAgentAfterMove(chessBoardWindow, board, moves);
        }
    }
    
    public void updateQAgentAfterMove(GameWithoutGUI chessBoardWindow, Board board, MoveState moves) {
        
        Types.Actions[] allActions = Types.Actions.class.getEnumConstants();
        
        float reward = qAgent.getReward(board, chessBoardWindow);
        int stateX = 0;
        int stateY = 0;
        int bestLastMove = qAgent.getBestMove();

        for (Types.Actions tempActions : allActions) {
            if (tempActions.name().equalsIgnoreCase("action"+bestLastMove)){
                stateX = tempActions.value[1];
                stateY = tempActions.value[0];
            }
        }
        
        qAgent.updateQTable(stateX, stateY, this.whitePiece, bestLastMove, reward, moves);
        qAgent.writeTableToFile4D();
        qAgent.serializeQTable4D();
        qAgent.serializeAlphaTable();
    }

    
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

    public MoveState merge(List<Piece> pieces, List<int[]> moves, List<Double> values, List<Piece> pieces2, List<int[]> moves2, List<Double> values2) {
        List<Piece> sortedPiece = new ArrayList<>();
        List<int[]> sortedMoves = new ArrayList<>();
        List<Double> sortedValues = new ArrayList<>();

        int i = 0; //next element in the left array to be compared
        int j = 0; //next element in the right array to be compared

        //while left(1) or right(2) array still has elements that have not been placed in the merged array,
        //compare two elements and place the smaller one in the merged array
        while(i < values.size() && j < values2.size()){
            if(values.get(i) > values2.get(j)) { //if the element of the left array is bigger, place it on the merged lists and move to next element
                sortedValues.add(values.get(i));
                sortedMoves.add(moves.get(i));
                sortedPiece.add(pieces.get(i));
                i++;
            }else{
                sortedValues.add(values2.get(j));
                sortedMoves.add(moves2.get(j));
                sortedPiece.add(pieces2.get(j));
                j++;
            }
        }

        //copying any remaining elements of the array
        while(i < values.size()){
            sortedValues.add(values.get(i));
            sortedMoves.add(moves.get(i));
            sortedPiece.add(pieces.get(i));
            i++;
        }

        //copying any remaining elements of the array
        while(j < values2.size()){
            sortedValues.add(values2.get(j));
            sortedMoves.add(moves2.get(j));
            sortedPiece.add(pieces2.get(j));
            j++;
        }

        return new MoveState(sortedPiece, sortedMoves, sortedValues);
    }

    // sort to descending order
    public MoveState sort(List<Piece> pieces, List<int[]> moves, List<Double> values) {
        // Find the middle point
        if (values.size() > 1) {
            int m = values.size() / 2;

            List<Piece> pieces1 = pieces.subList(0,m);
            List<int[]> moves1 = moves.subList(0,m);
            List<Double> values1 = values.subList(0,m);

            List<Piece> pieces2 = pieces.subList(m, pieces.size());
            List<int[]> moves2 = moves.subList(m, moves.size());
            List<Double> values2 = values.subList(m, values.size());

            MoveState l1 = sort(pieces1, moves1, values1);
            MoveState l2 = sort(pieces2, moves2, values2);

            return merge(l1.getPieces(), l1.getMoves(), l1.getValues(), l2.getPieces(), l2.getMoves(), l2.getValues());
        } else {
            return new MoveState(pieces, moves, values);
        }
    }
}
