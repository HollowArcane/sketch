package bin.sketch.wire;

import bin.graphics.shape.GCircle;
import bin.util.geometry.Vector2;

import java.awt.Graphics2D;
import java.awt.Point;

public class Joint
{
    private int size;
    private int constraintRadius;
    private double constraintAngle = 17*Math.PI/20;

    GCircle circle;
    
    public Joint(Vector2 position, int size)
    { this(position, size, 50); }

    public Joint(Vector2 position, int size, int constraintRadius)
    {
        this.size = size;
        circle = new GCircle(position.toPoint(), size);
        this.constraintRadius = constraintRadius;
    }

    public Vector2 position()
    { return new Vector2(circle.center()); }

    public void move(int dx, int dy)
    { circle.center().setLocation(circle.center().x + dx, circle.center().y + dy); } 

    public int size()
    { return size; }

    public void constrain(Joint j)
    {
        Vector2 jp = j.position(), p = position();
        jp.sub(p).scale((float)constraintRadius / jp.mag()).add(p);
        j.circle.p.setLocation(jp.toPoint());
    }

    public void constrainAngle(Joint j1, Joint j2)
    {
        Vector2 position = position();
        double angle = j2.position().sub(position).angle() - j1.position().sub(position).angle();
        while(angle < 0)
        { angle += 2*Math.PI; }

        if(angle < constraintAngle)
        { j1.circle.p.setLocation(j1.position().rotate(-constraintAngle + angle, position).toPoint()); }

        if(angle > 2*Math.PI - constraintAngle)
        { j1.circle.p.setLocation(j1.position().rotate(-2*Math.PI + constraintAngle + angle, position).toPoint()); }
    }

    public void draw(Graphics2D g)
    { circle.paint(g); }
}
