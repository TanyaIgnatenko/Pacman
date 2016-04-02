package ru.nsu.ignatenko;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class Controller  implements ActionListener
{
    private int screenData[];

    private final int LEFT = -1;
    private final int UP = -1;
    private final int RIGHT = 1;
    private final int DOWN = 1;
    private final int NO_MOTION = 0;

    private boolean ingame = false;

    private PacManGame pacmanGame;
    private View view;
    
    private Timer timer = new Timer(45, this);
    
    public void addView(View view_)
    {
        view = view_;
    }

    public void addPacManGame(PacManGame pacmanGame_)
    {
        pacmanGame = pacmanGame_;
        screenData = pacmanGame.getScreenData();
    }
    
    public int getScore(){return pacmanGame.getScore();}
    public int getCountOfLives(){return pacmanGame.getCountOfLives();}

    public int getBlinkyCoordX(){return pacmanGame.getBlinkyCoordX();}
    public int getPinkyCoordX(){return pacmanGame.getPinkyCoordX();}
    public int getInkyCoordX(){return pacmanGame.getInkyCoordX();}
    public int getClydeCoordX(){return pacmanGame.getClydeCoordX();}
    public int getPacManCoordX(){return pacmanGame.getPacManCoordX();}

    public int getBlinkyCoordY(){return pacmanGame.getBlinkyCoordY();}
    public int getPinkyCoordY(){return pacmanGame.getPinkyCoordY();}
    public int getInkyCoordY(){return pacmanGame.getInkyCoordY();}
    public int getClydeCoordY(){return pacmanGame.getClydeCoordY();}
    public int getPacManCoordY(){return pacmanGame.getPacManCoordY();}

    public int getPacManDirectionX(){return pacmanGame.getPacManDirectionX();}
    public int getPacManDirectionY(){return pacmanGame.getPacManDirectionY();}

    public Mode getBlinkyMode(){return pacmanGame.getBlinkyMode();}
    public Mode getPinkyMode(){return pacmanGame.getPinkyMode();}
    public Mode getInkyMode(){return pacmanGame.getInkyMode();}
    public Mode getClydeMode(){return pacmanGame.getClydeMode();}

    public void processKey(int key)
    {
        if (ingame)
        {
            if (key == KeyEvent.VK_LEFT)
            {
                pacmanGame.setPacManDirection(LEFT, NO_MOTION);
            }
            else if (key == KeyEvent.VK_RIGHT)
            {
                pacmanGame.setPacManDirection(RIGHT, NO_MOTION);
            }
            else if (key == KeyEvent.VK_UP)
            {
                pacmanGame.setPacManDirection(NO_MOTION, UP);
            }
            else if (key == KeyEvent.VK_DOWN)
            {
                pacmanGame.setPacManDirection(NO_MOTION, DOWN);
            }
            else if (key == KeyEvent.VK_PAUSE)
            {
                if (timer.isRunning())
                {
                    timer.stop();
                }
                else
                {
                    timer.start();
                }
            }
        }
        else if(key == 's' || key == 'S')
        {
            view.startGame(screenData);
            ingame = true;
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(ingame)
        {
            pacmanGame.play();
        }
        view.repaint();
    }

    public void restart()
    {
        pacmanGame.recoverData();
        screenData = pacmanGame.getScreenData();
        view.startGame(screenData);
        ingame = true;
        timer.start();
    }
    
    public void gameOver()
    {
        view.setGameOver();
        ingame = false;
    }
    
    public void win()
    {
        view.setWin();
        ingame = false;
    }
}
