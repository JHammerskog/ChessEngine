package ai;

import java.util.LinkedList;

public class MiniMax { // redundant code, refactor later

	State state; // will be replaced with "Board" class when developed

	private MiniMax() {
		// construct tree

	}

	public int minimax(int depth, int values[], boolean maximizingPlayer, int node) {
		int selectedMove;

		if (maximizingPlayer) {
			selectedMove = maximize(this.state, depth);

		} else {
			selectedMove = minimize(this.state, depth);

		}
		return selectedMove;
	}

	private int maximize(final State state, int depth) {
		int bestMove = Integer.MIN_VALUE; // Best move initially is set to the lowest possible number

		// for(LEGAL_MOVES): Make all moves and evaluate new board
		// if one move is yields a higher value than current bestMove, set currentMove
		// to BestMove

		// Implement opposite of above in minimize()

		return bestMove;
	}

	private int minimize(final State state, int depth) {
		int bestMove = Integer.MAX_VALUE; // Best move initially is set to the highest possible number

		return bestMove;
	}

	public class Node<E> {

		int depth;
		int score;
		boolean player;
		LinkedList<Node> children;
		E element;

		public Node(Comparable elem) {

		}

		public int getScore() {
			return score;
		}

		public LinkedList<Node> getChildren() {
			return children;
		}

	}

	public class Tree {
		private Node root;

		public Node insert(Node top, Comparable elem) {
			if (top == null) {
				Node newNode = new Node(elem);
				top = newNode;
				if (root == null) {
					setRoot(newNode);
				}
				return top;
			} else { // refactor for minimax
				int comp = elem.compareTo(top.element);
				if (comp < 0) {
					// top.left = insert(top.left, elem);
				} else if (comp > 0) {
					// top.right = insert(top.right, elem);
				}
			}
			return top;
		}

		public Node getRoot() {
			return root;
		}

		public void setRoot(Node root) {
			this.root = root;
		}

	}
}
