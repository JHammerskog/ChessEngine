package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ai.KRKSolver;
import board.Alliance;
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
 * TODO: 1. Add a FEN-reader feature
 * 
 * 2. Highlight legal moves?
 * 
 * 3. Add observer pattern to GUI so user doesn't have to click "make AI" move
 * themselves? Would work best with SwingWorker threads (Time)
 * 
 * 4. Some sort of instruction is sorely needed
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
		this.puzzleGUI.add(moveLogPanel(), BorderLayout.EAST);
		this.puzzleGUI.add(turnToMove(), BorderLayout.NORTH); // this doesn't update

		this.chessArea = new ChessBoardPanel();

		this.puzzleGUI.add(chessArea, BorderLayout.CENTER);

		this.puzzleGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.puzzleGUI.setVisible(true);

	}

	private void populateMenus(JMenuBar menuBar) {
		menuBar.add(createSettingsMenu());
		menuBar.add(selectPuzzleSolver());
		menuBar.add(setKRKPositions());
		menuBar.add(setKPKPositions());
	}

	private JMenu createSettingsMenu() {
		final JMenu settingsMenu = new JMenu("Settings");

		final JMenuItem inputFEN = new JMenuItem("Play custom position with FEN notation.");
		final JMenuItem exitItem = new JMenuItem("Exit the application");

		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("You have chosen to quit! Goodbye.");
				System.exit(0);
			}
		});

		settingsMenu.add(inputFEN);
		settingsMenu.add(exitItem);

		return settingsMenu;
	}

	private JMenu setKRKPositions() {
		final JMenu KRKMenu = new JMenu("Set KR-K Positions");

		final JMenuItem playPositionOne = new JMenuItem("Play position #1");
		final JMenuItem playPositionTwo = new JMenuItem("Play position #2");
		final JMenuItem playPositionThree = new JMenuItem("Play position #3");

		KRKMenu.add(playPositionOne);
		KRKMenu.add(playPositionTwo);
		KRKMenu.add(playPositionThree);

		playPositionOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KRKMateInTwo());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		playPositionTwo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KRKMateInFive());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		playPositionTwo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KRKMateInFive());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		playPositionThree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.TestBoard());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		return KRKMenu;
	}

	private JMenu setKPKPositions() {
		final JMenu KPKMenu = new JMenu("Set KP-K Positions");

		final JMenuItem mateInTwo = new JMenuItem("KPK needs to be made");
		final JMenuItem mateInFive = new JMenuItem("KPK needs to be made");

		KPKMenu.add(mateInTwo);
		KPKMenu.add(mateInFive);

		mateInTwo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KRKMateInTwo());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		mateInFive.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KRKMateInFive());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		return KPKMenu;
	}

	private JMenu selectPuzzleSolver() {
		final JMenu gamePreferences = new JMenu("Set puzzlesolver");
		final JMenuItem selectKRKPuzzle = new JMenuItem("Play the King-Rook vs King endgame");
		final JMenuItem selectKPKPuzzle = new JMenuItem("Play the King-Pawn vs King endgame");
		gamePreferences.add(selectKRKPuzzle);
		gamePreferences.add(selectKPKPuzzle);

		selectKRKPuzzle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// set the puzzleSolver as the KRK puzzleSolver
				System.out.println("You selected KRK");

			}
		});

		selectKPKPuzzle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// set the puzzleSolver as the King-Pawn King solver
				System.out.println("You selected KPK");

			}
		});

		return gamePreferences;
	}

	private JPanel moveLogPanel() {
		JPanel moveLogPanel = new JPanel();
		JLabel moveLogLabel = new JLabel("Click when White's turn");
		moveLogPanel.add(moveLogLabel);

		JButton makeKRKMove = new JButton("Make KRKMove");
		moveLogPanel.add(makeKRKMove);

		makeKRKMove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				KRKSolver k = new KRKSolver(getCurrentChessBoard());
				Move AIMove = k.generateRestrictingMove(getCurrentChessBoard());

				Board newBoard = AIMove.executeMoveAndBuildBoard();
				// If you get far enough to implement undo-move features, use the below line of
				// code
				// BoardTransition newBoardPosition = new
				// BoardTransition(getCurrentChessBoard(), newBoard, move);
				if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck() == true) {
					setCurrentChessBoard(newBoard);
					getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

				}

				if (getCurrentChessBoard().getCurrentPlayer().isCheckMate()) {
					popUpDialog("Checkmate! Please start a new game.");
				}

			}

		});

		return moveLogPanel;
	}

	private JLabel turnToMove() {
		// String turnString = "It is " + getCurrentChessBoard().getCurrentPlayer() +
		// "'s turn to move.";
		String turnString = "Add a whose turn it is to play on this label. (CURRENTLY ALWAYS WHITE TO MOVE FIRST)";
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
							if (!getCurrentChessBoard().getCurrentPlayer().getIsNotInCheck()
									&& !getCurrentChessBoard().getCurrentPlayer().isCheckMate()) {
								popUpDialog(getCurrentChessBoard().getCurrentPlayer() + " is in check!");
							}

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

					if (getCurrentChessBoard().getCurrentPlayer().isCheckMate()) {
						popUpDialog("Checkmate! Please start a new game.");
					} else if (getCurrentChessBoard().getCurrentPlayer().isStaleMate()) { // DOES NOT WORK
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
