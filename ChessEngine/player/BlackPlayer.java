package player;

import java.util.List;

import board.Alliance;
import board.Board;
import board.Move;
import pieces.Piece;

public class BlackPlayer extends Player {

	public BlackPlayer(Board board, List<Move> blackLegalMovesInPosition, List<Move> whiteLegalMovesInPosition) {
		super(board, blackLegalMovesInPosition, whiteLegalMovesInPosition);
	}

	public List<Piece> getActivePieces() {
		return this.board.getActiveBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}
	
	public String toString() {
		return "BLACK";
	}

	// Black Castling logic here if you have time
}
