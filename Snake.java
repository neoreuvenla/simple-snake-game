import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Snake {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                GamePanel gamePanel = new GamePanel();

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(gamePanel);
                frame.pack();
                frame.setResizable(false);
                frame.setVisible(true);

                // Adjust the JFrame dimensions to account for the borders
                Insets insets = frame.getInsets();
                int borderWidth = insets.left + insets.right;
                int borderHeight = insets.top + insets.bottom;
                frame.setSize(new Dimension(750 + borderWidth, 600 + borderHeight));
                frame.setLocationRelativeTo(null);
            }
        });
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    int[] snakeX = new int[100];
    int[] snakeY = new int[100];
    int snakeSize = 3;
    int appleX;
    int appleY;
    Random random = new Random();

    boolean running = true;
    Timer timer;

    char direction = 'R';
    int score = 0;

    public GamePanel() {
        timer = new Timer(125, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // initialize starting position of the snake
        for (int i = 0; i < snakeSize; i++) {
            snakeX[i] = 50 - i * 25;
            snakeY[i] = 50;
        }

        spawnApple();
    }

    // paint the game elements on the panel
    public void paint(Graphics g) {
        // clear the panel and set the background color to teal
        g.setColor(Color.CYAN.darker());
        g.fillRect(0, 0, getWidth(), getHeight());

        // draw the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);

        // draw the snake
        for (int i = 0; i < snakeSize; i++) {
            if (i == 0) {
                g.setColor(Color.GRAY); // head color: gray
            } else {
                g.setColor(Color.BLACK); // body color: black
            }
            g.fillRect(snakeX[i], snakeY[i], 25, 25);
        }

        // draw the apple
        g.setColor(Color.WHITE); // apple color: white
        g.fillRect(appleX, appleY, 25, 25);

        // draw gridlines (optional)
        g.setColor(Color.CYAN.darker());
        for (int i = 0; i < 29; i++) {
            g.drawLine(i * 25, 0, i * 25, 550);
            g.drawLine(0, i * 25, 725, i * 25);
        }

        // display game over message
        if (!running) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", 250, 275);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            checkCollision();
        }
        repaint();
    }

    // move the snake based on direction
    public void moveSnake() {
        for (int i = snakeSize; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
    // update movement logic based on direction
    switch (direction) {
        case 'U':
            snakeY[0] -= 25;
            break;
        case 'D':
            snakeY[0] += 25;
            break;
        case 'L':
            snakeX[0] -= 25;
            break;
        case 'R':
            snakeX[0] += 25;
            break;
    }

    // wrap around screen boundaries
    if (snakeX[0] < 0) {
        snakeX[0] = 700;
    }
    if (snakeX[0] > 700) {
        snakeX[0] = 0;
    }
    if (snakeY[0] < 0) {
        snakeY[0] = 525;
    }
    if (snakeY[0] > 525) {
        snakeY[0] = 0;
    }
}

// spawn a new apple at a random position
public void spawnApple() {
    appleX = random.nextInt(29) * 25;
    appleY = random.nextInt(22) * 25;
}

// check for collisions (snake eating apple or colliding with itself)
public void checkCollision() {
    // check if snake eats the apple
    if (snakeX[0] == appleX && snakeY[0] == appleY) {
        snakeSize++;
        score++; // increase the score when the snake eats an apple
        spawnApple();
    }

    // check if snake collides with itself
    for (int i = 1; i < snakeSize; i++) {
        if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
            running = false;
            timer.stop(); // stop the timer when the snake collides with itself
        }
    }
}

@Override
public void keyTyped(KeyEvent e) {
}

@Override
public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();

    // update direction based on arrow keys
    if (keyCode == KeyEvent.VK_UP && direction != 'D') {
        direction = 'U';
    }
    if (keyCode == KeyEvent.VK_DOWN && direction != 'U') {
        direction = 'D';
    }
    if (keyCode == KeyEvent.VK_LEFT && direction != 'R') {
        direction = 'L';
    }
    if (keyCode == KeyEvent.VK_RIGHT && direction != 'L') {
        direction = 'R';
    }

    // restart the game by pressing the space key when the game is over
    if (keyCode == KeyEvent.VK_SPACE && !running) {
        running = true;
        timer.start(); // restart the timer
        snakeSize = 3; // reset the snake size
        score = 0; // reset the score

        // reset the starting position of the snake
        for (int i = 0; i < snakeSize; i++) {
            snakeX[i] = 50 - i * 25;
            snakeY[i] = 50;
        }

        spawnApple(); // spawn a new apple
    }
}

@Override
public void keyReleased(KeyEvent e) {
}
}
