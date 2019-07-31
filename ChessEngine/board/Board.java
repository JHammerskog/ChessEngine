package board;

import java.util.List;
import java.util.Map;

import pieces.Piece;
import board.Tile;

/***
 * @author 1200046h This class is responsible for creating an instance of a
 *         board/position
 */

// TO DO: Create an instance of a board
public class Board {

	private List<Tile> chessBoardPosition;

	private Board(Builder builder) {
		this.chessBoardPosition = populateBoard(builder);
	}

	private List<Tile> populateBoard(Builder builder) { // needs testing

		List<Tile> tileList = null;
		for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
			tileList.add(Tile.createTile(i, builder.pieceConfig.get(i)));
		}
		return tileList;

	}

	// Use builder pattern to create an instance of a board position

	public class Builder {
		Map<Integer, Piece> pieceConfig;
		Player nextPlayerToMove;

		public Builder() {
		}

		public Builder setPiece(Piece piece) {
			this.pieceConfig.put(piece.getPiecePosition(), piece);
			return this;
		}

		public Builder setNextPlayerToMove(Player nextPlayerToMove) {
			this.nextPlayerToMove = nextPlayerToMove;
			return this;

		}

		public Board build() {
			return new Board(this);
		}
	}

	public Tile getTile(int candidateCoordinate) {
		return null;
	}
}
