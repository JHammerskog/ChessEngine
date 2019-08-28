package board;

/***
 * Constant player types that will never change
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
		
	};
	public abstract Alliance getOpponent();
	public abstract String toString();
}

