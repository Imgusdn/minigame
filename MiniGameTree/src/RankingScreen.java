import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RankingScreen {
    private JFrame frame;

    public RankingScreen(GameController controller) {
        ArrayList<String> formattedRankings = controller.getFormattedRankings();

        frame = new JFrame("게임 랭킹");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel rankingPanel = new JPanel();
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));

        for (String rank : formattedRankings) {
            JLabel rankLabel = new JLabel(rank);
            rankLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            rankingPanel.add(rankLabel);
        }

        JScrollPane scrollPane = new JScrollPane(rankingPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("Main");
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
