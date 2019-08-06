package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Piece.PieceType;
import player.BlackPlayer;
import player.WhitePlayer;
import board.Tile;

/***
 * This class describes the logic of
 */

public class Board {

	private List<Tile> chessBoardPosition;

	private List<Piece> activeWhitePieces; // Variables concerned with tracking pieces/playerstatus
	private List<Piece> activeBlackPieces;
	
	private WhitePlayer whitePlayer; // Use these to determine currentPlayer (currently in ther Player class)
	private BlackPlayer blackPlayer;

	private Board(Builder builder) {
		this.chessBoardPosition = populateBoard(builder);
		this.activeBlackPieces = calculateActivePieces(this.chessBoardPosition, Alliance.BLACK);
		this.activeWhitePieces = calculateActivePieces(this.chessBoardPosition, Alliance.WHITE);

		// The below variables are needed for check evaluation etc, a board needs to
		// keep track of the possible legal moves for each player.
		List<Move> whiteLegalMovesInPosition = calculateLegalMovesForPosition(this.activeWhitePieces);
		List<Move> blackLegalMovesInPosition = calculateLegalMovesForPosition(this.activeBlackPieces);

		this.whitePlayer = new WhitePlayer(this, whiteLegalMovesInPosition, blackLegalMovesInPosition);
		this.blackPlayer = new BlackPlayer(this, whiteLegalMovesInPosition, blackLegalMovesInPosition);

	}

	public Piece findKing(Alliance alliance) {
		Piece nullPlaceHolder = null;
		List<Piece> activePieces = new ArrayList<>();
		if (alliance == Alliance.BLACK) {
			activePieces = activeBlackPieces;
		} else {
			activePieces = activeWhitePieces;
		}

		for (Piece piece : activePieces) { // Add PieceType in constructor of piece
			if (piece.getPieceType() == PieceType.KING) {
				return piece;
			}
		}
		throw new RuntimeException("Illegal game state. The black player must have a king");
	}

	private List<Tile> populateBoard(Builder builder) { // needs testing

		List<Tile> tileList = new ArrayList<>();
		for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
			tileList.add(Tile.createTile(i, builder.pieceConfig.get(i)));
		}
		return tileList; // Collections.unmodifiableList?

	}

	private List<Piece> calculateActivePieces(List<Tile> chessBoardPosition, Alliance alliance) {
		List<Piece> activePiecesForPlayer = new ArrayList<>();
		for (Tile tile : chessBoardPosition) {
			if (tile.tileIsOccupied()) {
				Piece piece = tile.getPiece();
				if (piece.getPieceColour() == alliance) {
					activePiecesForPlayer.add(piece);
				}
			}
		}
		return activePiecesForPlayer;
	}

	private List<Move> calculateLegalMovesForPosition(List<Piece> activePieces) {
		List<Move> legalMoves = new ArrayList<>();

		for (Piece piece : activePieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
			// Changed all sets to Lists because of compatibility with this method
		}
		return legalMoves;
	}

	// Use builder pattern to create an instance of a board position

	public Tile getTile(int tileCoordinate) {
		return this.chessBoardPosition.get(tileCoordinate);
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

	public static class Builder { // This class actually builds a board
		Map<Integer, Piece> pieceConfig;
		Alliance nextPlayerToMove;

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
		Builder b = new Builder();
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

		Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE); // White to move

		b.setPiece(new King(4, Alliance.BLACK));

		b.setPiece(new King(11, Alliance.WHITE));
		b.setPiece(new Rook(19, Alliance.WHITE)); // Rook can be set to any number 16-63
		return b.build();

	}

}
