package ru.nsu.ignatenko;

import java.util.Arrays;

public class PacManGame
{
    private final int screenDataPattern[] = 
            {
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                1,2,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,2,1,
                1,3,1,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,1,3,1,
                1,2,1,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,1,2,1,
                1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
                1,2,1,1,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,2,1,
                1,2,2,2,2,2,1,2,1,1,1,1,1,1,1,2,1,2,2,2,2,2,1,
                1,1,1,1,1,2,1,2,2,2,2,1,2,2,2,2,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,1,1,1,0,1,0,1,1,1,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,0,1,1,1,6,1,1,1,0,1,2,1,1,1,1,1,
                4,0,0,0,0,2,0,0,1,0,0,0,0,0,1,0,0,2,0,0,0,0,5,
                1,1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1,1,
                1,1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1,1,
                1,2,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,2,1,
                1,2,1,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,1,2,1,
                1,3,2,2,1,2,2,2,2,2,2,0,2,2,2,2,2,2,1,2,2,3,1,
                1,1,1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,2,1,1,1,
                1,2,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,2,1,
                1,2,1,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,1,2,1,
                1,2,1,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,1,2,1,
                1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
            };

    private int screenData[];

    private final int nrow = 25;
    private final int ncollumn = 23;
    private final int blocksize = 24;

    private final int SUPER_DOT = 3;
    private final int DOT = 2;

    private final Ghost ghosts[] = new Ghost[4];
    private final PacMan pacman;

    private final int countOfAllDots = 194;
    private int leftLives = 3;
    private int score = 0;

    private boolean pacmanDead = false;

    Controller controller;

    public PacManGame()
    {
        screenData = Arrays.copyOf(screenDataPattern, nrow*ncollumn);
        pacman = new PacMan(screenData, nrow, ncollumn, blocksize, 11, 18);
        ghosts[0] = new Blinky(screenData, nrow, ncollumn, blocksize, 11, 9);
        ghosts[1] = new Pinky(screenData, nrow, ncollumn, blocksize, 11, 11);
        ghosts[2] = new Inky(screenData, nrow, ncollumn, blocksize, 10, 11);
        ghosts[3] = new Clyde(screenData, nrow, ncollumn, blocksize, 12, 11);

        ghosts[2].setBlinky((Blinky) ghosts[0]);

        for(Ghost ghost : ghosts)
        {
            ghost.setVictim(pacman);
        }
    }

    public void addController(Controller controller_)
    {
        controller = controller_;
    }

    public void play()
    {
        pacman.move();
        for(Ghost ghost : ghosts)
        {
            ghost.move();
        }

        int pacmanPos = pacman.getPos();
        synchronized((Object)pacmanPos)
        {
            if (screenData[pacmanPos] == DOT)
            {
                screenData[pacmanPos] -= DOT;
                score += 10;
            }
            else if (screenData[pacmanPos] == SUPER_DOT)
            {
                screenData[pacmanPos] -= SUPER_DOT;
                score += 50;

                for (Ghost ghost : ghosts)
                {
                    ghost.setFrightenedMode();
                }
            }

            for (Ghost ghost : ghosts)
            {
                if (ghost.getPos() == pacmanPos)
                {
                    if (ghost.getMode() == Mode.Frightened)
                    {
                        score += 200;
                        ghost.returnToInitialPosition();
                        ghost.stopFrighten();
                    }
                    else
                    {
                        --leftLives;
                        pacmanDead = true;
                        pacman.returnToInitialPosition();
                    }
                }
            }
        }

        if(pacmanDead)
        {
            for(Ghost ghost : ghosts)
            {
                ghost.returnToInitialPosition();
                ghost.makeScatter();
            }

            if(leftLives == 0)
            {
                controller.gameOver();
                return;
            }
            pacmanDead = false;
        }

        int currentCountOfDots = countNumberOfDots();
        if(currentCountOfDots == countOfAllDots - 30)
        {
            for(Ghost ghost : ghosts)
            {
                if(ghost.getName().equals("Inky"))
                {
                    ghost.canMove();
                }
            }
        }
        else if(currentCountOfDots == countOfAllDots/3)
        {
            for(Ghost ghost : ghosts)
            {
                if(ghost.getName().equals("Clyde"))
                {
                    ghost.canMove();
                }
            }
        }
        else if(currentCountOfDots == 0)
        {
            for(Ghost ghost : ghosts)
            {
                ghost.returnToInitialPosition();
                ghost.makeScatter();
            }
            controller.win();
        }
    }

    public void recoverData()
    {
        screenData = Arrays.copyOf(screenDataPattern, nrow*ncollumn);
        pacmanDead = false;
        leftLives = 3;
        score = 0;

        pacman.returnToInitialPosition();
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Inky") || ghost.getName().equals("Clyde"))
            {
                ghost.cantMove();
            }
            ghost.returnToInitialPosition();
            ghost.makeScatter();
        }
    }

    private int countNumberOfDots()
    {
        int count = 0;
        for(int val : screenData)
        {
            if(val == DOT || val == SUPER_DOT)
            {
                ++count;
            }
        }
        return count;
    }

    public int getScore(){return score;}

    public int getCountOfLives(){return leftLives;}

    public int [] getScreenData(){return screenData;}

    public void setPacManDirection(int directionX, int directionY){pacman.setDirection(directionX, directionY);}

    public int getPacManDirectionX(){return pacman.getDirectionX();}
    public int getPacManDirectionY(){return pacman.getDirectionY();}

    public int getPacManCoordX() {return pacman.getCoordX();}
    public int getPacManCoordY() {return pacman.getCoordY();}

    public int getBlinkyCoordX()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Blinky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getPinkyCoordX()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Pinky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getInkyCoordX()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Inky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getClydeCoordX()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Clyde"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getBlinkyCoordY()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Blinky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getPinkyCoordY()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Pinky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getInkyCoordY()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Inky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getClydeCoordY()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Clyde"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public Mode getBlinkyMode()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Blinky"))
            {
                return ghost.getMode();
            }
        }
        return Mode.Scatter;
    }

    public Mode getPinkyMode()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Pinky"))
            {
                return ghost.getMode();
            }
        }
        return Mode.Scatter;
    }

    public Mode getInkyMode()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Inky"))
            {
                return ghost.getMode();
            }
        }
        return Mode.Scatter;
    }

    public Mode getClydeMode()
    {
        for(Ghost ghost : ghosts)
        {
            if(ghost.getName().equals("Clyde"))
            {
                return ghost.getMode();
            }
        }
        return Mode.Scatter;
    }
}
