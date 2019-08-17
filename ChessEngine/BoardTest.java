import static org.junit.Assert.*;

import org.junit.Test;

import ai.MiniMax;
import board.Board;
import board.Move;

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
	public void testMiniMax() { // Testing a simple mate-in-two puzzle
		Board b = Board.KRKMateInTwo();
		MiniMax m = new MiniMax(2);

		Move bestMove = m.minimax(2, true, b); // should be Rook to tile 29
		Board b1 = bestMove.executeMoveAndBuildBoard();

		Move bestMove1 = m.minimax(2, false, b1); // Only legal move is King to tile 3
		Board b2 = bestMove1.executeMoveAndBuildBoard();

		Move bestMove2 = m.minimax(2, true, b2); // Should be rook to tile 5, checkmate
		Board b3 = bestMove2.executeMoveAndBuildBoard();

		assertEquals("bestMove destinationtile should be 29", 29, bestMove.getDestinationTileCoordinate());
		assertEquals("bestMove1 destinationtile should be 3", 3, bestMove1.getDestinationTileCoordinate());
		assertEquals("bestMove2 destinationtile should be 5", 5, bestMove2.getDestinationTileCoordinate());
		assertTrue(b3.getCurrentPlayer().isCheckMate()); // Checks checkmate, move to different TestCase?

	}

}
