package bin.sketch.packing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bin.util.geometry.Parallelogram;
import bin.util.geometry.Vector2;
import bin.view.processing.Canvas;

public class EntryPanelSimple extends JPanel
{
    public EntryPanelSimple(double width, double height, JFrame frame, Canvas canvas, ShapeBox2D box)
    {
        setPreferredSize(new Dimension((int)width, (int)height));
        setLayout(new GridBagLayout());

        JPanel form = new JPanel(new BorderLayout(20, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(50, 100, 50, 100));

        JLabel title = new JLabel("Insertion");
        title.setFont(new Font("sansserif", Font.BOLD, 24));

        JPanel inputWrap = new JPanel(new GridLayout(2, 1, 20, 20));
        inputWrap.setBackground(Color.WHITE);

        createFields(inputWrap);

        JPanel buttonWrap = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnSubmit = new JButton("Valider");
        btnSubmit.addActionListener(e -> {
            addShape(inputWrap, box);
            JOptionPane.showMessageDialog(null, "Insertion effectué avec succès", "Message", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnDisplay = new JButton("Afficher");
        btnDisplay.addActionListener(e -> {
            new Thread(() ->  box.fitBFDH(width, height, 100)).start();
            frame.setContentPane(canvas);
            frame.revalidate();
            frame.pack();
        });

        buttonWrap.add(btnSubmit);
        buttonWrap.add(btnDisplay);

        form.add(title, BorderLayout.NORTH);
        form.add(buttonWrap, BorderLayout.SOUTH);
        form.add(inputWrap);

        add(form);
    }   
    
    private void addShape(JPanel inputWrap, ShapeBox2D box)
    {
        box.addShape(Parallelogram.rectangle(
            Vector2.zero(),
            Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(0))).getComponent(1)).getText()),
            Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(1))).getComponent(1)).getText())
        ));
    }

    private void createFields(JPanel form)
    {
        form.add(createField("Base"));
        form.add(createField("Hauteur"));
    }

    private JPanel createField(String label)
    {
        JPanel input = new JPanel(new BorderLayout(10, 10));
        input.setBackground(Color.WHITE);
        input.add(new JLabel(label), BorderLayout.NORTH);
        input.add(new JTextField(20));

        return input;
    }
}
