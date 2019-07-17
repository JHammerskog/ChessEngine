import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import board.Tile;
import board.Tile.UnoccupiedTile;
import pieces.Bishop;
import pieces.Knight;
import board.Board;
import board.Player;

public class main {

	public static void main(String[] args) {

		// Test area

		Board b = new Board();
		// Bishop q = new Bishop(51, WHITE);
		// Knight q = new Knight(19, WHITE);

		int[] board = new int[64];

		for (int i = 0; i < board.length; i++) {
			board[i] = i;
		}

		// q.calculateLegalMoves(b);

		// Bishop and knight logic working

	}

	// future main
}