package board;

import java.util.Map;

import pieces.Piece;

import java.util.Collections;
import java.util.HashMap;

public abstract class Tile { // SuperClass that defines general tile logic

	private final int coordinate; // 0 = A8, 63 = H1

	private Tile(int coordinate) {
		this.coordinate = coordinate;
	}

	public abstract boolean tileIsOccupied();

	public abstract Piece getPiece();

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

		// Tile Factory
		return piece != null ? new OccupiedTile(coordinate, piece) : emptyTiles.get(coordinate);
		// if piece is not null (aka there is a piece on the tile) make a new Tile with
		// that piece on it : Otherwise make an empty tile at coordinate
	}

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
			if (this.getPiece().getPieceColour() == Alliance.BLACK) {
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

}
