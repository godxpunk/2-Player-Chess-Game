package com.chess.engine.player;
import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.*;

public class WhitePlayer extends Player{
	
	public WhitePlayer(final Board board,
			final Collection<Move> whiteStandardLegalMoves, 
			final Collection<Move> blackStandardLegalMoves) {
		super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}
	
	@Override
	protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
		final List<Move> kingCastles= new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			//whiteKingSide
			if(!this.board.getTile(5,0).isTileOccupied() && 
					!this.board.getTile(6,0).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(7, 0);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(5,opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						//TODO
						kingCastles.add(new Move.KingSideCastleMove(this.board,
								this.playerKing,
								6, 0,
								(Rook)rookTile.getPiece(),
								rookTile.getTileCoordinate(),
								5));
					}
				}
			}
			//QueenSide
			if(!this.board.getTile(1,0).isTileOccupied() && 
					!this.board.getTile(2, 0).isTileOccupied()&&
					!this.board.getTile(3,0).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(0, 0);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(2,opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						//TODO
						kingCastles.add(new Move.QueenSideCastleMove(this.board,
								this.playerKing,
								1, 0,
								(Rook)rookTile.getPiece(),
								rookTile.getTileCoordinate(),
								2));
					}
				}
			}
		}	
		
		return kingCastles;
	}

}
