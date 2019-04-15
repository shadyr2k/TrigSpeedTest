package com.trigtest.main;

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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Problem extends GameObject {
	
	static String printString = "";
	static boolean drawString = true;
	static boolean generateNewQuestion = true;
	static boolean interpretAnswer = false;
	static boolean startTimer = false;
	static String solved = ""; 
	static String newQuestion = "";
	static Image image = null;
	static String correctString = "";
	static int count = 0;
	
	private Rectangle baseRectangle = new Rectangle(Game.WIDTH/2 - 130, 425, 260, 100);
	private Rectangle baseRectangleHigh = new Rectangle(Game.WIDTH/2 - 125, 120, 250, 90);
	
	static int n_correct, n_incorrect, n_ofQuestions = 0; //# right, # wrong, running total of Qs

	public Problem(int x, int y, ID id) {
		super(x, y, id);

		//reset everything
		drawString = true;
		generateNewQuestion = true;
		interpretAnswer = false;
		startTimer = false;
		solved = "";
		newQuestion = "";
		image = null;
		correctString = "";
		n_correct = 0;
		n_incorrect = 0;
		n_ofQuestions = 0;
		count = 0;

		KeyInput.previousQuestion = "";
		KeyInput.previousAnswer = "";
		KeyInput.previousAnswer_1 = "";
		KeyInput.correctAnswer = "";
		KeyInput.color = new Color(0, 0, 0, 0);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(Game.WIDTH/2 - 130, 425, 260, 100);
		g.setColor(Color.WHITE); 
		g.fillRect(Game.WIDTH/2 - 125, 430, 250, 90);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-serif.ttf"));
			Font fontBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-rounded.otf"));
			Font fontQuestion = Font.createFont(Font.TRUETYPE_FONT,	new File("assets/fonts/math-question.TTF"));
			ge.registerFont(font); ge.registerFont(fontBold); ge.registerFont(fontQuestion);
			g2d.setFont(font.deriveFont(38.0f));
				
			FontMetrics m = g2d.getFontMetrics();	
			int setX = baseRectangle.x + (baseRectangle.width - m.stringWidth(printString)) / 2;
			int setY = baseRectangle.y + ((baseRectangle.height - m.getHeight()) / 2) + m.getAscent();

			if(m.stringWidth(printString) > 225) drawString = false;
			else drawString = true;
			g2d.setColor(Color.BLACK);
			g2d.drawString(printString, setX, setY);
			g2d.setFont(fontBold.deriveFont(48.0f));
			m = g2d.getFontMetrics();
			
			int setX_1 = baseRectangleHigh.x + (baseRectangleHigh.width - m.stringWidth(newQuestion)) / 2;
			int setY_1 = baseRectangleHigh.y + ((baseRectangleHigh.height - m.getHeight()) / 2) + m.getAscent();
			
			g.setColor(Color.WHITE);
			g.fillRect(Game.WIDTH/2 - ((m.stringWidth(newQuestion)+50)/2), 125, m.stringWidth(newQuestion) + 50, 80);
		
			g2d.setColor(Color.BLACK);
			//System.out.println(newQuestion);
			g2d.drawString(newQuestion, setX_1, setY_1);
			if(newQuestion.length() > 0) solved = solve(newQuestion);
			g2d.setFont(fontQuestion.deriveFont(26.0f));
			FontMetrics previous = g2d.getFontMetrics();	 		
	 		if(Menu.mode == 0) {	
	 			Rectangle setRectangle = new Rectangle(430, 230, 150, 150);
	 			
	 			g2d.fillRect(430, 230, 150, 150);
	 			g2d.setColor(Color.WHITE);
	 			g2d.fillRect(433, 233, 144, 144);
	 			g2d.setColor(KeyInput.color);
	 			g2d.fillRect(433, 233, 144, 144);
	 			g2d.setColor(Color.BLACK);
	 			g2d.drawString("PREVIOUS", 506 - previous.stringWidth("PREVIOUS")/2, 260);
	 			
	 			g2d.setFont(fontBold.deriveFont(18.0f));
	 			FontMetrics boldfont = g2d.getFontMetrics();
	 			g2d.drawString(KeyInput.previousQuestion, setRectangle.x + (setRectangle.width - boldfont.stringWidth(KeyInput.previousQuestion)) / 2, 290);
	 			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20.0f));
	 			FontMetrics boldfontB = g2d.getFontMetrics();
	 			g2d.drawString(KeyInput.previousAnswer, setRectangle.x + (setRectangle.width - boldfontB.stringWidth(KeyInput.previousAnswer)) / 2, 319);
	 			g2d.setFont(fontBold.deriveFont(18.0f));
	 			g2d.drawString(KeyInput.previousAnswer_1, setRectangle.x + (setRectangle.width - boldfont.stringWidth(KeyInput.previousAnswer_1)) / 2, 339);
	 			g2d.drawString(KeyInput.correctAnswer, setRectangle.x + (setRectangle.width - boldfont.stringWidth(KeyInput.correctAnswer)) / 2, 365);
	 		}
	 		g2d.setColor(Color.BLACK);
			if(generateNewQuestion) {
				++n_ofQuestions;
				++count;
				generateNewQuestion = false;
				newQuestion = getFunction(Menu.difficulty);
			}
			g2d.setFont(fontBold.deriveFont(32.0f));
			//g2d.drawString("(debug) answer: " + solved, 30, 600); => SHOW ANSWER
			ImageIcon correct = new ImageIcon("assets/green_check.png");
			ImageIcon incorrect = new ImageIcon("assets/red_x.png");	
			
			if(KeyInput.dontPress) {
				if(interpretAnswer) {
					image = resize(correct.getImage(), 50, 38);
				} else {
					image = resize(incorrect.getImage(), 50, 50);
				}	
			} 
			g2d.setFont(fontBold.deriveFont(21.0f));
			FontMetrics m1 = g2d.getFontMetrics();
			g2d.drawString(correctString, Game.WIDTH/2 - m1.stringWidth(correctString)/2, 400);
			g2d.drawImage(image, 465, 450, null);
				
			g2d.setFont(fontQuestion.deriveFont(36.0f));
			g2d.drawImage(resize(correct.getImage(), 45, 34), 50, 250, null);
			g2d.drawImage(resize(incorrect.getImage(), 43, 43), 50, 310, null);
			g2d.drawString(Integer.toString(n_correct), 105, 280);
			g2d.drawString(Integer.toString(n_incorrect), 105, 345); 
			g2d.setFont(fontQuestion.deriveFont(50.0f));
			FontMetrics m2 = g2d.getFontMetrics();
			
			if(Menu.mode == 0) {
				startTimer = n_ofQuestions == 1;
				g2d.drawString(toMS(KeyInput.interval), 590 - m2.stringWidth(toMS(KeyInput.interval)), 75);
			}
			String questionN = (Menu.mode != 1) ? "question " + n_ofQuestions : "question " + n_ofQuestions + "/" + Menu.amtOfQ;
			g2d.drawString(questionN, 40, 75);
			
		} catch(IOException | FontFormatException e){
			e.printStackTrace();
		}	
	}
	
	private static HashMap<String, String> radDeg = new HashMap<>();
	
	/**
	 * subroutine for getting a random function (i.e. the one displayed on screen for the user to answer)
	 * @return some random function
	 */	
	private String getFunction(int diff) {
		Random r = new Random();
		int layer1 = r.nextInt(2) + 1; //conversion problem or calculation problem
		String[] functions = {"sin", "cos", "tan", "sec", "csc", "cot"}; 
		String[] invFunctions = {"arcsin", "arccos", "arctan", "arcsec", "arccsc", "arccot"};

		radDeg.put("0", "0");
		radDeg.put("π/6", "30");
		radDeg.put("π/4", "45");
		radDeg.put("π/3", "60");
		radDeg.put("π/2", "90");
		radDeg.put("2π/3", "120");
		radDeg.put("3π/4", "135");
		radDeg.put("5π/6", "150");
		radDeg.put("π", "180");
		radDeg.put("7π/6", "210");
		radDeg.put("5π/4", "225");
		radDeg.put("4π/3", "240");
		radDeg.put("3π/2", "270");
		radDeg.put("5π/3", "300");
		radDeg.put("7π/4", "315");
		radDeg.put("11π/6", "330");
		radDeg.put("2π", "360");
		
		//30, 45, 60
		String[] funcAnswersSC = {"1/2", "√2/2", "√3/2"};
		String[] funcAnswersT = {"√3/3", "1", "√3"};
		String[] invFuncAnswersSC = {"2", "√2", "2/√3"};
		String[] invFuncAnswersT = {"√3", "1", "√3/3"};
		Object[] keys = radDeg.keySet().toArray();
		
		if(diff == 0)
			functions[3] = "sin"; functions[4] = "cos"; functions[5] = "tan"; //this is whatever for now
		if(layer1 == 1) {
			int layer2 = r.nextInt(2) + 1; //radian or degree (again)
			if(layer2 == 1) return (String) keys[r.nextInt(keys.length)];
			else return radDeg.get(keys[r.nextInt(keys.length)]);
		} else {
			if(diff != 2) return functions[r.nextInt(functions.length)] + "(" + ((r.nextInt(2)+1 == 2) ? (String) keys[r.nextInt(keys.length)] : radDeg.get(keys[(int) r.nextInt(keys.length)])) + ")";
			int layer2 = r.nextInt(2) + 1; //regular or inverse
			if(layer2 == 1) {
				return functions[r.nextInt(functions.length)] + "(" + ((r.nextInt(2)+1 == 2) ? (String) keys[r.nextInt(keys.length)] : radDeg.get(keys[(int) r.nextInt(keys.length)])) + ")";
			} else {
				String choice = invFunctions[r.nextInt(invFunctions.length)]; //sin/cos/tan or csc/sec/cot
				String neg = (r.nextInt(2)+1) == 1 ? "-" : "";
				switch(choice) {
					case "arcsin": 
					case "arccos": return choice + "(" + neg + funcAnswersSC[r.nextInt(funcAnswersSC.length)] + ")";
					case "arctan": return choice + "(" + neg + funcAnswersT[r.nextInt(funcAnswersT.length)] + ")";
					case "arcsec":
					case "arccsc": return choice + "(" + neg + invFuncAnswersSC[r.nextInt(invFuncAnswersSC.length)] + ")";
					case "arccot": return choice + "(" + neg + invFuncAnswersT[r.nextInt(invFuncAnswersT.length)] + ")";
				}
			}
		}
		return "lol spaghetti";
	}
	/**
	 * method for solving both user/ai-generated input
	 * @param s: string to parse and/or solve
	 * @return the solved string
	 */
	private static String solve(String s) {
		for(String st : KeyInput.zeroArray) {
			if(s.equals(st)) {
				return "0";
			}
		}
		for(String st : KeyInput.undefinedArray) {
			if(s.equals(st)) {
				return "0";
			}
		}
		String inFunction = "", function = "";
		if(Character.isDigit(s.charAt(0)) || s.startsWith("π")) {
			if(s.contains("π")) {
				return String.valueOf(radDeg.get(s));
			} else {
				for(String st : radDeg.keySet()) 
					if(radDeg.get(st).equals(s)) return st;
			}	
		} else {
			function = s.substring(0, s.indexOf("("));
			inFunction = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
			if(!(inFunction.contains("π") || function.contains("arc"))) {
				inFunction = String.valueOf(Math.toRadians(Double.valueOf(inFunction)));
			} else if(function.contains("arc") && inFunction.contains("/")) inFunction = "((" + inFunction.split("/")[0] + ")" + "/" + "(" + inFunction.split("/")[1] + "))";
			//System.out.println(inFunction);
			switch(function) { //compensate for "arc" and reciprocals not being in the system 
				case "sec": function = "1/cos"; break;
				case "csc": function = "1/sin"; break;
				case "cot": function = "1/tan"; break;
				case "arcsin": function = "asin"; break;
				case "arccos": function = "acos"; break;
				case "arctan": function = "atan"; break;
				case "arccsc": function = "asin"; inFunction = "1/" + inFunction; break;
				case "arcsec": function = "acos"; inFunction = "1/" + inFunction; break;
				case "arccot": function = "atan"; inFunction = "1/" + inFunction; break;
			}
		}
		String toSolve = (function + "(" + inFunction + ")").replaceAll("√", "sqrt").replaceAll("π", "pi");	
		Expression e = new ExpressionBuilder(toSolve).build(); //thanks maven
		
		/*
		System.out.println("original expression: " + s);
		System.out.println("toSolve: " + toSolve);
		System.out.println("solved: " + e.evaluate()); 
		*/
		
		return String.valueOf(e.evaluate());
	}
	
	static String toMS(int i) {
		int m = i/60;
		int s = i%60;
		String sec = (s <= 9) ? "0"+s : ""+s;
		return m + ":" + sec;
	}

	public void tick() {}
	
}
