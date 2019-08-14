import static org.junit.Assert.*;

import org.junit.Test;
import board.Board;

public class BoardTest {

	@Test
	public void testStartingLegalMoves() {
		Board b = Board.createStartingPosition();

		// Number of pieces (Test passes)
		assertEquals("# of Black pieces should be 16", 16, b.getActiveBlackPieces().size());
		assertEquals("# of White pieces should be 16", 16, b.getActiveWhitePieces().size());

		// Legal Moves (Test fails)
		assertEquals("legalmoves should be 20", 20, b.getCurrentPlayer().getLegalMovesInPosition().size());
		assertEquals("legalmoves should be 20", 20,
				b.getOpponent(b.getCurrentPlayer().getAlliance()).getLegalMovesInPosition().size());
		// This is failing because you havent correctly implemented pawnjump yet.

	}

	@Test
	public void testKRKMateInTwo() {
		Board b = Board.KRKMateInTwo(); // White to play, white should have 14 legal rook moves, and 5 legal king moves

		// Legal Moves (Test Fails)
		assertEquals("legalmoves should be 19", 19, b.getCurrentPlayer().getLegalMovesInPosition().size());
		assertEquals("legalmoves should be 19", 19, b.getCurrentPlayer().getLegalMovesInPosition().size());

		// This is failing because the king is being passed psuedolegalmoves instead of
		// legal ones.
		// This yields 3 extra "illegal" legalmoves

	}

	@Test
	public void testCheckmate() {

		Board b = Board.checkmatePosition();
		// Passes
		assertTrue(b.getCurrentPlayer().isCheckMate());

	}

}
