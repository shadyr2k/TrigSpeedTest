package com.trigtest.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FadingImage implements ActionListener {

    private Image image;

    private final int time = 500; //in ms
    private float alpha = 1f;
    private Timer timer = new Timer(time, this);

    public FadingImage(Image image){
        this.image = image;
    }

    public void drawFadingImage(Graphics2D g2d){
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, 0, 0, null);
    }

    public void startFade(){
        timer.start();
    }

    public void actionPerformed(ActionEvent e){
        alpha -= 0.1f;
        if(alpha <= 0) {
            timer.stop();
            alpha = 0;
        }
        System.out.println(alpha);
    }

}
