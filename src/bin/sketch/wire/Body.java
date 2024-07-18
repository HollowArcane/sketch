package bin.sketch.wire;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.Arrays;

import bin.util.geometry.Line;
import bin.util.geometry.Vector2;
import bin.util.paint.PaintUtilities;

public class Body
{
    Wire spine;
    private Vector2 direction;
    public float tension = 5f;

    private Color strokeColor;
    private Color fillColor;

    public Body(Wire spine, Vector2 direction)
    {
        this.spine = spine;
        this.direction = direction;

        strokeColor = Color.BLACK;
        fillColor = Color.RED;
    }

    public void move(int dx, int dy)
    {
        spine.moveJoint(0, dx, dy);
        if(dx != 0 && dy != 0)
        { direction.set(dx, dy); }
    }

    public static Body read(Point origin, Integer[] jointSizes)
    { return new Body(Wire.read(origin, jointSizes), Vector2.ihat().scale(-1)); }

    public Vector2 getLeftPoint(Joint j, Vector2 d1)
    { return d1.normal().normalize().scale(j.size()).add(j.position()); }

    public Vector2 getRightPoint(Joint j, Vector2 d1)
    { return d1.normal().normalize().scale(-j.size()).add(j.position()); }

    public Vector2[] getLeftCurvature(Joint j1, Vector2 d1, Joint j2, Vector2 d2)
    {
        Vector2 l1 = getLeftPoint(j1, d1.copy());
        Vector2 l2 = getLeftPoint(j2, d2.copy());

        return new Vector2[] {
            l1, Vector2.add(l1, d1.normalize().scale(tension)),
            Vector2.sub(l2, d2.normalize().scale(tension)), l2
        };
    }

    public Vector2[] getRightCurvature(Joint j1, Vector2 d1, Joint j2, Vector2 d2)
    {
        Vector2 l1 = getRightPoint(j1, d1.copy());
        Vector2 l2 = getRightPoint(j2, d2.copy());

        return new Vector2[] {
            l1, Vector2.add(l1, d1.normalize().scale(tension)),
            Vector2.sub(l2, d2.normalize().scale(tension)), l2
        };
    }

    public Vector2 getJointDirection(int joint)
    {
        if(joint < 0 || joint > spine.joints.length)
        { return null; }

        if(joint == 0)
        { return direction.copy(); }

        return Vector2.sub(
            spine.joints[joint - 1].position(),
            spine.joints[joint].position()
        );
    }

    public Path2D getOutline()
    {
        Path2D.Float outline = new Path2D.Float();
        
        Joint[] joints = spine.joints;

        Vector2 point = getLeftPoint(joints[0], getJointDirection(0));
        outline.moveTo(point.x, point.y);
        for(int i = 1; i < joints.length; i++)
        {
            Vector2[] points = getLeftCurvature(joints[i], getJointDirection(i), joints[i - 1], getJointDirection(i - 1));
            outline.curveTo(points[2].x, points[2].y, points[1].x, points[1].y, points[0].x, points[0].y);
        }
        
        Joint lastJoint = joints[spine.joints.length - 1];
        Vector2 lastDirection = getJointDirection(spine.joints.length - 1);

        Vector2 base = Vector2.normalize(lastDirection).normalize().scale(lastJoint.size());
        point = Vector2.rotate(base, 5*Math.PI/6).add(lastJoint.position());
        outline.lineTo(point.x, point.y);
        point = Vector2.rotate(base, -5*Math.PI/6).add(lastJoint.position());
        outline.lineTo(point.x, point.y);

        point = getRightPoint(lastJoint, lastDirection);
        outline.lineTo(point.x, point.y);

        for(int i = joints.length - 1; i > 0; i--)
        {
            Vector2[] points = getRightCurvature(joints[i], getJointDirection(i), joints[i - 1], getJointDirection(i - 1));
            outline.curveTo(points[1].x, points[1].y, points[2].x, points[2].y, points[3].x, points[3].y);
        }

        Joint firstJoint = joints[0];
        Vector2 firstDirection = getJointDirection(0);

        base = Vector2.normalize(firstDirection).normalize().scale(firstJoint.size());
        point = Vector2.rotate(base, -Math.PI/6).add(firstJoint.position());
        outline.lineTo(point.x, point.y);
        point = Vector2.rotate(base, Math.PI/6).add(firstJoint.position());
        outline.lineTo(point.x, point.y);

        outline.closePath();
        return outline;
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawString(tension + "", 100, 100);
        Path2D outline = getOutline();

        Graphics2D gc = (Graphics2D)g2.create();
        gc.setStroke(new BasicStroke(3));
        gc.setColor(fillColor);
        gc.fill(outline);

        gc.setColor(strokeColor);
        gc.draw(outline);

        Joint firstJoint = spine.joints[0];
        Vector2 firstDirection = getJointDirection(0);

        Vector2 base = Vector2.normalize(firstDirection).normalize().scale(firstJoint.size());
        Vector2 point = Vector2.rotate(base, 4 * Math.PI / 6).scale(1f).add(firstJoint.position());
        gc.drawOval((int)point.x - 2, (int)point.y - 2, 4, 4);
        
        point = Vector2.rotate(base, -4 * Math.PI / 6).scale(1f).add(firstJoint.position());
        gc.drawOval((int)point.x - 2, (int)point.y - 2, 4, 4);
        

        // spine.draw(g2);
    }
}
