package ru.nsu.ignatenko;

enum Mode
{
    Scatter, Chase, Frightened, FrightenedEnd
}

public interface Ghost
{
    public void move();
    public void setFrightenedMode();
    public void returnToInitialPosition();
    public void stopFrighten();
    public void setScatterMode();
    public void setStartPos(int posX, int posY);
    public int getPos();
    public int getCoordX();
    public int getCoordY();
    public Mode getMode();
    public String getName();
    public void canMove();
    public void cantMove();
    public void stop();
    public void setVictim(PacMan pacman);
    public void setBlinky(Blinky blinky);
    public Direction getDirection();

}
