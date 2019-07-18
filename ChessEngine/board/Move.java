package board;

import pieces.Piece;

public class Move {

	Player attackingPlayer;
	Piece movedPiece;
	Tile destinationTile;

	private static final int numberOfTiles = 64; // Set to tiles on a chess board
	private static final int tilesPerColumn = 8; // Set to tiles per column on a chess board

	private static int[] generateColumn(int currentTilePosition) { // Currently redundant, keeping for now

		int[] columnArray = new int[8];
		int tempTile = currentTilePosition;

		while (tempTile > 0) {
			// While loop not necessary at the moment, but may be useful if I want to pass
			// the method a tileCoordinate that isn't on the 8th rank
			tempTile = tempTile - tilesPerColumn;
		}

		for (int i = 0; i < columnArray.length; i++) {
			columnArray[i] = tempTile;
			tempTile = tempTile + tilesPerColumn;
		}
		return columnArray;
	}

	public static boolean contains(int[] column, int candidateCoordinate) { // Currently redundant, keeping for now

		boolean result = false;

		for (int i : column) {
			if (i == candidateCoordinate) {
				result = true;
				break;
			}
		}

		return result;
	}

	public static int calculateColumn(int currentPiecePosition) {
		int result;

		result = currentPiecePosition % tilesPerColumn;

		return result;
	}

	/*
	 * Although the above method reduces abstraction elsewhere, it only affects
	 * classes which are specifically designed to play chess. This is preferable
	 * solution compared with using int[] and for loops.
	 */

	public static boolean validDestinationTile(int destPosition) {
		return (destPosition >= 0 && destPosition < numberOfTiles);
	}

}
