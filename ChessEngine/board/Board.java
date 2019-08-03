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
import board.Tile;

/***
 * @author 1200046h This class is responsible for creating an instance of a
 *         board/position
 */

// TO DO: Create an instance of a board
public class Board {

	private List<Tile> chessBoardPosition;
	private List<Piece> activeWhitePieces;
	private List<Piece> activeBlackPieces;

	private Board(Builder builder) {
		this.chessBoardPosition = populateBoard(builder);
		this.activeBlackPieces = calculateActivePieces(this.chessBoardPosition, Alliance.BLACK);
		this.activeWhitePieces = calculateActivePieces(this.chessBoardPosition, Alliance.WHITE);
	}

	// Board needs to know all legal moves for each player in a position
	// calculateLegalMovesForPosition() ?

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

	// Use builder pattern to create an instance of a board position

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

	public static class Builder {
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

	public Tile getTile(int tileCoordinate) {
		return this.chessBoardPosition.get(tileCoordinate);
	}
}
