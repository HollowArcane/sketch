package bin.sketch.starfield;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import bin.util.Maths;
import bin.util.paint.PaintUtilities;

public class Star
{
    private static final Random RANDOM = new Random();
    private float x, y, z;
    private float px, py;
    private Color color;

    public Star(int width, int height)
    {
        color = Color.getHSBColor(RANDOM.nextFloat(), .2f, 1);
        reset(width, height);
        z = RANDOM.nextFloat(width);
    }

    public void update(int width, int height, int speed)
    {
        z -= speed;
        
        if(z < 1)
        { reset(width, height); }
    }

    public void reset(int width, int height)
    {
        x = RANDOM.nextFloat(-width/2, width/2);
        px = x;
        y = RANDOM.nextFloat(-height/2, height/2);
        py = y;
        z = width;
    }

    public void paint(Graphics2D g, int width, int height)
    {
        g.setColor(color);
        int sx = (int)(width * x / z),
            sy = (int)(height * y / z),
            r = (int)Maths.map(z, width, 0, 0, 6);
        g.drawLine((int)sx, (int)sy, (int)px, (int)py);
        PaintUtilities.fillCircle(g, sx, sy, r);

        px = sx;
        py = sy;
    }
}
