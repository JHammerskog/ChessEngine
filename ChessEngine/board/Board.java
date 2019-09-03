package board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pieces.Bishop; // Ugly imports, but do not need piece super class or BoardTransition here
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import player.BlackPlayer;
import player.Player;
import player.WhitePlayer;

/***
 * This class describes the logic of creating a board. It also has a nested
 * class which uses the builder pattern to create an instance of a board.
 */

public class Board {

	private List<Tile> chessBoardPosition;

	private List<Piece> activeWhitePieces; // Variables concerned with tracking pieces/playerstatus
	private List<Piece> activeBlackPieces;

	private WhitePlayer whitePlayer; // Use these to determine currentPlayer (currently in there Player class)
	private BlackPlayer blackPlayer;
	private Player currentPlayer;

	private Board(Builder builder) {

		this.chessBoardPosition = populateBoard(builder);
		this.activeBlackPieces = calculateActivePieces(this.chessBoardPosition, Alliance.BLACK);
		this.activeWhitePieces = calculateActivePieces(this.chessBoardPosition, Alliance.WHITE);

		// The below variables are needed for check evaluation etc, a board needs to
		// keep track of the possible legal moves for each player.
		List<Move> whiteLegalMovesInPosition = calculateLegalMovesForPosition(this.activeWhitePieces);
		List<Move> blackLegalMovesInPosition = calculateLegalMovesForPosition(this.activeBlackPieces);
		List<Piece> defendedWhitePieces = calculateDefendedPieces(this.activeWhitePieces);
		List<Piece> defendedBlackPieces = calculateDefendedPieces(this.activeBlackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteLegalMovesInPosition, blackLegalMovesInPosition,
				defendedWhitePieces);
		this.blackPlayer = new BlackPlayer(this, blackLegalMovesInPosition, whiteLegalMovesInPosition,
				defendedBlackPieces);

		this.currentPlayer = setCurrentPlayer(builder.nextPlayerToMove);

	}

	private List<Tile> populateBoard(Builder builder) {

		final List<Tile> tileList = new ArrayList<>();
		for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
			tileList.add(Tile.createTile(i, builder.pieceConfig.get(i)));
		}
		return Collections.unmodifiableList(tileList);

	}

	private List<Piece> calculateActivePieces(List<Tile> chessBoardPosition, Alliance alliance) {

		final List<Piece> activePiecesForPlayer = new ArrayList<>();
		for (Tile tile : chessBoardPosition) {
			if (tile.tileIsOccupied()) {
				Piece piece = tile.getPiece();
				if (piece.getPieceAlliance() == alliance) {
					activePiecesForPlayer.add(piece);
				}
			}
		}

		return Collections.unmodifiableList(activePiecesForPlayer);
	}

	private List<Piece> calculateDefendedPieces(List<Piece> activePieces) {
		final List<Piece> defendedPieces = new ArrayList<>();

		for (Piece piece : activePieces) {
			for (Piece originalPiece : piece.getDefendedPieces()) {
				if (!defendedPieces.contains(originalPiece)) {
					defendedPieces.add(originalPiece);
				}
			}
		}
		return Collections.unmodifiableList(defendedPieces);
	}

	private List<Move> calculateLegalMovesForPosition(List<Piece> activePieces) {

		final List<Move> legalMoves = new ArrayList<>();

		for (Piece piece : activePieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return Collections.unmodifiableList(legalMoves);
	}

	// Getters and Setters below

	public Tile getTile(int tileCoordinate) {
		return this.chessBoardPosition.get(tileCoordinate);
	}

	// Maybe find better solution than the two below methods?
	public List<Piece> getActiveWhitePieces() {
		return activeWhitePieces;
	}

	public List<Piece> getActiveBlackPieces() {
		return activeBlackPieces;
	}

	public Player getOpponent(Alliance alliance) {
		if (alliance == Alliance.WHITE) {
			return this.blackPlayer;
		} else if (alliance == Alliance.BLACK) {
			return this.whitePlayer;
		}

		throw new RuntimeException("Illegal Alliance modifier");
	}

	public WhitePlayer getWhitePlayer() {
		return whitePlayer;
	}

	public BlackPlayer getBlackPlayer() {
		return blackPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Alliance setCurrentPlayerAlliance(Alliance currentPlayer) {
		return currentPlayer;
	}

	public Player setCurrentPlayer(Alliance currentPlayer) {
		if (currentPlayer == Alliance.BLACK) {
			return this.blackPlayer;
		} else if (currentPlayer == Alliance.WHITE) {
			return this.whitePlayer;
		}
		throw new RuntimeException("Player type can only be 'BLACK' or 'WHITE'.");
	}

	public boolean onlyKingsLeft() {
		if (getActiveBlackPieces().size() + getActiveWhitePieces().size() == 2) {
			return true;
		}
		return false;
	}

	public String toString() {
		StringBuilder strbuilder = new StringBuilder();
		for (int i = 0; i < BoardUtility.numberOfTiles; i++) {
			String tileString = this.chessBoardPosition.get(i).toString();
			strbuilder.append(String.format("%2s", tileString));
			if ((i + 1) % BoardUtility.tilesPerColumn == 0) {
				strbuilder.append("\n");
			}
		}

		return strbuilder.toString();
	}

	public static class Builder { // Class using builder pattern
		protected Map<Integer, Piece> pieceConfig;
		protected Alliance nextPlayerToMove;

		public Builder() {
			this.pieceConfig = new HashMap<>();
		}

		public Builder setPiece(Piece piece) {
			this.pieceConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setNextPlayerToMove(Alliance nextPlayerToMove) {
			this.nextPlayerToMove = nextPlayerToMove;
			return this;

		}

		public Board build() {
			return new Board(this);
		}
	}

	public static Board createStartingPosition() { // Use this type of method to create specific positions
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		// White pieces
		b.setPiece(new Rook(63, Alliance.WHITE));
		b.setPiece(new Knight(62, Alliance.WHITE));
		b.setPiece(new Bishop(61, Alliance.WHITE));
		b.setPiece(new King(60, Alliance.WHITE));
		b.setPiece(new Queen(59, Alliance.WHITE));
		b.setPiece(new Bishop(58, Alliance.WHITE));
		b.setPiece(new Knight(57, Alliance.WHITE));
		b.setPiece(new Rook(56, Alliance.WHITE));

		b.setPiece(new Pawn(55, Alliance.WHITE));
		b.setPiece(new Pawn(54, Alliance.WHITE));
		b.setPiece(new Pawn(53, Alliance.WHITE));
		b.setPiece(new Pawn(52, Alliance.WHITE));
		b.setPiece(new Pawn(51, Alliance.WHITE));
		b.setPiece(new Pawn(50, Alliance.WHITE));
		b.setPiece(new Pawn(49, Alliance.WHITE));
		b.setPiece(new Pawn(48, Alliance.WHITE));

		// Black pieces
		b.setPiece(new Rook(0, Alliance.BLACK));
		b.setPiece(new Knight(1, Alliance.BLACK));
		b.setPiece(new Bishop(2, Alliance.BLACK));
		b.setPiece(new Queen(3, Alliance.BLACK));
		b.setPiece(new King(4, Alliance.BLACK));
		b.setPiece(new Bishop(5, Alliance.BLACK));
		b.setPiece(new Knight(6, Alliance.BLACK));
		b.setPiece(new Rook(7, Alliance.BLACK));

		b.setPiece(new Pawn(8, Alliance.BLACK));
		b.setPiece(new Pawn(9, Alliance.BLACK));
		b.setPiece(new Pawn(10, Alliance.BLACK));
		b.setPiece(new Pawn(11, Alliance.BLACK));
		b.setPiece(new Pawn(12, Alliance.BLACK));
		b.setPiece(new Pawn(13, Alliance.BLACK));
		b.setPiece(new Pawn(14, Alliance.BLACK));
		b.setPiece(new Pawn(15, Alliance.BLACK));
		return b.build();
	}

	public static Board KRKMateInTwo() {
		// This is an easy King-Rook-King (KRK)puzzle where white can check mate black
		// in two moves.

		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE); // White to move

		b.setPiece(new King(4, Alliance.BLACK));

		b.setPiece(new King(19, Alliance.WHITE));
		b.setPiece(new Rook(24, Alliance.WHITE)); // rook can be anywhere > 20 && not 5th column
		return b.build();

	}

	public static Board KRKMateInFive() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(16, Alliance.BLACK));

		b.setPiece(new King(33, Alliance.WHITE));
		b.setPiece(new Rook(26, Alliance.WHITE));

		return b.build();
	}

	public static Board TestBoard() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(19, Alliance.BLACK));

		b.setPiece(new King(37, Alliance.WHITE));
		b.setPiece(new Rook(31, Alliance.WHITE));

		return b.build();
	}

	public static Board KPKBoardOne() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(35, Alliance.BLACK));

		b.setPiece(new King(17, Alliance.WHITE));
		b.setPiece(new Pawn(24, Alliance.WHITE));

		return b.build();
	}

	public static Board KPKBoardTwo() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(22, Alliance.BLACK));

		b.setPiece(new King(37, Alliance.WHITE));
		b.setPiece(new Pawn(30, Alliance.WHITE));

		return b.build();
	}

	public static Board KPKBoardThree() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(3, Alliance.BLACK));

		b.setPiece(new King(17, Alliance.WHITE));
		b.setPiece(new Pawn(18, Alliance.WHITE));

		return b.build();
	}

	public static Board StaleMateBoard() {

		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.BLACK);

		b.setPiece(new King(15, Alliance.BLACK));

		b.setPiece(new King(13, Alliance.WHITE));
		b.setPiece(new Pawn(21, Alliance.WHITE));

		return b.build();
	}

	public static Board clearBoard() { // This crashes the player class, try/catch the findKing() method and refactor
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		return b.build();
	}
	
	public static Board queenBoard() { // This crashes the player class, try/catch the findKing() method and refactor
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);
		
		b.setPiece(new King(8, Alliance.BLACK));

		b.setPiece(new King(24, Alliance.WHITE));
		b.setPiece(new Queen(7, Alliance.WHITE));

		return b.build();
	}

}
