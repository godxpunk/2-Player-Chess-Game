package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

public class Bishop extends Piece{
	
	private final static int[][] CANDIDATE_MOVE_COORDINATES= {{1,1},{1,-1},{-1,-1},{-1,1}};

	public Bishop(int piecePositionX, int piecePositionY, Alliance pieceAlliance) {
		super(piecePositionX, piecePositionY, pieceAlliance,PieceType.BISHOP,true);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final Collection<Move> legalMoves= new ArrayList<>();
		
		boolean isSomePieceInTheWay[] = new boolean[4];
		for(int i=1;i<8;i++) {
			for(int j=0;j<4;j++) {
				final int candidateDestinationCoordinateX = this.piecePositionX + CANDIDATE_MOVE_COORDINATES[j][0]*i ;
				final int candidateDestinationCoordinateY = this.piecePositionY + CANDIDATE_MOVE_COORDINATES[j][1]*i ;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinateX,candidateDestinationCoordinateY)
						&& !isSomePieceInTheWay[j]) {
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
						isSomePieceInTheWay[j]=true;
					}				
				}	
			}
		}
		
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.BISHOP.toString();
	}

	@Override
	public Bishop movePiece(Move move) {
		return new Bishop(move.getDestinationTileNumber()%8,
				move.getDestinationTileNumber()/8,
				move.getMovedPiece().getPieceAlliance());
	}

}
