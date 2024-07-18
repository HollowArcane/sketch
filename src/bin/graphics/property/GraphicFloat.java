package bin.graphics.property;

import bin.util.Maths;

public class GraphicFloat extends Property
{
    private float value;

    public GraphicFloat(float value)
    { this.value = value; }

    public float get()
    { return value; }

    @Override
    public Property interpolate(Property end, float value)
    {
        if(end instanceof GraphicFloat gs)
        { return new GraphicFloat((float)Maths.lerp(value, this.value, gs.value)); }
        
        throw new IllegalArgumentException("Expecting GraphicsStoke: " + end);
    }

    @Override
    public String toString()
    { return "GraphicTrace [value=" + value + "]"; }

    @Override
    protected void setCurrentValues(Property p)
    {
        if(p instanceof GraphicFloat gf)
        { value = gf.value; }
    }
}
