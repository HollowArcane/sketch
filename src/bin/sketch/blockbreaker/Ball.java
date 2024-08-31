package bin.sketch.blockbreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import bin.util.Maths;
import bin.util.geometry.Circle;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;

public class Ball
{
    private Circle shape;
    private Vector2 velocity;

    private Level level;

    public Ball(int x, int y, int size)
    {
        shape = new Circle(new Vector2(x, y), size);
        velocity = new Vector2(0, 4);
    }

    public void setLevel(Level level)
    { this.level = level; }

    public void update(Platform platform, ArrayList<Brick> bricks, int width, int height)
    {
        if(!checkBricks(bricks))
        { shape.translate(velocity); }
        
        checkSides(width, height);
        checkPlatform(platform);
    }

    private boolean isLucky()
    { return Math.random() < 0.1; }

    private void checkSides(int width, int height)
    {
        Vector2 tl = Vector2.zero()          , tr = new Vector2(width, 0)   ,
                bl = new Vector2(0, height), br = new Vector2(width, height);

        if(shape.intersectsSegment(tl, bl) || shape.intersectsSegment(tr, br))
        { velocity.x *= -1; }

        if(shape.intersectsSegment(tl, tr))
        { velocity.y *= -1; }
    }

    private boolean checkBricks(ArrayList<Brick> bricks)
    {
        int size = bricks.size();
        for(int i = 0; i < bricks.size(); i++)
        {
            double dx = velocity.x;
            shape.translate(dx, 0);
            if(shape.intersects(bricks.get(i).getShape()))
            {
                if(isLucky())
                { level.addBonus(bricks.get(i).spawnBonus()); }

                bricks.remove(i--);
                velocity.x *= -1;
                return true;
            }
            shape.translate(-dx, 0);

            double dy = velocity.y;
            shape.translate(0, dy);
            if(shape.intersects(bricks.get(i).getShape()))
            {
                if(isLucky())
                { level.addBonus(bricks.get(i).spawnBonus()); }

                bricks.remove(i--);
                velocity.y *= -1;
                return true;
            }
            shape.translate(0, -dy);
        }
        return false;
    }

    private void checkPlatform(Platform platform)
    {
        if(touches(platform))
        {
            shape.center().y = platform.getShape().getCenter().y - platform.getShape().getHeight() / 2 - shape.radius;
            velocity.y *= -1;
            velocity.rotate(-Maths.constrain(Math.acos(2*(shape.center().x - platform.x() - platform.width()/2)/platform.width()), Math.PI/6, 5*Math.PI/6) - velocity.angle());
        }
    }

    public int x()
    { return (int)(shape.center().x - shape.radius); }

    public int y()
    { return (int)(shape.center().y - shape.radius); }

    public Ball x2()
    {
        double misdirection = Math.PI / 6;
        Vector2 velocity = this.velocity.copy();

        this.velocity.rotate(misdirection);
        Ball newBall = new Ball((int)(x() + shape.radius), (int)(y() + shape.radius), (int)shape.radius);
        newBall.velocity = velocity;
        return newBall;
    }

    public boolean touches(Platform p)
    { return p.getShape().intersects(shape); }

    public void paint(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        PaintUtilities.fillCircle(g, (int)shape.center().x, (int)shape.center().y, (int)shape.radius);
    }
}
