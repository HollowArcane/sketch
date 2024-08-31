package bin.sketch.blockbreaker;

import java.awt.Color;
import java.util.ArrayList;

import bin.util.Maths;

public class Platform extends Brick
{
    private Level level;

    public Platform(int x, int y, int width, int height)
    { super(x, y, width, height, Color.RED); }

    public void setLevel(Level level)
    { this.level = level; }

    public void granted(Bonus b)
    {
        if(b.getType() == Bonus.Type.X2)
        {
            ArrayList<Ball> balls = level.getBalls();
            for(int i = 0, size = balls.size(); i < size; i++)
            { level.addBall(balls.get(i).x2()); }
        }

        else if(b.getType() == Bonus.Type.SIZE)
        { setShape(x() - 5, y(), width() + 10, height()); }

        level.removeBonus(b);
    }

    public void setX(int newX, int width)
    {
        double dx = Maths.constrain(newX, getShape().getBase() / 2, width - getShape().getBase() / 2) - getShape().getCenter().x;
        getShape().translate(dx, 0);
    }
}