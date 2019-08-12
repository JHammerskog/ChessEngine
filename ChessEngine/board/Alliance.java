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

	},
	BLACK {

		@Override
		public Alliance getOpponent() {
			return WHITE;
		}
		
	};
	public abstract Alliance getOpponent();
}

