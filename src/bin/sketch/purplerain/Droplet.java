package bin.sketch.purplerain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import bin.util.geometry.Vector2;

public class Droplet
{
    Vector2 position;
    float speed, opacity;
    int size;

    public Droplet(int x, int height, int size, float opacity)
    {
        position = new Vector2(x, 0);
        resetHeight(height);

        speed = 0;
        this.opacity = opacity;
        this.size = size;
    }

    public static Droplet random(int width, int height, int minSize, int maxSize, float opacity)
    {
        Random random = new Random();
        return new Droplet(random.nextInt(width), height, random.nextInt(minSize, maxSize), opacity);
    }

    public void update(int height)
    {
        speed += size / 10;
        position.y += speed;

        if(position.y > height)
        { resetHeight(height); }
    }

    public void resetHeight(int height)
    { position.y = -height; }

    public void paint(Graphics2D g, Color baseColor)
    {
        g.setColor(new Color(
            (float)baseColor.getRed()/255,
            (float)baseColor.getGreen()/255,
            (float)baseColor.getBlue()/255,
            opacity
        ));
        g.fillRoundRect((int)position.x, (int)position.y, size / 5, size, size / 10, size / 10);
    }
}
