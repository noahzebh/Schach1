import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StartMenu extends JFrame {

    public StartMenu() {
        setTitle("Schach – Startmenü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Zeitmodus wählen:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        Map<String, int[]> timeModes = new LinkedHashMap<>();
        timeModes.put("1 + 0", new int[]{1, 0});
        timeModes.put("2 + 1", new int[]{2, 1});
        timeModes.put("3 + 0", new int[]{3, 0});
        timeModes.put("3 + 2", new int[]{3, 2});
        timeModes.put("5 + 0", new int[]{5, 0});
        timeModes.put("10 + 0", new int[]{10, 0});
        timeModes.put("15 + 10", new int[]{15, 10});
        timeModes.put("30 + 20", new int[]{30, 20});

        for (Map.Entry<String, int[]> entry : timeModes.entrySet()) {
            JButton btn = new JButton(entry.getKey());
            int[] values = entry.getValue();
            btn.addActionListener(e -> {
                new ChessGUI(values[0], values[1]); // Minuten, Inkrement
                dispose();
            });
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
