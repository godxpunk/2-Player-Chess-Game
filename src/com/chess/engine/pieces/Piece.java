package com.chess.engine.pieces;
import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int piecePositionX;
	protected final int piecePositionY;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	protected final PieceType pieceType;
	private final int cachedHashCode;
	
	public Piece(final int piecePositionX, final int piecePositionY,
			final Alliance pieceAlliance,final PieceType pieceType,final boolean isFirstMove){
		this.pieceAlliance=pieceAlliance;
		this.piecePositionX=piecePositionX;
		this.piecePositionY=piecePositionY;
		this.pieceType=pieceType;
		//Work to do here
		this.isFirstMove=isFirstMove;
		this.cachedHashCode=computeHashCode();
	}
	private int computeHashCode() {
		int result= pieceType.hashCode();
		result = result*31 + pieceAlliance.hashCode();
		result = result*31 + piecePositionY*8+piecePositionX;
		result = result*31 + (isFirstMove()?1:0);
		return result;
	}
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public int getPiecePosition() {
		return piecePositionY*8 + piecePositionX;
	}
	
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}
	
	public boolean isFirstMove() {
		return isFirstMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public abstract Piece movePiece(Move move);
	
	@Override
	public boolean equals(final Object other) {
		if(this==other)
			return true;
		if(!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece= (Piece)other;
		return piecePositionX==otherPiece.getPiecePosition()%8 &&
				piecePositionY==otherPiece.getPiecePosition()/8 &&
				pieceType==otherPiece.getPieceType() &&
				pieceAlliance==otherPiece.getPieceAlliance() &&
				isFirstMove==otherPiece.isFirstMove();
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	
	public enum PieceType {
		PAWN("P"), BISHOP("B"), KNIGHT("N"), KING("K"), QUEEN("Q"), ROOK("R");
		private String pieceName;
		PieceType(final String pieceName){
			this.pieceName=pieceName;
		}	
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public boolean isKing() {
			return this==KING;
		}
		public boolean isRook() {
			return this==ROOK;
		}
	}
	
}
