package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import board.Alliance;
import pieces.Piece.PieceType;

/**
 * This class is responsible for representing the board setup panel on the right
 * side of the frame. ActionListeners are used on each grid to inform the
 * puzzleGUI class of which piece to put on which tile.
 * 
 ***/

public class PiecePanel extends JPanel {

	PuzzleGUI puzzleGUI;

	public PiecePanel(PuzzleGUI puzzleGUI) {
		super(new GridLayout(7, 2));
		this.puzzleGUI = puzzleGUI;
		populatePiecePanel();
		// this.add(clearTileButton(), BorderLayout.SOUTH);
	}

	private void populatePiecePanel() {
		JButton whiteKing = new JButton("\u2654");
		JButton blackKing = new JButton("\u265A");
		JButton whiteRook = new JButton("\u2656");
		JButton blackRook = new JButton("\u265C");
		JButton whiteQueen = new JButton("\u2655");
		JButton blackQueen = new JButton("\u265B");
		JButton whiteBishop = new JButton("\u2657");
		JButton blackBishop = new JButton("\u265D");
		JButton whiteKnight = new JButton("\u2658");
		JButton blackKnight = new JButton("\u265E");
		JButton whitePawn = new JButton("\u2659");
		JButton blackPawn = new JButton("\u265F");
		JButton clearTile = new JButton("Clear Tile");
		JButton stopPlacing = new JButton("Stop placing pieces");

		whiteKing.setFont(getFont().deriveFont(48f));
		blackKing.setFont(getFont().deriveFont(48f));
		whiteRook.setFont(getFont().deriveFont(48f));
		blackRook.setFont(getFont().deriveFont(48f));
		whiteQueen.setFont(getFont().deriveFont(48f));
		blackQueen.setFont(getFont().deriveFont(48f));
		whiteBishop.setFont(getFont().deriveFont(48f));
		blackBishop.setFont(getFont().deriveFont(48f));
		whiteKnight.setFont(getFont().deriveFont(48f));
		blackKnight.setFont(getFont().deriveFont(48f));
		whitePawn.setFont(getFont().deriveFont(48f));
		blackPawn.setFont(getFont().deriveFont(48f));

		this.add(whiteKing);
		this.add(blackKing);
		this.add(whiteRook);
		this.add(blackRook);
		this.add(whiteQueen);
		this.add(blackQueen);
		this.add(whiteBishop);
		this.add(blackBishop);
		this.add(whiteKnight);
		this.add(blackKnight);
		this.add(whitePawn);
		this.add(blackPawn);
		this.add(clearTile);
		this.add(stopPlacing);

		whiteKing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.KING);
			}

		});

		blackKing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.KING);
			}

		});

		whiteQueen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.QUEEN);
			}

		});

		blackQueen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.QUEEN);
			}

		});

		whiteRook.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.ROOK);
			}

		});

		blackRook.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.ROOK);
			}

		});

		whiteBishop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.BISHOP);
			}

		});

		blackBishop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.BISHOP);
			}

		});

		whiteKnight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.KNIGHT);
			}

		});

		blackKnight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.KNIGHT);
			}

		});

		whitePawn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.WHITE);
				puzzleGUI.setHumanSetPieceType(PieceType.PAWN);
			}

		});

		blackPawn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.PAWN);
			}

		});

		blackPawn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(Alliance.BLACK);
				puzzleGUI.setHumanSetPieceType(PieceType.PAWN);
			}

		});

		clearTile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(null);
				puzzleGUI.setHumanSetPieceType(null);
				puzzleGUI.setPlayerWishesToClearTile(true);
			}

		});
		stopPlacing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				puzzleGUI.setHumanSetPieceAlliance(null);
				puzzleGUI.setHumanSetPieceType(null);
			}

		});
	}

}
