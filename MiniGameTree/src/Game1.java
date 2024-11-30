import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Game1 {
    private JFrame frame;
    private GamePanel gamePanel;
    private long startTime;

    public Game1(GameController controller) {
        frame = new JFrame("총알 피하기 게임");
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
        private int playerX = 280, playerY = 280;
        private final int PLAYER_SIZE = 40; // 플레이어 크기 (기존 20에서 2배로 증가)
        private ArrayList<Bullet> bullets = new ArrayList<>();
        private Random random = new Random();
        private Timer bulletTimer;
        private Timer gameLoop;
        private GameController controller;

        private BufferedImage playerImage; // 플레이어 이미지
        private BufferedImage bulletImage; // 총알 이미지
        private BufferedImage backgroundImage; // 배경 이미지

        public GamePanel(GameController controller) {
            this.controller = controller;

            // 이미지 로드
            try {
                playerImage = ImageIO.read(new File("src/images/6.png")); // 플레이어 이미지
                bulletImage = ImageIO.read(new File("src/images/4.png")); // 총알 이미지
                backgroundImage = ImageIO.read(new File("src/images/5.png")); // 배경 이미지
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "이미지를 로드할 수 없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            }

            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> playerY -= 10;
                        case KeyEvent.VK_DOWN -> playerY += 10;
                        case KeyEvent.VK_LEFT -> playerX -= 10;
                        case KeyEvent.VK_RIGHT -> playerX += 10;
                    }
                    repaint();
                }
            });
        }

        public void startGame(long startTime) {
            bulletTimer = new Timer(1000, e -> bullets.add(new Bullet()));
            bulletTimer.start();

            gameLoop = new Timer(30, e -> {
                moveBullets();
                checkCollision(startTime);
                repaint();
            });
            gameLoop.start();
        }

        private void moveBullets() {
            bullets.removeIf(bullet -> {
                bullet.move();
                return bullet.x < 0 || bullet.x > 600 || bullet.y < 0 || bullet.y > 600;
            });
        }

        private void checkCollision(long startTime) {
            for (Bullet bullet : bullets) {
                if (new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)
                        .intersects(new Rectangle(bullet.x, bullet.y, bullet.size, bullet.size))) {
                    endGame(startTime);
                    break;
                }
            }
        }

        private void endGame(long startTime) {
            bulletTimer.stop();
            gameLoop.stop();
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            JOptionPane.showMessageDialog(this, "게임 오버! 생존 시간: " + elapsedTime + "초");
            frame.dispose();
            controller.gameOver1();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 배경 그리기
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.BLACK); // 기본 배경
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            // 플레이어 그리기
            if (playerImage != null) {
                g.drawImage(playerImage, playerX, playerY, PLAYER_SIZE, PLAYER_SIZE, null);
            } else {
                g.setColor(Color.BLUE); // 기본 플레이어
                g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
            }

            // 총알 그리기
            for (Bullet bullet : bullets) {
                if (bulletImage != null) {
                    g.drawImage(bulletImage, bullet.x, bullet.y, bullet.size, bullet.size, null);
                } else {
                    g.setColor(Color.RED); // 기본 총알
                    g.fillOval(bullet.x, bullet.y, bullet.size, bullet.size);
                }
            }
        }

        class Bullet {
            int x, y, size;
            double speed;
            double angle;

            public Bullet() {
                size = 30; // 총알 크기 (기존 15에서 2배로 증가)
                speed = 5;
                switch (random.nextInt(4)) {
                    case 0 -> { x = random.nextInt(600); y = 0; }
                    case 1 -> { x = random.nextInt(600); y = 600; }
                    case 2 -> { x = 0; y = random.nextInt(600); }
                    case 3 -> { x = 600; y = random.nextInt(600); }
                }
                double playerCenterX = playerX + (PLAYER_SIZE / 2);
                double playerCenterY = playerY + (PLAYER_SIZE / 2);
                angle = Math.atan2(playerCenterY - y, playerCenterX - x);
            }

            public void move() {
                x += (int) (speed * Math.cos(angle));
                y += (int) (speed * Math.sin(angle));
            }
        }
    }
}
