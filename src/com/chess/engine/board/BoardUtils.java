package com.chess.engine.board;

public class BoardUtils {

	private BoardUtils() {
		throw new RuntimeException("You cannot instantiate this class");
	}

	public static boolean isValidTileCoordinate(int candidateX, int candidateY) {		
		return candidateX>=0 && candidateX<8 && candidateY<8 && candidateY>=0;
	}
	
}
