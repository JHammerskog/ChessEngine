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

	public int calculateKingStepsToDefendTile(int kingColumn, int kingRow, int desiredDefendedTile) {

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

	public class PawnSolver extends KPKSolver {

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

			this.playerKingStepsToDefense = calculateKingStepsToDefendTile(getPlayerKingColumn(), getPlayerKingRow(),
					getPawn().getPiecePosition());

		}

		public Move generateKingPawnMove() {

			// while playerKing is able to defend pawn before capture OR pawn can promote
			// before capture:

			if (getTargetKingStepsToPromotionTile() > getPawnStepsToPromotion()) {
				return makePawnMove();
			} else if (playerKingIsOnTargetRow()) {
				return makePawnMove(); // NEEDS REFACTORING
			} else if (getPlayerKing().getDefendedPieces().contains(getPawn())) {
				if (playerKingIsOnTargetRow()) {
					// If the king defending the pawn AND on the row before the promotional rank,
					// then it is safe to promote.
					return makePawnMove();
				} else if (kingIsOnPawnRow()) {
					Move move = attemptToMoveKingUp();
					if (!(move == null)) {
						if (moveWouldLeadToStalemate(move)) {
							if (pawnWouldBeSafeAfterMove()) {
								if (moveWouldLeadToStalemate(makePawnMove())) {
									return kingWaitingMove();
								}
								return makePawnMove();
							} else {
								throw new RuntimeException("Bad things happening.");
							}
						}
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
			// throw new RuntimeException("Bad things happening.");
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

		public boolean winningCaseOne() {
			// very specific position which is an exception to the rule when the king is in front of the enemy pawn
			int distanceBetweenPawnAndTargetKing = getTargetKing().getPiecePosition() - getPawn().getPiecePosition();

			if (getBoard().getCurrentPlayer().getAlliance() == Alliance.WHITE) {
				if (getPlayerKingRow() == 2 && !(getPlayerKing().getDefendedPieces().isEmpty())) {
					if (distanceBetweenPawnAndTargetKing == -16) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean winningCaseTwo() {
			
			int distanceBetweenKings = getTargetKing().getPiecePosition() - getPlayerKing().getPiecePosition();
			if(getBoard().getCurrentPlayer().getAlliance() == Alliance.WHITE) {
				if(getPlayerKingRow() == 2 && (getPawn().getPiecePosition() - 8 == getPlayerKing().getPiecePosition())) {
					if(distanceBetweenKings == -15 || distanceBetweenKings == -17) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean winningCaseThree() {
			// Winning case three involves a situation where the king can move into
			// opposition with the enemy king in front of the pawn. Unfortunately, there was
			// no time to implement this in this iteration.
			return false;
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

			if (winningCaseOne() || winningCaseTwo()) {
				return false;
			}

			if (pawnIsOnEdge(getPawn().getPiecePosition())) {
				if (calculateKingStepsToDefendTile(getPlayerKingColumn(), getPlayerKingColumn(),
						getPromotionTileCoordinate()) > calculateKingStepsToDefendTile(getTargetKingColumn(),
								getTargetKingColumn(), getPromotionTileCoordinate())
						&& (getTargetKingStepsToPromotionTile() <= getPawnStepsToPromotion())) {
					// Rook pawn that cannot promote for free or be defended on time
					return true;
				} else if (false/* Whites king is in front of the pawn and shut in by the black king */) {
					// implement asap
				}
			} else if (getPlayerKingStepsToDefense() > getTargetKingStepsToAttack()) {
				// if the pawn can be captured before the king can defend it
				return true;
			} else if ((getTargetKing().getPiecePosition() == getPawn().getPiecePosition() - 8) || (getTargetKing()
					.getPiecePosition() == getPawn().getPiecePosition() - 16)
					&& !(getPlayerKing().getPiecePosition() == getTargetKing().getPiecePosition() + 15
							|| !(getPlayerKing().getPiecePosition() == getTargetKing().getPiecePosition() + 17))) {
				// Only true for white to move
				return true;
			}

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

		final boolean targetKingOnMatingRow;

		public QueenSolver(Board board) {
			super(board);
			this.playerQueen = getLonePiece();

			this.targetKingOnMatingRow = getTargetKingRow() == getMatingEdge();
		}

		public Move generateKingQueenMove() {

			if (isTargetKingOnMatingRow()) {
				if (isQueenPinningTargetKingAgainstMatingRow()) {
					if (isKingOnRestrictingRow(getMatingEdge())) {
						if (isCheckingPositionPosition()) {
							return findCheckmate();
						} else {
							return makeKingRestrictingMove();
						}
					} else {
						return moveKingTowardRestrictingRow();
					}
				} else {
					if (moveWouldLeadToStalemate(moveQueenToEdgeRestriction())) {
						return makeQueenSafetyMove();
					} else {
						return moveQueenToEdgeRestriction();
					}

				}

			} else {
				return moveQueenToEdgeRestriction();
			}

			// throw new RuntimeException("Broken here");
		}

		private Move findCheckmate() {
			for (Move move : getPlayerQueen().calculateLegalMoves(getBoard())) {
				Board checkmateBoard = move.executeMoveAndBuildBoard();

				if (checkmateBoard.getCurrentPlayer().isCheckMate()) {
					return move;
				}
			}
			throw new RuntimeException("Unexpected problem here");
		}

		public Move moveQueenToEdgeRestriction() {
			Move restrictingMove = null;
			int targetKingPosition = getTargetKing().getPiecePosition();
			int[] desiredCoordinates = { 6, 10 };

			if (getMatingEdge() == 0) {
				// don't change
			} else if (getMatingEdge() == 7) {
				desiredCoordinates[0] = -desiredCoordinates[0];
				desiredCoordinates[1] = -desiredCoordinates[1];
			}

			for (int i : desiredCoordinates) {

				if ((getTargetKingColumn() == 0 || getTargetKingColumn() == 1) && (i == 6 || i == -10)) {
					continue;
				} else if ((getTargetKingColumn() == 6 || getTargetKingColumn() == 7) && (i == -6 || i == 10)) {
					continue;
				}
				restrictingMove = traverseQueenMoves(targetKingPosition + i);
				if (restrictingMove == null) {
					continue;
				} else {
					if (pieceNotAttackedAfterMove(restrictingMove)) {
						return restrictingMove;
					} else {
						continue;
					}
				}
			}

			if (restrictingMove == null) {
				return makeQueenSafetyMove();
			}
			throw new RuntimeException("Problem moving the queen to restriction!");
		}

		public Move makeQueenSafetyMove() {
			int direction = 1337;

			if (getMatingEdge() == 0) {
				direction = 1;
			} else {
				direction = -1;
			}
			for (Move move : getPlayerQueen().calculateLegalMoves(getBoard())) { // good move
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == getTargetKingRow() + direction) {
					Board newBoard = move.executeMoveAndBuildBoard();
					Piece newQueen = findLonePiece(
							newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getActivePieces());
					if (pieceNotAttackedAfterMove(move)
							|| BoardUtility.isPieceDefended(newQueen, newBoard, newQueen.getPieceAlliance())) {
						if (moveWouldLeadToStalemate(move)) {
							continue;
						} else {
							return move;
						}
					}
				}
			}
			// move to not crash the program
			for (Move move : getPlayerQueen().calculateLegalMoves(getBoard())) {
				Board newBoard = move.executeMoveAndBuildBoard();
				Piece newQueen = findLonePiece(
						newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getActivePieces());
				if (pieceNotAttackedAfterMove(move)
						|| BoardUtility.isPieceDefended(newQueen, newBoard, newQueen.getPieceAlliance())) {
					if (moveWouldLeadToStalemate(move)) {
						continue;
					} else {
						return move;
					}

				}
			}

			throw new RuntimeException("This method needs attention");
		}

		public boolean isCheckingPositionPosition() { // if it is whites move and this returns true, then search queen
														// moves for good checking mvoes
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

		public Move traverseQueenMoves(int desiredDestinationCoordinate) {
			for (Move move : getPlayerQueen().calculateLegalMoves(this.board)) {
				if (move.getDestinationTileCoordinate() == desiredDestinationCoordinate) {
					return move;
				}
			}
			// throw new RuntimeException("No move like that exists! This method needs
			// attention");
			return null;
		}

		public boolean isQueenPinningTargetKingAgainstMatingRow() {
			if (getMatingEdge() == 0) {
				return BoardUtility.calculateRow(getPlayerQueen().getPiecePosition()) == 1;
			} else {
				return BoardUtility.calculateRow(getPlayerQueen().getPiecePosition()) == 6;
			}
		}

		public Piece getPlayerQueen() {
			return playerQueen;
		}

		public boolean isTargetKingOnMatingRow() {
			return targetKingOnMatingRow;
		}

	}

}
