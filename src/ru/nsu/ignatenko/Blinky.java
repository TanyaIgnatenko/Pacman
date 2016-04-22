package ru.nsu.ignatenko;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import java.util.Random;

public class Blinky implements Ghost, ActionListener
{
	private final String name = "Blinky";

	private ScreenData screenData[];
	private int blocksize;
	private int ncollumn;
	private int nrow;

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

    private Timer timer1 = new Timer(7000, this);
    private Timer timer2 = new Timer(10000, this);

	PacMan pacman;

	public Blinky(ScreenData screenData_[], int nrow_, int ncollumn_, int blocksize_, int x, int y)
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

        timer1.setDelay(20000);
        timer1.start();
	}

    public void setStartPos(int x, int y)
    {
        startPosX = x;
        startPosY = y;
        posX = startPosX;
        posY = startPosY;
        coordX = x * blocksize + blocksize/2;
        coordY = y * blocksize + blocksize/2;
    }

    public void stop()
    {
        timer1.stop();
        timer2.stop();
    }

	public void setVictim(PacMan pacman_)
	{
		pacman = pacman_;
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

	public Direction getDirection()
	{
		if(directionX == -1)
		{
			return Direction.Left;
		}
		else if(directionX == 1)
		{
			return Direction.Right;
		}
		else if(directionY == -1)
		{
			return Direction.Up;
		}
		else
		{
			return Direction.Down;
		}
	}

	public int getPosX()
	{
        posX = coordX / blocksize;
		return posX;
	}

	public int getPosY()
	{
        posY = coordY / blocksize;
		return posY;
	}

	public int getPos()
	{
        posX = coordX / blocksize;
        posY = coordY / blocksize;
        pos = posY * ncollumn + posX;
		return pos;
	}

	public void setBlinky(Blinky blinky){}

	public void canMove(){}

	public void cantMove() {}

	private void changeMode()
	{
        if(currentMode == Mode.Scatter)
        {
            currentMode = Mode.Chase;
        }
        else
        {
            currentMode = Mode.Scatter;
            timer1.restart();
        }
	}

	public void stopFrighten()
	{
		currentMode = lastMode;
		timer2.stop();
		timer1.start();
	}

	public void setScatterMode()
	{
        currentMode = Mode.Scatter;
		timer1.setInitialDelay(7000);
        timer1.setDelay(20000);
        timer1.restart();
	}

	private double calculateDistance(int posX_, int posY_)
	{
        return ((posX_ - posTargetX) * (posX_ - posTargetX) + (posY_ - posTargetY) * (posY_ - posTargetY));
	}

	public void move()
	{
        if(coordX % blocksize == blocksize/2 && coordY % blocksize == blocksize/2) // if blinky enter in center of the cell
		{
			directionX = nextDirectionX;
			directionY = nextDirectionY;

			if(currentMode == Mode.Frightened || currentMode == Mode.FrightenedEnd)
			{
				currentSpeed = frightenSpeed;

				int min = 0;
				int maxY = nrow;
				int maxX = ncollumn;
				int rangeY = maxY - min;
				int rangeX = maxX - min;

				Random rn = new Random();
				posTargetX =  rn.nextInt(rangeX) + min;
				posTargetY =  rn.nextInt(rangeY) + min;
			}
			else
			{
				currentSpeed = normalSpeed;

				if(currentMode == Mode.Scatter)
				{
					posTargetX = ncollumn - 1;
					posTargetY = -2;
				}
				else if(currentMode == Mode.Chase)
				{
					posTargetX = pacman.getPosX();
					posTargetY = pacman.getPosY();
				}
			}

            posX = coordX / blocksize;
            posY = coordY / blocksize;
            pos = posY * ncollumn + posX;

            int nextPosX = posX + directionX;
            int nextPosY = posY + directionY;
            int nextPos = nextPosY * ncollumn + nextPosX;

            double distance[] = new double[4];
            distance[0] = calculateDistance(nextPosX, nextPosY - 1); // up cell
            distance[1] = calculateDistance(nextPosX - 1, nextPosY); // left cell
            distance[2] = calculateDistance(nextPosX, nextPosY + 1); // down cell
            distance[3] = calculateDistance(nextPosX + 1, nextPosY); // right cell

			obstacleOnTop = (screenData[nextPos - ncollumn] == ScreenData.Wall);
			obstacleOnBottom = (screenData[nextPos + ncollumn] == ScreenData.Wall) ||
							   (screenData[nextPos + ncollumn] == ScreenData.GhostDoor);
			obstacleOnLeft = (screenData[nextPos - 1] == ScreenData.Wall);
			obstacleOnRight = (screenData[nextPos + 1] == ScreenData.Wall);

            boolean is_possible[] = new boolean[4];
            is_possible[0] = !obstacleOnTop && directionY != DOWN;
            is_possible[1] = !obstacleOnLeft && directionX != RIGHT;
            is_possible[2] = !obstacleOnBottom && directionY != UP;
            is_possible[3] = !obstacleOnRight && directionX != LEFT;

			int choice = -1;
			for(int i = 0; i < 4; i++)
			{
				if(is_possible[i] && (choice == -1 || distance[i] < distance[choice]))
				{
					choice = i;
				}
			}

			switch(choice)
			{
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

        if(directionX == LEFT && !obstacleOnLeft && (screenData[pos-1] == ScreenData.LeftPortal))
        {
            coordX += blocksize*(ncollumn - 2);
        }
        else if(directionX == RIGHT && !obstacleOnRight && (screenData[pos+1] == ScreenData.RightPortal))
        {
            coordX -= blocksize*(ncollumn - 2);
        }
        else
        {
			coordX += directionX * currentSpeed;
			coordY += directionY * currentSpeed;
        }
    }

    public void returnToInitialPosition()
    {
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
        if(currentMode != Mode.Frightened && currentMode != Mode.FrightenedEnd)
        {
            timer1.stop();
            lastMode = currentMode;
        }

        currentMode = Mode.Frightened;
        timer2.restart();

        nextDirectionX = -directionX;
        nextDirectionY = -directionY;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
        if(currentMode == Mode.Frightened)
        {
            currentMode = Mode.FrightenedEnd;
            timer2.setDelay(4000);
        }
        else if(currentMode == Mode.FrightenedEnd)
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
