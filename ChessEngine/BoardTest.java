import static org.junit.Assert.*;

import org.junit.Test;

import ai.KPKSolver;
import ai.KPKSolver.PawnSolver;
import ai.KRKSolver;
import ai.MiniMax;
import board.Board;
import board.Move;

public class BoardTest {

	@Test
	public void testStartingLegalMoves() {
		Board b = Board.createStartingPosition();
		int whiteLegalMoves = 0;
		int blackLegalMoves = 0;

		// Number of pieces (Test passes)
		assertEquals("# of Black pieces should be 16", 16, b.getActiveBlackPieces().size());
		assertEquals("# of White pieces should be 16", 16, b.getActiveWhitePieces().size());

		for (Move move : b.getCurrentPlayer().getLegalMovesInPosition()) {
			Board newBoard = move.executeMoveAndBuildBoard();
			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck()) {
				whiteLegalMoves++;
			}
		}

		for (Move move : b.getOpponent(b.getCurrentPlayer().getAlliance()).getLegalMovesInPosition()) {
			Board newBoard = move.executeMoveAndBuildBoard();
			if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck()) {
				blackLegalMoves++;
			}
		}

		assertEquals("legalmoves should be 12", 12, whiteLegalMoves); // it is set to 12 instead of 20 since pawn double
																		// jump is not implemented // jump is not
																		// implemented
		assertEquals("legalmoves should be 12", 12, blackLegalMoves);

	}

	@Test
	public void testStaleAndCheckmate() {
		Board b = Board.CheckmateBoard();
		Board b1 = Board.StaleMateBoard();

		assertTrue("Position should be checkmate", b.getCurrentPlayer().isCheckMate());
		assertTrue("Position should be stalemate", b1.getCurrentPlayer().isStaleMate());

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

	@Test
	public void testKRKGenerateMoveInLessThanOneSecond() {

		Board testBoard = Board.KRKMateInFive();

		long startTime = System.currentTimeMillis();

		KRKSolver k = new KRKSolver(testBoard);

		Board buildBoardAswell = k.generateRestrictingMove(testBoard).executeMoveAndBuildBoard(); // Do this for more positions?

		long endTime = System.currentTimeMillis() - startTime;
		assertTrue("Movecounter should be less than 50", endTime < 1000);

		Board testBoard1 = Board.KRKMateInTwo();

		long startTime1 = System.currentTimeMillis();

		KRKSolver k1 = new KRKSolver(testBoard1);

		Board buildBoardAswell1 = k1.generateRestrictingMove(testBoard1).executeMoveAndBuildBoard(); // Do this for more positions?

		long endTime1 = System.currentTimeMillis() - startTime;
		assertTrue("Movecounter should be less than 50", endTime1 < 1000);
		
		Board testBoard2 = Board.KPKBoardOne();

		long startTime2 = System.currentTimeMillis();

		KPKSolver k2 = new KPKSolver(testBoard2);

		Board buildBoardAswell2 = k2.generateKPKMove(testBoard2).executeMoveAndBuildBoard(); // Do this for more positions?

		long endTime2 = System.currentTimeMillis() - startTime;
		assertTrue("Movecounter should be less than 50", endTime2 < 1000);
		
		Board testBoard3 = Board.KPKBoardOne();

		long startTime3 = System.currentTimeMillis();

		KPKSolver k3 = new KPKSolver(testBoard3);

		Board buildBoardAswell3 = k3.generateKPKMove(testBoard3).executeMoveAndBuildBoard(); // Do this for more positions?

		long endTime3 = System.currentTimeMillis() - startTime;
		assertTrue("Movecounter should be less than 50", endTime3 < 1000);

	}
	@Test
	public void testKPKEvaluator() {
		Board testBoard = Board.KPKBoardOne();
		KPKSolver k = new KPKSolver(testBoard);
		
		Move move = k.generateKPKMove(testBoard);
		assertFalse("TestBoard is winnable.", move == null); // This should only be null if position is not winnable

		// make some winnable and some nonwinnable positions
	}

}
