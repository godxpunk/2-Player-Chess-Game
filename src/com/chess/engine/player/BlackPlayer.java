package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class BlackPlayer extends Player{
	
	public BlackPlayer(final Board board, 
			final Collection<Move> whiteStandardLegalMoves, 
			final Collection<Move> blackStandardLegalMoves) {
		super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
	}
	
	@Override
	public Collection<Piece> getActivePieces(){
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
		final List<Move> kingCastles= new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			//whiteKingSide
			if(!this.board.getTile(5,7).isTileOccupied() && 
					!this.board.getTile(6,7).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(7, 7);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(61,opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						//TODO
						kingCastles.add(new Move.KingSideCastleMove(this.board,
								this.playerKing,
								6, 7,
								(Rook)rookTile.getPiece(),
								rookTile.getTileCoordinate(),
								61));
					}
					
				}
			}
			//QueenSide
			if(!this.board.getTile(1,7).isTileOccupied() && 
					!this.board.getTile(2, 7).isTileOccupied()&&
					!this.board.getTile(3,7).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(0, 7);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(58,opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						//TODO
						kingCastles.add(new Move.QueenSideCastleMove(this.board,
								this.playerKing,
								2, 7,
								(Rook)rookTile.getPiece(),
								rookTile.getTileCoordinate(),
								59));
					}
				}
			}
		}	
		
		return kingCastles;
	}

}
