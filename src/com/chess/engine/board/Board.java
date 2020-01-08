package com.chess.engine.board;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.chess.engine.*;
import java.util.*;
public class Board {
	
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	private Board(Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.blackPieces= calculateActivePieces(this.gameBoard,Alliance.BLACK);
		this.whitePieces= calculateActivePieces(this.gameBoard,Alliance.WHITE);
		
		final Collection<Move> whiteStandardLegalMoves= calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves= calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer= new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer= new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
		
	}
	
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i=0;i<64;i++) {
			final String tileText=this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if(i%8==7) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		
		final List<Move> legalMoves = new ArrayList<>();
		for(final Piece piece: pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return legalMoves;
	}

	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance passedAlliance) {
		final List<Piece> activePieces= new ArrayList<>();
		
		for(final Tile tile: gameBoard) {
			if(tile.isTileOccupied()) {
				if(tile.getPiece().getPieceAlliance()==passedAlliance) {
					activePieces.add(tile.getPiece());
				}
			}
		}
		return activePieces;
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}

	public Tile getTile(final int tileCoordinateX, final int tileCoordinateY) {
		// To work
		return gameBoard.get(tileCoordinateX+tileCoordinateY*8);
	}
	
	private static List<Tile> createGameBoard(final Builder builder){
		final Tile tiles[]= new Tile[64];
		for(int i=0;i<64;i++) {
			tiles[i]=Tile.createTile(i,builder.boardConfig.get(i));
		}
		return Arrays.asList(tiles);
	}
	
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
		
		//White
		builder.setPiece(new Rook(0,0,Alliance.WHITE));
		builder.setPiece(new Rook(7,0,Alliance.WHITE));
		builder.setPiece(new Knight(1,0,Alliance.WHITE));
		builder.setPiece(new Knight(6,0,Alliance.WHITE));
		builder.setPiece(new Bishop(5,0,Alliance.WHITE));
		builder.setPiece(new Bishop(2,0,Alliance.WHITE));
		builder.setPiece(new King(3,0,Alliance.WHITE,true,true));
		builder.setPiece(new Queen(4,0,Alliance.WHITE));
		builder.setPiece(new Pawn(1,1,Alliance.WHITE));
		builder.setPiece(new Pawn(2,1,Alliance.WHITE));
		builder.setPiece(new Pawn(3,1,Alliance.WHITE));
		builder.setPiece(new Pawn(4,1,Alliance.WHITE));
		builder.setPiece(new Pawn(5,1,Alliance.WHITE));
		builder.setPiece(new Pawn(6,1,Alliance.WHITE));
		builder.setPiece(new Pawn(7,1,Alliance.WHITE));
		builder.setPiece(new Pawn(0,1,Alliance.WHITE));
		
		//BLACK
		builder.setPiece(new Pawn(0,6,Alliance.BLACK));
		builder.setPiece(new Pawn(1,6,Alliance.BLACK));
		builder.setPiece(new Pawn(2,6,Alliance.BLACK));
		builder.setPiece(new Pawn(3,6,Alliance.BLACK));
		builder.setPiece(new Pawn(4,6,Alliance.BLACK));
		builder.setPiece(new Pawn(5,6,Alliance.BLACK));
		builder.setPiece(new Pawn(6,6,Alliance.BLACK));
		builder.setPiece(new Pawn(7,6,Alliance.BLACK));
		builder.setPiece(new Rook(0,7,Alliance.BLACK));
		builder.setPiece(new Knight(1,7,Alliance.BLACK));
		builder.setPiece(new Bishop(2,7,Alliance.BLACK));
		builder.setPiece(new Queen(4,7,Alliance.BLACK));
		builder.setPiece(new King(3,7,Alliance.BLACK,true,true));
		builder.setPiece(new Bishop(5,7,Alliance.BLACK));
		builder.setPiece(new Knight(6,7,Alliance.BLACK));
		builder.setPiece(new Rook(7,7,Alliance.BLACK));
		
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}
	
	public static class Builder {
		
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		Move transitionMove;
		
		public Builder() {
			this.boardConfig=new HashMap<>();
		}
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}
		public Builder setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn=enPassantPawn;
			return this;
		}
		public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }
	}

	public Collection<Move> getAllLegalMove() {
		final List<Move> allLegalMoves= new ArrayList<>();
		allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
		allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
		return allLegalMoves;
	}
	
}
