package board;

import board.Board.Builder;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Piece.PieceType;
import pieces.Queen;
import pieces.Rook;

public class BoardSetups {

	// player set Position

	public static Board addPieceToBoard(Board board, int piecePosition, PieceType pieceType, Alliance alliance) {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		for (Piece piece : board.getActiveBlackPieces()) {
			b.setPiece(piece);
		}

		for (Piece piece : board.getActiveWhitePieces()) {
			b.setPiece(piece);
		}
		if (pieceType == PieceType.KING) {
			b.setPiece(new King(piecePosition, alliance));
		} else if (pieceType == PieceType.QUEEN) {
			b.setPiece(new Queen(piecePosition, alliance));
		} else if (pieceType == PieceType.ROOK) {
			b.setPiece(new Rook(piecePosition, alliance));
		} else if (pieceType == PieceType.BISHOP) {
			b.setPiece(new Bishop(piecePosition, alliance));
		} else if (pieceType == PieceType.KNIGHT) {
			b.setPiece(new Knight(piecePosition, alliance));
		} else if (pieceType == PieceType.PAWN) {
			b.setPiece(new Pawn(piecePosition, alliance));
		} else {
			throw new RuntimeException("BROKEN!");
		}

		return b.build();
	}

	public static Board removePieceFromBoard(Board board, Piece removedPiece) {
		Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		for (Piece piece : board.getActiveBlackPieces()) {
			if (piece != removedPiece) {
				b.setPiece(piece);
			}

		}

		for (Piece piece : board.getActiveWhitePieces()) {
			if (piece != removedPiece) {
				b.setPiece(piece);
			}
		}

		return b.build();

	}

	// Preset starting position

	public static Board createStartingPosition() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		// White pieces
		b.setPiece(new Rook(63, Alliance.WHITE));
		b.setPiece(new Knight(62, Alliance.WHITE));
		b.setPiece(new Bishop(61, Alliance.WHITE));
		b.setPiece(new King(60, Alliance.WHITE));
		b.setPiece(new Queen(59, Alliance.WHITE));
		b.setPiece(new Bishop(58, Alliance.WHITE));
		b.setPiece(new Knight(57, Alliance.WHITE));
		b.setPiece(new Rook(56, Alliance.WHITE));

		b.setPiece(new Pawn(55, Alliance.WHITE));
		b.setPiece(new Pawn(54, Alliance.WHITE));
		b.setPiece(new Pawn(53, Alliance.WHITE));
		b.setPiece(new Pawn(52, Alliance.WHITE));
		b.setPiece(new Pawn(51, Alliance.WHITE));
		b.setPiece(new Pawn(50, Alliance.WHITE));
		b.setPiece(new Pawn(49, Alliance.WHITE));
		b.setPiece(new Pawn(48, Alliance.WHITE));

		// Black pieces
		b.setPiece(new Rook(0, Alliance.BLACK));
		b.setPiece(new Knight(1, Alliance.BLACK));
		b.setPiece(new Bishop(2, Alliance.BLACK));
		b.setPiece(new Queen(3, Alliance.BLACK));
		b.setPiece(new King(4, Alliance.BLACK));
		b.setPiece(new Bishop(5, Alliance.BLACK));
		b.setPiece(new Knight(6, Alliance.BLACK));
		b.setPiece(new Rook(7, Alliance.BLACK));

		b.setPiece(new Pawn(8, Alliance.BLACK));
		b.setPiece(new Pawn(9, Alliance.BLACK));
		b.setPiece(new Pawn(10, Alliance.BLACK));
		b.setPiece(new Pawn(11, Alliance.BLACK));
		b.setPiece(new Pawn(12, Alliance.BLACK));
		b.setPiece(new Pawn(13, Alliance.BLACK));
		b.setPiece(new Pawn(14, Alliance.BLACK));
		b.setPiece(new Pawn(15, Alliance.BLACK));
		return b.build();
	}

	public static Board clearBoard() { // clear board feature
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		return b.build();
	}

	// KRK preset boards

	public static Board KRKMateInTwo() {
		// This is an easy King-Rook-King (KRK)puzzle where white can check mate black
		// in two moves.

		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE); // White to move

		b.setPiece(new King(4, Alliance.BLACK));

		b.setPiece(new King(19, Alliance.WHITE));
		b.setPiece(new Rook(24, Alliance.WHITE)); // rook can be anywhere > 20 && not 5th column
		return b.build();

	}

	public static Board KRKMateInFive() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(16, Alliance.BLACK));

		b.setPiece(new King(33, Alliance.WHITE));
		b.setPiece(new Rook(26, Alliance.WHITE));

		return b.build();
	}

	public static Board KRKBoardThree() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(35, Alliance.BLACK));

		b.setPiece(new King(0, Alliance.WHITE));
		b.setPiece(new Rook(63, Alliance.WHITE));

		return b.build();
	}
	
	public static Board KRKBoardFour() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(26, Alliance.BLACK));

		b.setPiece(new King(63, Alliance.WHITE));
		b.setPiece(new Rook(1, Alliance.WHITE));

		return b.build();
	}
	
	public static Board KRKBoardFive() {
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(28, Alliance.BLACK));

		b.setPiece(new King(0, Alliance.WHITE));
		b.setPiece(new Rook(59, Alliance.WHITE));

		return b.build();
	}

	// King pawn vs King preset boards

	public static Board KPKBoardOne() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(13, Alliance.BLACK));

		b.setPiece(new King(18, Alliance.WHITE));
		b.setPiece(new Pawn(19, Alliance.WHITE));

		return b.build();
	}

	public static Board KPKBoardTwo() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(40, Alliance.BLACK));

		b.setPiece(new King(58, Alliance.WHITE));
		b.setPiece(new Pawn(47, Alliance.WHITE));

		return b.build();
	}

	public static Board KPKBoardThree() {
		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(50, Alliance.BLACK));

		b.setPiece(new King(35, Alliance.WHITE));
		b.setPiece(new Pawn(52, Alliance.WHITE));

		return b.build();
	}
	
	public static Board unKPKWinnableBoard1() { // This crashes the player class, try/catch the findKing() method and refactor
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(28, Alliance.BLACK));

		b.setPiece(new King(43, Alliance.WHITE));
		b.setPiece(new Pawn(29, Alliance.WHITE));

		return b.build();
	}
	public static Board unKPKWinnableBoard2() { // This crashes the player class, try/catch the findKing() method and refactor
		final Builder b = new Builder();
		b.setNextPlayerToMove(Alliance.WHITE);

		b.setPiece(new King(12, Alliance.BLACK));

		b.setPiece(new King(28, Alliance.WHITE));
		b.setPiece(new Pawn(20, Alliance.WHITE));

		return b.build();
	}

	// Boards used for testing below

	public static Board StaleMateBoard() {

		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.BLACK);

		b.setPiece(new King(15, Alliance.BLACK));

		b.setPiece(new King(13, Alliance.WHITE));
		b.setPiece(new Queen(21, Alliance.WHITE));

		return b.build();
	}

	public static Board CheckmateBoard() {

		final Builder b = new Builder();

		b.setNextPlayerToMove(Alliance.BLACK);

		b.setPiece(new King(0, Alliance.BLACK));

		b.setPiece(new King(16, Alliance.WHITE));
		b.setPiece(new Queen(3, Alliance.WHITE));

		return b.build();
	}
}
