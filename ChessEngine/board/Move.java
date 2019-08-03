package board;

import pieces.Piece;

/***
 * @author 1200046h Move.java will contain the Move class and its children which
 *         are responsible for the intricacies of move logic.
 */

public class Move {

	protected Board board;
	protected Piece movedPiece;
	protected Tile destinationTile;

	public Move(Board board, Piece movedPiece, Tile destinationTile) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationTile = destinationTile;
	}
	public static final class NonAttackingMove extends Move { // Remember to make this work

		public NonAttackingMove(Board board, Piece movedPiece, Tile destinationTile) {
			super(board, movedPiece, destinationTile);
		}
	}
	
	public static final class AttackingMove extends Move {

		protected Piece attackedPiece;

		public AttackingMove(Board board, Piece movedPiece, Tile destinationTile, Piece attackedPiece) {
			super(board, movedPiece, destinationTile);
			this.attackedPiece = attackedPiece;
		}
	}
}
