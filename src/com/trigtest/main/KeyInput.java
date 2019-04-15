package com.trigtest.main;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
//import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.trigtest.main.Game.STATE;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class KeyInput extends KeyAdapter {
	
	private Game game;
	private Handler handler;
	static Timer timer = new Timer();
	//static HashMap<String,String> wrongQA = new HashMap<String,String>();
	static String previousQuestion = ""; static String previousAnswer = ""; static String previousAnswer_1 = ""; static String correctAnswer = "";
	static Color color = new Color(0, 0, 0, 0);
	
	static String[] zeroArray = {"sin(0)", "tan(0)", "cos(90)", "cot(90)", "sin(180)", "tan(180)", "cot(270)", "cos(270)", "sin(360)", "tan(360)", "cos(π/2)", "cot(π/2)", "sin(π)", "tan(π)", "cot(3π/2)", "cos(3π/2)", "sin(2π)", "tan(2π)"};
	static String[] undefinedArray = {"csc(0)", "cot(0)", "tan(90)", "sec(90)", "csc(180)", "cot(180)", "tan(270)", "sec(270)", "csc(360)", "cot(360)", "tan(π/2)", "sec(π/2)", "csc(π)", "cot(π)", "tan(3π/2)", "sec(3π/2)", "csc(2π)", "cot(2π)"};
	
	public KeyInput(Game game, Handler handler){
		this.game = game;
		this.handler = handler;
	}
	
	static boolean dontPress = false;
	static int interval = 300; //default timer value set in Menu class

	public void keyPressed(KeyEvent e){
		//System.out.print(e.getKeyChar() + " ");
		if(game.gameState == STATE.Game) {
			if(!dontPress) {
				if(Problem.drawString && e.getKeyChar() == '/') Problem.printString += "/"; 
				else if(Problem.drawString && e.getKeyChar() == '-') Problem.printString += "-";
				if(Problem.drawString && Character.isLetterOrDigit(e.getKeyChar())) {
					if(Problem.printString.endsWith("sqr") && e.getKeyChar() == 't') {
						Problem.printString = Problem.printString.substring(0, Problem.printString.length() - 3) + "√";
					} else if(Problem.printString.endsWith("p") && e.getKeyChar() == 'i'){
						Problem.printString = Problem.printString.substring(0, Problem.printString.length() - 1) + "π";
					} else {
						Problem.printString += Character.toLowerCase(e.getKeyChar());	
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && Problem.printString.length() > 0) {
					Problem.printString = Problem.printString.substring(0, Problem.printString.length() - 1);
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(Problem.startTimer) startTheTimer();
					DecimalFormat d = new DecimalFormat("#.####");
					String toCompare = !(Problem.printString.matches(".*\\d+.*") || Problem.printString.equals("π")) ? Problem.printString : d.format(Double.valueOf(parseInput(Problem.printString.replaceAll("π", "pi").replaceAll("√", "sqrt"))));
					//System.out.println(Problem.solved);
					String solved = d.format(Problem.solved.contains("π") ? Double.valueOf(parseInput(Problem.solved.replaceAll("π", "pi").replaceAll("√", "sqrt"))) : Double.valueOf(Problem.solved.trim()));
					System.out.println("comparing: " + toCompare.trim() + " and " + solved.trim());
					for(String s : zeroArray) {
						if(Problem.newQuestion.equals(s)) {
							solved = "0";
						}
					}
					for(String s : undefinedArray) {
						if(Problem.newQuestion.equals(s)) {
							solved = "undefined";
						}
					}
					if(toCompare.equalsIgnoreCase(solved)) {
						Problem.interpretAnswer = true;
					} else {
						Problem.interpretAnswer = false;	
						Problem.correctString = "The correct answer was: " + findCorrectSolution(solved);
					}
					if(Problem.interpretAnswer) {
						++Problem.n_correct;
						if(Menu.sound)
							AudioPlayer.getSound("pass").play();
					} else {
						++Problem.n_incorrect;
						if(Menu.sound)
							AudioPlayer.getSound("fail").play();
					}
					
					dontPress = true;
					
					if(Menu.mode != 0) {
						new Timer().schedule(
							new TimerTask() {
								public void run() { 
									if(Menu.mode == 1 && Problem.count == Menu.amtOfQ) { //if accuracy mode and reached amount of questions
										game.gameState = STATE.End;
										handler.clearGame();
									}
									dontPress = false;
									Problem.generateNewQuestion = true;
									Problem.printString = "";
									Problem.correctString = "";
									Problem.image = null;
								}
							}, 1500
						);	
					} else {
						previousQuestion = Problem.newQuestion;
						previousAnswer = Problem.printString; 
						previousAnswer_1 = " was " + (Problem.interpretAnswer ? "correct" : "incorrect");
						correctAnswer = Problem.interpretAnswer ? "" : "Ans: " + findCorrectSolution(solved);
						if(Problem.interpretAnswer) color = new Color(0, 255, 0, 50); //green	
						else color = new Color(255, 0, 0, 50); //red
						dontPress = false;
						Problem.generateNewQuestion = true;
						Problem.printString = "";
						Problem.correctString = "";
						Problem.image = null;
					}
				} 
			}	
		}
	}
	
	private String parseInput(String s) {
		if(s.isEmpty()) return "-300";
		else if(Character.isDigit(s.charAt(0)) && !s.contains("pi") && !s.contains("sqrt") && !s.contains("/")) return s;
		if(s.contains("/")) s = "(" + s.split("/")[0] + ")" + "/" + s.split("/")[1];
		Expression e = new ExpressionBuilder(s).build();
		return String.valueOf(e.evaluate());
	}
	
	private void startTheTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println(setInterval());
			}
		}, 1000, 1000);
	}
	
	private int setInterval() {
		if(interval == 1) {
			game.gameState = STATE.End;
			timer.cancel(); //end game
		}
		return --interval;
	}
	
	//this is literally a table of values lol
	private String findCorrectSolution(String s) {
		switch(s) {
			case "0.5": return "1/2";
			case "0.7071": return "1/√2 or √2/2";
			case "0.866": return "√3/2";
			case "0.5774": return "1/√3 or √3/3";
			case "1.7321": return "3/√3 or √3";
			case "1.4142": return "2/√2 or √2";
			case "1.1547": return "2/√3 or (2√3)/3";
			case "0.5236": return "π/6";
			case "0.7854": return "π/4";
			case "1.0472": return "π/3";
			case "1.5708": return "π/2";
			case "2.0944": return "2π/3";
			case "2.3562": return "3π/4";
			case "2.618": return "5π/6";
			case "3.1416": return "π";
			case "3.6652": return "7π/6";
			case "3.927": return "5π/4";
			case "4.1888": return "4π/3";
			case "4.7124": return "3π/2";
			case "5.236": return "5π/3";
			case "5.4978": return "7π/4";
			case "5.7596": return "11π/6";
			case "6.2832": return "2π";
			case "-0.5": return "-1/2";
			case "-0.7071": return "-1/√2 or √2/2";
			case "-0.866": return "-√3/2";
			case "-0.5774": return "-1/√3 or -√3/3";
			case "-1.7321": return "-3/√3 or -√3";
			case "-1.4142": return "-2/√2 or -√2";
			case "-1.1547": return "-2/√3 or -(2√3)/3";
			case "-0.5236": return "-π/6";
			case "-0.7854": return "-π/4";
			case "-1.0472": return "-π/3";
			default: return s;
		}
	}
}
