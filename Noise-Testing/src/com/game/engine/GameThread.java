package com.game.engine;

public class GameThread extends Thread {

	private final GameLoop loop;
	private final Thread gameLoopThread;
	
	// This makes a new thread in the processes
	public GameThread(Game game){
		loop = new GameLoop(game);
		gameLoopThread = new Thread(loop, game.title +" loop Thread");
	}
	
	public synchronized void start(){
		gameLoopThread.start();
	}
}
