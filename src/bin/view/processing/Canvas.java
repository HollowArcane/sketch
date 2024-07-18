package bin.view.processing;

import javax.swing.JPanel;

import bin.view.Window;
import bin.view.event.Keyboard;
import bin.view.event.Mouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Canvas extends JPanel
{
    public static interface CanvasHandler
    { public void handle(Graphics g); }

    private Mouse mouse;
    private Keyboard keyboard;

    private CanvasHandler draw;

    private int width;
    private int height;

    public Canvas()
    { this(600, 600); }

    public Canvas(int width, int height)
    {
        setLayout(new BorderLayout());

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(width, height));

        this.width = width;
        this.height = height;

        mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);

        keyboard = new Keyboard();
        addKeyListener(keyboard);
    }

    public Window display(String title)
    {
        Window window =  new Window(title, this);
        return window;
    }

    public void draw(CanvasHandler handler)
    { this.draw = handler; }

    public Mouse mouse()
    { return mouse; }

    public Keyboard keys()
    { return keyboard; }

    public int width()
    { return width; }

    public int height()
    { return height; }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(draw != null)
        { draw.handle(g); }
        
        repaint();
    }
}