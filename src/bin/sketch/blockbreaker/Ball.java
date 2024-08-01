package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bin.util.geometry.Circle;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;

public class Ball
{
    private Circle shape;
    private Vector2 velocity;

    public Ball(int x, int y, int size)
    {
        shape = new Circle(new Vector2(x, y), size);
        velocity = new Vector2(1, 2);
    }

    public void update(Platform platform, ArrayList<Brick> bricks, int width, int height)
    {
        shape.translate(velocity);
        
        Vector2 tl = Vector2.zero()          , tr = new Vector2(width, 0)   ,
                bl = new Vector2(0, height), br = new Vector2(width, height);

        if(shape.intersectsSegment(tl, bl) || shape.intersectsSegment(tr, br))
        { velocity.x *= -1; }

        if(shape.intersectsSegment(tl, tr))
        { velocity.y *= -1; }

        if(touches(platform))
        {
            shape.center().y = platform.getShape().center().y - platform.getShape().getHeight() / 2 - shape.radius;
            velocity.y *= -1;
        }
    }

    public boolean touches(Platform p)
    { return p.getShape().intersects(shape); }

    public void paint(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        PaintUtilities.fillCircle(g, (int)shape.centerX(), (int)shape.centerY(), (int)shape.radius);
    }
}
