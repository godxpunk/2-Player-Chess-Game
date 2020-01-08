package com.chess.engine.board;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;

public abstract class Tile {
	
	private Tile(int tileNumber) {
		this.tileNumberX=tileNumber%8;
		this.tileNumberY=tileNumber/8;
	}
	
	protected final int tileNumberX;
	protected final int tileNumberY;
	public int getTileCoordinate() {
		return this.tileNumberY*8+this.tileNumberX;
	}
	private static Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		for(int i=0;i<64;i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return Collections.unmodifiableMap(emptyTileMap);
	}
	
	public abstract boolean isTileOccupied();
	public abstract Piece getPiece();
	
	public static Tile createTile(int tileCoordinate, Piece piece) {
		return piece==null ? EMPTY_TILES_CACHE.get(tileCoordinate) : new OccupiedTile(tileCoordinate,piece);
	}
	
	public static final class EmptyTile extends Tile {
		private EmptyTile(int coordinate) {
			super(coordinate);
		}
		
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		@Override
		public String toString() {
			return "-";
		}
		
		@Override
		public Piece getPiece() {
			return null;
		}
	}
	
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOnTile;
		
		private OccupiedTile(int coordinate, Piece passedPiece) {
			super(coordinate);
			pieceOnTile=passedPiece;
		}
		
		@Override
		public boolean isTileOccupied() {
			return true;
		}	
		@Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase()
					: getPiece().toString();
		}
		@Override
		public Piece getPiece() {
			return pieceOnTile;
		}
	}
	
}
