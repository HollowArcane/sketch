package bin.sketch.packing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bin.util.geometry.Circle;
import bin.util.geometry.Parallelogram;
import bin.util.geometry.Triangle;
import bin.util.geometry.Vector2;
import bin.view.processing.Canvas;

public class EntryPanelComplex extends JPanel
{
    public EntryPanelComplex(JFrame frame, Canvas canvas, ShapeBox2D box)
    {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                exec.shutdownNow(); 
                System.out.println("WINDOW CLOSED");
            }
        });

        setPreferredSize(new Dimension(1280, 720));
        setLayout(new GridBagLayout());

        JPanel form = new JPanel(new BorderLayout(20, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(50, 100, 50, 100));

        JLabel title = new JLabel("Insertion");
        title.setFont(new Font("sansserif", Font.BOLD, 24));

        JPanel inputWrap = new JPanel(new GridLayout(3, 1, 20, 20));
        inputWrap.setBackground(Color.WHITE);

        JPanel selectShape = createSelect("Forme", new String[] { "Rectangle", "Cercle", "Triangle" });
        @SuppressWarnings("unchecked")
        JComboBox<String> select = (JComboBox<String>)selectShape.getComponent(1);

        select.addActionListener(e -> {
            inputWrap.removeAll();

            inputWrap.add(select);
            createFields(inputWrap, select.getSelectedItem());

            inputWrap.revalidate();
            inputWrap.repaint();
        });

        inputWrap.add(select);
        createFields(inputWrap, "Rectangle");

        JPanel buttonWrap = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnSubmit = new JButton("Valider");
        btnSubmit.addActionListener(e -> {
            addShape(select, inputWrap, box);
            JOptionPane.showMessageDialog(null, "Insertion effectué avec succès", "Message", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnDisplay = new JButton("Afficher");
        btnDisplay.addActionListener(e -> {
            exec.execute(() -> box.fitBFDH(canvas.width(), canvas.height(), 100));
            exec.shutdown();
            frame.setContentPane(canvas);
            frame.revalidate();
        });

        buttonWrap.add(btnSubmit);
        buttonWrap.add(btnDisplay);

        form.add(title, BorderLayout.NORTH);
        form.add(buttonWrap, BorderLayout.SOUTH);
        form.add(inputWrap);

        add(form);
    }   
    
    private void addShape(JComboBox<String> select, JPanel inputWrap, ShapeBox2D box)
    {
        switch (select.getSelectedItem().toString())
        {
            case "Rectangle" -> box.addShape(Parallelogram.rectangle(
                Vector2.zero(),
                Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(1))).getComponent(1)).getText()),
                Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(2))).getComponent(1)).getText())
            ));
            case "Triangle" -> box.addShape(Triangle.isocelis(
                Vector2.zero(),
                Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(1))).getComponent(1)).getText()),
                Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(2))).getComponent(1)).getText())
            ));
            case "Cercle" -> box.addShape(new Circle(
                Vector2.zero(),
                Double.parseDouble(((JTextField)((Container)(inputWrap.getComponent(1))).getComponent(1)).getText())
            ));
            default -> {}
        };
    }

    private void createFields(JPanel form, Object selectedItem)
    {
        switch (selectedItem.toString()) {
            case "Rectangle", "Triangle" -> {
                form.add(createField("Base"));
                form.add(createField("Hauteur"));
            }
            case "Cercle" -> form.add(createField("Rayon"));
            default -> {}
        };
    }

    private JPanel createSelect(String label, String[] values)
    {
        JPanel input = new JPanel(new BorderLayout());
        input.setBackground(Color.WHITE);
        input.add(new JLabel(label), BorderLayout.NORTH);

        JComboBox<String> select = new JComboBox<>(values);
        input.add(select);

        return input;
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
