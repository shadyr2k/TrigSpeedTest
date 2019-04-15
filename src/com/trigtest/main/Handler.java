package com.trigtest.main;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {

	private LinkedList<GameObject> object = new LinkedList<>();
	void tick(){
		for(GameObject o : object){
			o.tick();
		}
	}
	
	void render(Graphics g){
		for (GameObject o : object) {
			o.render(g);
		}
	}
	
	void addObject(GameObject obj){
		this.object.add(obj);
	}
	
	public void removeObject(GameObject obj){
		this.object.remove(obj);
	}

	//cant remove wtf
	void clearGame() {
		for(int i = 0; i < object.size(); ++i) {
			GameObject g = object.get(i);
			if(g.getID() == ID.Problem) {
				object.remove(i);
			}
		}
	}
	void clearEndBG() {
		for(int i = 0; i < object.size(); ++i) {
			GameObject g = object.get(i);
			if(g.getID() == ID.EndBG) {
				object.remove(i);
			}
		}
	}
}
