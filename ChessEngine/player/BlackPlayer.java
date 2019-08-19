package player;

import java.util.List;

import board.Alliance;
import board.Board;
import board.Move;
import pieces.Piece;

public class BlackPlayer extends Player {

	public BlackPlayer(Board board, List<Move> blackLegalMovesInPosition, List<Move> whiteLegalMovesInPosition, List<Piece> defendedPieces) {
		super(board, blackLegalMovesInPosition, whiteLegalMovesInPosition, defendedPieces);
	}

	public List<Piece> getActivePieces() {
		return this.board.getActiveBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}
	
	@Override
	public Alliance getOpponentAlliance() {
		return Alliance.WHITE;
	}
	
	public String toString() {
		return "BLACK";
	}

	// Black Castling logic here if you have time
}
