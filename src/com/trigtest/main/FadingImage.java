package com.trigtest.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FadingImage {

    private Image image;

    private final long fadeTime = 1000;
    private float alpha = 1f;
    private long startTime = -1;

    Timer timer = new Timer(40, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(startTime < 0) startTime = System.currentTimeMillis();
            else {
                long time = System.currentTimeMillis();
                long timeElapsed = time - startTime;
                if(timeElapsed >= fadeTime){
                    startTime = -1;
                    ((Timer) e.getSource()).stop();
                    alpha = 0f;
                    Menu.fadeConfetti = false;
                } else
                    alpha = 1f - ((float) timeElapsed / (float) fadeTime);
            }
        }
    });

    public FadingImage(Image image){
        this.image = image;
    }

    public void drawFadingImage(Graphics2D g2d){
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, 0, 0, null);
    }

    public void fadeImage(){
        timer.start();
    }
}
