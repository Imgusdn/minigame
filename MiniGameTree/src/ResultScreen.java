import javax.swing.*;
import java.awt.*;

public class ResultScreen {
    private JFrame frame;

    public ResultScreen(GameController controller, long totalTime) {
        frame = new JFrame("게임 결과");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("총 걸린 시간: " + totalTime + "초", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 30));
        frame.add(resultLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("메인 화면으로 돌아가기");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonPanel.add(backButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            frame.dispose();
            MainScreen.showMainScreen();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
