package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import board.Board;
import board.BoardUtility;
import board.Move;
import board.Move.MoveMaker;
import board.Tile;
import pieces.Piece;

/***
 * This class is a simple GUI which will allow a user to play against the puzzle
 * solvers.
 * 
 * TODO: - 1.Hardcode some positions into a JFileMenu 2. Add a FEN-reader
 * feature 3. Fix GUI proportions so it looks better 4. Maybe refactor classes
 * into less messy structure? 5. Highlight legal moves? 
 * 
 ***/

public class PuzzleGUI extends JFrame {

	private JFrame puzzleGUI;
	private ChessBoardPanel chessArea;

	private Board currentChessBoard;

	private Piece movedPiece;
	private Tile originTile;
	private Tile destinationTile;

	public PuzzleGUI() {

		this.puzzleGUI = new JFrame("Puzzle Solver");
		this.puzzleGUI.setSize(850, 700);

		final JMenuBar menuBar = new JMenuBar();
		populateMenus(menuBar);
		this.puzzleGUI.setJMenuBar(menuBar);
		

		this.currentChessBoard = Board.KRKMateInTwo();
		this.puzzleGUI.add(sidePanel(), BorderLayout.EAST);
		this.puzzleGUI.add(turnToMove(), BorderLayout.NORTH); // this doesn't update

		this.chessArea = new ChessBoardPanel();

		this.puzzleGUI.add(chessArea, BorderLayout.CENTER);

		this.puzzleGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.puzzleGUI.setVisible(true);

	}

	private void populateMenus(JMenuBar menuBar) {
		menuBar.add(createSettingsMenu());
		menuBar.add(selectSideToPlay());
	}

	private JMenu createSettingsMenu() {
		final JMenu settingsMenu = new JMenu("Settings");
		
		final JMenuItem clearBoard = new JMenuItem("Clear the current Board");
		final JMenuItem inputFEN = new JMenuItem("Play custom position with FEN notation.");
		final JMenuItem selectKRKPuzzle = new JMenuItem("Play the King-Rook vs King endgame");
		final JMenuItem selectKPKPuzzle = new JMenuItem("Play hte King-Pawn vs King endgame");
		final JMenuItem exitItem = new JMenuItem("Exit the application");

		settingsMenu.add(clearBoard);
		settingsMenu.add(inputFEN);
		settingsMenu.add(selectKRKPuzzle);
		settingsMenu.add(selectKPKPuzzle);
		settingsMenu.add(exitItem);
		
		// add action listeners here
		
		return settingsMenu;
	}

	private JMenu selectSideToPlay() {
		final JMenu allianceMenu = new JMenu("Select Alliance");
		final JMenuItem selectWhite = new JMenuItem("Play as White");
		final JMenuItem selectBlack = new JMenuItem("Play as Black");
		
		allianceMenu.add(selectWhite);
		allianceMenu.add(selectBlack);
		
		// add actionlisteners here
		
		return allianceMenu;
	}

	private JPanel sidePanel() {
		// Use this sidepanel as a move log instead?
		JPanel sidePanel = new JPanel();
		JButton clearBoardButton = new JButton("Clear Board");
		// Change to textfield where you can enter a FENString
		sidePanel.add(clearBoardButton);

		return sidePanel;
	}

	private JLabel turnToMove() {
		String turnString = "It is " + getCurrentChessBoard().getCurrentPlayer().getAlliance() + "'s turn to move.";
		JLabel turnLabel = new JLabel(turnString);
		return turnLabel;
	}

	private void popUpDialog(String popUp) {
		// simple but effective way of stopping player making moves in illegal positions
		JOptionPane.showMessageDialog(puzzleGUI, popUp);

	}

	public class ChessBoardPanel extends JPanel {
		// draw background board

		final List<SingleTilePanel> chessBoardPanel = new ArrayList<>(); // Use this to manipulate individual panels

		final Color backgroundColor = new Color(255, 255, 255);

		public ChessBoardPanel() {
			super(new GridLayout(BoardUtility.getNumberOfTilesPerColumn(), BoardUtility.getNumberOfTilesPerColumn()));
			for (int i = 0; i < BoardUtility.getNumberOfTiles(); i++) {
				SingleTilePanel singleTile = new SingleTilePanel(this, i);
				this.chessBoardPanel.add(singleTile);
				this.add(singleTile);
			}

			Dimension test = new Dimension(300, 300); // Setting the size is not working
			this.setPreferredSize(test);

		}

		public void reDrawBoardAfterMove(Board board) {
			// Only needs to be passed a board for reDrawTileAfterMove()
			for (SingleTilePanel singleTile : getChessBoardPanel()) {
				singleTile.reDrawTileAfterMove(board);
				this.add(singleTile);

			}

			validate();
			repaint();

		}

		public List<SingleTilePanel> getChessBoardPanel() {
			return chessBoardPanel;
		} // Confusing names???

	}

	public class SingleTilePanel extends JPanel {
		// Contains almost all piece moving logic
		private final int tileCoordinate;

		public SingleTilePanel(ChessBoardPanel boardPanel, int tileCoordinate) {
			super(new GridBagLayout());
			this.tileCoordinate = tileCoordinate;
			this.setBackground(setTileColour());
			this.setSize(15, 15); // does nothing
			this.setBorder(new LineBorder(Color.LIGHT_GRAY));

			setPieceOnTile(getCurrentChessBoard());
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent event) {

					// This getButton() check could break the GUI for custom mice with a differetn
					// button order than just #1 = leftclick & #3 = rightclick
					if (event.getButton() == 1) {
						// highlight clicked square
						// This should automattically be removed when board is redrawn after a move
						if (getOriginTile() == null) {
							// first click
							setBorder(new LineBorder(Color.GREEN));
							setOriginTile(getCurrentChessBoard().getTile(getTileCoordinate()));
							setMovedPiece(getCurrentChessBoard().getTile(getTileCoordinate()).getPiece());
							if (getMovedPiece() == null || !(getMovedPiece()
									.getPieceAlliance() == getCurrentChessBoard().getCurrentPlayer().getAlliance())) {
								// This only happens if there is no piece on the clicked tile
								returnTileBorder(
										getChessArea().getChessBoardPanel().get(getOriginTile().getTileCoordinate()));
								setOriginTile(null);

							}
						} else {
							// second click
							setDestinationTile(getCurrentChessBoard().getTile(getTileCoordinate()));
							final Move move = MoveMaker.getMove(getCurrentChessBoard(),
									getOriginTile().getTileCoordinate(), getDestinationTile().getTileCoordinate());

							if (getCurrentChessBoard().getCurrentPlayer().getLegalMovesInPosition().contains(move)) {

								Board newBoard = move.executeMoveAndBuildBoard();
								// If you get far enough to implement undo-move features, use the below line of
								// code
								// BoardTransition newBoardPosition = new
								// BoardTransition(getCurrentChessBoard(), newBoard, move);
								if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance())
										.getIsNotInCheck() == true) {
									setCurrentChessBoard(newBoard);
									getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

								}

							}
							// happens after every mouse event
							returnTileBorder(
									getChessArea().getChessBoardPanel().get(getOriginTile().getTileCoordinate()));
							setMovedPiece(null);
							setOriginTile(null);
							setDestinationTile(null);

						}


					} else if (event.getButton() == 3) {
						// Right clicking on the board means that you remove all previous tile
						// selections. This is a quality of life feature which is far from functional,
						// but improves quality of life with insane amounts (from personal experience)
						returnTileBorder(getChessArea().getChessBoardPanel().get(getOriginTile().getTileCoordinate()));
						setMovedPiece(null);
						setOriginTile(null);
						setDestinationTile(null);

					}

					if (getCurrentChessBoard().getCurrentPlayer().isCheckMate() == true) {
						popUpDialog("Checkmate! Please start a new game.");
					} else if (getCurrentChessBoard().getCurrentPlayer().isStaleMate() == true) {
						popUpDialog("Stalemate! Please start a new game.");
					}

				}

				@Override
				public void mouseEntered(MouseEvent event) {
					// unused

				}

				@Override
				public void mouseExited(MouseEvent event) {
					// unused

				}

				@Override
				public void mousePressed(MouseEvent event) {
					// unused

				}

				@Override
				public void mouseReleased(MouseEvent event) {
					/// unused

				}

			});

		}

		// Helper methods for tiles

		public void returnTileBorder(SingleTilePanel highlightedTilePanel) {
			highlightedTilePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		}

		public void reDrawTileAfterMove(Board board) {
			removeAll();
			this.setBackground(setTileColour());
			setPieceOnTile(board);
			validate();
			repaint();
		}

		public int getTileCoordinate() {
			return tileCoordinate;
		}

		public void setPieceOnTile(Board board) {
			// exception handling here?
			if (board.getTile(getTileCoordinate()).tileIsOccupied()) {
				String pieceOnTile = board.getTile(getTileCoordinate()).toString();
				add(new JLabel(pieceOnTile));
			}

			// Find nicer way to represent pieces than FEN notation

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

	public Board getCurrentChessBoard() {
		return currentChessBoard;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public Tile getOriginTile() {
		return originTile;
	}

	public Tile getDestinationTile() {
		return destinationTile;
	}

	public ChessBoardPanel getChessArea() {
		return chessArea;
	}

	public void setCurrentChessBoard(Board newBoard) {
		this.currentChessBoard = newBoard;
	}

	public void setMovedPiece(Piece movedPiece) {
		this.movedPiece = movedPiece;
	}

	public void setOriginTile(Tile originTile) {
		this.originTile = originTile;
	}

	public void setDestinationTile(Tile destinationTile) {
		this.destinationTile = destinationTile;
	}

}
