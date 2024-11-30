import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Random;

public class Game3 extends JPanel implements ActionListener {
    private int dinoY = 200;
    private int dinoX = 50;
    private boolean isJumping = false;
    private boolean gameOver = false;
    private ArrayList<Integer> obstacles = new ArrayList<>();
    private Timer timer;
    private BufferedImage dinoImage; // 공룡 이미지
    private BufferedImage obstacleImage; // 장애물 이미지
    private BufferedImage backgroundImage; // 배경 이미지
    private GameController controller; // GameController와 연계

    public Game3(GameController controller) {
        this.controller = controller; // GameController 연계
        setBackground(Color.WHITE);
        setFocusable(true);

        // 키 입력 처리
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!gameOver && !isJumping) { // 점프 중이 아니고 게임이 종료되지 않았을 때
                        isJumping = true;
                        new Timer(20, new ActionListener() {
                            int jumpHeight = 0;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (jumpHeight < 100) {
                                    dinoY -= 5; // 공룡이 올라감
                                    jumpHeight += 5;
                                } else {
                                    ((Timer) e.getSource()).stop();
                                    new Timer(20, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (jumpHeight > 0) {
                                                dinoY += 5; // 공룡이 내려감
                                                jumpHeight -= 5;
                                            } else {
                                                isJumping = false;
                                                ((Timer) e.getSource()).stop();
                                            }
                                        }
                                    }).start();
                                }
                                repaint();
                            }
                        }).start();
                    }
                }
            }
        });

        // 이미지 로드
        try {
            System.out.println("Trying to load images from: " + new File("images/").getAbsolutePath());
            dinoImage = ImageIO.read(new File("src/images/1.png")); // 공룡 이미지
            obstacleImage = ImageIO.read(new File("src/images/2.png")); // 장애물 이미지
            backgroundImage = ImageIO.read(new File("src/images/3.png")); // 배경 이미지
            System.out.println("Images loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading images:");
            e.printStackTrace();
            // 이미지 로드 실패 시 기본 동작
            dinoImage = null;
            obstacleImage = null;
            backgroundImage = null;
        }

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight()); // 기본 배경 색상
        }

        // 공룡 이미지 그리기
        if (dinoImage != null) {
            g.drawImage(dinoImage, dinoX, dinoY, 50, 50, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(dinoX, dinoY, 50, 50); // 기본 공룡 표시
        }

        // 장애물 이미지 그리기
        for (int obstacleX : obstacles) {
            if (obstacleImage != null) {
                g.drawImage(obstacleImage, obstacleX, 220, 50, 50, null);
            } else {
                g.setColor(Color.RED);
                g.fillRect(obstacleX, 220, 50, 50); // 기본 장애물 표시
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            for (int i = 0; i < obstacles.size(); i++) {
                obstacles.set(i, obstacles.get(i) - 10); // 장애물 이동
                if (obstacles.get(i) < 0) {
                    obstacles.remove(i);
                    i--;
                } else if (obstacles.get(i) < dinoX + 50 && obstacles.get(i) > dinoX && dinoY >= 200) {
                    // 충돌 감지
                    gameOver = true;
                    timer.stop();
                    handleGameOver(); // 게임 종료 처리
                }
            }
            if (new Random().nextInt(100) < 10) {
                obstacles.add(500); // 새로운 장애물 추가
            }
            repaint();
        }
    }

    private void handleGameOver() {
        controller.gameOver3(); // 결과 화면 호출
    }

    public void start() {
        JFrame frame = new JFrame("Dino Game");
        frame.add(this);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
