import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class HelloWorld {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 520;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pong - 2 Spieler");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new PongPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static class PongPanel extends JPanel {
        private static final int PADDLE_WIDTH = 14;
        private static final int PADDLE_HEIGHT = 90;
        private static final int PADDLE_SPEED = 6;
        private static final int BALL_SIZE = 14;

        private int leftPaddleY = WINDOW_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        private int rightPaddleY = WINDOW_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        private int ballX = WINDOW_WIDTH / 2 - BALL_SIZE / 2;
        private int ballY = WINDOW_HEIGHT / 2 - BALL_SIZE / 2;
        private int ballSpeedX = 4;
        private int ballSpeedY = 3;

        private int scoreLeft = 0;
        private int scoreRight = 0;

        private boolean leftUpPressed = false;
        private boolean leftDownPressed = false;
        private boolean rightUpPressed = false;
        private boolean rightDownPressed = false;

        private final Timer timer;

        PongPanel() {
            setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);

            bindKey("pressed W", () -> leftUpPressed = true);
            bindKey("released W", () -> leftUpPressed = false);
            bindKey("pressed S", () -> leftDownPressed = true);
            bindKey("released S", () -> leftDownPressed = false);

            bindKey("pressed O", () -> rightUpPressed = true);
            bindKey("released O", () -> rightUpPressed = false);
            bindKey("pressed L", () -> rightDownPressed = true);
            bindKey("released L", () -> rightDownPressed = false);

            timer = new Timer(16, this::tick);
            timer.start();
        }

        private void bindKey(String keyStroke, Runnable action) {
            InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = getActionMap();
            String actionName = keyStroke + "_action";
            inputMap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    action.run();
                }
            });
        }

        private void tick(ActionEvent e) {
            if (leftUpPressed) {
                leftPaddleY -= PADDLE_SPEED;
            }
            if (leftDownPressed) {
                leftPaddleY += PADDLE_SPEED;
            }
            if (rightUpPressed) {
                rightPaddleY -= PADDLE_SPEED;
            }
            if (rightDownPressed) {
                rightPaddleY += PADDLE_SPEED;
            }

            leftPaddleY = clamp(leftPaddleY, 0, WINDOW_HEIGHT - PADDLE_HEIGHT);
            rightPaddleY = clamp(rightPaddleY, 0, WINDOW_HEIGHT - PADDLE_HEIGHT);

            ballX += ballSpeedX;
            ballY += ballSpeedY;

            if (ballY <= 0 || ballY >= WINDOW_HEIGHT - BALL_SIZE) {
                ballSpeedY *= -1;
            }

            int leftPaddleX = 30;
            int rightPaddleX = WINDOW_WIDTH - 30 - PADDLE_WIDTH;

            boolean hitLeftPaddle = ballX <= leftPaddleX + PADDLE_WIDTH
                && ballX + BALL_SIZE >= leftPaddleX
                && ballY + BALL_SIZE >= leftPaddleY
                && ballY <= leftPaddleY + PADDLE_HEIGHT;

            boolean hitRightPaddle = ballX + BALL_SIZE >= rightPaddleX
                && ballX <= rightPaddleX + PADDLE_WIDTH
                && ballY + BALL_SIZE >= rightPaddleY
                && ballY <= rightPaddleY + PADDLE_HEIGHT;

            if (hitLeftPaddle && ballSpeedX < 0) {
                ballSpeedX *= -1;
                ballX = leftPaddleX + PADDLE_WIDTH;
            }
            if (hitRightPaddle && ballSpeedX > 0) {
                ballSpeedX *= -1;
                ballX = rightPaddleX - BALL_SIZE;
            }

            if (ballX < 0) {
                scoreRight++;
                resetBall(1);
            }
            if (ballX > WINDOW_WIDTH - BALL_SIZE) {
                scoreLeft++;
                resetBall(-1);
            }

            repaint();
        }

        private void resetBall(int directionX) {
            ballX = WINDOW_WIDTH / 2 - BALL_SIZE / 2;
            ballY = WINDOW_HEIGHT / 2 - BALL_SIZE / 2;
            ballSpeedX = 4 * directionX;
            ballSpeedY = (ballSpeedY < 0) ? -3 : 3;
        }

        private int clamp(int value, int min, int max) {
            return Math.max(min, Math.min(max, value));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);

            int leftPaddleX = 30;
            int rightPaddleX = WINDOW_WIDTH - 30 - PADDLE_WIDTH;
            g2.fillRect(leftPaddleX, leftPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
            g2.fillRect(rightPaddleX, rightPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
            g2.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

            for (int y = 0; y < WINDOW_HEIGHT; y += 24) {
                g2.fillRect(WINDOW_WIDTH / 2 - 2, y, 4, 12);
            }

            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            g2.drawString(String.valueOf(scoreLeft), WINDOW_WIDTH / 2 - 70, 40);
            g2.drawString(String.valueOf(scoreRight), WINDOW_WIDTH / 2 + 50, 40);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.drawString("Links: W/S", 20, WINDOW_HEIGHT - 16);
            g2.drawString("Rechts: O/L", WINDOW_WIDTH - 120, WINDOW_HEIGHT - 16);
        }
    }
}
