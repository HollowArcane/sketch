package bin.view.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Mouse extends MouseAdapter
{
    public static interface MouseHandler
    { public void handle(Mouse m); }

    // events
    private MouseHandler wheel;
    private MouseHandler click;
    private MouseHandler press;
    private MouseHandler release;
    private MouseHandler move;
    private MouseHandler drag;

    // attribute to store lastest window event
    private MouseEvent event;

    // current mouse position
    private int x;
    private int y;

    // previous mouse position
    private int px;
    private int py;

    // mouse anchor position
    private int anchorX;
    private int anchorY;

    private boolean pressed;

    private Object record;

    public Mouse()
    { pressed = false; }

    private void handleAnchor(int newX, int newY)
    {
        anchorX = newX;
        anchorY = newY;
    }

    private void handleMove(int newX, int newY)
    {
        px = x;
        py = y;
        x = newX;
        y = newY;
    }

    public int x()
    { return x; }

    public int y()
    { return y; }

    public int dx()
    { return x - px; }

    public int dy()
    { return y - py; }

    public float speed()
    { return (float)Math.sqrt(Math.pow(dx(), 2) + Math.pow(dy(), 2)); }

    public void record(Object o)
    { this.record = o; }

    public Object record()
    { return record; }

    public void unrecord()
    { this.record = null; }

    public int previousX()
    { return px; }

    public int previousY()
    { return py; }

    public int anchorX()
    { return anchorX; }

    public int anchorY()
    { return anchorY; }

    public boolean isPressed()
    { return pressed; }

    public MouseEvent details()
    { return event; }

    public void wheel(MouseHandler handler)
    { this.wheel = handler; }

    public void click(MouseHandler handler)
    { this.click = handler; }

    public void press(MouseHandler handler)
    { this.press = handler; }

    public void release(MouseHandler handler)
    { this.release = handler; }

    public void move(MouseHandler handler)
    { this.move = handler; }

    public void drag(MouseHandler handler)
    { this.drag = handler; }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        this.event = e;
        if(wheel != null)
        { wheel.handle(this); }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        this.event = e;
        if(click != null)
        { click.handle(this); }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        this.event = e;
        handleMove(e.getX(), e.getY());
        if(drag != null)
        { drag.handle(this); }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        this.event = e;
        handleMove(e.getX(), e.getY());
        if(move != null)
        { move.handle(this); }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        this.event = e;
        pressed = true;
        handleAnchor(e.getX(), e.getY());

        if(press != null)
        { press.handle(this); }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        this.event = e;
        pressed = false;
        if(release != null)
        { release.handle(this); }
    }
}
