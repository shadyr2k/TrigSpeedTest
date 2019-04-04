package com.trigtest.main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Page {
    private int page;
    private Graphics2D g2d;

    private ArrayList<Image> picList = new ArrayList<>();

    public Page(Graphics2D g2d, int page){
        this.page = page;
        this.g2d = g2d;
    }
    public void drawPage(){
        for(int i = 0; i <= 5; ++i){
            picList.add(resize(new ImageIcon("assets/pages/" + i + ".png").getImage(), 634, 641));
        }
        switch(page){
            case 0: g2d.drawImage(picList.get(0), 0, 0, null); break;
            case 1: g2d.drawImage(picList.get(1), 0, 0, null); break;
            case 2: g2d.drawImage(picList.get(2), 0, 0, null); break;
            case 3: g2d.drawImage(picList.get(3), 0, 0, null); break;
            case 4: g2d.drawImage(picList.get(4), 0, 0, null); break;
            case 5: g2d.drawImage(picList.get(5), 0, 0, null); break;
        }
    }

    private Image resize(Image i, int w, int h) {
        BufferedImage r = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = r.createGraphics();
        g2d.drawImage(i, 0, 0, w, h, null);
        g2d.dispose();
        return r;
    }
}
