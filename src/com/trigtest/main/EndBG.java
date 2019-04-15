package com.trigtest.main;

import java.awt.*;
import java.io.File;

public class EndBG extends GameObject {

    private int gameMode;
    private Handler handler;

    EndBG(int x, int y, ID id, int gameMode, Handler handler) {
        super(x, y, id);
        this.gameMode = gameMode;
        this.handler = handler;
    }

    public void render(Graphics g) {
        Menu.drawEnd = false;
        Graphics2D g2d = (Graphics2D) g;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-credits.otf"));
            Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/math-menu-bold.otf"));
            ge.registerFont(font); ge.registerFont(font1);
            if(gameMode == 0) {
                drawEnd(g2d, font, font1);
            } else if(gameMode == 1) {
                handler.clearGame();
                g2d.setFont(font.deriveFont(32.0f));
                FontMetrics m = g2d.getFontMetrics();
                g2d.drawString("You scored a: ", Game.WIDTH/2 - m.stringWidth("You scored a: ")/2, 120);

                g2d.setFont(font1.deriveFont(42.0f));
                FontMetrics m1 = g2d.getFontMetrics();
                g2d.drawString(Problem.n_correct + " / " + Menu.amtOfQ, Game.WIDTH/2 - m1.stringWidth(Problem.count + " / " + Menu.amtOfQ)/2, 170);
            } else if(gameMode == 2) {
                //do i really want this?
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        Menu.backButton(g2d, 45, 528);
    }

    private void drawEnd(Graphics2D g2d, Font f, Font f1) {
        handler.clearGame();

        String endMessage = "In " + Problem.toMS(Menu.time) + ", you answered ";
        g2d.setFont(f.deriveFont(28.0f));

        FontMetrics m = g2d.getFontMetrics();
        g2d.drawString("Game Over!", Game.WIDTH/2 - m.stringWidth("Game Over!")/2, 80);
        g2d.drawString(endMessage, Game.WIDTH/2 - m.stringWidth(endMessage)/2, 120);

        g2d.setFont(f1.deriveFont(42.0f));
        FontMetrics m1 = g2d.getFontMetrics();
        g2d.drawString((Problem.n_correct + Problem.n_incorrect) + " questions", Game.WIDTH/2 - m1.stringWidth((Problem.n_correct + Problem.n_incorrect) + " questions")/2, 170);
        g2d.setFont(f.deriveFont(28.0f));
        g2d.drawString("with an accuracy of: ", Game.WIDTH/2 - m.stringWidth("with an accuracy of: ")/2, 240);
        g2d.setFont(f1.deriveFont(42.0f));
        g2d.drawString(Problem.n_correct + " / " + (Problem.n_correct + Problem.n_incorrect), Game.WIDTH/2 - m1.stringWidth(Problem.n_correct + " / " + (Problem.n_correct + Problem.n_incorrect))/2, 290);
        g2d.drawString("= " + (int) Math.ceil(((double) Problem.n_correct/(double) (Problem.n_correct + Problem.n_incorrect) * 100)) + "%", Game.WIDTH/2 - m1.stringWidth("= " + (int) Math.ceil(((double) Problem.n_correct/(double) (Problem.n_correct + Problem.n_incorrect) * 100)) + "%")/2, 330);
    }

    @Override
    public void tick() { }
}
