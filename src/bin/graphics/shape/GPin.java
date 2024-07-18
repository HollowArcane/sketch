package bin.graphics.shape;

import bin.util.geometry.Vector2;

public class GPin
{
    private GPath pin;
    private float position;

    public void pin(GPath path, float position)
    {
        this.pin = path;
        this.position = position;
    }

    public GPath path()
    { return pin; }

    public Vector2 center()
    { 
        if(pin == null)
        { return null; }

        return pin.p(position);
    }

    public Vector2 direction()
    { 
        if(pin == null)
        { return null; }

        return pin.v(position);
    }
}
