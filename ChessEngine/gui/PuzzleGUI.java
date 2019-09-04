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
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ai.KPKSolver;
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
	// private JPanel turnLabelPanel;
	private JLabel turnLabel;

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

		this.currentChessBoard = Board.KPKBoardTwo();
		this.puzzleGUI.add(heuristicPanel(), BorderLayout.SOUTH);

		this.turnLabel = turnToMoveLabel();

		this.puzzleGUI.add(this.turnLabel, BorderLayout.NORTH); // this doesn't update

		this.chessArea = new ChessBoardPanel();

		this.puzzleGUI.add(chessArea, BorderLayout.CENTER);

		this.puzzleGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.puzzleGUI.setVisible(true);

		displayPurposeOfApplication();
		displayMoveInstructions(); // Displays instruction at the launch of the application

	}

	private void populateMenus(JMenuBar menuBar) {
		menuBar.add(createSettingsMenu());
		menuBar.add(setKRKPositions());
		menuBar.add(setKPKPositions());
		menuBar.add(createHelpMenu());
	}

	private JMenu createSettingsMenu() {
		final JMenu settingsMenu = new JMenu("Settings");

		final JMenuItem startingPosition = new JMenuItem("Set the board to a chess starting position");
		final JMenuItem clearBoard = new JMenuItem("Clear board.");
		final JMenuItem exitItem = new JMenuItem("Exit the application");

		startingPosition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCurrentChessBoard(Board.createStartingPosition());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());
			}
		});

		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("You have chosen to quit! Goodbye.");
				System.exit(0);
			}
		});

		clearBoard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCurrentChessBoard(Board.clearBoard());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());
			}
		});

		settingsMenu.add(startingPosition);
		settingsMenu.add(clearBoard);
		settingsMenu.add(exitItem);

		return settingsMenu;
	}

	private JMenu createHelpMenu() {
		final JMenu helpMenu = new JMenu("Help");

		final JMenuItem moveInstructionButton = new JMenuItem("Controls/Move information");
		final JMenuItem purposeOfApplicationButton = new JMenuItem("Purpose of Application.");

		moveInstructionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayMoveInstructions();
			}

		});

		purposeOfApplicationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCurrentChessBoard(Board.clearBoard());
				displayPurposeOfApplication();
			}
		});

		helpMenu.add(moveInstructionButton);
		helpMenu.add(purposeOfApplicationButton);
		return helpMenu;
	}

	private void displayPurposeOfApplication() {
		popUpDialog("Hi and welcome to the Endgame Puzzle solver!\n" + "\nPurpose of the application: \n"
				+ "\nThe purpose of this application is to showcase some heuristics that are able to solve endgame puzzles in chess."
				+ "\nThe search depth and branching factor of endgame positions in chess are astronomical, "
				+ "\nso developing a computer move which ends in a desirable outcome (winning) is quite difficult!"
				+ "\nThis application tries to replace brute-force methods (searching as many moves and positions as possible) "
				+ "\nwith some neat heuristics which generate moves very quickly on low-level hardware."
				+ "\n\nThe role of you, the user: \n"
				+ "\nIn the current iteration, the application can tackle the King-Rook vs King(KRK) and the King-Pawn vs King (KPK) endgames."
				+ "\nYou as the user can use this user interface to test the developed heuristics.");

		// ADD HOW TO SETUP A BOARD WHEN DONE
	}

	private void displayMoveInstructions() {
		popUpDialog("How to make a manual move:\n"
				+ "Simply click on a tile with a piece on it, and then click on the tile you want that piece to go."
				+ "\nIf the move you are attempting to make is legal, the tile you clicked will be highlighted in a green colour."
				+ "\nIf nothing happens - its probably the other players turn!"
				+ "\nIf you are wondering which piece to move, there is an indicator just above the board and underneath the menubar."
				+ "\n\nHow to make a 'heuristic move':\n"
				+ "\nAt the bottom of this frame, there are some buttons which enable you to generate a heuristic move."
				+ "\nAs the user, you are able to move as both white and black if you wish. "
				+ "\nHowever, if you would like to test one of the developed heuristics simply click on the button that corresponds to the endgame puzzle you would like to solve."
				+ "\n\nMenus: \n"
				+ "\nIf at any point you want to change the game settings, or you would like to view this message again,"
				+ "\nsimply navigate to the menu bar at the top.");

		// When development of KPK and KRK is finished, and the GUI has been fully
		// optimized - remake this help message

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

		final JMenuItem KPKPositionOne = new JMenuItem("KPK position #1");
		final JMenuItem KPKPositionTwo = new JMenuItem("KPK position #2");
		final JMenuItem KPKPositionThree = new JMenuItem("KPK position #3");

		KPKMenu.add(KPKPositionOne);
		KPKMenu.add(KPKPositionTwo);
		KPKMenu.add(KPKPositionThree);

		KPKPositionOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.queenBoard());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		KPKPositionTwo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KPKBoardTwo());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		KPKPositionThree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setCurrentChessBoard(Board.KPKBoardThree());
				getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());

			}

		});

		return KPKMenu;
	}

	private JPanel heuristicPanel() { // change this to piece panel
		JPanel heuristicPanel = new JPanel();
		JLabel moveLogLabel = new JLabel("Click when White's turn");
		heuristicPanel.add(moveLogLabel);

		JButton makeKRKMove = new JButton("Make KRKMove");
		JButton makeKPKMove = new JButton("Make KPKMove");
		heuristicPanel.add(makeKRKMove);
		heuristicPanel.add(makeKPKMove);

		makeKRKMove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				KRKSolver k = new KRKSolver(getCurrentChessBoard());
				Move AIMove = k.generateRestrictingMove(getCurrentChessBoard());

				Board newBoard = AIMove.executeMoveAndBuildBoard();
				if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck() == true) {
					setCurrentChessBoard(newBoard);
					getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());
				}
				if (getCurrentChessBoard().getCurrentPlayer().isCheckMate()) {
					popUpDialog("Checkmate! Please start a new game.");
				}
				updateTurnLabel(turnLabel, newBoard);

			}

		});

		makeKPKMove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				KPKSolver k = new KPKSolver(getCurrentChessBoard());
				Move AIMove = k.generateKPKMove(getCurrentChessBoard());
				
				if(AIMove == null) {
					popUpDialog("The heuristic has determined that this position will lead to a stalemate! Please choose a new position.");
				}

				Board newBoard = AIMove.executeMoveAndBuildBoard();
				if (newBoard.getOpponent(newBoard.getCurrentPlayer().getAlliance()).getIsNotInCheck() == true) {
					setCurrentChessBoard(newBoard);
					getChessArea().reDrawBoardAfterMove(getCurrentChessBoard());
				}
				if (getCurrentChessBoard().getCurrentPlayer().isCheckMate()) {
					popUpDialog("Checkmate! Please start a new game.");
				}

				updateTurnLabel(turnLabel, newBoard);

			}

		});

		return heuristicPanel;
	}

	private JLabel turnToMoveLabel() {
		String turnString = "It is " + getCurrentChessBoard().getCurrentPlayer().toString() + "'s turn to move.";
		JLabel turnLabel = new JLabel(turnString);
		return turnLabel;
	}

	public void updateTurnLabel(JLabel turnLabel, Board board) {
		this.turnLabel.setText("It is " + board.getCurrentPlayer().toString() + "'s turn to move.");
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
									// updateTurnLabel(turnLabelPanel, newBoard);
									updateTurnLabel(turnLabel, newBoard);
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
					} else if (getCurrentChessBoard().onlyKingsLeft()) {
						popUpDialog("Only kings are left, no possible way to win! Please start a new game.");
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

		public void setPieceImageOnTile(Board board) { // This method will replace setPieceOnTile()
			// removeAll needed here?

			if (board.getTile(getTileCoordinate()).tileIsOccupied()) {
				Alliance pieceAlliance = board.getTile(getTileCoordinate()).getPiece().getPieceAlliance();

				String pathToImageDirectory = ""; // Set this when you add piece images
				pathToImageDirectory += addAllianceToDirectoryPath(pieceAlliance);
				BufferedImage pieceImage = null;

				try {
					pieceImage = ImageIO.read(new File(
							pathToImageDirectory + board.getTile(getTileCoordinate()).getPiece().toString() + ".png"));
					// update this when you add image folder
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					add(new JLabel(new ImageIcon(pieceImage)));
				}
			}

		}

		public String addAllianceToDirectoryPath(Alliance alliance) {
			String pathString = "/";
			if (alliance == Alliance.WHITE) {
				pathString += "WhitePieces/";
			} else if (alliance == Alliance.BLACK) {
				pathString += "BlackPieces/";
			}
			return pathString;
		}

		public Color setTileColour() {

			// The implementation of this method directly relates to the features of a
			// chessboard.

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
