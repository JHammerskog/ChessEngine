
import java.util.Set;

public abstract class Piece {
	
	protected final int piecePosition;
	protected final Player playerColour;
	
	
	
	protected Piece(final int piecePosition, final Player playerColour) {
		this.piecePosition=piecePosition;
		this.playerColour=playerColour;
	}
	
	
	public abstract Set<Move> calculateLegalMoves(Board Board);
	
	private boolean destinationPositionIsOnBoard(int destPosition) {
		return (destPosition >= 0 && destPosition <= 64);
	}
	// This method will most likely be moved to MoveLogic when done
	
	public Player getPieceColour() { // returns the colour of the piece
		return this.playerColour;
	}
}
