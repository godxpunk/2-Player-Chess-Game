package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
	WHITE {
		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return whitePlayer;
		}
	}, BLACK {
		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return blackPlayer;
		}
	};
	public int getDirection() {
		if(this==WHITE)
			return 1;
		else return -1;
	}

	public boolean isBlack() {
		return this==BLACK;
	}

	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
