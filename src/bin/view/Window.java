package bin.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame
{
    public Window(String title, JPanel contentPane)
    {
        setTitle(title);
        
        setContentPane(contentPane);
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        contentPane.requestFocus();
    }
}
