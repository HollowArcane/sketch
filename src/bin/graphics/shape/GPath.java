package bin.graphics.shape;

import java.awt.Color;

import bin.graphics.property.GraphicColor;
import bin.graphics.property.GraphicStroke;
import bin.graphics.property.GraphicFloat;
import bin.util.geometry.Vector2;

public abstract class GPath
{
    GraphicStroke stroke;
    GraphicColor color;
    GraphicFloat trace;

    public GPath(Color color, float width)
    {
        this.color = new GraphicColor(color);
        this.stroke = new GraphicStroke(width);
        this.trace = new GraphicFloat(1f);
    }

    public GPath(Color color, float width, float[] dash)
    {
        this.color = new GraphicColor(color);
        this.stroke = new GraphicStroke(width, dash);
        this.trace = new GraphicFloat(1f);
    }

    public abstract Vector2 p(float t);
    public abstract Vector2 v(float t);
}
