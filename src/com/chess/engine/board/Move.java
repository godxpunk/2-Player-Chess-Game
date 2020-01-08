package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
	
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinateX;
	final int destinationCoordinateY;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinateX,final int destinationCoordinateY){
		this.board=board;
		this.movedPiece=movedPiece;
		this.destinationCoordinateX=destinationCoordinateX;
		this.destinationCoordinateY=destinationCoordinateY;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(final Board board, final int destinationCoordinate) {
		this.board=board;
		this.destinationCoordinateX=destinationCoordinate%8;
		this.destinationCoordinateY=destinationCoordinate/8;
		this.movedPiece = null;
        this.isFirstMove = false;
	}
	
	public Board execute() {
		final Builder builder= new Builder();
		for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		//move the piece
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		builder.setMoveTransition(this);
		return builder.build();
	}
	
	public Board getBoard() {
        return this.board;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinateX + this.destinationCoordinateY*8;
    }
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	public boolean isAttack() {
		return false;
	}
	public boolean isCastlingMove() {
		return false;
	}
	public Piece getAttackedPiece() {
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime=31;
		int result=1;
		result=prime*result+ this.destinationCoordinateY*8 + this.destinationCoordinateX;
		result=prime*result+ this.movedPiece.hashCode();
		result=prime*result+ this.movedPiece.getPiecePosition();
		return result;
	}
	@Override
	public boolean equals(final Object other) {
		if(this==other) {
			return true;
		}
		if(!(other instanceof Move)) {
			return false;
		}
		final Move otherMove= (Move)other;
		return getCurrentCoordinate()==otherMove.getCurrentCoordinate() && getDestinationTileNumber()==otherMove.getDestinationTileNumber() &&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	public static final class MajorMove extends Move{
		public MajorMove(Board board, Piece movedPiece, int destinationCoordinateX, int destinationCoordinateY) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this==other || other instanceof MajorMove && super.equals(other);
		}
	}
	
	public static abstract class AttackMove extends Move{
		private final Piece attackedPiece;		
		public AttackMove(Board board, Piece movedPiece, int destinationCoordinateX,
				int destinationCoordinateY,final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinateX ,destinationCoordinateY);
			this.attackedPiece=attackedPiece;
		}
		@Override
		public Board execute() {
			final Builder builder= new Builder();
			for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
				if(!this.attackedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			//move the piece
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}	
		
		@Override
		public boolean isAttack() {
			return true;
		}
		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}
		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		@Override
		public boolean equals(final Object other) {
			if(this==other) {
				return true;
			}
			if(!(other instanceof Move)) {
				return false;
			}
			final AttackMove otherAttackMove= (AttackMove)other;
			return super.equals(otherAttackMove) && getAttackedPiece()==otherAttackMove.getAttackedPiece();
		}
	}
	
	 public static class MajorAttackMove
     extends AttackMove {

		 public MajorAttackMove(final Board board,
                        final Piece pieceMoved,
                        final int destinationCoordinate,
                        final Piece pieceAttacked) {
			 super(board, pieceMoved,destinationCoordinate%8,destinationCoordinate/8, pieceAttacked);
		 }

		 @Override
		 public boolean equals(final Object other) {
			 	return this == other || other instanceof MajorAttackMove && super.equals(other);

		 }

	 }	

	public int getDestinationTileNumber() {
		return destinationCoordinateY*8+destinationCoordinateX;
	}

	public static final class PawnAttackMove extends AttackMove {	
		public PawnAttackMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinateX ,destinationCoordinateY,attackedPiece);
		}
		@Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
	}
	
	public static final class PawnMove extends Move {
		public PawnMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY);
		}
		@Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }
	}
	
	public static final class PawnEnPassantAttackMove extends AttackMove {	
		public PawnEnPassantAttackMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinateX ,destinationCoordinateY,attackedPiece);
		}

        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
            this.board.currentPlayer().getOpponent().getActivePieces().stream().filter(piece -> !piece.equals(this.getAttackedPiece())).forEach(builder::setPiece);
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }
	}
	
	public static final class PawnJump extends Move {
		public PawnJump(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY);
		}
		@Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }
		
		@Override
		public Board execute() {
			final Builder builder= new Builder();
			for(final Piece piece:this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn= (Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			builder.setMoveTransition(this);
			return builder.build();
		}
	}
	
	static abstract class CastleMove extends Move {
		
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		
		public CastleMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY);
			this.castleRook=castleRook;
			this.castleRookStart=castleRookStart;
			this.castleRookDestination=castleRookDestination;
		}
		public Rook getCastleRook() {
			return this.castleRook;
		}
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		@Override
		public Board execute() {
			final Builder builder= new Builder();
			for(final Piece piece: this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			//move the king
			builder.setPiece(this.movedPiece.movePiece(this));
			//set rook
			builder.setPiece(new Rook(this.castleRookDestination%8,
					this.castleRookDestination/8,
					this.castleRook.getPieceAlliance()));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			builder.setMoveTransition(this);
			return builder.build();
		}
		@Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }
		 @Override
	        public boolean equals(final Object other) {
	            if (this == other) {
	                return true;
	            }
	            if (!(other instanceof CastleMove)) {
	                return false;
	            }
	            final CastleMove otherCastleMove = (CastleMove) other;
	            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
	        }
		
	}
	public static final class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY,
					castleRook,castleRookStart,castleRookDestination);
		}
		@Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O";
        }
	}
	
	public static final class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinateX,
				final int destinationCoordinateY,
				final Rook castleRook,
				final int castleRookStart,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinateX,destinationCoordinateY,
					castleRook,castleRookStart,castleRookDestination);
		}
		@Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            final QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
	}
	
	public static final class NullMove extends Move {
		public NullMove() {
			super(null,-1);
		}
		@Override
		public Board execute() {
			throw new RuntimeException("cannot execute the null move");
		}
		@Override
	    public String toString() {
	        return "Null Move";
	    }
		@Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public int getDestinationCoordinate() {
            return -1;
        }
	}
	
	public static class MoveFactory{
		
		private static final Move NULL_MOVE = new NullMove();
		private MoveFactory() {			
			throw new RuntimeException("Not instanciable.");
		}
		public static Move getNullMove() {
            return NULL_MOVE;
        }
		public static Move createMove(final Board board,final int currentCoordinate, final int destinationCoordinate) {
			for(final Move move: board.getAllLegalMove()) {
				if(move.getCurrentCoordinate()==currentCoordinate &&
						move.getDestinationTileNumber()==destinationCoordinate) {
					return move;
				}
			}
			return NULL_MOVE;
		}
		
	}
	
}
