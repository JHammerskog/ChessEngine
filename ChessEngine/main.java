import gui.PuzzleGUI;

public class main {

	public static void main(String[] args) {

		PuzzleGUI puzzleGUI = new PuzzleGUI(); // This call starts the GUI itself

// 		Board b = Board.createStartingPosition();
//		Board b = Board.KRKMateInTwo();
// 		Board b = Board.TestBoard();
//		System.out.println(b);
//		
//		KRKSolver k = new KRKSolver(b); // HARDCODED, you need functions to determine final two
//		
//		k.generateRestrictingMove(b); // Currently doesnt do anything, but needed to set mating parameters
////		System.out.println(k.findKRKMove(b));
//		
//		System.out.println(b.getCurrentPlayer().getDefendedPieces());
//		
//		System.out.println(k.isRookDefended());
//		
//
//		System.out.println(k.kingsAreInOpposition());	
//		System.out.println(k.getRookIsSafe());	
//		System.out.println(k.waitingMoveRequired());
//
//		System.out.println("Original board:\n" + b);
//
//		MiniMax m = new MiniMax(0);
//
//		Move bestMove1 = m.minimax(2, true, b); //
//
//		Board b1 = bestMove1.executeMoveAndBuildBoard();
//
//		System.out.println("1. BEST MOVE: \n\n" + b1);
//
//		Move bestMove2 = m.minimax(2, false, b1);
//
//		Board b2 = bestMove2.executeMoveAndBuildBoard();
//
//		System.out.println("2. BEST MOVE: \n\n" + b2);
//
//		Move bestMove3 = m.minimax(2, true, b2);
//
//		Board b3 = bestMove3.executeMoveAndBuildBoard();
//
//		System.out.println("3. BEST MOVE: \n\n" + b3);
//
//		if (b3.getCurrentPlayer().isCheckMate()) {
//			System.out.println(b3.getCurrentPlayer() + " has been checkmated!");
//		}
//
//		Scanner s = new Scanner(System.in);

//		while (true) { // This while loop is a very testing area
//
//			System.out.println(b);
//
//			System.out.println("IT IS " + b.getCurrentPlayer().getAlliance() + "'s turn to move.");
//
//			System.out.println(b.getCurrentPlayer().getAlliance() + " has pieces on the following coordinates: ");
//			for (Piece piece : b.getCurrentPlayer().getActivePieces()) {
//				System.out.println(piece.getPiecePosition());
//			}
//
//			System.out.println("\n\nPlease enter a tile to move a piece from (0-63).");
//
//			int originTile = s.nextInt();
//
//			if (b.getTile(originTile).tileIsOccupied()) {
//				System.out.println("The piece at tile '" + originTile + "' has the following legal moves:");
//
//				for (Move move : b.getTile(originTile).getPiece().calculateLegalMoves(b)) {
//					System.out.println(move.getDestinationTileCoordinate());
//				}
//
//			}
//
//			System.out.println("\n\nPlease enter a tile to move the piece to. (0-63).");
//
//			int destinationTile = s.nextInt();
//
//			Move move = MoveMaker.getMove(b, originTile, destinationTile);
//			BoardTransition attemptTransition = b.getCurrentPlayer().makeHalfMove(move);
//
//			b = attemptTransition.getNewBoard();
//
//		}

	}

}