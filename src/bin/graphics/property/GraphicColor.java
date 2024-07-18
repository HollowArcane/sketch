package bin.graphics.property;

import bin.util.paint.PaintUtilities;
import java.awt.Color;

public class GraphicColor extends Property
{
    Color color;

    public GraphicColor(Color color)
    { this.color = color; }

    public GraphicColor(float r, float g, float b, float a)
    { this.color = new Color(r, g, b, a); }

    public Color color()
    { return color; }

    @Override
    public Property interpolate(Property end, float progression)
    {
        if(end instanceof GraphicColor gs)
        { return new GraphicColor(PaintUtilities.lerp(Math.clamp(progression, 0, 1f), color, gs.color)); }
        
        throw new IllegalArgumentException("Expecting GraphicColor: " + end);
    }

    @Override
    public String toString()
    { return "GraphicColor [color=" + color + "]"; }

    @Override
    protected void setCurrentValues(Property p)
    {
        if(p instanceof GraphicColor gc)
        { color = gc.color; }
    }
}
