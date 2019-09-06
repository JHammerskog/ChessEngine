package board;

/***
 * This enumeration is used to represent unchanging player logic which form the functional
 * requirements and implementation of player in games of chess.
 ***/

//consider moving this to different package

public enum Alliance {

	WHITE {

		@Override
		public Alliance getOpponent() {
			return BLACK;
		}

		@Override
		public String toString() {
			return "W";
		}

		@Override
		public boolean isPromotionRow(int tileCoordinate) {
			return BoardUtility.calculateRow(tileCoordinate) == 0;
		}

	},
	BLACK {

		@Override
		public Alliance getOpponent() {
			return WHITE;
		}

		@Override
		public String toString() {
			return "B";
		}

		@Override
		public boolean isPromotionRow(int tileCoordinate) {
			return BoardUtility.calculateRow(tileCoordinate) == 7;
		}

	};
	public abstract Alliance getOpponent();

	public abstract boolean isPromotionRow(int tileCoordinate);

	public abstract String toString();
}
