package ai;

import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;
import pieces.Piece.PieceType;

public class KPKSolver extends EndgameSolver {

	private final Piece lonePiece;

	public KPKSolver(Board board) {
		super(board);

		this.lonePiece = findLonePiece(board.getCurrentPlayer().getActivePieces());

		// check for cannotWinScenarios() last thing in constructor?

	}

	public Move generateKPKMove(Board board) {

		if (getLonePiece().getPieceType() == PieceType.PAWN) {
			PawnSolver p = new PawnSolver(board);

			if (p.cannotWinScenario()) {
				return null; // Offer draw
			}
			return p.generateKingPawnMove();
		} else if (getLonePiece().getPieceType() == PieceType.QUEEN) {
			QueenSolver q = new QueenSolver(board);
			return q.generateKingQueenMove();
		}

		throw new RuntimeException("Inelligible board or turn for this heuristic!");
	}

	// Helper methods

	public int rowsToTargetSquare(int currentRow, int targetSquare) {
		int targetSquareRow = BoardUtility.calculateRow(targetSquare);
		if (currentRow > targetSquareRow) {
			return currentRow - targetSquareRow;
		} else {
			return targetSquareRow - currentRow;
		}
	}

	public int columnsToTargetSquare(int currentColumn, int targetSquare) {
		int targetSquareColumn = BoardUtility.calculateColumn(targetSquare);
		if (currentColumn > targetSquareColumn) {
			return currentColumn - targetSquareColumn;
		} else {
			return targetSquareColumn - currentColumn;
		}
	}

	public int calculateAmountOfEfficientDiagonalMoves(int kingColumn, int kingRow, int targetSquare) {

		// Without obstruction, the king can get to any square on the board in 7 or less
		// moves. This method checks whether the column or row distance is larger, and
		// returns the longest type of distance ot the target square.

		int rowDistance = rowsToTargetSquare(kingRow, targetSquare);
		int columnDistance = columnsToTargetSquare(kingColumn, targetSquare);

		if (rowDistance >= columnDistance) {
			return rowDistance;
		} else {
			return columnDistance;
		}
	}

	public int calculateKingStepsToDefense(int kingColumn, int kingRow, int desiredDefendedTile) {

		int[] vectorsFromDesiredTile = { -9, -8, -7, -1, 1, 7, 8, 9 };

		int lowestNumberOfMovesToDefend = Integer.MAX_VALUE;

		for (int vector : vectorsFromDesiredTile) {
			int defenseTile = desiredDefendedTile;
			defenseTile += vector;
			int temp = calculateAmountOfEfficientDiagonalMoves(kingColumn, kingRow, defenseTile);

			if (temp < lowestNumberOfMovesToDefend) {
				lowestNumberOfMovesToDefend = temp;
			}
		}
		return lowestNumberOfMovesToDefend;
	}

	public Piece findLonePiece(List<Piece> activePieces) { // Method to identify either
		for (Piece piece : activePieces) {
			if (!(piece.getPieceType() == PieceType.KING)) {
				return piece;
			}

		}
		throw new RuntimeException("This heuristic only works for a King-Pawn vs. King endgame");
	}

	// Getters and setter

	public Piece getLonePiece() {
		return lonePiece;
	}

	public class PawnSolver extends EndgameSolver {

		private final int pawnStepsToPromotion;
		private final int promotionTileCoordinate;

		final Piece playerPawn;

		private final int pawnColumn;
		private final int pawnRow;

		private int playerKingStepsToDefense;
		private int targetKingStepsToAttack;
		private int targetKingStepsToPromotionTile;

		public PawnSolver(Board board) {
			super(board);
			this.playerPawn = getLonePiece();
			this.pawnStepsToPromotion = stepsToPromotion(getPawn().getPiecePosition());
			this.promotionTileCoordinate = calculatePromotionTileCoordinate(getPawn().getPiecePosition());

			this.pawnColumn = BoardUtility.calculateColumn(playerPawn.getPiecePosition());
			this.pawnRow = BoardUtility.calculateRow(playerPawn.getPiecePosition());

			this.targetKingStepsToPromotionTile = calculateAmountOfEfficientDiagonalMoves(getTargetKingColumn(),
					getTargetKingRow(), this.promotionTileCoordinate);

			this.targetKingStepsToAttack = calculateAmountOfEfficientDiagonalMoves(getTargetKingColumn(),
					getTargetKingRow(), getPawn().getPiecePosition());

			this.playerKingStepsToDefense = calculateKingStepsToDefense(getPlayerKingColumn(), getPlayerKingRow(),
					getPawn().getPiecePosition());

		}

		public Move generateKingPawnMove() {

			// while playerKing is able to defend pawn before capture OR pawn can promote
			// before capture:

			if (getTargetKingStepsToPromotionTile() > getPawnStepsToPromotion()) {
				return makePawnMove();
			} else if (getPlayerKing().getDefendedPieces().contains(getPawn())) {
				if (playerKingIsOnTargetRow()) {
					// If the king defending the pawn AND on the row before the promotional rank,
					// then it is safe to promote.
					return makePawnMove();
				} else if (kingIsOnPawnRow()) {
					Move move = attemptToMoveKingUp();
					if (!(move == null)) {
						return move;
					} else if (pawnWouldBeSafeAfterMove()) {
						return makePawnMove();
					} else {
						return kingWaitingMove();
					}
				} else {
					return kingWaitingMove();
				}

			} else {
				return moveKingTowardDefense();
			}
		}

		private Move attemptToMoveKingUp() {
			Move move = MoveMaker.getMove(getBoard(), getPlayerKing().getPiecePosition(),
					getPlayerKing().getPiecePosition() - 8);

			if (move == null) {
				return null;
			}

			Board newBoard = move.executeMoveAndBuildBoard();
			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck()) {
				return move;
			}
			return null;

		}

		private Move makePawnMove() {

			if (!(getBoard().getTile(getPawn().getPiecePosition() - 8).tileIsOccupied())) {
				Move pawnMove = MoveMaker.getMove(getBoard(), getPawn().getPiecePosition(),
						getPawn().getPiecePosition() - 8);
				return pawnMove;
			}

			// This method only works for a white pawn
			// When adding functionality for play as black, do +8 instead

			// throw new RuntimeException("Pawn was asked to make a bad move!");
			return null;
		}

		public Move moveKingTowardDefense() { // Breaks down when piece is in the way or king moves into check
			Move moveTowardRestriction = null;
			int direction = 1337;

			if (!getPinAgainstColumn()) {

				if (getPlayerKingRow() > getPawnRow()) {
					if (getPlayerKingColumn() == getPawnColumn() || getPlayerKingColumn() == getPawnColumn() - 1
							|| getPlayerKingColumn() == getPawnColumn() + 1) { // the three colums here are acceptable
																				// columns for the king to defend
						direction = -8;
					} else if (getPlayerKingColumn() < getPawnColumn()) {
						direction = -9;
					} else {
						direction = -7;
					}
				} else if (getPlayerKingRow() == getPawnRow()) {
					if (getPlayerKingColumn() == getPawnColumn() || getPlayerKingColumn() == getPawnColumn() - 1
							|| getPlayerKingColumn() == getPawnColumn() + 1) { // the three colums here are acceptable
																				// columns for the king to defend
						direction = -8;
					} else if (getPlayerKingColumn() < getPawnColumn()) {
						direction = 1;
					} else {
						direction = -1;
					}
				} else {

					if ((getPlayerKingColumn() == getPawnColumn() || getPlayerKingColumn() == getPawnColumn() - 1
							|| getPlayerKingColumn() == getPawnColumn() + 1)) {
						direction = 8;
					} else if (getPlayerKingColumn() < getPawnColumn()) {
						direction = 9;
					} else {
						direction = 7;
					}
				}

			}

			moveTowardRestriction = MoveMaker.getMove(this.board, getPlayerKing().getPiecePosition(),
					getPlayerKing().getPiecePosition() + direction);
			if (pieceNotAttackedAfterMove(moveTowardRestriction)) {
				return moveTowardRestriction;
			}

			throw new RuntimeException("NEEDS ATTENTION");
		}

		public Move kingWaitingMove() {
			Move waitingMove = null;

			int[] bestKingDefenseMoves = { -9, -7, -1, 1, 7, 8, 9 };
			for (int i : bestKingDefenseMoves) {
				waitingMove = MoveMaker.getMove(getBoard(), getPlayerKing().getPiecePosition(),
						getPawn().getPiecePosition() + i);
				if (!(waitingMove == null)) {
					break;
				}
			}

			// -1 and +1 are best, because they mean the king are moving up the board and
			// can defend the pawn next move. If that is not possible then try to move
			// directly behind so it can go -1 or +1 next turn.

			if (waitingMove == null) {
				Board newBoard = null;
				for (Move move : getPlayerKing().calculateLegalMoves(getBoard())) {
					newBoard = move.executeMoveAndBuildBoard();
					if (!(newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).findKing().getDefendedPieces()
							.isEmpty())) {
						waitingMove = move;
						return waitingMove;
					}
				} // if
			} else {
				return waitingMove;
			}
			// This only happens if there are no kingmoves that defend the pawn and this
			// method has been called
			throw new RuntimeException("King cannot make a move!");
		}

		public boolean kingIsOnPawnRow() {
			return getPlayerKingRow() == BoardUtility.calculateRow(getPawn().getPiecePosition());
		}

		public int stepsToPromotion(int tileCoordinate) {
			return tileCoordinate / 8; // for white, implement for black asap
		}

		public int calculatePromotionTileCoordinate(int tileCoordinate) {
			// this is coupled with the stepsToPromotion() method in order to avoid calling
			// BoardUtility statically again.

			// This also assumes white is the promoting side

			return tileCoordinate - (stepsToPromotion(tileCoordinate) * 8);
			// To implement this if black is the side with the pawn, simply do + instead of
			// - as above.
		}

		public boolean pawnIsOnEdge(int tileCoordinate) {
			return BoardUtility.isPieceOnEdge(tileCoordinate);
		}

		public boolean cannotWinScenario() {
			if (pawnIsOnEdge(getPawn().getPiecePosition())) {
				// check if targetking is closer to promotionTile than playerKing
			} else if (playerKingStepsToDefense > targetKingStepsToAttack) {
				return true;
			}

			// if either of the above are true, offer draw

			return false;
		}

		public boolean pawnWouldBeSafeAfterMove() { // needs testing
			Move move = makePawnMove();

			if (move == null) {
				return false;
			}
			if (pieceNotAttackedAfterMove(move)) {
				return true;
			}
			Board newBoard = move.executeMoveAndBuildBoard();
			Piece newPawn = findLonePiece(
					newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getActivePieces());
			if (BoardUtility.isPieceDefended(newPawn, newBoard, newPawn.getPieceAlliance())) {
				return true;
			}
			return false;
		}

		public boolean playerKingIsOnTargetRow() {
			if (getBoard().getCurrentPlayer().getAlliance() == Alliance.WHITE) {
				return getBoard().getCurrentPlayer().getAlliance()
						.isPromotionRow(getPlayerKing().getPiecePosition() - 8);
			} else {
				return getBoard().getCurrentPlayer().getAlliance()
						.isPromotionRow(getPlayerKing().getPiecePosition() + 8);
			}

		}

		// getters and setters

		public int getStepsToPromotion() {
			return this.pawnStepsToPromotion;
		}

		public Piece getPawn() {
			return this.playerPawn;
		}

		public int getPawnStepsToPromotion() {
			return pawnStepsToPromotion;
		}

		public int getPromotionTileCoordinate() {
			return promotionTileCoordinate;
		}

		public Piece getPlayerPawn() {
			return playerPawn;
		}

		public int getPawnColumn() {
			return pawnColumn;
		}

		public int getPawnRow() {
			return pawnRow;
		}

		public int getPlayerKingStepsToDefense() {
			return playerKingStepsToDefense;
		}

		public int getTargetKingStepsToAttack() {
			return targetKingStepsToAttack;
		}

		public int getTargetKingStepsToPromotionTile() {
			return targetKingStepsToPromotionTile;
		}

	}

	public class QueenSolver extends KPKSolver {

		final Piece playerQueen;

		final boolean targetKingOnEdge;

		public QueenSolver(Board board) {
			super(board);
			this.playerQueen = getLonePiece();

			this.targetKingOnEdge = targetKingIsOnEdge(getTargetKing().getPiecePosition());
		}

		public Move generateKingQueenMove() {
			// Check for stalemate before making everymove

			if (targetKingOnEdge) {
				return moveQueenToEdgeRestriction();

			}
			// 1. If yes, figure out which edge it is restricted to - after which you move
			// the queen to that edge +1
			// If queen is restricting to edge, move king to the edge + 2 and move it into
			// checkmate position
			// if in checkmate position, check all queenmoves that would result in checkmate

			// if the targetking is not on the edge, do move that restricts the king :S

			return null;
		}

		public Move moveQueenToEdgeRestriction() {
			Move restrictionMove = null;

			if (getTargetKingColumn() == 0) {
				// move queen to column 1
			} else if (getTargetKingColumn() == 7) {
				// move queen to column 6
			} else if (getTargetKingRow() == 0) {
				// move queen to row 1
			} else if (getTargetKingRow() == 7) {
				// move queen to row 7
			}

			if (pieceNotAttackedAfterMove(restrictionMove)) {
				return restrictionMove;
			}

			throw new RuntimeException("Problem moving the queen to restriction!");
		}

		public boolean couldBeCheckmatePosition() { // if it is whites move and this returns true, check queen moves for
			// checkmate
			int distanceBetweenKings = getTargetKing().getPiecePosition() - getPlayerKing().getPiecePosition();

			if (getPinAgainstColumn()) {
				if (distanceBetweenKings == -10 || distanceBetweenKings == -6 || distanceBetweenKings == -2
						|| distanceBetweenKings == 2 || distanceBetweenKings == 6 || distanceBetweenKings == 10) {
					return true;
				}

			} else if (!getPinAgainstColumn()) {
				if (distanceBetweenKings == -15 || distanceBetweenKings == -17 || distanceBetweenKings == -16
						|| distanceBetweenKings == 16 || distanceBetweenKings == 15 || distanceBetweenKings == 17) {
					return true;
				}

			}
			return false;
		}

		public boolean moveWouldLeadToStalemate(Move madeMove) {
			Board newBoard = madeMove.executeMoveAndBuildBoard();

			if (newBoard.getCurrentPlayer().isStaleMate()) {
				return true;
			}
			return false;
		}

		public boolean targetKingIsOnEdge(int tileCoordinate) {
			return BoardUtility.isPieceOnEdge(tileCoordinate);
		}

	}

}
