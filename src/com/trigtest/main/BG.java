package com.trigtest.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BG extends GameObject {
	
	static int background = 2;
	public BG(int x, int y, ID id) {super(x, y, id);} 
	public void render(Graphics g) {
		try {
			BufferedImage GameBG = ImageIO.read(new File("assets/bg" + background + ".png"));
			g.drawImage(GameBG, x, y, null);	
		} catch(IOException e) {
			e.printStackTrace();
		}			
		g.setColor(new Color(255, 255, 255, 180)); //alpha = 180
		g.fillRect(20, 20, 595, 605);
	}
	
	public void tick() {/*nothing needed here*/}

}
