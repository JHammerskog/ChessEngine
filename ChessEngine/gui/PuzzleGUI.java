package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import board.BoardUtility;

/***
 * This class is a simple GUI which will allow a user to play against the puzzle
 * solvers.
 * 
 * TODO: 
 * - Add mouse listener to tiles 
 * - Write move logic into mouse listeners 
 * - Hardcode some positions into a JComboBox or something 
 * - Add a FEN-reader feature 
 * - Fix GUI proportions so it looks better
 ***/

public class PuzzleGUI extends JFrame {

	JFrame puzzleGUI;
	ChessBoardPanel chessArea;

	public PuzzleGUI() {

		this.puzzleGUI = new JFrame("Puzzle Solver");
		this.puzzleGUI.setSize(850, 700);

		this.puzzleGUI.add(sidePanel(), BorderLayout.EAST);

		this.chessArea = new ChessBoardPanel();
		this.puzzleGUI.add(chessArea, BorderLayout.CENTER);

		this.puzzleGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.puzzleGUI.setVisible(true);
	}

	private JPanel sidePanel() {
		JPanel sidePanel = new JPanel();
		JButton openFileButton = new JButton("Choose file");
		// Change to textfield where you can enter a FENString
		sidePanel.add(openFileButton);
		add(sidePanel, BorderLayout.EAST);
		return sidePanel;
	}

	public class ChessBoardPanel extends JPanel {

		final List<SingleTilePanel> chessBoard = new ArrayList<>(); // Use this to manipulate individual panels
		final Color backgroundColor = new Color(255, 255, 255);

		public ChessBoardPanel() {
			super(new GridLayout(BoardUtility.getNumberOfTilesPerColumn(), BoardUtility.getNumberOfTilesPerColumn()));
			for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
				SingleTilePanel singleTile = new SingleTilePanel(this, i);
				this.chessBoard.add(singleTile);
				this.add(singleTile);
			}

			Dimension test = new Dimension(300, 300); // Setting the size is not working
			this.setPreferredSize(test);

		}

	}

	public class SingleTilePanel extends JPanel {
		int tileCoordinate;

		public SingleTilePanel(ChessBoardPanel boardPanel, int tileCoordinate) {
			super(new GridBagLayout());
			this.tileCoordinate = tileCoordinate;
			this.setBackground(setTileColour());
			this.setSize(15, 15); // does nothing
			this.setBorder(new LineBorder(Color.LIGHT_GRAY));

		}

		public Color setTileColour() { 

			Color lightTileColour = new Color(255, 255, 204);
			Color darkTileColour = new Color(153, 102, 0);

			if (BoardUtility.calculateRow(tileCoordinate) % 2 == 0) {
				if (this.tileCoordinate % 2 == 0) {
					return lightTileColour;
				} else {
					return darkTileColour;
				}
			} else {
				if (this.tileCoordinate % 2 == 0) {
					return darkTileColour;
				} else {
					return lightTileColour;
				}
			}
		}

	}

}
