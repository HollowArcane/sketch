package bin.graphics.property;

import bin.util.paint.PaintUtilities;
import java.awt.Color;

public class GraphicColor extends Property
{
    Color color;

    public GraphicColor(Color color)
    { this.color = color; }

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
}
