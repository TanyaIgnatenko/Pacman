package ru.nsu.ignatenko;

class PacMan
{
    private final int WALL = 1;
    private final int LEFT_PORTAL = 4;
    private final int RIGHT_PORTAL = 5;
    private final int GHOST_DOOR = 6;

    private int screenData[];
    private int blocksize;
    private int ncollumn;
    private int nrow;

    private int coordX;
    private int coordY;
    private int startPosX;
    private int startPosY;
    private int posX;
    private int posY;
    private int pos;

    private final int LEFT = -1;
    private final int UP = -1;
    private final int RIGHT = 1;
    private final int DOWN = 1;
    private final int NO_MOTION = 0;

    private int directionX = LEFT;
    private int directionY = NO_MOTION;
    private int requiredDirectionX;
    private int requiredDirectionY;

    private int speed = 6;

    private boolean obstacleOnTop;
	private boolean obstacleOnBottom;
	private boolean obstacleOnLeft;
	private boolean obstacleOnRight;

	public PacMan(int screenData_[], int nrow_, int ncollumn_, int blocksize_, int x, int y)
	{
        screenData = screenData_;
        nrow = nrow_;
        ncollumn = ncollumn_;
        blocksize = blocksize_;
        
        startPosX = x;
        startPosY = y;
        posX = startPosX;
        posY = startPosY;
        pos = posY * ncollumn + posX;
        coordX = posX * blocksize + blocksize/2;
        coordY = posY * blocksize + blocksize/2;

        obstacleOnTop 	= 	(screenData[pos - ncollumn] == WALL) || (screenData[pos - ncollumn + directionX] == WALL);
        obstacleOnBottom =	(screenData[pos + ncollumn] == WALL) || (screenData[pos + ncollumn + directionX] == WALL);
        obstacleOnLeft 	=	(screenData[pos - 1] == WALL) || (screenData[pos - 1 + directionY*ncollumn] == WALL);
        obstacleOnRight =	(screenData[pos + 1] == WALL) || (screenData[pos + 1 + directionY*ncollumn] == WALL);
	}

	public void setDirection(int directionX_, int directionY_)
    {
        requiredDirectionX = directionX_;
        requiredDirectionY = directionY_;
	}

    public void setStartPos(int x, int y)
    {
        startPosX = x;
        startPosY = y;
        posX = startPosX;
        posY = startPosY;
        pos = posY * ncollumn + posX;
        coordX = posX * blocksize + blocksize/2;
        coordY = posY * blocksize + blocksize/2;

        obstacleOnTop 	= 	(screenData[pos - ncollumn] == WALL) || (screenData[pos - ncollumn + directionX] == WALL);
        obstacleOnBottom =	(screenData[pos + ncollumn] == WALL) || (screenData[pos + ncollumn + directionX] == WALL);
        obstacleOnLeft 	=	(screenData[pos - 1] == WALL) || (screenData[pos - 1 + directionY*ncollumn] == WALL);
        obstacleOnRight =	(screenData[pos + 1] == WALL) || (screenData[pos + 1 + directionY*ncollumn] == WALL);
    }

    public void returnToInitialPosition()
    {
        posX = startPosX;
        posY = startPosY;
        pos = posY * ncollumn + posX;
        coordX = posX * blocksize + blocksize/2;
        coordY = posY * blocksize + blocksize/2;

        directionX = LEFT;
        directionY = 0;
        requiredDirectionX = 0;
        requiredDirectionY = 0;
    }

	public void move()
    {
		if(coordX % blocksize == blocksize/2 && coordY % blocksize == blocksize/2)
		{
            obstacleOnTop 	= 	(screenData[pos - ncollumn] == WALL) || (screenData[pos - ncollumn + directionX] == WALL);
            obstacleOnBottom =	(screenData[pos + ncollumn] == WALL) || (screenData[pos + ncollumn + directionX] == WALL)
                             || (screenData[pos + ncollumn] == GHOST_DOOR)|| (screenData[pos + ncollumn + directionX] == GHOST_DOOR);
            obstacleOnLeft 	=	(screenData[pos - 1] == WALL) || (screenData[pos - 1 + directionY*ncollumn] == WALL);
            obstacleOnRight =	(screenData[pos + 1] == WALL) || (screenData[pos + 1 + directionY*ncollumn] == WALL);

            boolean reqObstacleOnTop 	= (screenData[pos - ncollumn] == WALL) || (screenData[pos - ncollumn + requiredDirectionX] == WALL);
            boolean reqObstacleOnBottom =	(screenData[pos + ncollumn] == WALL) || (screenData[pos + ncollumn + requiredDirectionX] == WALL)
                                         || (screenData[pos + ncollumn] == GHOST_DOOR)|| (screenData[pos + ncollumn + requiredDirectionX] == GHOST_DOOR);
            boolean reqObstacleOnLeft = (screenData[pos - 1] == WALL) || (screenData[pos - 1 + requiredDirectionY*ncollumn] == WALL);
            boolean reqObstacleOnRight = (screenData[pos + 1] == WALL) || (screenData[pos + 1 + requiredDirectionY*ncollumn] == WALL);

            if(requiredDirectionX == LEFT && !reqObstacleOnLeft)
            {
                directionX = requiredDirectionX;
				directionY = NO_MOTION;
                obstacleOnLeft = reqObstacleOnLeft;
            }
            else if(requiredDirectionX == RIGHT && !reqObstacleOnRight)
            {
                directionX = requiredDirectionX;
				directionY = NO_MOTION;
                obstacleOnRight = reqObstacleOnRight;
            }
            else if(requiredDirectionY == UP && !reqObstacleOnTop)
            {
                directionY = requiredDirectionY;
				directionX = NO_MOTION;
                obstacleOnTop = reqObstacleOnTop;
            }
            else if(requiredDirectionY == DOWN && !reqObstacleOnBottom)
            {
                directionY = requiredDirectionY;
				directionX = NO_MOTION;
                obstacleOnBottom = reqObstacleOnBottom;
            }
		}

        if(directionX == LEFT && !obstacleOnLeft && (screenData[pos] == LEFT_PORTAL))
        {
            coordX += blocksize*(ncollumn - 1);
        }
        else if(directionX == RIGHT && !obstacleOnRight && (screenData[pos] == RIGHT_PORTAL))
        {
            coordX -= blocksize*(ncollumn - 1);
        }
		else if(directionX == RIGHT && !obstacleOnRight)
		{
            coordX += speed;
		}
        else if(directionX == LEFT && !obstacleOnLeft)
		{
			coordX -= speed;
		}
		else if(directionY == DOWN && !obstacleOnBottom)
		{
			coordY += speed;
		}
		else if(directionY == UP && !obstacleOnTop)
		{
			coordY -= speed;
		}

        posX = coordX / blocksize;
        posY = coordY / blocksize;
        pos = posY * ncollumn + posX;
	}

	public int getCoordX() {
		return coordX;
	}

	public int getCoordY() {
		return coordY;
	}

    public int getPosX() {return posX;}

    public int getPosY() {
        return posY;
    }

    public int getPos()
    {
        posX = coordX / blocksize;
        posY = coordY / blocksize;
        pos = posY * ncollumn + posX;
        return pos;
    }

	public int getDirectionX() {
		return directionX;
	}

	public int getDirectionY() {
		return directionY;
	}
}