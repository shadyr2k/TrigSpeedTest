package com.trigtest.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FadingImage {

    private Image image;

    private final long fadeTime = 1000;
    private float alpha = 0f;
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
                    Menu.drawConfetti = false;
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

    public void decrAlpha(Float f){
        while(Float.compare(f, 0f) > 0) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            alpha -= f;
                        }
                    }, 500
            );
            System.out.println(alpha);
        }
        delFadingImage();
    }

    private void delFadingImage(){
        image = new ImageIcon("assets/blank_.png").getImage();
    }

    public Float getAlpha(){ return alpha; }

    public void fadeImage(){
        timer.start();
    }
}
