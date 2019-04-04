package com.trigtest.main;

import java.awt.*;

public class Page {
    private int page;
    private Graphics2D g2d;

    public Page(Graphics2D g2d, int page){
        this.page = page;
        this.g2d = g2d;
    }
    public void drawPage(){
        switch(page){
            case 0: page0(g2d); break;
            case 1: page1(g2d); break;
            case 2: page2(g2d); break;
            case 3: page3(g2d); break;
            case 4: page4(g2d); break;
        }
    }

    private void page0(Graphics2D g2d){
        g2d.drawString("Welcome to \"Don't get Triggered\"!", 60, 50);
    }
    private void page1(Graphics2D g2d){

    }
    private void page2(Graphics2D g2d){

    }
    private void page3(Graphics2D g2d){

    }
    private void page4(Graphics2D g2d) {

    }
}
