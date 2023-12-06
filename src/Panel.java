import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int objectSize = 25;
    static final int screenUnits = (screenHeight * screenWidth) / objectSize;
    static final int delay = 75;

    String snakeDirection = "RIGHT";

    boolean running = false;
    Timer timer;
    Random random = new Random();

    final int[] xAxis = new int[screenUnits];
    final int[] yAxis = new int [screenUnits];

    public int appleX;
    public int appleY;


    int snakeParts = 2;
    int score;
    public Panel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        startGame();
    }

    public void  startGame(){
        newApple();
        running = true;
        this.timer = new Timer(delay,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, objectSize, objectSize);
            String text = "Score: ";

            for (int i = 0; i < snakeParts; i++){
                if (i == 0){
                    g.setColor(Color.orange);
                    g.fillRect(xAxis[i], yAxis[i], objectSize, objectSize);
                }else {
                    g.setColor(Color.green);
                    g.fillRect(xAxis[i], yAxis[i], objectSize, objectSize);
                }

                g.setColor(Color.red);
                g.setFont(new Font("Ink Free", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString(text + score, (screenWidth - metrics.stringWidth(text))/2, g.getFont().getSize());
            }
        }else {
            endGame(g);
        }

    }

    public void move(){
        for (int i = snakeParts; i >0 ; i--){
            xAxis[i] = xAxis[i-1];
            yAxis[i] = yAxis[i-1];
        }

        switch (snakeDirection){
            case "UP" -> yAxis[0] = yAxis[0] - objectSize;
            case "DOWN" -> yAxis[0] = yAxis[0] + objectSize;
            case "LEFT" -> xAxis[0] = xAxis[0] - objectSize;
            case "RIGHT" -> xAxis[0] = xAxis[0] + objectSize;
        }

    }

    public void newApple(){
        appleX = random.nextInt(screenWidth/objectSize) * objectSize;
        appleY = random.nextInt(screenHeight/objectSize) * objectSize;
    }

    public void checkApple(){
        if ((xAxis[0] == appleX) && (yAxis[0] == appleY)){
            snakeParts++;
            score++;
            newApple();
        }
    }

    public void checkCollisions(){

        for (int i = snakeParts; i > 0; i--){
            if ((xAxis[0] == xAxis[i]) && (yAxis[0] == yAxis[i])){
                running = false;
            }
        }

        if (xAxis[0] < 0 || xAxis[0] > screenWidth || yAxis[0] < 0 || yAxis[0] > screenHeight){
            running = false;
        }

        if (!running){
            this.timer.stop();
        }
    }

    public void endGame(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String scoreResult = "Score: ";
        String text = "Game Over";
        g.drawString(scoreResult + score,
                (screenWidth - metrics.stringWidth(scoreResult + score))/2,
                g.getFont().getSize());
        g.drawString(text, (screenWidth - metrics.stringWidth(text))/2, screenHeight/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class GameKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT -> {
                    if (!Objects.equals(snakeDirection, "RIGHT")){
                        snakeDirection = "LEFT";
                    };
                }
                case KeyEvent.VK_RIGHT -> {
                    if (!Objects.equals(snakeDirection, "LEFT")){
                        snakeDirection = "RIGHT";
                    };
                }
                case KeyEvent.VK_UP -> {
                    if (!Objects.equals(snakeDirection, "DOWN")){
                        snakeDirection = "UP";
                    };
                }
                case KeyEvent.VK_DOWN -> {
                    if (!Objects.equals(snakeDirection, "UP")){
                        snakeDirection = "DOWN";
                    };
                }
            }
        }
    }

}
