package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import board.Alliance;
import board.Board;
import board.Move;
import pieces.Piece;
import pieces.Piece.PieceType;

/***
 * This class describes logic related to player situations (determining
 * check/checkmate/stalemate) as well as making a move for a player.
 ***/

public abstract class Player {

	private boolean isNotInCheck;

	private Piece playerKing;
	private List<Move> legalMovesInPosition;

	protected Board board;

	public Player(Board board, List<Move> legalMovesInPosition, List<Move> opponentMovesInPosition) {
		this.board = board;
		this.legalMovesInPosition = legalMovesInPosition;

		this.playerKing = findKing();
		this.isNotInCheck = attacksOnTile(opponentMovesInPosition, playerKing.getPiecePosition()).isEmpty();

	}

	public Piece findKing() {

		List<Piece> activePieces = new ArrayList<>();
		if (this.getAlliance() == Alliance.BLACK) {
			activePieces = board.getActiveBlackPieces();
		} else if (this.getAlliance() == Alliance.WHITE) {
			activePieces = board.getActiveWhitePieces();
		}

		for (Piece piece : activePieces) {
			if (piece.getPieceType() == PieceType.KING) {
				return piece;
			}
		}
		throw new RuntimeException("Illegal game state. Both players must have a king");
	}

	public List<Move> attacksOnTile(List<Move> opponentMoves, int tileCoordinate) {
		// Use this method to figure out legal king moves
		final List<Move> movesAttackingTile = new ArrayList<>();

		for (Move possibleAttackMove : opponentMoves) {
			if (possibleAttackMove.getDestinationTileCoordinate() == tileCoordinate) {
				movesAttackingTile.add(possibleAttackMove);
			}
		}
		return Collections.unmodifiableList(movesAttackingTile);
	}

	public boolean verifyLegalMove(Move move) {
		return this.legalMovesInPosition.contains(move);
	}

	public BoardTransition makeHalfMove(Move halfMove) { // Possibly move this method?

		if (!(verifyLegalMove(halfMove))) {
			System.out.println("Not a legal move!");
			return new BoardTransition(this.board, this.board, halfMove);
		}

		final Board newBoard = halfMove.executeMoveAndBuildBoard();

		// Below List declaration is hard to read. Attacks on tile for the player NEXT
		// TO MOVE on the NEW board, and second argument is the potential position of
		// the current players king after the move.
		final List<Move> movesThatPutKingInCheck = attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
				newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getPlayerKing().getPiecePosition());

		if (!(movesThatPutKingInCheck.isEmpty())) { // Potentially inefficient way to check for check?
			System.out.println("Move would put king in check!");
			return new BoardTransition(this.board, this.board, halfMove);
		}
		// TO-DO game over logic
		if (newBoard.getCurrentPlayer().isCheckMate()) { // Tidy this if/else if up

			System.out.println("Game is over due to checkmate.");

		} else if (newBoard.getCurrentPlayer().isStaleMate()) {
			System.out.println("Game is over due to stalemate.");

		}

		return new BoardTransition(this.board, newBoard, halfMove);
	}

	public boolean isCheckMate() { // Currently does not work
		if (!isNotInCheck && legalMovesInPosition.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isStaleMate() { // Currently does not work
		if (isNotInCheck && legalMovesInPosition.isEmpty()) {
			return true;
		}
		return false;
	}

	// Getters and Setters & Abstract Methods

	public boolean getCheckStatus() {
		return this.isNotInCheck;
	}

	public Piece getPlayerKing() {
		return playerKing;
	}

	public List<Move> getLegalMovesInPosition() {
		return legalMovesInPosition;
	}

	public abstract List<Piece> getActivePieces();

	public abstract Alliance getAlliance();

}
