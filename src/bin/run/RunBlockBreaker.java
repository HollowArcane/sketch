package bin.run;

import java.awt.Color;

import bin.sketch.blockbreaker.Level;
import bin.view.processing.Canvas;

public class RunBlockBreaker
{
    static int width, height;
    static Canvas canvas;
    
    static Level level;

    public static void main(String[] args)
    {
        width = 1280;
        height = 720;

        canvas = new Canvas(width, height);
        canvas.setBackground(Color.BLACK);
        level = new Level(width, height);

        canvas.mouse().move(mouse -> level.getPlatform().setX(mouse.x(), width));

        canvas.draw(g -> {
            level.paint(g);
            level.update();
        });

        canvas.display("Block Breaker");    
    }
}
