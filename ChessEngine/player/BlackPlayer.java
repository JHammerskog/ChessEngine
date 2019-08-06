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
		return null; // getBlackPieces from board

	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

}
