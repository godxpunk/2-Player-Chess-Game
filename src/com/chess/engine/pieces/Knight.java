package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.board.BoardUtils;

public class Knight extends Piece {
	
	private final static int[][] CANDIDATE_MOVE_COORDINATES = {{-1,-2},{-1,2},{-2,1},{-2,-1},{1,2},{1,-2},{2,1},{2,-1}};

	public Knight(final int piecePositionX,final int piecePositionY, final Alliance pieceAlliance) {
		super(piecePositionX,piecePositionY, pieceAlliance,PieceType.KNIGHT,true);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves= new ArrayList<>();

		for(int i=0;i<8;i++) {
			final int candidateDestinationCoordinateX = this.piecePositionX + CANDIDATE_MOVE_COORDINATES[i][0] ;
			final int candidateDestinationCoordinateY = this.piecePositionY + CANDIDATE_MOVE_COORDINATES[i][1] ;
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinateX,candidateDestinationCoordinateY)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinateX,candidateDestinationCoordinateY);
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinateX,candidateDestinationCoordinateY));
				}
				else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();					
					if(this.pieceAlliance!=pieceAlliance) {
						legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinateY*8+candidateDestinationCoordinateX,pieceAtDestination));
					}
				}				
			}		
		}
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	@Override
	public Knight movePiece(Move move) {
		return new Knight(move.getDestinationTileNumber()%8,
				move.getDestinationTileNumber()/8,
				move.getMovedPiece().getPieceAlliance());
	}

	
}
