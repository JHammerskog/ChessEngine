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
	private List<Piece> defendedPieces;

	protected Board board;

	public Player(Board board, List<Move> legalMovesInPosition, List<Move> opponentMovesInPosition,
			List<Piece> defendedPieces) {
		this.board = board;
		this.legalMovesInPosition = legalMovesInPosition;
		this.defendedPieces = defendedPieces;

		this.playerKing = findKing();
		if (!(this.playerKing == null)) {
			this.isNotInCheck = attacksOnTile(opponentMovesInPosition, playerKing.getPiecePosition()).isEmpty();
		} // Without this check, the "clear board" feature of the GUI causes a null
			// pointer exception
		if(!isNotInCheck) {
			System.out.println(); 
		}

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
		// throw new RuntimeException("Illegal game state. Both players must have a
		// king");
		return null;
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

	public boolean tileIsDefended(List<Move> legalMoves, int tileCoordinate) {

		final List<Move> movesDefendingTile = new ArrayList<>();

		for (Move possibleDefensiveMove : legalMoves) {
			if (possibleDefensiveMove.getDestinationTileCoordinate() == tileCoordinate) {
				movesDefendingTile.add(possibleDefensiveMove);
			}
		}

		if (movesDefendingTile.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean verifyLegalMove(Move move) {
		return this.legalMovesInPosition.contains(move);
	}

//	public BoardTransition makeHalfMove(Move halfMove) { // Possibly move this method?
//
//		if (!(verifyLegalMove(halfMove))) {
//			System.out.println("The method 'makeHalfMove' was just passed an illegal move!");
//			return new BoardTransition(this.board, this.board, halfMove);
//		}
//
//		final Board newBoard = halfMove.executeMoveAndBuildBoard();
//
//		// Below List declaration is hard to read. Attacks on tile for the player NEXT
//		// TO MOVE on the NEW board, and second argument is the potential position of
//		// the current players king after the move.
//		final List<Move> movesThatPutKingInCheck = attacksOnTile(newBoard.getCurrentPlayer().getLegalMovesInPosition(),
//				newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getPlayerKing().getPiecePosition());
//
//		if (!(movesThatPutKingInCheck.isEmpty())) { // Potentially inefficient way to check for check?
//
//			// System.out.println("Move would put king in check!"); // Uncomment to
//			// highlight inefficiency
//
//			return new BoardTransition(this.board, this.board, halfMove);
//		}
//
//		if (newBoard.getCurrentPlayer().isCheckMate()) { // Tidy this if/else if up
//
//			// TODO Game over?
//
//		} else if (newBoard.getCurrentPlayer().isStaleMate()) {
//			System.out.println("Game is over due to stalemate.");
//			// TODO do this later
//		}
//		return new BoardTransition(this.board, newBoard, halfMove);
//	}

	public boolean isCheckMate() {
		if (!isNotInCheck && !(playerHasLegalMoves(this.legalMovesInPosition))) {
			return true;
		}
		return false;
	}

	public boolean isStaleMate() { // Currently does not work
		if (isNotInCheck && !(playerHasLegalMoves(this.legalMovesInPosition))) {
			return true;
		}
		return false;
	}

	public boolean playerHasLegalMoves(List<Move> threatenedPlayersLegalMoves) {
		// Since the pieces generate pseudolegal moves, this method actually checks
		// whether a move would lead to an illegal position or not.

		List<Move> escapeMoves = new ArrayList<>();

		for (Move move : threatenedPlayersLegalMoves) {
			final Board potentialCheckMateBoard = move.executeMoveAndBuildBoard();
			if (potentialCheckMateBoard.getOpponent(potentialCheckMateBoard.getCurrentPlayer().getAlliance())
					.getIsNotInCheck() == true) { // On the new board, the current player's status is the one we should
													// check
				escapeMoves.add(move);
			}
		}
		if (escapeMoves.isEmpty()) {
			return false;
		}

		return true;
	}

	// Getters and Setters & Abstract Methods

	public boolean getIsNotInCheck() {
		return this.isNotInCheck;
	}

	public Piece getPlayerKing() {
		return playerKing;
	}

	public List<Move> getLegalMovesInPosition() {
		return legalMovesInPosition;
	}

	public List<Piece> getDefendedPieces() {
		return defendedPieces;
	}

	public abstract List<Piece> getActivePieces();

	public abstract Alliance getAlliance();

	public abstract Alliance getOpponentAlliance();

}
