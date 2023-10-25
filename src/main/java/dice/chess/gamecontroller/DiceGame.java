package dice.chess.gamecontroller;

import dice.chess.gamecontroller.pieces.Piece;
import dice.chess.ui.pieces.DrawablePieces;

import java.util.List;

public class DiceGame {

	enum GameState{
		ACTIVE,
		FINISHED
	}

	private Player player1;
	private Player player2;
	private Board board;
	private Player playing;

	private GameState gameStatus;

	public DiceGame(List<DrawablePieces> drawablePieces){
		this.reset(drawablePieces);
	}

	public void reset(List<DrawablePieces> drawablePieces){
		this.gameStatus = GameState.ACTIVE;
		this.board = new Board(drawablePieces);
		this.player1 = board.player1;
		this.player2 = board.player2;
		this.playing = player2; // player 2 is white and white always starts
	}

	public void finishGame(){
		this.gameStatus = GameState.FINISHED;
	}

	public boolean isActive(){
		return this.gameStatus == GameState.ACTIVE ;
	}
	
	public void play(Piece piece, int posY, int posX){
		this.board.movePiece(piece, posY, posX);
		if(playing.equals(player1)){
			playing = player2;
		} else {
			playing = player1;
		}
	}
	
	public void playCastlingRook(Piece piece, int posY, int posX){
		this.board.movePiece(piece, posY, posX);
	}

	public Board getBoard() {
		return board;
	}

	public Player getPlaying() {
		return playing;
	}

	// Should be called every second in another thread.
	public void tick(){
		this.playing.decreaseTime();
		if (this.playing.getTimeLeft() == 0){
			//other player won the game
		}
	}



}
