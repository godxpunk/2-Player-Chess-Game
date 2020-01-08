package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;

public class King extends Piece{
	
	private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;
	
	private final static int[][] CANDIDATE_MOVE_COORDINATES= {{1,1},{1,-1},{-1,-1},{-1,1},{1,0},{0,1},{-1,0},{0,-1}};

	public King(final int piecePositionX,final int piecePositionY,final Alliance pieceAlliance,final boolean kingSideCastleCapable,
            final boolean queenSideCastleCapable) {
		super(piecePositionX, piecePositionY, pieceAlliance,PieceType.KING,true);
		// TODO Auto-generated constructor stub
		this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
	}
	public King(final Alliance alliance,
			final int piecePositionX,final int piecePositionY,
            final boolean isFirstMove,
            final boolean isCastled,
            final boolean kingSideCastleCapable,
            final boolean queenSideCastleCapable) {
		super(piecePositionX, piecePositionY,alliance,PieceType.KING,true);
		this.isCastled = isCastled;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}
	
	public boolean isCastled() {
        return this.isCastled;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }
	

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves= new ArrayList<>();
		
		for(int i=0;i<8;i++) {
			final int candidateDestinationCoordinateX = this.piecePositionX + CANDIDATE_MOVE_COORDINATES[i][0] ;
			final int candidateDestinationCoordinateY = this.piecePositionY + CANDIDATE_MOVE_COORDINATES[i][1] ;
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinateX, candidateDestinationCoordinateY)) {
				continue;
			}
			if(!board.getTile(candidateDestinationCoordinateX, candidateDestinationCoordinateY).isTileOccupied()) {
				legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinateX,candidateDestinationCoordinateY));
			}
			else {
				Piece pieceAtDestination = board.getTile(candidateDestinationCoordinateX,candidateDestinationCoordinateY).getPiece();
				if(pieceAtDestination.getPieceAlliance()!=this.getPieceAlliance()) {
					legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinateY*8+candidateDestinationCoordinateX,pieceAtDestination));
				}
			}
		}
		
		
		return legalMoves;
	}
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}

	@Override
	public King movePiece(Move move) {
		return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationTileNumber()%8,
				move.getDestinationTileNumber()/8,
				false, move.isCastlingMove(), false, false);
	}

}
