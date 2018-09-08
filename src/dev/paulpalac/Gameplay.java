package dev.paulpalac;

import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 4;

    private int playerX = 310;

    private int ballPosX = 50+(70*(int)Math.random());
    private int ballPosY = 150+(200*(int)Math.random());;
    private int  ballXdir;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay(){
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }
    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //drawMap
        map.draw((Graphics2D)g);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //scores
        g.setColor(Color.cyan);
        g.setFont(new Font("serif", Font.BOLD,25 ));
        g.drawString("SCORE: "+score, 520, 30);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD,30 ));
            g.drawString("PANALO KA!", 230,280 );

            g.setFont(new Font("serif", Font.BOLD,20 ));
            g.drawString("PRESS ENTER TO RESTART", 200,380 );
        }

        if(ballPosY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD,30 ));
            g.drawString("GAME OVER!", 235,280 );
                    g.drawString("MOTHERFUCKA!", 205,320 );

            g.setFont(new Font("serif", Font.BOLD,20 ));
            g.drawString("PRESS ENTER TO RESTART", 200,380 );
        }

        g.dispose();

    }
  //ACTION LISTENER
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = -ballYdir;
            }

            outer: for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x
                            +brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break outer;
                        }
                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if(ballPosX < 0){
                ballXdir = -ballXdir;
            }
            if(ballPosY < 0){
                ballYdir = -ballYdir;
            }
            if(ballPosX > 670){
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    //KEY LISTENER
    @Override
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX > 600){
                playerX = 600;
            }else{
                moveRight();
            }

        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPosX = 50+(70*(int)Math.random());
                ballPosY = 300+(50*(int)Math.random());
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();

            }
        }
    }
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

}
