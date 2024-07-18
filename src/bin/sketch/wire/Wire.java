package bin.sketch.wire;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import bin.graphics.shape.GJoint;
import bin.util.Arrays;
import bin.util.geometry.Vector2;

public class Wire
{
    Joint[] joints;

    ArrayList<GJoint> rods;

    public Wire(Joint... joints)
    {
        this.joints = joints;
        rods = new ArrayList<>(joints.length - 1);
        for(int i = 1; i < joints.length; i++)
        { rods.add(new GJoint(joints[i-1].circle, joints[i].circle)); }
    }

    public static Wire read(Point origin, Integer[] jointSizes)
    {
        if(jointSizes == null || jointSizes.length == 0)
        { return null; }

        int cr = 20;

        Joint[] joints = new Joint[jointSizes.length];
        for(int i = 0; i < joints.length;  i++)
        { joints[i] = new Joint(new Vector2(origin.x + i * cr, origin.y), jointSizes[i], cr); }
        
        return new Wire(joints);
    }

    public int getJointAt(int x, int y)
    { 
        return  Arrays.ifirst(
            joints,
            (_, j) -> new Vector2(x, y).distance2(j.position()) < j.size() * j.size()
        );
    }

    public void moveJoint(int i, int dx, int dy)
    {
        if(i < 0 || i >= joints.length)
        { return; }

        joints[i].move(dx, dy);

        for(int j = i - 1; j >= 0; j--)
        { 
            joints[j + 1].constrain(joints[j]);
            if(j < joints.length + 2)
            { joints[j + 1].constrainAngle(joints[j], joints[j + 2]); }
        }

        for(int j = i + 1; j < joints.length; j++)
        {
            joints[j - 1].constrain(joints[j]);
            if(j >= 2)
            { joints[j - 1].constrainAngle(joints[j], joints[j - 2]); }
        }
    }

    public void draw(Graphics g)
    {
        for(Joint j: joints)
        { j.draw((Graphics2D)g); }

        for(GJoint r: rods)
        { r.paint((Graphics2D)g); }
    }
}
