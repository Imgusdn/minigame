import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Game2 {
    private JFrame frame;
    private GamePanel gamePanel;
    private long startTime;

    public Game2(GameController controller) {
        frame = new JFrame("하늘에서 떨어지는 똥 피하기");
        gamePanel = new GamePanel(controller);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(gamePanel);
        frame.setResizable(false);
    }

    public void start() {
        startTime = System.currentTimeMillis();
        frame.setVisible(true);
        gamePanel.startGame(startTime);
    }

    class GamePanel extends JPanel {
        private int playerX = 280; // 플레이어 초기 위치
        private final int PLAYER_Y = 500; // 플레이어의 고정 Y 위치
        private final int PLAYER_SIZE = 40;
        private ArrayList<FallingObject> fallingObjects = new ArrayList<>();
        private Random random = new Random();
        private Timer objectTimer;
        private Timer gameLoop;
        private GameController controller;

        private BufferedImage playerImage; // 플레이어 이미지
        private BufferedImage fallingObjectImage; // 떨어지는 객체 이미지
        private BufferedImage backgroundImage; // 배경 이미지

        public GamePanel(GameController controller) {
            this.controller = controller;

            // 이미지 로드
            try {
                playerImage = ImageIO.read(new File("src/images/8.png")); // 플레이어 이미지
                fallingObjectImage = ImageIO.read(new File("src/images/7.png")); // 떨어지는 객체 이미지
                backgroundImage = ImageIO.read(new File("src/images/9.jpg")); // 배경 이미지
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }

            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> playerX -= 15;
                        case KeyEvent.VK_RIGHT -> playerX += 15;
                    }
                    // 화면 경계 체크
                    playerX = Math.max(0, Math.min(playerX, getWidth() - PLAYER_SIZE));
                    repaint();
                }
            });
        }

        public void startGame(long startTime) {
            objectTimer = new Timer(1000, e -> fallingObjects.add(new FallingObject()));
            objectTimer.start();

            gameLoop = new Timer(30, e -> {
                moveObjects();
                checkCollision(startTime);
                repaint();
            });
            gameLoop.start();
        }

        private void moveObjects() {
            fallingObjects.removeIf(obj -> {
                obj.move();
                return obj.y > getHeight();
            });
        }

        private void checkCollision(long startTime) {
            for (FallingObject obj : fallingObjects) {
                if (new Rectangle(playerX, PLAYER_Y, PLAYER_SIZE, PLAYER_SIZE)
                        .intersects(new Rectangle(obj.x, obj.y, obj.size, obj.size))) {
                    endGame(startTime);
                    break;
                }
            }
        }

        private void endGame(long startTime) {
            objectTimer.stop();
            gameLoop.stop();
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            JOptionPane.showMessageDialog(this, "게임 오버! 생존 시간: " + elapsedTime + "초");
            frame.dispose();
            controller.gameOver2(); // Game3 호출
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 배경 그리기
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.WHITE); // 기본 배경 색상
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            // 플레이어 그리기
            if (playerImage != null) {
                g.drawImage(playerImage, playerX, PLAYER_Y, PLAYER_SIZE, PLAYER_SIZE, null);
            } else {
                g.setColor(Color.BLUE); // 기본 플레이어
                g.fillRect(playerX, PLAYER_Y, PLAYER_SIZE, PLAYER_SIZE);
            }

            // 떨어지는 객체 그리기
            for (FallingObject obj : fallingObjects) {
                if (fallingObjectImage != null) {
                    g.drawImage(fallingObjectImage, obj.x, obj.y, obj.size, obj.size, null);
                } else {
                    g.setColor(Color.DARK_GRAY); // 기본 객체
                    g.fillOval(obj.x, obj.y, obj.size, obj.size);
                }
            }
        }

        class FallingObject {
            int x, y, size, speed;

            public FallingObject() {
                size = 30;
                speed = random.nextInt(5) + 3; // 속도: 3 ~ 7
                x = random.nextInt(getWidth() - size);
                y = 0; // 초기 위치: 위쪽
            }

            public void move() {
                y += speed; // 아래로 이동
            }
        }
    }
}
