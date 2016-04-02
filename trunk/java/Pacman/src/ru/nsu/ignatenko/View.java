package ru.nsu.ignatenko;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class View extends JPanel
{
    private int width;
    private int height;
    private final int mazeIndentX = 25;
    private final int mazeIndentY = 70;

    private int screenData[];
    private final int nrow = 25;
    private final int ncollumn = 23;
    private final int blocksize = 24;

    private final int WALL = 1;
    private final int DOT = 2;
    private final int SUPER_DOT = 3;
    private final int GHOST_DOOR = 6;

    private final int LEFT = -1;
    private final int UP = -1;
    private final int RIGHT = 1;

    private final Color mazecolor = new Color(96, 128, 255);
    private final Color dotcolor = new Color(240, 143, 238);
    private final Color ghostDoorColor = new Color(240, 143, 238);
    private final Color scorecolor = new Color(109, 142, 255);
    private final Color winTextColor = new Color(255, 244, 138);
    private final Color gameOverTextColor =new Color(255, 244, 138);

    private final Image pacmanLeft[] = new Image[4];
    private final Image pacmanRight[] = new Image[4];
    private final Image pacmanDown[] = new Image[4];
    private final Image pacmanUp[] = new Image[4];

    private final Image normalBlinky = new ImageIcon("images/BlinkyLeft.png").getImage();
    private final Image normalPinky = new ImageIcon("images/PinkyLeft.png").getImage();
    private final Image normalInky = new ImageIcon("images/InkyLeft.png").getImage();
    private final Image normalClyde = new ImageIcon("images/ClydeLeft.png").getImage();
    private final Image scaredGhost = new ImageIcon("images/ScaredGhostLeft.png").getImage();

    private Image imageBlinky;
    private Image imagePinky;
    private Image imageInky;
    private Image imageClyde;

    private final int animationDelay = 2;
    private int wait = animationDelay;

    private final int maxOpenedMouth = 3;
    private final int closedMouth = 0;
    private int openedMouthDegree = maxOpenedMouth;
    private int openedMouthMovementDirection = -1;

    private final Font smallfont = new Font("Helvetica", Font.BOLD, 15);
    private final Font mediumfont = new Font("Helvetica", Font.BOLD, 20);
    private final Font bigfont = new Font("Helvetica", Font.BOLD, 25);

    private boolean ingame = false;
    private boolean win = false;
    private boolean gameOver = false;

    private Controller controller;

    JButton restart;

    public View(int width_, int height_)
    {
        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                controller.processKey(e.getKeyCode());
            }
        });

        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);

        height = height_;
        width = width_;
        loadImages();

        attachRestartButton();
    }

    public void startGame(int[] screenData_)
    {
        ingame = true;
        win = false;
        gameOver = false;
        requestFocus();
        screenData = screenData_;
    }

    void loadImages()
    {
        pacmanLeft[0] = new ImageIcon("images/PacMan1.png").getImage();
        pacmanLeft[1] = new ImageIcon("images/PacMan2left.png").getImage();
        pacmanLeft[2] = new ImageIcon("images/PacMan3left.png").getImage();
        pacmanLeft[3] = new ImageIcon("images/PacMan4left.png").getImage();

        pacmanRight[0] = new ImageIcon("images/PacMan1.png").getImage();
        pacmanRight[1] = new ImageIcon("images/PacMan2right.png").getImage();
        pacmanRight[2] = new ImageIcon("images/PacMan3right.png").getImage();
        pacmanRight[3] = new ImageIcon("images/PacMan4right.png").getImage();

        pacmanUp[0] = new ImageIcon("images/PacMan1.png").getImage();
        pacmanUp[1] = new ImageIcon("images/PacMan2up.png").getImage();
        pacmanUp[2] = new ImageIcon("images/PacMan3up.png").getImage();
        pacmanUp[3] = new ImageIcon("images/PacMan4up.png").getImage();

        pacmanDown[0] = new ImageIcon("images/PacMan1.png").getImage();
        pacmanDown[1] = new ImageIcon("images/PacMan2down.png").getImage();
        pacmanDown[2] = new ImageIcon("images/PacMan3down.png").getImage();
        pacmanDown[3] = new ImageIcon("images/PacMan4down.png").getImage();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (!ingame)
        {
            restart.setVisible(false);
            showIntroScreen(g2d);
        }
        else
        {
            restart.setVisible(true);
            showGame(g2d);
        }
    }

    private void showGame(Graphics2D g2d)
    {
        drawMaze(g2d);
        drawDots(g2d);
        drawScore(g2d);
        drawLeftLives(g2d);

        if (gameOver)
        {
            showGameOverScreen(g2d);
        }
        else if (win)
        {
            showWinScreen(g2d);
        }
        else
        {
            doAnimation();
            drawPacMan(g2d);
        }
        drawGhosts(g2d);
    }

    private void drawScore(Graphics2D g)
    {
        g.setFont(mediumfont);
        g.setColor(scorecolor);
        String s = "Score: " + controller.getScore();
        g.drawString(s, mazeIndentX, mazeIndentY - 20);
    }

    private void drawLeftLives(Graphics2D g)
    {
        int coordX;
        int coordY = mazeIndentY + blocksize * nrow + 10;
        for (int i = 0; i < controller.getCountOfLives(); ++i)
        {
            coordX = i * 28 + mazeIndentX;
            g.drawImage(pacmanLeft[2], coordX, coordY, this);
        }
    }

    private void drawGhosts(Graphics2D g2d)
    {
        chooseGhostImages();

        int coordX = controller.getBlinkyCoordX() - (blocksize / 2) + mazeIndentX;
        int coordY = controller.getBlinkyCoordY() - (blocksize / 2) + mazeIndentY;
        g2d.drawImage(imageBlinky, coordX, coordY, this);

        coordX = controller.getPinkyCoordX() - (blocksize / 2) + mazeIndentX;
        coordY = controller.getPinkyCoordY() - (blocksize / 2) + mazeIndentY;
        g2d.drawImage(imagePinky, coordX, coordY, this);

        coordX = controller.getInkyCoordX() - (blocksize / 2) + mazeIndentX;
        coordY = controller.getInkyCoordY()- (blocksize / 2) + mazeIndentY;
        g2d.drawImage(imageInky, coordX, coordY, this);

        coordX = controller.getClydeCoordX()- (blocksize / 2) + mazeIndentX;
        coordY = controller.getClydeCoordY() - (blocksize / 2) + mazeIndentY;
        g2d.drawImage(imageClyde, coordX, coordY, this);
    }

    private void chooseGhostImages()
    {
        if(controller.getBlinkyMode() != Mode.Frightened)
        {
            imageBlinky = normalBlinky;
        }
        else
        {
            imageBlinky = scaredGhost;
        }

        if(controller.getPinkyMode()!= Mode.Frightened)
        {
            imagePinky = normalPinky;
        }
        else
        {
            imagePinky = scaredGhost;
        }

        if(controller.getInkyMode()!= Mode.Frightened)
        {
            imageInky = normalInky;
        }
        else
        {
            imageInky = scaredGhost;
        }

        if(controller.getClydeMode()!= Mode.Frightened)
        {
            imageClyde = normalClyde;
        }
        else
        {
            imageClyde = scaredGhost;
        }
}

    private void drawPacMan(Graphics2D g2d)
    {
        int coordX = controller.getPacManCoordX()-(blocksize/2) + mazeIndentX;
        int coordY = controller.getPacManCoordY()-(blocksize/2) + mazeIndentY;

        if (controller.getPacManDirectionX() == LEFT)
        {
            g2d.drawImage(pacmanLeft[openedMouthDegree], coordX, coordY, this);
        }
        else if (controller.getPacManDirectionX() == RIGHT)
        {
            g2d.drawImage(pacmanRight[openedMouthDegree], coordX, coordY, this);
        }
        else if (controller.getPacManDirectionY() == UP)
        {
            g2d.drawImage(pacmanUp[openedMouthDegree], coordX, coordY, this);
        }
        else
        {
            g2d.drawImage(pacmanDown[openedMouthDegree], coordX, coordY, this);
        }
    }

    private void drawMaze(Graphics2D g2d)
    {
        g2d.setColor(mazecolor);
        int i = 0;

        for (int y = mazeIndentY; y < blocksize * nrow + mazeIndentY ; y += blocksize)
        {
            for (int x = mazeIndentX; x < blocksize * ncollumn + mazeIndentX; x += blocksize)
            {
                if (screenData[i] == WALL)
                {
                    g2d.fillRect(x, y, blocksize, blocksize);
                }
                else if (screenData[i] == GHOST_DOOR)
                {
                    g2d.setColor(ghostDoorColor);
                    g2d.fillRect(x, y, blocksize, blocksize);
                    g2d.setColor(mazecolor);
                }
                i++;
            }
        }
    }

    private void drawDots(Graphics2D g2d)
    {
        g2d.setColor(dotcolor);
        int i = 0;

        for (int y = mazeIndentY; y < blocksize * nrow + mazeIndentY ; y += blocksize)
        {
            for (int x = mazeIndentX; x < blocksize * ncollumn + mazeIndentX; x += blocksize)
            {
                if (screenData[i] ==  DOT)
                {
                    g2d.fillRect(x + 11, y + 11, 4, 4);
                }
                else if (screenData[i] == SUPER_DOT)
                {
                    g2d.fillOval(x + 6, y + 3, 13, 13);
                }
                i++;
            }
        }
    }

    private void doAnimation()
    {
        wait--;

        if (wait == 0)
        {
            wait = animationDelay;
            openedMouthDegree += openedMouthMovementDirection;

            if (openedMouthDegree == maxOpenedMouth || openedMouthDegree == closedMouth)
            {
                openedMouthMovementDirection = -openedMouthMovementDirection;
            }
        }
    }

    public void setWin(){win = true;}

    public void setGameOver(){gameOver = true;}

    public void showWinScreen(Graphics2D g)
    {
        g.setFont(bigfont);
        g.setColor(winTextColor);
        g.drawString("You win!", mazeIndentX+ncollumn/2*blocksize - 40, mazeIndentY + nrow/2*blocksize+80);
    }

    public void showGameOverScreen(Graphics2D g)
    {
        g.setFont(bigfont);
        g.setColor(gameOverTextColor);
        g.drawString("Game over!", mazeIndentX+ncollumn/2*blocksize - 57, mazeIndentY + nrow/2*blocksize+80);
    }

    private void showIntroScreen(Graphics2D g2d)
    {
        g2d.setColor(new Color(0, 32, 48));
        g2d.fillRect(width/2 - 105, height/2 - 45, 200, 40);
        g2d.setColor(Color.white);
        g2d.drawRect(width/2 - 105, height/2 - 45, 200, 40);

        g2d.setFont(smallfont);
        g2d.setColor(Color.white);
        g2d.drawString("Press s to start.", width/2 - 61, height/2 - 18);
    }

    public void addController(Controller controller_)
    {
        controller = controller_;
    }

    public void attachRestartButton()
    {
        setLayout(new FlowLayout(FlowLayout.RIGHT, mazeIndentX, mazeIndentY / 3 + 5));
        restart = new JButton("Restart");
        restart.setPreferredSize(new Dimension(width / 5, 30));
        restart.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                controller.restart();
            }
        });
        add(restart);
    }

    @Override
    public void addNotify()
    {
        super.addNotify();
    }
}
