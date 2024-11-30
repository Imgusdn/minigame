import javax.swing.*;
import java.awt.*;

public class MainScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::showMainScreen);
    }

    public static void showMainScreen() {
        JFrame frame = new JFrame("미겜3!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("미겜3!!!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton startButton = new JButton("Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonPanel.add(startButton);

        JButton rankButton = new JButton("Ranking");
        rankButton.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonPanel.add(rankButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        GameController controller = new GameController();

        startButton.addActionListener(e -> {
            frame.dispose();
            controller.startGame();
        });

        rankButton.addActionListener(e -> {
            frame.dispose();
            new RankingScreen(controller);
        });

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "정말로 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
