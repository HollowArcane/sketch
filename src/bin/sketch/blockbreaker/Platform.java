package bin.sketch.blockbreaker;

import java.awt.Color;

import bin.util.Maths;

public class Platform extends Brick
{
    public Platform(int x, int y, int width, int height)
    { super(x, y, width, height, Color.RED); }

    public void setX(int newX, int width)
    {
        double dx = Maths.constrain(newX, getShape().getBase() / 2, width - getShape().getBase() / 2) - getShape().center().x;
        getShape().translate(dx, 0);
    }
}