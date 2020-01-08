package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;

public class Pawn extends Piece{
	
	private final static int[][] CANDIDATE_MOVE_COORDINATES = {{0,1},{0,2},{1,1},{-1,1}};

	public Pawn(int piecePositionX, int piecePositionY, Alliance pieceAlliance) {
		super(piecePositionX, piecePositionY, pieceAlliance,PieceType.PAWN,true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final Collection<Move> legalMoves= new ArrayList<>();
		
		for(int i=0;i<4;i++) {
			final int candidateDestinationCoordinateX = this.piecePositionX + CANDIDATE_MOVE_COORDINATES[i][0] ;
			final int candidateDestinationCoordinateY = this.piecePositionY + CANDIDATE_MOVE_COORDINATES[i][1]*this.pieceAlliance.getDirection();
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinateX, candidateDestinationCoordinateY)) {
				continue;
			}
			if(i==0 && !board.getTile(candidateDestinationCoordinateX, candidateDestinationCoordinateY).isTileOccupied()) {
				legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinateX,candidateDestinationCoordinateY));
			}
			else if(i==1 && this.isFirstMove() 
					&& !board.getTile(candidateDestinationCoordinateX, candidateDestinationCoordinateY).isTileOccupied()) {
				if(this.piecePositionY==1 && !board.getTile(piecePositionX, 2).isTileOccupied() 
						&& !this.getPieceAlliance().isBlack()) {
					legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinateX,candidateDestinationCoordinateY));
				}
				else if(this.piecePositionY==6 && !board.getTile(piecePositionX, 5).isTileOccupied()
						&& this.getPieceAlliance().isBlack()) {
					legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinateX,candidateDestinationCoordinateY));
				}
			}
			else if(i>1) {
				if(board.getTile(candidateDestinationCoordinateX,candidateDestinationCoordinateY).isTileOccupied()) {
					Piece pieceAtDestination = board.getTile(candidateDestinationCoordinateX,candidateDestinationCoordinateY).getPiece();
					if(pieceAtDestination.getPieceAlliance()!=this.getPieceAlliance()) {
						legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinateY*8+candidateDestinationCoordinateX,pieceAtDestination));
					}
				}
			}
		}
		
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationTileNumber()%8,
				move.getDestinationTileNumber()/8,
				move.getMovedPiece().getPieceAlliance());
	}

}
