package com.trigtest.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

import com.trigtest.main.Game.STATE;
import org.newdawn.slick.openal.Audio;

public class Menu extends MouseAdapter {
	
	private Game game;
	private Handler handler;

	public Menu(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;
	}
	
	int i = 1;
	Image image = null;
	boolean reverse = false;
	boolean drawTemp = true;
	
	//ACTUAL DEFAULT VALUES STORED HERE
	static int difficulty = 1; //0 = normal, 1 = hard, 2 = abyssal
	static int mode = 1; //0 = timed, 1 = accuracy, 2 = endless
	static int amtOfQ = 30;
	
	int page = 0; //tutorial pages
	Page onPage;
	int time;
	
	static int minutes = 5;
	static int seconds1 = 0;
	static int seconds2 = 0;
	
	static int tens = 3;
	static int ones = 0;
	
	//hitboxes (options page)
	private Rectangle baseRectangleHigh = new Rectangle(Game.WIDTH/2 - 125, 120, 250, 90);
	private Rectangle backButton = new Rectangle(40, 38, 88, 70);

	private Rectangle r = new Rectangle(Game.WIDTH/2 - 225, 50, 450, 80);

	private Rectangle quit = new Rectangle(273, 480, 92, 31);
	private Rectangle credits = new Rectangle(242, 379, 158, 32);
	private Rectangle options = new Rectangle(238, 280, 171, 41);
	private Rectangle play = new Rectangle(278, 179, 88, 41);

	private Rectangle bg0r = new Rectangle(Game.WIDTH/5 - 25 - 3, 500 - 3, 106, 106);
	private Rectangle bg1r = new Rectangle(2*Game.WIDTH/5 - 25 - 3, 500 - 3, 106, 106);
	private Rectangle bg2r = new Rectangle(3*Game.WIDTH/5 - 25 - 3, 500 - 3, 106, 106);
	private Rectangle bg3r = new Rectangle(4*Game.WIDTH/5 - 25 - 3, 500 - 3, 106, 106);

	private Rectangle tutorial = new Rectangle(429, 356, 102, 18);
	
	//select boxes
	private Rectangle normal = new Rectangle(110, 138, 104, 33);
	private Rectangle hard = new Rectangle(290, 140, 71, 31);
	private Rectangle abyssal = new Rectangle(432, 139, 110, 36);

	private Rectangle timed = new Rectangle(117, 250, 91, 31);
	private Rectangle accuracy = new Rectangle(259, 249, 132, 36);
	private Rectangle endless = new Rectangle(433, 250, 107, 31);

	private Rectangle on = new Rectangle(42, 390, 47, 31);
	private Rectangle off = new Rectangle(168, 390, 57, 31);
	
	//default selections 
	private Rectangle selectedDiff = hard;
	private Rectangle selectedMode = accuracy;
	private Rectangle selectedSound = on;
	
	//time selections
	private Rectangle minutesTop = new Rectangle(245, 319, 50, 20);
	private Rectangle minutesBottom = new Rectangle(245, 388, 50, 20);
	private Rectangle seconds1Top = new Rectangle(301, 319, 50, 20);
	private Rectangle seconds1Bottom = new Rectangle(301, 388, 50, 20);
	private Rectangle seconds2Top = new Rectangle(357, 319, 50, 20);
	private Rectangle seconds2Bottom = new Rectangle(357, 388, 50, 20);

	private Rectangle minutes_0 = new Rectangle(243, 317, 54, 93);
	private Rectangle seconds1_0 = new Rectangle(299, 317, 54, 93);
	private Rectangle seconds2_0 = new Rectangle(355, 317, 54, 93);
	
	//question number selections
	private Rectangle tensTop = new Rectangle(245, 320, 78, 20);
	private Rectangle tensBottom = new Rectangle(245, 389, 78, 20);
	private Rectangle onesTop = new Rectangle(329, 320, 78, 20);
	private Rectangle onesBottom = new Rectangle(329, 389, 78, 20);

	private Rectangle tens_0 = new Rectangle(245, 320, 78, 89);
	private Rectangle ones_0 = new Rectangle(329, 320, 78, 89);

	private Rectangle endBack = new Rectangle(40, 528, 88, 70);

	//tutorial buttons
	private Rectangle back = new Rectangle(20, 550, 150, 75);
	private Rectangle next = new Rectangle(465, 550, 150, 75);

	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(game.gameState == STATE.Credits) {
			if(mouseOver(mouseX, mouseY, backButton)) { 
				game.gameState = STATE.Menu;
			}
		} else if(game.gameState == STATE.Menu) {
			if(mouseOver(mouseX, mouseY, quit)) { 
				System.exit(-420);
			} else if(mouseOver(mouseX, mouseY, credits)) {
				game.gameState = STATE.Credits;
			} else if(mouseOver(mouseX, mouseY, options)) {
				game.gameState = STATE.Options;
			} else if(mouseOver(mouseX, mouseY, play)) {
				game.gameState = STATE.Game;
				handler.addObject(new Problem(0, 0, ID.Problem));
				time = KeyInput.interval;
			}
		} else if(game.gameState == STATE.Options) {
			if(mouseOver(mouseX, mouseY, bg0r)) {
				BG.background = 0;
			} else if(mouseOver(mouseX, mouseY, bg1r)) {
				BG.background = 1;
			} else if(mouseOver(mouseX, mouseY, bg2r)) {
				BG.background = 2;
			} else if(mouseOver(mouseX, mouseY, bg3r)) {
				BG.background = 3;
			} else if(mouseOver(mouseX, mouseY, backButton)) { 
				game.gameState = STATE.Menu;
			} else if(mouseOver(mouseX, mouseY, tutorial)) {
				game.gameState = STATE.Tutorial;
			} else if(mouseOver(mouseX, mouseY, normal)) {
				selectedDiff = normal;
				difficulty = 0;
			} else if(mouseOver(mouseX, mouseY, hard)) {
				selectedDiff = hard;
				difficulty = 1;
			} else if(mouseOver(mouseX, mouseY, abyssal)) {
				selectedDiff = abyssal;
				difficulty = 2;
			} else if(mouseOver(mouseX, mouseY, timed)) {
				selectedMode = timed;
				mode = 0;
			} else if(mouseOver(mouseX, mouseY, accuracy)) {
				selectedMode = accuracy;
				mode = 1;
			} else if(mouseOver(mouseX, mouseY, endless)) {
				selectedMode = endless;
				mode = 2;
			} else if(mouseOver(mouseX, mouseY, on)) {
			    selectedSound = on;
			} else if(mouseOver(mouseX, mouseY, off)) {
				selectedSound = off;
			} else if(mode == 0) {
				if(mouseOver(mouseX, mouseY, minutesTop)) {
					if(minutes == 9) minutes = 0;
					else ++minutes;
				} else if(mouseOver(mouseX, mouseY, minutesBottom)) {
					if(minutes == 0) minutes = 9;
					else --minutes;
				} else if(mouseOver(mouseX, mouseY, seconds1Top)) {
					if(seconds1 == 5) seconds1 = 0;
					else ++seconds1;
				} else if(mouseOver(mouseX, mouseY, seconds1Bottom)) {
					if(seconds1 == 0) seconds1 = 5;
					else --seconds1;
				} else if(mouseOver(mouseX, mouseY, seconds2Top)) {
					if(seconds2 == 9) seconds2 = 0;
					else ++seconds2;
				} else if(mouseOver(mouseX, mouseY, seconds2Bottom)) {
					if(seconds2 == 0) seconds2 = 9;
					else --seconds2;
				}
			} else if(mode == 1) {
				if(mouseOver(mouseX, mouseY, tensTop)) {
					if(tens == 9) tens = 0;
					else ++tens;
				} else if(mouseOver(mouseX, mouseY, tensBottom)) {
					if(tens == 0) tens = 9;
					else --tens;
				} else if(mouseOver(mouseX, mouseY, onesTop)) {
					if(ones == 9) ones = 0;
					else ++ones;
				} else if(mouseOver(mouseX, mouseY, onesBottom)) {
					if(ones == 0) ones = 9;
					else --ones;	
				}
			}
		} else if(game.gameState == STATE.Tutorial) {
			if(mouseOver(mouseX, mouseY, next) && page < 5) ++page;
			else if(mouseOver(mouseX, mouseY, back) && page > 0) --page;
		} else if(game.gameState == STATE.End) {
			if(mouseOver(mouseX, mouseY, endBack)) {
				game.gameState = STATE.Menu;
			}
		}
	} 
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		try {
			if(game.gameState == STATE.Menu) {
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
				Font font2 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				ge.registerFont(font);
				ge.registerFont(font2);
				g2d.setFont(font.deriveFont(46.0f));
			
				g2d.setColor(Color.BLACK);
				
				FontMetrics m = g2d.getFontMetrics();
				int setX = r.x + (r.width - m.stringWidth("don't get TRIGgered")) / 2;
				
				g2d.drawString("don't get TRIGgered", setX-10, 90);		
				g2d.setFont(font2.deriveFont(44.0f));
				g2d.drawString("Play", Game.WIDTH/2 - 50, 210);
				g2d.drawString("Options", Game.WIDTH/2 - 90, 310);
				g2d.drawString("Credits", Game.WIDTH/2 - 85, 410);
				g2d.drawString("Quit", Game.WIDTH/2 - 55, 510);
			} 
			
			if(game.gameState == STATE.Credits) {
				g2d.setColor(Color.BLACK);
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				ge.registerFont(font);
				g2d.setFont(font.deriveFont(60.0f));
				FontMetrics m = g2d.getFontMetrics();
				
				int setX = baseRectangleHigh.x + (baseRectangleHigh.width - m.stringWidth("Credits")) / 2;
				
				g2d.drawString("Credits", setX, 90);
				g2d.setFont(font.deriveFont(36.0f)); //titles
				
				g2d.drawString("Designers", 40, 160);
				g2d.drawString("Artists", 360, 160);
				g2d.drawString("Programming", 40, 310);
				g2d.drawString("Testing", 410, 310);
				g2d.drawString("Special Thanks", 40, 420);
				
				g2d.setFont(font.deriveFont(18.0f)); //names
				g2d.drawString("Mari Yanagawa", 45, 200);
				g2d.drawString("Carsten Streichardt", 45, 220);
				g2d.drawString("Camille Boiteux", 45, 240);
				g2d.drawString("Nick Lopatin", 45, 260);
				
				g2d.drawString("Carsten Streichardt", 45, 350);
				
				g2d.drawString("Kendall Goto (inspiration)", 45, 460);
				g2d.drawString("Victor Nguyen (making me do this)", 45, 480);
				g2d.drawString("Eunice Moon/Lee (inspiration)", 45, 500);
				g2d.drawString("Nick Lopatin (huge help)", 45, 520);
				g2d.drawString("Cornelia Penn (<3)", 45, 540);
				g2d.drawString("Camille Boiteux (much inspiration)", 45, 560);
				
				g2d.drawString("Mari Yanagawa", 360, 200);
				g2d.drawString("Carsten Streichardt", 360, 220);
				
				g2d.drawString("TBD", 410, 350);
				
				g2d.setFont(font.deriveFont(12.0f)); //additional text
				g2d.drawString("version 0.1.0", 45, 600);
				
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				
				if(drawTemp) 
					g2d.drawImage(resize(new ImageIcon("assets/sprites/back_button/back_" + i + ".png").getImage(), 88, 70), 40, 38, null);
				backButton(g2d, 40, 38);
			}
			
			if(game.gameState == STATE.Options) {
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(2f));
				g2d.draw(selectedDiff);
				g2d.draw(selectedMode);
				g2d.draw(selectedSound);
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
				ge.registerFont(font);
				g2d.setFont(font.deriveFont(45.0f));
				
				g2d.drawString("Options", 400, 74);
				
				g2d.setFont(font1.deriveFont(25.0f));
				FontMetrics m1 = g2d.getFontMetrics();
				
				g2d.drawString("Difficulty", Game.WIDTH/2 - m1.stringWidth("Difficulty")/2, 124);
				g2d.drawString("Gamemode", Game.WIDTH/2 - m1.stringWidth("Gamemode")/2, 234);
				g2d.drawString("Sound", Game.WIDTH/5 - m1.stringWidth("Sound")/2, 374);
				g2d.drawString("Tutorial", 480 - m1.stringWidth("Tutorial")/2, 374);
				g2d.drawString("Background", Game.WIDTH/2 - m1.stringWidth("Background")/2, 474);
				
				g2d.setFont(font.deriveFont(25.0f));
				FontMetrics m2 = g2d.getFontMetrics();
				g2d.drawString("Normal", Game.WIDTH/4 - m2.stringWidth("Normal")/2, 164);
				g2d.drawString("Hard", Game.WIDTH/2 - m2.stringWidth("Hard")/2, 164);
				g2d.drawString("Abyssal", 3*Game.WIDTH/4 - m2.stringWidth("Abyssal")/2, 164);
				
				g2d.drawString("Timed", Game.WIDTH/4 - m2.stringWidth("Timed")/2, 274);
				g2d.drawString("Accuracy", Game.WIDTH/2 - m2.stringWidth("Accuracy")/2, 274);
				g2d.drawString("Endless", 3*Game.WIDTH/4 - m2.stringWidth("Endless")/2, 274);
				
				g2d.drawString("On", Game.WIDTH/10 - m2.stringWidth("On")/2, 414);
				g2d.drawString("Off", 3*Game.WIDTH/10 - m2.stringWidth("Off")/2, 414);
				
				
				Image bg0 = resize(new ImageIcon("assets/bg0_icon.png").getImage(), 100, 100);
				Image bg1 = resize(new ImageIcon("assets/bg1_icon.png").getImage(), 100, 100);
				Image bg2 = resize(new ImageIcon("assets/bg2_icon.png").getImage(), 100, 100);
				Image bg3 = resize(new ImageIcon("assets/bg3_icon.png").getImage(), 100, 100);
				
				g2d.fillRect(Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(2*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(3*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(4*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				
				g2d.drawImage(bg0, Game.WIDTH/5 - bg0.getWidth(null)/2, 500, null);
				g2d.drawImage(bg1, 2*Game.WIDTH/5 - bg1.getWidth(null)/2, 500, null);
				g2d.drawImage(bg2, 3*Game.WIDTH/5 - bg2.getWidth(null)/2, 500, null);
				g2d.drawImage(bg3, 4*Game.WIDTH/5 - bg3.getWidth(null)/2, 500, null);
				
				Font font2 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/bahnschrift.ttf"));
				ge.registerFont(font2);
				g2d.setFont(font2.deriveFont(40.0f));
				FontMetrics m3 = g2d.getFontMetrics();
				
				if(mode == 0) {
					Image timePanel = resize(new ImageIcon("assets/sprites/clement.png").getImage(), 172, 99);
					g2d.drawImage(timePanel, 240, 315, null);
					g2d.drawString(Integer.toString(minutes),  (int) minutes_0.x + (minutes_0.width - m3.stringWidth(Integer.toString(minutes))) / 2, 377);
					g2d.drawString(Integer.toString(seconds1), (int) seconds1_0.x + (seconds1_0.width - m3.stringWidth(Integer.toString(seconds1))) / 2, 377);
					g2d.drawString(Integer.toString(seconds2), (int) seconds2_0.x + (seconds2_0.width - m3.stringWidth(Integer.toString(seconds2))) / 2, 377);
					
					int time = Menu.minutes*60 + Integer.parseInt(Menu.seconds1 + "" + Menu.seconds2);
					KeyInput.interval = (time < 1) ? 1 : time;
					
				} else if(mode == 1) {
					Image questionPanel = resize(new ImageIcon("assets/sprites/camille.png").getImage(), 172, 99);
					g2d.drawImage(questionPanel, 240, 315, null);
					g2d.drawString(Integer.toString(tens),  (int) tens_0.x + (tens_0.width - m3.stringWidth(Integer.toString(tens))) / 2, 377);
					g2d.drawString(Integer.toString(ones), (int) ones_0.x + (ones_0.width - m3.stringWidth(Integer.toString(ones))) / 2, 377);
					
					amtOfQ = (amtOfQ < 1) ? 1 : tens*10 + ones;
				}
				
				backButton(g2d, 40, 38);
			
			}
			
			if(game.gameState == STATE.End) {
				g2d.setColor(Color.BLACK);
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
				ge.registerFont(font); ge.registerFont(font1);
				
				
				if(mode == 0) {
					drawEnd(g2d, font, font1);
				} else if(mode == 1) {
					handler.clearGame();
					g2d.setFont(font.deriveFont(32.0f));
					FontMetrics m = g2d.getFontMetrics(); 
					g2d.drawString("You scored a: ", Game.WIDTH/2 - m.stringWidth("You scored a: ")/2, 120);
					
					g2d.setFont(font1.deriveFont(42.0f));
					FontMetrics m1 = g2d.getFontMetrics();
					g2d.drawString(Problem.n_correct + " / " + amtOfQ, Game.WIDTH/2 - m1.stringWidth(Problem.count + " / " + amtOfQ)/2, 170);
				} else if(mode == 2) {
					//do i really want this?
				}
				
				backButton(g2d, 45, 528);
			}
			if(game.gameState == STATE.Tutorial) {
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
				ge.registerFont(font); ge.registerFont(font1);
				
				g2d.setFont(font1.deriveFont(48.0f));
				g2d.setColor(Color.BLACK);
				g2d.drawString("back", 35, 600);
				g2d.drawString("next", 480, 600);
				g2d.drawString("menu", 250, 600);

				onPage = new Page(g2d, page);
				onPage.drawPage();

				//backButton(g2d);
			} 
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawEnd(Graphics2D g2d, Font f, Font f1) {
		handler.clearGame();
		String endMessage = "In " + Problem.toMS(time) + ", you answered ";
		g2d.setFont(f.deriveFont(28.0f));
		
		FontMetrics m = g2d.getFontMetrics(); 
		g2d.drawString("Game Over!", Game.WIDTH/2 - m.stringWidth("Game Over!")/2, 80);
		g2d.drawString(endMessage, Game.WIDTH/2 - m.stringWidth(endMessage)/2, 120);

		g2d.setFont(f1.deriveFont(42.0f));
		FontMetrics m1 = g2d.getFontMetrics();
		g2d.drawString((Problem.n_correct + Problem.n_incorrect) + " questions", Game.WIDTH/2 - m1.stringWidth((Problem.n_correct + Problem.n_incorrect) + " questions")/2, 170);
		g2d.setFont(f.deriveFont(28.0f));
		g2d.drawString("with an accuracy of: ", Game.WIDTH/2 - m.stringWidth("with an accuracy of: ")/2, 240);
		g2d.setFont(f1.deriveFont(42.0f));
		g2d.drawString(Problem.n_correct + " / " + (Problem.n_correct + Problem.n_incorrect), Game.WIDTH/2 - m1.stringWidth(Problem.n_correct + " / " + (Problem.n_correct + Problem.n_incorrect))/2, 290);
		g2d.drawString("= " + (int) Math.ceil(((double) Problem.n_correct/(double) (Problem.n_correct + Problem.n_incorrect) * 100)) + "%", Game.WIDTH/2 - m1.stringWidth("= " + (int) Math.ceil(((double) Problem.n_correct/(double) (Problem.n_correct + Problem.n_incorrect) * 100)) + "%")/2, 330);
	}
	
	private boolean mouseOver(int mouseX, int mouseY, Rectangle r) {
		if(mouseX > r.getX() && mouseX < r.getX() + r.getWidth()) {
			if(mouseY > r.getY() && mouseY < r.getY() + r.getHeight())
				return true;
			return false;
		}
		return false;
	}
	
	private Image resize(Image i, int w, int h) {
		BufferedImage r = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = r.createGraphics();
	    g2d.drawImage(i, 0, 0, w, h, null);
	    g2d.dispose();
	    return r; 
	}
	
	private void backButton(Graphics2D g2d, int x, int y) {
		if(i > 25) reverse = true;
		else if(i < 2) reverse = false;
		
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				drawTemp = false; i = reverse ? --i : ++i;
				image = resize(new ImageIcon("assets/sprites/back_button/back_" + i + ".png").getImage(), 88, 70);	
			}
		}, 10);	
		g2d.drawImage(image, x, y, null);
	}
}
