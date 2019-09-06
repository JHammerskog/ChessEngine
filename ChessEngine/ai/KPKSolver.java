package ai;

import java.util.List;

import board.Alliance;
import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import pieces.Piece;
import pieces.Piece.PieceType;

/***
 * This class will contain the logic for solving a King-Pawn vs King endgame and
 * inherits from the EndgameSolver class.
 * 
 * This endgame has a plethora of exceptions and edge-cases, and is not perfect
 * in its current implementation.
 * 
 * This heuristic will ONLY work on a board with ONLY three pieces as described
 * above. Currently, this King-Pawn vs King solver can solve the majority of KPK
 * puzzles given to it.
 * 
 ***/

public class KPKSolver extends EndgameSolver {

	private final Piece lonePiece;

	public KPKSolver(Board board) {
		super(board);

		this.lonePiece = findLonePiece(board.getCurrentPlayer().getActivePieces());

	}

	public Move generateKPKMove(Board board) {

		if (getLonePiece().getPieceType() == PieceType.PAWN) {
			PawnSolver p = new PawnSolver(board);

			if (p.cannotWinScenario(board)) { // If the piece is a pawn, there are many unwinnable positions which are
												// checked for here
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

	// Getter

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

		public Move generateKingPawnMove() { // Main function responsible for generating a kingPawn move

			if (getTargetKingStepsToPromotionTile() > getPawnStepsToPromotion()) {
				return makePawnMove();
			}

			else if (playerKingIsOnTargetRow()) {
				if (pawnWouldBeSafeAfterMove()) {
					return makePawnMove();
				} else {
					return moveKingTowardDefense();
				}
			} else {
				if (getTargetKingRow() >= getPlayerKingRow()) {
					if (getPlayerKingColumn() > getTargetKingColumn()
							&& (getPlayerKingColumn() < getPawnColumn() || getPlayerKingColumn() < getTargetKingColumn()
									&& getPlayerKingColumn() > getPawnColumn())) {
						if (getTargetKingStepsToAttack() - getPlayerKingStepsToDefense() > 2) {
							return attemptToBlockTargetKing();
						} else {
							return makePawnMove();
						}

					}
				}

				Move move = tryWinningScenarios();// last attempt to generate winning position
				if (move != null) {
					return move;
				} else {

					// The below code should only be run if the heuristics encounter a supposed
					// winning situation but can't generate any of its preferred moves.
					if (getPlayerKingRow() == 2) {
						Move lastDitchMove = MoveMaker.getMove(getBoard(), getPlayerKing().getPiecePosition(),
								getPlayerKing().getPiecePosition() - 8);
						if (lastDitchMove != null) {
							return lastDitchMove;
						} else {
							return makePawnMove();
						}
					}

				}

			}
			return null; // if the code reaches here, the heuristic will claim a draw
		}

		private Move attemptToBlockTargetKing() {
			int[] idealCoordinates = { -8, -7, -9 };
			Move kingMove = null;

			if (getPlayerKingColumn() > getTargetKingColumn()) {
				int temp = idealCoordinates[1];
				idealCoordinates[1] = idealCoordinates[2];
				idealCoordinates[2] = temp; // If the kings column is higher than the king, blcoking with a -9 move is
											// more ideal than a -7 move.
			}

			for (int coordinate : idealCoordinates) {
				Move move = MoveMaker.getMove(getBoard(), getPlayerKing().getPiecePosition(),
						getPlayerKing().getPiecePosition() + coordinate);
				if (move != null) {
					kingMove = move;
					break;
				}
			}

			Board newBoard = kingMove.executeMoveAndBuildBoard();
			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck()) {
				return kingMove;
			}
			throw new RuntimeException("Problem with moving king up");

		}

		public Move tryWinningScenarios() {
			for (Move move : getBoard().getCurrentPlayer().getLegalMovesInPosition()) {
				Board hopefulWinningBoard = move.executeMoveAndBuildBoard();
				if (winningCaseOne(hopefulWinningBoard) || winningCaseTwo(hopefulWinningBoard)) {
					return move;
				}
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

			moveTowardRestriction = MoveMaker.getMove(getBoard(), getPlayerKing().getPiecePosition(),
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

		public boolean winningCaseOne(Board board) {
			// very specific position which is an exception to the rule when the king is in
			// front of the enemy pawn
			int distanceBetweenPawnAndTargetKing = getTargetKing().getPiecePosition() - getPawn().getPiecePosition();

			if (board.getCurrentPlayer().getAlliance() == Alliance.WHITE) {
				if (getPlayerKingRow() == 2 && !(getPlayerKing().getDefendedPieces().isEmpty())) {
					if (distanceBetweenPawnAndTargetKing == -16) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean winningCaseTwo(Board board) {

			int distanceBetweenKings = getTargetKing().getPiecePosition() - getPlayerKing().getPiecePosition();
			if (board.getCurrentPlayer().getAlliance() == Alliance.WHITE) {
				if (getPlayerKingRow() == 2
						&& (getPawn().getPiecePosition() - 8 == getPlayerKing().getPiecePosition())) {
					if (distanceBetweenKings == -15 || distanceBetweenKings == -17) {
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

		public boolean cannotWinScenario(Board board) {

			if (winningCaseOne(board) || winningCaseTwo(board)) {
				return false;
			}

			if (pawnIsOnEdge(getPawn().getPiecePosition())) {
				if (calculateKingStepsToDefendTile(getPlayerKingColumn(), getPlayerKingColumn(),
						getPromotionTileCoordinate()) > calculateKingStepsToDefendTile(getTargetKingColumn(),
								getTargetKingColumn(), getPromotionTileCoordinate())
						&& (getTargetKingStepsToPromotionTile() <= getPawnStepsToPromotion()
								|| (getTargetKingColumn() == getPawnColumn()))) {
					// Rook pawn that cannot promote for free or be defended on time, or when king
					// is in front of the pawn
					return true;
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

		public Move generateKingQueenMove() { // function called to generate a king-queen move

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
			} else if (getMatingEdge() == 7) { // If mating on the bottom row, the desired coordinates are above instead
												// of below the king.
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
			int desiredRowDirection = 1337;

			if (getMatingEdge() == 0) {
				desiredRowDirection = 1;
			} else {
				desiredRowDirection = -1;
			}
			for (Move move : getPlayerQueen().calculateLegalMoves(getBoard())) { // good move
				if (BoardUtility.calculateRow(move.getDestinationTileCoordinate()) == getTargetKingRow()
						+ desiredRowDirection) {
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
			// if the above loop does not generate a move, then this funtion simply returns
			// a legal queen move which is safe. This will generate a new position where it
			// is extremely likely that the heuristic will be able to generate a winning
			// move.
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
