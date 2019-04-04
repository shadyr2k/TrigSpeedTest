package com.trigtest.main;

import javax.swing.*;
import java.awt.*;

public class FadingImage {

    private Image image;

    private float alpha = 1f;

    public FadingImage(Image image){
        this.image = image;
    }

    public void drawFadingImage(Graphics2D g2d){
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(image, 0, 0, null);
    }

    public void delFadingImage(){
        image = new ImageIcon("assets/blank_.png").getImage();
    }

    public void decrAlpha(Float f){
        alpha -= f;
        System.out.println(alpha);
    }

    public Float getAlpha(){
        return alpha;
    }

}
