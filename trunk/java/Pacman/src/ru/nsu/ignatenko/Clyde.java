package ru.nsu.ignatenko;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import java.util.Random;

public class Clyde implements Ghost,  ActionListener
{
    private final String name = "Clyde";

    private int screenData[];
    private int blocksize;
    private int ncollumn;
    private int nrow;

    private final int WALL = 1;
    private final int LEFT_PORTAL = 4;
    private final int RIGHT_PORTAL = 5;
    private final int GHOST_DOOR = 6;

    private final int LEFT = -1;
    private final int UP = -1;
    private final int RIGHT = 1;
    private final int DOWN = 1;
    private final int NO_MOTION = 0;

    private int startPosX;
    private int startPosY;
    private int posX;
    private int posY;
    private int pos;
    private int posTargetX;
    private int posTargetY;
    private int coordX;
    private int coordY;
    private int directionX = LEFT;
    private int directionY = NO_MOTION;
    private int nextDirectionX = LEFT;
    private int nextDirectionY = NO_MOTION;

    private boolean obstacleOnTop;
    private boolean obstacleOnBottom;
    private boolean obstacleOnLeft;
    private boolean obstacleOnRight;

    private Mode currentMode = Mode.Scatter;
    private Mode lastMode;

    private final int normalSpeed = 6;
    private final int frightenSpeed = 2;
    private int currentSpeed = normalSpeed;

    private boolean is_start = true;
    private boolean is_first = true;
    private boolean canMove = false;

    private Timer timer1 = new Timer(7000, this);
    private Timer timer2;
    PacMan pacman;

    public Clyde(int screenData_[], int nrow_, int ncollumn_, int blocksize_, int x, int y)
    {
        nrow = nrow_;
        ncollumn = ncollumn_;
        blocksize = blocksize_;
        screenData = screenData_;
        startPosX = x;
        startPosY = y;
        posX = startPosX;
        posY = startPosY;
        coordX = x * blocksize + blocksize/2;
        coordY = y * blocksize + blocksize/2;

        timer1.start();
    }

    public String getName(){return name;}

    public Mode getMode(){return currentMode;}

    public int getCoordX()
    {
        return coordX;
    }

    public int getCoordY()
    {
        return coordY;
    }

    public int getPos()
    {
        return pos;
    }

    public void setVictim(PacMan pacman_)
    {
        pacman = pacman_;
    }

    public void setBlinky(Blinky blinky){}

    public void canMove(){canMove = true;}

    public void cantMove() {canMove = false;}

    private void changeMode()
    {
        if(currentMode == Mode.Scatter)
        {
            currentMode = Mode.Chase;
            timer1.stop();
            timer1 = new Timer(20000, this);
            timer1.start();
        }
        else
        {
            makeScatter();
        }
    }

    public void stopFrighten()
    {
        currentMode = lastMode;
        timer2.stop();
        timer1.start();
    }

    public void makeScatter()
    {
        currentMode = Mode.Scatter;
        timer1.stop();
        timer1 = new Timer(7000, this);
        timer1.start();
    }
    private double calculateDistance(int posX_, int posY_)
    {
        return ((posX_ - posTargetX) * (posX_ - posTargetX) + (posY_ - posTargetY) * (posY_ - posTargetY));
    }

    public void move()
    {
        if(canMove)
        {
            if(is_first)
            {
                timer1.start();
                is_first = false;
            }

            if (coordX % blocksize == blocksize/2 && coordY % blocksize == blocksize/2) // if blClyde enter in center of the cell
            {
                directionX = nextDirectionX;
                directionY = nextDirectionY;

                if (is_start)
                {
                    posTargetX = startPosX - 1;
                    posTargetY = startPosY - 1;
                }

                else if (currentMode == Mode.Frightened)
                {
                        currentSpeed = frightenSpeed;

                        int min = 0;
                        int maxY = nrow;
                        int maxX = ncollumn;
                        int rangeY = maxY - min;
                        int rangeX = maxX - min;

                        Random rn = new Random();
                        posTargetX = rn.nextInt(rangeX) + min;
                        posTargetY = rn.nextInt(rangeY) + min;
                    }
                    else
                    {
                        currentSpeed = normalSpeed;

                        if (currentMode == Mode.Scatter)
                        {
                            posTargetX = 0;
                            posTargetY = nrow + 1;
                        }
                        else if (currentMode == Mode.Chase)
                        {
                            posTargetX = pacman.getPosX();
                            posTargetY = pacman.getPosY();

                            if (calculateDistance(posX, posY) <= 8)
                            {
                                posTargetX = 0;
                                posTargetY = nrow + 1;
                            }
                        }
                    }

                    posX = coordX / blocksize;
                    posY = coordY / blocksize;
                    pos = posY * ncollumn + posX;
                    if (pos == (startPosY - 1) * ncollumn + startPosX - 1) {
                        is_start = false;
                    }

                    int nextPosX = posX + directionX;
                    int nextPosY = posY + directionY;
                    int nextPos = nextPosY * ncollumn + nextPosX;

                    double distance[] = new double[4];
                    distance[0] = calculateDistance(nextPosX, nextPosY - 1); // up cell
                    distance[1] = calculateDistance(nextPosX - 1, nextPosY); // left cell
                    distance[2] = calculateDistance(nextPosX, nextPosY + 1); // down cell
                    distance[3] = calculateDistance(nextPosX + 1, nextPosY); // right cell

                    obstacleOnTop = (screenData[nextPos - ncollumn] == WALL);
                    obstacleOnBottom = (screenData[nextPos + ncollumn] == WALL) || (screenData[nextPos + ncollumn] == GHOST_DOOR);
                    obstacleOnLeft = (screenData[nextPos - 1] == WALL);
                    obstacleOnRight = (screenData[nextPos + 1] == WALL);

                    boolean is_possible[] = new boolean[4];
                    is_possible[0] = !obstacleOnTop && directionY != DOWN;
                    is_possible[1] = !obstacleOnLeft && directionX != RIGHT;
                    is_possible[2] = !obstacleOnBottom && directionY != UP;
                    is_possible[3] = !obstacleOnRight && directionX != LEFT;

                    int choice = -1;
                    for (int i = 0; i < 4; i++) {
                        if (is_possible[i] && (choice == -1 || distance[i] < distance[choice])) {
                            choice = i;
                        }
                    }

                    switch (choice) {
                        case 0:
                            nextDirectionX = NO_MOTION;
                            nextDirectionY = UP;
                            break;
                        case 1:
                            nextDirectionX = LEFT;
                            nextDirectionY = NO_MOTION;
                            break;
                        case 2:
                            nextDirectionX = NO_MOTION;
                            nextDirectionY = DOWN;
                            break;
                        case 3:
                            nextDirectionX = RIGHT;
                            nextDirectionY = NO_MOTION;
                            break;
                        default:
                            nextDirectionX = NO_MOTION;
                            nextDirectionY = NO_MOTION;
                    }
                }

                if (directionX == LEFT && !obstacleOnLeft && (screenData[pos - 1] == LEFT_PORTAL))
                {
                    coordX += blocksize * (ncollumn - 2);
                }
                else if (directionX == RIGHT && !obstacleOnRight && (screenData[pos + 1] == RIGHT_PORTAL))
                {
                    coordX -= blocksize * (ncollumn - 2);
                }
                else
                {
                    coordX += directionX * currentSpeed;
                    coordY += directionY * currentSpeed;
                }
            }
        }


    public void returnToInitialPosition()
    {
        is_start = true;
        posX = startPosX;
        posY = startPosY;
        pos = posY * ncollumn + posX;
        coordX = posX * blocksize + blocksize/2;
        coordY = posY * blocksize + blocksize/2;

        directionX = LEFT;
        directionY = NO_MOTION;
        nextDirectionX = LEFT;
        nextDirectionY = NO_MOTION;
    }

    public void setFrightenedMode()
    {
        if(currentMode == Mode.Frightened)
        {
            timer2.stop();
        }
        else
        {
            timer1.stop();

            lastMode = currentMode;
            currentMode = Mode.Frightened;
        }

        timer2 = new Timer(10000, this);
        timer2.start();

        nextDirectionX = -directionX;
        nextDirectionY = -directionY;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(currentMode == Mode.Frightened)
        {
            currentMode = lastMode;
            timer2.stop();
            timer1.start();
        }
        else
        {
            changeMode();
            nextDirectionX = -directionX;
            nextDirectionY = -directionY;
        }
    }
}