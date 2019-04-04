package com.trigtest.main;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	
	public static final int WIDTH = 650, HEIGHT = 680;
	
	private static final long serialVersionUID = 7676616993152955582L;
	private Thread thread;
	private boolean running = false;
	
	private Handler handler; 
	private Menu menu;

	public enum STATE {
		Menu,  
		Game,
		Credits,
		Options,
		Tutorial,
		End
	}
	
	public STATE gameState = STATE.Menu;
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try {
			thread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns; 
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(running){
				render();
				frames++;
			}
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick(){
		handler.tick(); 
	}
	
	private void render(){
		BufferStrategy b = this.getBufferStrategy();
		if(b == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = b.getDrawGraphics();
		handler.render(g);
		if(gameState == STATE.Menu || gameState == STATE.Credits || gameState == STATE.Options || gameState == STATE.Tutorial || gameState == STATE.End) {
			menu.render(g); 
		}
		g.dispose();
		b.show();
	}
	
	public Game() {
		new Window(WIDTH, HEIGHT, "Trigonometry Speed Tests", this);

		handler = new Handler();
		menu = new Menu(this, handler);

		AudioPlayer.loadMusic();
		AudioPlayer.getMusic("backgroundLoop").loop();

		handler.addObject(new BG(0, 0, ID.BG));
		if(gameState == STATE.Game) {	
			handler.addObject(new Problem(0, 0, ID.Problem));
		} 
		this.addKeyListener(new KeyInput(this, handler));	
		this.addMouseListener(menu);
	}
	
	public static void main(String[] args){
		new Game();
	}
}
