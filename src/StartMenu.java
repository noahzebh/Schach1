import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("Schach – Startmenü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Willkommen zum Schachspiel!", SwingConstants.CENTER);
        JButton startButton = new JButton("Spiel starten");

        startButton.addActionListener(e -> {
            new ChessGUI();
            dispose();
        });

        add(label, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }}