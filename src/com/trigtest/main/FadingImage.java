package com.trigtest.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FadingImage {

    private Image image;

    private float alpha = 1f;
    private long startTime = -1;

    private Timer timer = new Timer(40, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(startTime < 0) startTime = System.currentTimeMillis();
            else {
                long time = System.currentTimeMillis();
                long timeElapsed = time - startTime;
                long fadeTime = 1000;
                if(timeElapsed >= fadeTime){
                    startTime = -1;
                    ((Timer) e.getSource()).stop();
                    alpha = 0f;
                    image = new ImageIcon("assets/blank_.png").getImage();
                    Menu.fadeConfetti = false;
                } else
                    alpha = 1f - ((float) timeElapsed / (float) fadeTime);
            }
        }
    });

    FadingImage(Image image){
        this.image = image;
    }

    void drawFadingImage(Graphics2D g2d){
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, 0, 0, null);
        System.out.println("drawing image");
    }

    void fadeImage(){
        timer.start();
    }
}
