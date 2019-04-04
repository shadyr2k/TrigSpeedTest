package com.trigtest.main;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	public void tick(){
		for(int i = 0; i < object.size(); ++i){
			GameObject temp = object.get(i);
			temp.tick();
		}
	}
	
	public void render(Graphics g){
		for(int i = 0; i < object.size(); ++i){
			GameObject temp = object.get(i);
			temp.render(g);
		}
	}
	
	public void addObject(GameObject obj){
		this.object.add(obj);
	}
	
	public void removeObject(GameObject obj){
		this.object.remove(obj);
	}
	
	public void clearGame() {
		for(int i = 0; i < object.size(); ++i) {
			GameObject g = object.get(i);
			if(g.getID() == ID.Problem) {
				object.remove(i);
			}
		}
	}
}
