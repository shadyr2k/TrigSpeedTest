package com.trigtest.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

import com.trigtest.main.Game.STATE;

public class Menu extends MouseAdapter {
	
	private Game game;
	private Handler handler;

	private static FadingImage confetti;

	static boolean fadeConfetti = true;
	static boolean drawEnd = true;

	//boolean drawConfetti = true;
	private boolean playEndSound = true;

	Menu(Game game, Handler handler) {
		this.game = game;
		this.handler = handler;
	}

	private static int i = 1;
	private static Image image = null;
	private static boolean reverse = false;
	private static boolean drawTemp = true;

	private int bgCoverX = 340;

	//ACTUAL DEFAULT VALUES STORED HERE
	static int difficulty = 1; //0 = normal, 1 = hard, 2 = abyssal
	static int mode = 1; //0 = timed, 1 = accuracy, 2 = endless
	static int amtOfQ = 30;

	static boolean sound = true;
	private static boolean paused = false;
	private float volume = 1f;

	private int page = 0; //tutorial pages

	static int time;
	
	private static int minutes = 5;
	private static int seconds1 = 0;
	private static int seconds2 = 0;
	
	private static int tens = 3;
	private static int ones = 0;
	
	//hitboxes (options page)
	private Rectangle baseRectangleHigh = new Rectangle(Game.WIDTH/2 - 125, 120, 250, 90);
	private Rectangle backButton = new Rectangle(40, 38, 88, 70);

	private Rectangle r = new Rectangle(Game.WIDTH/2 - 225, 50, 450, 80);

	private Rectangle quit = new Rectangle(273, 480, 92, 31);
	private Rectangle credits = new Rectangle(242, 379, 158, 32);
	private Rectangle options = new Rectangle(238, 280, 171, 41);
	private Rectangle play = new Rectangle(278, 179, 88, 41);

	private Rectangle bg0r = new Rectangle(Game.WIDTH/5 - 50 - 3, 500 - 3, 106, 106);
	private Rectangle bg1r = new Rectangle(2*Game.WIDTH/5 - 50 - 3, 500 - 3, 106, 106);
	private Rectangle bg2r = new Rectangle(3*Game.WIDTH/5 - 50 - 3, 500 - 3, 106, 106);
	private Rectangle bg3r = new Rectangle(4*Game.WIDTH/5 - 50 - 3, 500 - 3, 106, 106);

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
	private Rectangle menu = new Rectangle(243, 564, 154, 48);

	private Rectangle volumeSliderHitbox = new Rectangle(69, 442, 133, 20);
	private Shape volumeSlider = new Ellipse2D.Double(201, 442, 20, 20);

	private Random random = new Random();

	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		if(game.gameState == STATE.Credits) {
			if(mouseOver(mouseX, mouseY, backButton)) {
				if(sound) AudioPlayer.getSound("back").play();
				game.gameState = STATE.Menu;
			}
		} else if(game.gameState == STATE.Menu) {
			if(mouseOver(mouseX, mouseY, quit)) {
				if(sound) {
					AudioPlayer.getMusic("backgroundLoop").stop();
					AudioPlayer.getSound("menuPress").play();
					AudioPlayer.getSound("bye").play();
				}
				new Timer().schedule(
					new TimerTask(){
						public void run(){
							System.exit(-420);
						}
					}, 1300);
			} else if(mouseOver(mouseX, mouseY, credits)) {
				game.gameState = STATE.Credits; if(sound) AudioPlayer.getSound("menuPress").play();
			} else if(mouseOver(mouseX, mouseY, options)) {
				game.gameState = STATE.Options; if(sound) AudioPlayer.getSound("menuPress").play();
			} else if(mouseOver(mouseX, mouseY, play)) {
				if(sound) AudioPlayer.getSound("menuPlayPress").play();
				game.gameState = STATE.Game;
				fadeConfetti = true;
				confetti = new FadingImage(new ImageIcon("assets/gameWin.gif").getImage());
				handler.addObject(new Problem(0, 0, ID.Problem));
				if(sound){
					AudioPlayer.stop("backgroundLoop");
					AudioPlayer.getMusic("gameLoop").loop();
					AudioPlayer.setVolume(volume);
					playEndSound = true;
				}
				KeyInput.timer = new Timer();
				time = minutes*60 + Integer.parseInt(Menu.seconds1 + "" + Menu.seconds2);
				KeyInput.interval = (time < 1) ? 1 : time;
				time = KeyInput.interval;
			}
		} else if(game.gameState == STATE.Options) {
			if(mouseOver(mouseX, mouseY, bg0r)) {
				if(sound) AudioPlayer.getSound("bgPress").play();
				BG.background = 0;
				bgCoverX = 80;
			} else if(mouseOver(mouseX, mouseY, bg1r)) {
				if(sound) AudioPlayer.getSound("bgPress").play();
				BG.background = 1;
				bgCoverX = 210;
			} else if(mouseOver(mouseX, mouseY, bg2r)) {
				if(sound) AudioPlayer.getSound("bgPress").play();
				BG.background = 2;
				bgCoverX = 340;
			} else if(mouseOver(mouseX, mouseY, bg3r)) {
				if(sound) AudioPlayer.getSound("bgPress").play();
				BG.background = 3;
				bgCoverX = 470;
			} else if(mouseOver(mouseX, mouseY, backButton)) { 
				game.gameState = STATE.Menu;
				if(sound) AudioPlayer.getSound("back").play();
			} else if(mouseOver(mouseX, mouseY, tutorial)) {
				game.gameState = STATE.Tutorial;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, normal)) {
				selectedDiff = normal;
				difficulty = 0;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, hard)) {
				selectedDiff = hard;
				difficulty = 1;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, abyssal)) {
				selectedDiff = abyssal;
				difficulty = 2;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, timed)) {
				selectedMode = timed;
				mode = 0;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, accuracy)) {
				selectedMode = accuracy;
				mode = 1;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, endless)) {
				selectedMode = endless;
				mode = 2;
				if(sound) AudioPlayer.getSound("menuSelectionPress").play();
			} else if(mouseOver(mouseX, mouseY, on) && !selectedSound.equals(on)) {
				AudioPlayer.getSound("menuSelectionPress").play();
				selectedSound = on;
				sound = true;
				if(!paused)
					AudioPlayer.getMusic("backgroundLoop").loop();
				else AudioPlayer.getMusic("backgroundLoop").resume();
			} else if(mouseOver(mouseX, mouseY, off) && !selectedSound.equals(off)) {
				selectedSound = off;
				sound = false; paused = true;
				AudioPlayer.pause("backgroundLoop");
			} else if(mouseOver(mouseX, mouseY, volumeSliderHitbox) && selectedSound.equals(on)){
				if(sound) {
					volume = (float) (mouseX - 69) / (float) 133;
					AudioPlayer.setVolume(volume);
					volumeSlider = new Ellipse2D.Double(mouseX - 10, 442, 20, 20);
				}
			} else if(mode == 0) {
				if(mouseOver(mouseX, mouseY, minutesTop)) {
					if(minutes == 9) minutes = 0;
					else ++minutes; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, minutesBottom)) {
					if(minutes == 0) minutes = 9;
					else --minutes; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, seconds1Top)) {
					if(seconds1 == 5) seconds1 = 0;
					else ++seconds1; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, seconds1Bottom)) {
					if(seconds1 == 0) seconds1 = 5;
					else --seconds1; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, seconds2Top)) {
					if(seconds2 == 9) seconds2 = 0;
					else ++seconds2; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, seconds2Bottom)) {
					if(seconds2 == 0) seconds2 = 9;
					else --seconds2; if(sound) AudioPlayer.getSound("press").play();
				}
			} else if(mode == 1) {
				if(mouseOver(mouseX, mouseY, tensTop)) {
					if(tens == 9) tens = 0;
					else ++tens; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, tensBottom)) {
					if(tens == 0) tens = 9;
					else --tens; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, onesTop)) {
					if(ones == 9) ones = 0;
					else ++ones; if(sound) AudioPlayer.getSound("press").play();
				} else if(mouseOver(mouseX, mouseY, onesBottom)) {
					if(ones == 0) ones = 9;
					else --ones; if(sound) AudioPlayer.getSound("press").play();
				}
			}
		} else if(game.gameState == STATE.Tutorial) {
			if(mouseOver(mouseX, mouseY, next) && page < 5) {
				if(sound) AudioPlayer.getSound("tutorialPress").play();
				++page;
			}
			else if(mouseOver(mouseX, mouseY, next) && page == 5) {
				if(sound) AudioPlayer.getSound("tutorialPress").play();
				game.gameState = STATE.Options;
			}
			else if(mouseOver(mouseX, mouseY, back) && page > 0){
				if(sound) AudioPlayer.getSound("tutorialPress").play();
				--page;
			}
			else if(mouseOver(mouseX, mouseY, menu)){
				if(sound) AudioPlayer.getSound("tutorialPress").play();
				game.gameState = STATE.Options;
			}
		} else if(game.gameState == STATE.End) {
			if(mouseOver(mouseX, mouseY, endBack)) {
				if(sound) AudioPlayer.getSound("back").play();
				handler.clearEndBG();
				drawEnd = true;
				if(sound) {
					if(AudioPlayer.getSound("win").playing()) AudioPlayer.getSound("win").stop();
					AudioPlayer.getMusic("backgroundLoop").loop();
				}
				AudioPlayer.setVolume(volume);
				game.gameState = STATE.Menu;
			}
		}
	} 
	
	void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
				g2d.drawString("Sound", 430, 510);
				
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
				g2d.drawString("Cornelia Penn (i love you <3)", 45, 540);
				g2d.drawString("Camille Boiteux (much inspiration)", 45, 560);
				
				g2d.drawString("Mari Yanagawa", 360, 200);
				g2d.drawString("Carsten Streichardt", 360, 220);
				
				g2d.drawString("TBD", 410, 350);

				g2d.setFont(font.deriveFont(12.0f)); //additional text
				g2d.drawString("Menu: EPSSounds - Shallow", 390, 530);
				g2d.drawString("Water Game Loop", 390, 545);
				g2d.drawString("Game: Kevin MacLeod - Artifact", 390, 560);
				g2d.drawString("version 0.2.0", 45, 600);
				
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

				g2d.drawLine(70, 452, 200, 452);
				g2d.fill(volumeSlider);

				Image bg0 = resize(new ImageIcon("assets/bg0_icon.png").getImage(), 100, 100);
				Image bg1 = resize(new ImageIcon("assets/bg1_icon.png").getImage(), 100, 100);
				Image bg2 = resize(new ImageIcon("assets/bg2_icon.png").getImage(), 100, 100);
				Image bg3 = resize(new ImageIcon("assets/bg3_icon.png").getImage(), 100, 100);
				Image bgCover = new ImageIcon("assets/bgCover.png").getImage();

				g2d.fillRect(Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(2*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(3*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				g2d.fillRect(4*Game.WIDTH/5 - bg0.getWidth(null)/2 - 3, 500 - 3, 106, 106);
				
				g2d.drawImage(bg0, Game.WIDTH/5 - bg0.getWidth(null)/2, 500, null);
				g2d.drawImage(bg1, 2*Game.WIDTH/5 - bg1.getWidth(null)/2, 500, null);
				g2d.drawImage(bg2, 3*Game.WIDTH/5 - bg2.getWidth(null)/2, 500, null);
				g2d.drawImage(bg3, 4*Game.WIDTH/5 - bg3.getWidth(null)/2, 500, null);
				g2d.drawImage(bgCover, bgCoverX, 500, null);
				
				Font font2 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/bahnschrift.ttf"));
				ge.registerFont(font2);
				g2d.setFont(font2.deriveFont(40.0f));
				FontMetrics m3 = g2d.getFontMetrics();
				
				if(mode == 0) {
					Image timePanel = resize(new ImageIcon("assets/sprites/clement.png").getImage(), 172, 99);
					g2d.drawImage(timePanel, 240, 315, null);
					g2d.drawString(Integer.toString(minutes),  minutes_0.x + (minutes_0.width - m3.stringWidth(Integer.toString(minutes))) / 2, 377);
					g2d.drawString(Integer.toString(seconds1), seconds1_0.x + (seconds1_0.width - m3.stringWidth(Integer.toString(seconds1))) / 2, 377);
					g2d.drawString(Integer.toString(seconds2), seconds2_0.x + (seconds2_0.width - m3.stringWidth(Integer.toString(seconds2))) / 2, 377);
				} else if(mode == 1) {
					Image questionPanel = resize(new ImageIcon("assets/sprites/camille.png").getImage(), 172, 99);
					g2d.drawImage(questionPanel, 240, 315, null);
					g2d.drawString(Integer.toString(tens),  tens_0.x + (tens_0.width - m3.stringWidth(Integer.toString(tens))) / 2, 377);
					g2d.drawString(Integer.toString(ones), ones_0.x + (ones_0.width - m3.stringWidth(Integer.toString(ones))) / 2, 377);
					
					amtOfQ = (amtOfQ < 1) ? 1 : tens*10 + ones;
				}
				backButton(g2d, 40, 38);
			}
			if(game.gameState == STATE.End) {
				confetti.drawFadingImage(g2d);
				if(playEndSound && sound){
					AudioPlayer.stop("gameLoop");
					AudioPlayer.getSound("win").play();
					playEndSound = false;
				}
				if(drawEnd) {
					handler.addObject(new EndBG(0, 0, ID.EndBG, mode, random.nextInt(6), handler));
				}

				if(fadeConfetti){
					Timer fade = new Timer();
					fade.schedule(
							new TimerTask(){
								public void run(){
									confetti.fadeImage();
								}
							}, 6500
					);
					if(game.gameState != STATE.End) {
						fade.cancel();
						confetti.delImage();
					}
				}
			}

			if(game.gameState == STATE.Tutorial) {
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
				Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
				ge.registerFont(font); ge.registerFont(font1);
				
				g2d.setFont(font1.deriveFont(48.0f));
				g2d.setColor(Color.BLACK);
				if(page != 0)
					g2d.drawString("back", 35, 600);
				if(page != 5)
				g2d.drawString("next", 480, 600);
				if(page == 5)
					g2d.drawString("done", 480, 600);

				g2d.drawString("menu", 250, 600);

				Page onPage = new Page(g2d, page);
				onPage.drawPage();

				//backButton(g2d);
			} 
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}


	private boolean mouseOver(int mouseX, int mouseY, Rectangle r) {
		if(mouseX > r.getX() && mouseX < r.getX() + r.getWidth())
			return mouseY > r.getY() && mouseY < r.getY() + r.getHeight();
		return false;
	}
	
	private static Image resize(Image i, int w, int h) {
		BufferedImage r = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = r.createGraphics();
	    g2d.drawImage(i, 0, 0, w, h, null);
	    g2d.dispose();
	    return r; 
	}
	
	static void backButton(Graphics2D g2d, int x, int y) {
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
