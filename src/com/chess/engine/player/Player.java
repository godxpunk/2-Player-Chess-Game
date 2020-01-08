package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.King;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

public abstract class Player {
	
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	
	Player(final Board board, Collection<Move> legalMoves, final Collection<Move> opponentMoves){
		this.board=board;
		this.playerKing=establishKing();
		legalMoves.addAll(calculateKingCastles(legalMoves, opponentMoves));
		this.legalMoves=legalMoves;
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
	}
	
	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){
		final List<Move> attackMoves= new ArrayList<>();
		for(final Move move : moves) {
			if(piecePosition == move.getDestinationTileNumber()) {
				attackMoves.add(move);
			}
		}
		return attackMoves;
	}

	private King establishKing() {
		for(final Piece piece: getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return (King)piece;
			}
		}
		throw new RuntimeException("Should not reach here, Not a valid Board!");
	}
	
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	public boolean isInCheck() {
		return this.isInCheck;
	}
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}
	private boolean hasEscapeMoves() {
		for(final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean hasCastled() {
		return false;
	}
	public Collection<Move> getLegalMoves(){
		return this.legalMoves;
	}
	public King getPlayerKing() {
		return this.playerKing;
	}
	public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }
    public MoveTransition makeMove(final Move move) {
        if (!this.legalMoves.contains(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = move.execute();
        if(transitionedBoard.currentPlayer().getOpponent().isInCheck()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return transitionedBoard.currentPlayer().getOpponent().isInCheck() ?
                new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK) :
                new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
    }	
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);


}
