package player;

import java.util.ArrayList;
import java.util.List;

import board.Alliance;
import board.Board;
import board.Move;
import pieces.Piece;

/***
 * Slightly abstract class which has children specifically implements the rules
 * of chess.
 ***/

public abstract class Player {

	private boolean isNotInCheck;

	private Piece playerKing;
	private Piece blackKing;
	private List<Move> legalMovesInPosition;
	private Board boardPosition;
	private Alliance currentPlayer; // Logic to set this needs development

	public Player(Board board, List<Move> legalMovesInPosition, List<Move> opponentMovesInPosition) {
		this.boardPosition = board;
		this.legalMovesInPosition = legalMovesInPosition;

		this.playerKing = board.findKing(currentPlayer);

		this.isNotInCheck = attacksOnTile(opponentMovesInPosition, playerKing.getPiecePosition()).isEmpty();
		// Instead of 1337, use a findKingPosition() method.

	}

	public abstract List<Piece> getActivePieces(); // These will be fleshed out in children

	public abstract Alliance getAlliance();

	/*
	 * Finish player when you've fleshed out how you will deal with move logic
	 */

	public BoardTransition makeHalfMove(Move halfMove) { // half-move refers to a single move made by one player
		// Check if move is in the list of legal moves

		// isInCheck method can slice the legalMoves to moves that stop king from being
		// in check?

		// If both above conditions are met, then return a new Board with updated move

		return null;
	}

	public List<Move> attacksOnTile(List<Move> opponentMoves, int tileCoordinate) {

		List<Move> movesAttackingTile = new ArrayList<>();

		for (Move possibleAttackMove : opponentMoves) {
			if (possibleAttackMove.getDestinationTileCoordinate() == tileCoordinate) {
				movesAttackingTile.add(possibleAttackMove);
			}
		}
		return movesAttackingTile;
	}
	
	public boolean verifyLegalMove(Move move) { // for later use
		return this.legalMovesInPosition.contains(move);
	}

	public boolean getCheckStatus() {
		return this.isNotInCheck;
	}

	public boolean isCheckMate() { // The checkMate and statemate methods need to be run AFTER a board is created.
		if (!isNotInCheck && legalMovesInPosition.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isStaleMate() {
		if (isNotInCheck && legalMovesInPosition.isEmpty()) {
			return true;
		}
		return false;
	}
	// Castling method here later if you have time
}
