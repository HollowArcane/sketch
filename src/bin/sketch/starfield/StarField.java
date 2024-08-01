package bin.sketch.starfield;

import java.awt.Graphics2D;

import bin.util.Arrays;

public class StarField
{
    Star[] stars;
    int width, height;
    int speed;

    public StarField(int n, int width, int height)
    {
        stars = Arrays.fill(new Star[n], i -> new Star(width, height));
        this.width = width;
        this.height = height;
        speed = 1;
    }

    public void setSpeed(int newSpeed)
    { speed = newSpeed; }

    public void update()
    {
        for (Star star : stars)
        { star.update(width, height, speed); }
    }

    public void paint(Graphics2D g)
    {
        update();
        g.translate(width/2, height/2);

        for (Star star : stars)
        { star.paint(g, width, height); }
    }
}
