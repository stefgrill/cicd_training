import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class HelloWorld {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hallo Welt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(360, 180);
            frame.setLocationRelativeTo(null);

            JLabel label = new JLabel("Hallo Welt", SwingConstants.CENTER);
            JButton closeButton = new JButton("Beenden");
            closeButton.addActionListener(e -> frame.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            frame.add(label, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}
