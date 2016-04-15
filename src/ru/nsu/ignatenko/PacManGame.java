package ru.nsu.ignatenko;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

enum Direction
{
    Left, Right, Up, Down
};

public class PacManGame
{
    private int screenData[];

    private final int nrow = 25;
    private final int ncollumn = 23;
    private final int blocksize = 24;

    private final int SUPER_DOT = 3;
    private final int DOT = 2;

    private final int BLINKY_PLACE = 7;
    private final int PINKY_PLACE = 8;
    private final int INKY_PLACE = 9;
    private final int CLYDE_PLACE = 10;
    private final int PACMAN_PLACE = 11;

    private Map<String, Integer> ghostPlaces = new HashMap<>(4);

    private Ghost ghosts[] = new Ghost[4];
    private PacMan pacman;

    private final int countOfAllDots = 194;
    private final int mazeCount = 3;
    private int mazeNumber = 0;
    private String mazeFiles[] = new String[mazeCount];
    private int leftLives = 3;
    private int score = 0;

    private boolean pacmanDead = false;

    Controller controller;

    public PacManGame()
    {
        mazeFiles[0] = "map1.txt";
        mazeFiles[1] = "map2.txt";
        mazeFiles[2] = "map3.txt";

        ghostPlaces.put("Blinky", BLINKY_PLACE);
        ghostPlaces.put("Pinky", PINKY_PLACE);
        ghostPlaces.put("Inky", INKY_PLACE);
        ghostPlaces.put("Clyde", CLYDE_PLACE);
    }

    public void initGame()
    {
        screenData  = loadScreenData(mazeFiles[mazeNumber]);
        pacman = new PacMan(screenData, nrow, ncollumn, blocksize, 11, 18);
        ghosts[0] = new Blinky(screenData, nrow, ncollumn, blocksize, 11, 9);
        ghosts[1] = new Pinky(screenData, nrow, ncollumn, blocksize, 11, 11);
        ghosts[2] = new Inky(screenData, nrow, ncollumn, blocksize, 10, 11);
        ghosts[3] = new Clyde(screenData, nrow, ncollumn, blocksize, 12, 11);

        placePacman();
        for (Ghost ghost : ghosts)
        {
            placeGhost(ghost);
        }

        ghosts[2].setBlinky((Blinky) ghosts[0]);

        for (Ghost ghost : ghosts)
        {
            ghost.setVictim(pacman);
        }
    }

    public void addController(Controller controller_)
    {
        controller = controller_;
    }

    public void setMaze(int number)
    {
        mazeNumber = number;
    }

    public void placePacman()
    {
        int posX = 0;
        int posY = 0;
        for (int data : screenData)
        {
            if(data == PACMAN_PLACE)
            {
                pacman.setStartPos(posX, posY);
                break;
            }
            if(posX == ncollumn-1)
            {
                posX = 0;
                ++posY;
            }
            else
            {
                ++posX;
            }
        }
    }

    public void placeGhost(Ghost ghost)
    {
        int posX = 0;
        int posY = 0;
        for (int data : screenData)
        {
            if(data == ghostPlaces.get(ghost.getName()))
            {
                ghost.setStartPos(posX, posY);
                break;
            }
            if(posX == ncollumn-1)
            {
                posX = 0;
                ++posY;
            }
            else
            {
                ++posX;
            }
        }
    }


    public void play()
    {
            pacman.move();
            int pacmanPos = pacman.getPos();

            checkCollisions(pacmanPos);
            for (Ghost ghost : ghosts)
            {
                ghost.move();
            }
            checkCollisions(pacmanPos);

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

            int currentCountOfDots = countNumberOfDots();
            if (currentCountOfDots == countOfAllDots - 30)
            {
                for (Ghost ghost : ghosts)
                {
                    if (ghost.getName().equals("Inky"))
                    {
                        ghost.canMove();
                    }
                }
            }
            else if (currentCountOfDots == countOfAllDots / 3)
            {
                for (Ghost ghost : ghosts)
                {
                    if (ghost.getName().equals("Clyde"))
                    {
                        ghost.canMove();
                    }
                }
            }
            else if (currentCountOfDots == 0)
            {
                for (Ghost ghost : ghosts)
                {
                    ghost.returnToInitialPosition();
                    ghost.setScatterMode();
                }
                controller.win();
            }
    }


    private void checkCollisions(int pacmanPos)
    {
        for (Ghost ghost : ghosts)
        {
            
            if (ghost.getPos() == pacmanPos)
            {
                if (ghost.getMode() == Mode.Frightened || ghost.getMode() == Mode.FrightenedEnd)
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

        if (pacmanDead)
        {
            for (Ghost ghost : ghosts)
            {
                ghost.returnToInitialPosition();
                ghost.setScatterMode();
            }

            if (leftLives == 0)
            {
                controller.gameOver();
                return;
            }
            pacmanDead = false;
        }
    }

    public void stop()
    {
        for (Ghost ghost : ghosts)
        {
            ghost.stop();
        }
    }


    public void recoverData()
    {
        screenData  = loadScreenData(mazeFiles[mazeNumber]);
        pacmanDead = false;
        leftLives = 3;
        score = 0;

        pacman.returnToInitialPosition();
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Inky") || ghost.getName().equals("Clyde"))
            {
                ghost.cantMove();
            }
            ghost.returnToInitialPosition();
            ghost.setScatterMode();
        }
    }

    private int countNumberOfDots()
    {
        int count = 0;
        for (int val : screenData)
        {
            if (val == DOT || val == SUPER_DOT)
            {
                ++count;
            }
        }
        return count;
    }

    public int getScore()
    {
        return score;
    }

    public int getCountOfLives()
    {
        return leftLives;
    }

    public int[] getScreenData()
    {
        return screenData;
    }

    public void setPacManDirection(int directionX, int directionY)
    {
        pacman.setDirection(directionX, directionY);
    }

    public int getPacManDirectionX()
    {
        return pacman.getDirectionX();
    }

    public int getPacManDirectionY()
    {
        return pacman.getDirectionY();
    }

    public int getPacManCoordX()
    {
        return pacman.getCoordX();
    }

    public int getPacManCoordY()
    {
        return pacman.getCoordY();
    }

    public int getGhostDirection(String ghostName)
    {
        int direction = 0;
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals(ghostName))
            {
                direction = ghost.getDirection().ordinal();
            }
        }
        return direction;
    }

    public Mode getGhostMode(String ghostName)
    {
        Mode mode = Mode.Scatter;
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals(ghostName))
            {
                mode = ghost.getMode();
            }
        }
        return mode;
    }

    public int getBlinkyCoordX()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Blinky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getPinkyCoordX()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Pinky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getInkyCoordX()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Inky"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getClydeCoordX()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Clyde"))
            {
                return ghost.getCoordX();
            }
        }
        return -1;
    }

    public int getBlinkyCoordY()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Blinky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getPinkyCoordY()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Pinky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getInkyCoordY()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Inky"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int getClydeCoordY()
    {
        for (Ghost ghost : ghosts)
        {
            if (ghost.getName().equals("Clyde"))
            {
                return ghost.getCoordY();
            }
        }
        return -1;
    }

    public int[] loadScreenData(String filename)
    {
        int[] screenData;
        Map<Integer, Integer> symbolCodes;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        try(InputStream file = systemClassLoader.getResourceAsStream(filename))
        {
            if(file != null)
            {
                screenData = new int[nrow * ncollumn];
                symbolCodes = new HashMap<>();

                symbolCodes.put(32, 0);   //  [space] - empty
                symbolCodes.put(35, 1);   //  # - wall
                symbolCodes.put(46, 2);   //  . - dot
                symbolCodes.put(111, 3);  //  o - super dot
                symbolCodes.put(60, 4);   //  < - left portal
                symbolCodes.put(62, 5);   //  > - right portal
                symbolCodes.put(43, 6);   //  + - ghost door
                symbolCodes.put(66, 7);   //  B - Blinky start position
                symbolCodes.put(80, 8);   //  P - Pinky start position
                symbolCodes.put(73, 9);   //  I - Inky start position
                symbolCodes.put(67, 10);  //  C - Clyde start position
                symbolCodes.put(64, 11);  //  @ - Pacman start position

                final int END_OF_LINE1 = 13; // /r
                final int END_OF_LINE2 = 10; // /n
                final int END_OF_FILE = -1;

                int с;
                Integer output;
                for(int i = 0; i < nrow; i++)
                {
                    for(int j = 0; j < ncollumn; j++)
                    {
                        с = file.read();
                        if((output = symbolCodes.get(с)) != null)
                        {
                            screenData[i * ncollumn + j] = output.intValue();
                        }
                        else
                        {
                            throw new IOException("Invalid file " + i + " " + j);
                        }
                    }
                    с = file.read();
                    if (с != END_OF_LINE2)
                    {
                        if(!(с == END_OF_LINE1 && file.read() == END_OF_LINE2))
                        {
                            throw new IOException("Invalid file " + i);
                        }
                    }
                }
                if(file.read() != END_OF_FILE)
                {
                    throw new IOException("Garbage in the end of file");
                }
            }
            else
            {
                throw new IOException("File not found");
            }
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException();
        }
        return screenData;
    }
}

