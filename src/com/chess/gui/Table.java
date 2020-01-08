package com.chess.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.SwingUtilities.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

public class Table {
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(300,300);
	private static final Dimension TILE_PANEL_DIMENSTION = new Dimension(10,10);
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	
	private static final String pieceIconPath="C://Users/BLANK/eclipse-workspace/JChess/gui/images/pieces/Type1/";
	private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
	public Table() {
		this.gameFrame=new JFrame("JChess");
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.gameFrame.setLayout(new BorderLayout());
		this.chessBoard= Board.createStandardBoard();
		this.boardDirection=BoardDirection.NORMAL;
		this.boardPanel= new BoardPanel();
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
		
	}
	private JMenuBar createTableMenuBar() {
		JMenuBar tableMenuBar= new JMenuBar();
		 tableMenuBar.add(createFileMenu());
		 tableMenuBar.add(createPreferencesMenu());
		 return tableMenuBar;
	}
	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open up that pgn file");
			}		
		});
		fileMenu.add(openPGN);
		return fileMenu;
	}
	private JMenu createPreferencesMenu() {
			final JMenu preferencesMenu = new JMenu("Preferences");
			final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
			flipBoardMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					boardDirection=boardDirection.opposite();
					boardPanel.drawBoard(chessBoard);
				}
			});
			preferencesMenu.add(flipBoardMenuItem);
			return preferencesMenu;
	}
	
	public enum BoardDirection {
		NORMAL{
			@Override
			List<TilePanel> getTileList(final List<TilePanel> boardTiles,final List<TilePanel> reversed){
				return boardTiles;
			}
			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED{

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}

			@Override
			List<TilePanel> getTileList(final List<TilePanel> boardTiles,final List<TilePanel> reversed) {
				// TODO Auto-generated method stub
				
				return reversed;
			}
			
		};
		
		abstract BoardDirection opposite();
		abstract List<TilePanel> getTileList(final List<TilePanel> boardTiles,final List<TilePanel> reversed);
	}
	
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		final List<TilePanel> reversed;
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles= new ArrayList<>();
			this.reversed= new ArrayList<>();
			int x=64;
			for(int j=0;j<8;j++) {
				for(int i=8;i>0;i--) {
					final TilePanel tilePanel = new TilePanel(this,x-i);
					this.boardTiles.add(tilePanel);
					add(tilePanel);
				}
				x-=8;
			}
			for(int i=63;i>=0;i--) {
				this.reversed.add(boardTiles.get(i));
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		public void drawBoard(final Board board) {
			removeAll();
			for(final TilePanel tilePanel: boardDirection.getTileList(boardTiles,reversed)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
			
		}
	}
	
	private class TilePanel extends JPanel {
		private final int tileID;
		TilePanel(final BoardPanel boardPanel, final int tileID){
			super(new GridBagLayout());
			this.tileID=tileID;
			setPreferredSize(TILE_PANEL_DIMENSTION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					if(isRightMouseButton(e)){
						sourceTile=null;
						destinationTile=null;
						humanMovedPiece=null;
					}
					else if(isLeftMouseButton(e)) {
						if(sourceTile==null){
							//First click of move
							sourceTile= chessBoard.getTile(tileID%8, tileID/8);
							humanMovedPiece = sourceTile.getPiece();
							if(humanMovedPiece==null) {
								sourceTile=null;
							}
						}
						else {
							//second click of move
							destinationTile= chessBoard.getTile(tileID%8, tileID/8);
							final Move move=Move.MoveFactory.createMove
									(chessBoard,sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								chessBoard=transition.getToBoard();
							}
							sourceTile=null;
							destinationTile=null;
							humanMovedPiece=null;
							
						}						
					}
					SwingUtilities.invokeLater(new Runnable() {		
						@Override
						public void run() {
							boardPanel.drawBoard(chessBoard);	
						}
					});
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			validate();
		}
		
		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegalMoves(chessBoard);
			validate();
			repaint();
			
		}

		private void assignTilePieceIcon(final Board board) {
				this.removeAll();
				if(board.getTile(this.tileID%8,this.tileID/8).isTileOccupied()) {
					
					try {
					final BufferedImage image = 
							ImageIO.read(new File(pieceIconPath + board.getTile(this.tileID%8,this.tileID/8)
							.getPiece().getPieceAlliance().toString().substring(0, 1) + 
							board.getTile(this.tileID%8,this.tileID/8).getPiece().toString() +".PNG"));
					add(new JLabel(new ImageIcon(image)));
					}
					catch (IOException e) {	
						e.printStackTrace();
					}
				}
		}
		private void assignTileColor() {
			if(this.tileID%16>7) {
				setBackground(this.tileID%2==0? lightTileColor: darkTileColor);
			}
			else {
				setBackground(this.tileID%2==1? lightTileColor: darkTileColor);
			}
		}
		
		private void highlightLegalMoves(final Board board) {
			if(true) {
				for(final Move move: pieceLegalMoves(board)) {
					if(move.getDestinationTileNumber()==this.tileID) {
						try {
							add(new JLabel(new ImageIcon
									(ImageIO.read(new File
											("C://Users/BLANK/eclipse-workspace/"
													+ "JChess/gui/images/highlights/green_dot.PNG")))));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		private Collection<Move> pieceLegalMoves(final Board board) {
			if(humanMovedPiece!=null && humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}		
	}
}
