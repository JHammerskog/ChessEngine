import board.Board;

public class main {

	public static void main(String[] args) {

		Board b = Board.createStartingPosition();
		//Board b = Board.KRKMateInTwo();

		System.out.println(b);
		
	//	System.out.println(b.getCurrentPlayer().getLegalMovesInPosition().size());


	}

}