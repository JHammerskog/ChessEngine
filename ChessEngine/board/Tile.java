package board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pieces.Piece;

/***
 * This class defines the behaviour of a singular tile. It's two children define
 * needed logic for occupied/unoccupied tiles.
 */

public abstract class Tile {

	private int tileCoordinate;

	private Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	private static Map<Integer, UnoccupiedTile> emptyTiles = makeAllEmptyTiles();

	private static Map<Integer, UnoccupiedTile> makeAllEmptyTiles() {
		final Map<Integer, UnoccupiedTile> emptyTiles = new HashMap<>();

		for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
			emptyTiles.put(i, new UnoccupiedTile(i));
		}

		return Collections.unmodifiableMap(emptyTiles); // See how much immutability you can work into rest of
														// application
	}

	public static Tile createTile(final int coordinate, final Piece piece) { // Make more readable

		if (piece != null) {
			return new OccupiedTile(coordinate, piece);
		} else {
			return emptyTiles.get(coordinate);
		}

		// Tile Factory

	}

	public abstract boolean tileIsOccupied();

	public abstract Piece getPiece();

	private static final class OccupiedTile extends Tile { // tiles with piece on them

		private final Piece occupyingPiece;

		OccupiedTile(int coordinate, Piece occupyingPiece) {
			super(coordinate);
			this.occupyingPiece = occupyingPiece;

		}

		@Override
		public boolean tileIsOccupied() {
			return true;

		}

		@Override
		public Piece getPiece() {
			return this.occupyingPiece;
		}

		public String toString() {
			if (this.getPiece().getPieceAlliance() == Alliance.BLACK) {
				return this.getPiece().toString().toLowerCase();
			}
			return this.getPiece().toString();
		}
	}

	public static final class UnoccupiedTile extends Tile { // Tiles with no current pieces

		private UnoccupiedTile(int coordinate) {
			super(coordinate);

		}

		@Override
		public boolean tileIsOccupied() {
			return false;

		}

		@Override
		public Piece getPiece() {
			return null;
		}

		public String toString() {
			return "-";
		}
	}
	
	public int getTileCoordinate() {
		return tileCoordinate;
	}

}
