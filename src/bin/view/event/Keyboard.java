package bin.view.event;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter
{
    public static interface KeyHandler
    { public void handle(Keyboard m); }

    private boolean[] keys;

    // events
    private KeyHandler press;
    private KeyHandler release;

    // attribute to store last key event;
    private KeyEvent event;

    public Keyboard()
    { keys = new boolean[256]; }

    public KeyEvent details()
    { return event; }

    public boolean key(int keycode)
    { return keycode > 0 && keycode < keys.length && keys[keycode]; }

    public void press(KeyHandler handler)
    { this.press = handler; }

    public void release(KeyHandler handler)
    { this.release = handler; }

    @Override
    public void keyPressed(KeyEvent e)
    {
        keys[e.getKeyCode()] = true;
        if(press != null)
        { press.handle(this); }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        keys[e.getKeyCode()] = false;
        if(release != null)
        { release.handle(this); }
    }
}
