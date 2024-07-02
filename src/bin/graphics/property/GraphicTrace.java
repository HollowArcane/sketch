package bin.graphics.property;

import bin.util.Maths;

public class GraphicTrace extends Property
{
    private float progression;

    public GraphicTrace(float progression)
    { this.progression = progression; }

    public float get()
    { return progression; }

    @Override
    public Property interpolate(Property end, float progression)
    {
        if(end instanceof GraphicTrace gs)
        { return new GraphicTrace((float)Maths.lerp(progression, this.progression, gs.progression)); }
        
        throw new IllegalArgumentException("Expecting GraphicsStoke: " + end);
    }

    @Override
    public String toString()
    { return "GraphicTrace [progression=" + progression + "]"; }
}
