package board;

import java.util.Map;

import pieces.Piece;

import java.util.Collections;
import java.util.HashMap;

public abstract class Tile { // Class that defines the 64 tiles

	private final int coordinate; // 0 = A8, 63 = H1
	// private&final because all coordinates should be set at initialization

	private Tile(int coordinate) {
		this.coordinate = coordinate;
	}

	public abstract boolean tileIsOccupied();

	public abstract Piece getPiece();

	private Map<Integer, UnoccupiedTile> emptyTiles = makeAllEmptyTiles();

	private Map<Integer, UnoccupiedTile> makeAllEmptyTiles() {
		final Map<Integer, UnoccupiedTile> emptyTiles = new HashMap<>();

		for (int i = 0; i < 64; i++) {
			emptyTiles.put(i, new UnoccupiedTile(i));
		}

		return Collections.unmodifiableMap(emptyTiles); // Makes the map immutable so it cant be unintentionally
														// modified
	}

	public Tile createTile(final int coordinate, final Piece piece) { // Tile Factory
		return piece != null ? new OccupiedTile(coordinate, piece) : emptyTiles.get(coordinate);
		// if piece is not null (aka there is a piece on the tile, make a new Tile with
		// that piece on it : Otherwise make an empty tile at coordinate
	}

	private final class OccupiedTile extends Tile { // tiles with piece on them

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

	}

	public final class UnoccupiedTile extends Tile { // Tiles with no current pieces

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
	}

}
