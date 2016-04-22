package ru.nsu.ignatenko;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.HashMap;
import java.util.Map;

public class View extends JPanel
{
	private int width;
	private int height;
	private final Font mediumfont = new Font("Helvetica", Font.BOLD, 20);
	private final Font bigfont = new Font("Helvetica", Font.BOLD, 25);

	private final Color mazecolor = new Color(96, 128, 255);

	private final Image rulesImage = new ImageIcon("images/menu/rules.png").getImage();
	private final ImageIcon nextTipButtonIcon = new ImageIcon("images/menu/NextTipButton.png");
    private final ImageIcon nextButtonIcon = new ImageIcon("images/menu/NextButton.png");
	private final ImageIcon backButtonIcon = new ImageIcon("images/menu/BackButton.png");
    private final ImageIcon chooseButtonIcon = new ImageIcon("images/menu/ChooseButton.png");

	private Controller controller;

	private GamePanel gamePanel;
	private MainPanel mainPanel;
	private TipsPanel tipsPanel;
	private RulesPanel rulesPanel;
	private IntroPanel introPanel;
	private ChooseMazePanel chooseMazePanel;

	private JPanel actualPanel = null;

	private JButton restart;

    private boolean ingame = false;

	public View(int width_, int height_, Controller controller_)
	{
		width = width_;
		height = height_;
		setPreferredSize(new Dimension(width, height));

		introPanel = new IntroPanel();
        mainPanel = new MainPanel();
        tipsPanel = new TipsPanel();
        rulesPanel = new RulesPanel();
        chooseMazePanel= new ChooseMazePanel();
        gamePanel = new GamePanel(width, height, controller_);

		add(introPanel);
        add(mainPanel);
		add(tipsPanel);
		add(gamePanel);
		add(rulesPanel);
        add(chooseMazePanel);

		introPanel.setVisible(false);
        mainPanel.setVisible(false);
		tipsPanel.setVisible(false);
		gamePanel.setVisible(false);
		rulesPanel.setVisible(false);
        chooseMazePanel.setVisible(false);

		controller = controller_;

		setShownPanel(introPanel);
	}

    public void startGame(ScreenData[] screenData_)
    {
        gamePanel.setScreenData(screenData_);
        setShownPanel(gamePanel);
        gamePanel.requestFocus();
        ingame = true;
    }

    public void restart()
    {
        gamePanel.init();
    }

	public void setWin()
	{
        gamePanel.setWin();
	}

	public void setGameOver()
	{
        gamePanel.setGameOver();
	}

    public void pauseGame()
    {
        setShownPanel(mainPanel);
    }

	private void setShownPanel(JPanel panel)
	{
		if(actualPanel != null)
		{
			actualPanel.setVisible(false);
		}
		actualPanel = panel;
        actualPanel.setVisible(true);
        actualPanel.repaint();
    }

	private class IntroPanel extends JPanel
	{
		private JButton playButton = new JButton("Play");
		private JButton rulesButton = new JButton("Rules");
		private JButton hintsButton = new JButton("Hints");
		private JButton exitButton = new JButton("Exit");


		public IntroPanel()
		{
			setBackground(Color.black);
			setPreferredSize(new Dimension(width, height));

			playButton.setMaximumSize(new Dimension(300, 50));
			rulesButton.setMaximumSize(new Dimension(300, 50));
			hintsButton.setMaximumSize(new Dimension(300, 50));
			exitButton.setMaximumSize(new Dimension(300, 50));

			playButton.setBackground(mazecolor);
			rulesButton.setBackground(mazecolor);
			hintsButton.setBackground(mazecolor);
			exitButton.setBackground(mazecolor);

			playButton.setForeground(Color.black);
			rulesButton.setForeground(Color.black);
			hintsButton.setForeground(Color.black);
			exitButton.setForeground(Color.black);

			playButton.setBorderPainted(false);
			rulesButton.setBorderPainted(false);
			hintsButton.setBorderPainted(false);
			exitButton.setBorderPainted(false);

			playButton.setFont(mediumfont);
			rulesButton.setFont(mediumfont);
			hintsButton.setFont(mediumfont);
			exitButton.setFont(mediumfont);

			playButton.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
                    setShownPanel(chooseMazePanel);
                }
			});
			rulesButton.addMouseListener(new MouseAdapter()
			{
				@Override
                public void mouseReleased(MouseEvent e)
				{
					setShownPanel(rulesPanel);
				}
			});
			hintsButton.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					setShownPanel(tipsPanel);
				}
			});
			exitButton.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					System.exit(0);
				}
			});

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            hintsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createRigidArea(new Dimension(0, height/2 - 160)));
			add(playButton);
			add(Box.createRigidArea(new Dimension(0, 20)));
			add(rulesButton);
			add(Box.createRigidArea(new Dimension(0, 20)));
			add(hintsButton);
			add(Box.createRigidArea(new Dimension(0, 20)));
			add(exitButton);
		}

		@Override
		protected void paintComponent(Graphics g2d)
		{
			super.paintComponent(g2d);
			g2d.setColor(mazecolor);
			g2d.fillRoundRect(playButton.getX() - 4, playButton.getY() - 4, 310, 58, 70, 70);
			g2d.fillRoundRect(rulesButton.getX() - 4, rulesButton.getY() - 4, 310, 58, 70, 70);
			g2d.fillRoundRect(hintsButton.getX() - 4, hintsButton.getY() - 4, 310, 58, 70, 70);
			g2d.fillRoundRect(exitButton.getX() - 4, exitButton.getY() - 4, 310, 58, 70, 70);
		}
	}

    private class MainPanel extends JPanel
    {
        private JButton resumeButton = new JButton("Resume");
        private JButton newGameButton = new JButton("New Game");
        private JButton rulesButton = new JButton("Rules");
        private JButton hintsButton = new JButton("Hints");
        private JButton exitButton = new JButton("Exit");

        public MainPanel()
        {
            setBackground(Color.black);
            setPreferredSize(new Dimension(width, height));

            resumeButton.setMaximumSize(new Dimension(300, 50));
            newGameButton.setMaximumSize(new Dimension(300, 50));
            rulesButton.setMaximumSize(new Dimension(300, 50));
            hintsButton.setMaximumSize(new Dimension(300, 50));
            exitButton.setMaximumSize(new Dimension(300, 50));

            resumeButton.setBackground(mazecolor);
            newGameButton.setBackground(mazecolor);
            rulesButton.setBackground(mazecolor);
            hintsButton.setBackground(mazecolor);
            exitButton.setBackground(mazecolor);

            resumeButton.setForeground(Color.black);
            newGameButton.setForeground(Color.black);
            rulesButton.setForeground(Color.black);
            hintsButton.setForeground(Color.black);
            exitButton.setForeground(Color.black);

            rulesButton.setBorderPainted(false);
            hintsButton.setBorderPainted(false);
            exitButton.setBorderPainted(false);
            resumeButton.setBorderPainted(false);
            newGameButton.setBorderPainted(false);

            resumeButton.setFont(mediumfont);
            newGameButton.setFont(mediumfont);
            rulesButton.setFont(mediumfont);
            hintsButton.setFont(mediumfont);
            exitButton.setFont(mediumfont);

            resumeButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    setShownPanel(gamePanel);
                    gamePanel.requestFocus();
                    controller.resumeGame();
                }
            });
            newGameButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    restart();
                    setShownPanel(chooseMazePanel);
                }
            });
            rulesButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    setShownPanel(rulesPanel);
                }
            });
            hintsButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    setShownPanel(tipsPanel);
                }
            });
            exitButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    System.exit(0);
                }
            });

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            hintsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createRigidArea(new Dimension(0, height/2 - 160)));
            add(newGameButton);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(resumeButton);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(rulesButton);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(hintsButton);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(exitButton);
        }

        @Override
        protected void paintComponent(Graphics g2d)
        {
            super.paintComponent(g2d);
            g2d.setColor(mazecolor);
            g2d.fillRoundRect(resumeButton.getX() - 4, resumeButton.getY() - 4, 310, 58, 70, 70);
            g2d.fillRoundRect(newGameButton.getX() - 4, newGameButton.getY() - 4, 310, 58, 70, 70);
            g2d.fillRoundRect(rulesButton.getX() - 4, rulesButton.getY() - 4, 310, 58, 70, 70);
            g2d.fillRoundRect(hintsButton.getX() - 4, hintsButton.getY() - 4, 310, 58, 70, 70);
            g2d.fillRoundRect(exitButton.getX() - 4, exitButton.getY() - 4, 310, 58, 70, 70);
        }
    }

	private class TipsPanel extends JPanel
	{
        private int idx = 0;
        private final Image hintsImage[] = new Image[6];
        private JPanel buttonPanel = new JPanel();
        private JButton backButton = new JButton(backButtonIcon);
        private JButton nextTipButton = new JButton(nextTipButtonIcon);

            public TipsPanel()
            {
                loadImages();
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                add(Box.createRigidArea(new Dimension(0, height - 110)));
                buttonPanel.setLayout(new BoxLayout( buttonPanel, BoxLayout.X_AXIS));
                buttonPanel.setBackground(Color.black);
                add(buttonPanel);

                backButton.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseReleased(MouseEvent e)
                    {
                        idx = 0;
                        nextTipButton.setVisible(true);
                        if(!ingame)
                        {
                            setShownPanel(introPanel);
                        }
                        else
                        {
                            setShownPanel(mainPanel);
                        }
                    }
                });
                nextTipButton.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseReleased(MouseEvent e)
                    {
                        ++idx;
                        repaint();
                    }
                });

                backButton.setBackground(Color.black);
                nextTipButton.setBackground(Color.black);

                backButton.setFocusPainted(false);
                nextTipButton.setFocusPainted(false);

                backButton.setBorderPainted(false);
                nextTipButton.setBorderPainted(false);

                setPreferredSize(new Dimension(width, height));

                buttonPanel.add(backButton);
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(nextTipButton);
		}

        private void loadImages()
        {
            hintsImage[0] = new ImageIcon("images/menu/hint1.png").getImage();
            hintsImage[1] = new ImageIcon("images/menu/hint2.png").getImage();
            hintsImage[2] = new ImageIcon("images/menu/hint3.png").getImage();
            hintsImage[3] = new ImageIcon("images/menu/hint4.png").getImage();
            hintsImage[4] = new ImageIcon("images/menu/hint5.png").getImage();
            hintsImage[5] = new ImageIcon("images/menu/hint6.png").getImage();
        }

		@Override
		protected void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			graphics.drawImage(hintsImage[idx], 0, 0, this);
            if(idx == 5)
            {
                nextTipButton.setVisible(false);
            }
        }
	}

	private class GamePanel extends JPanel
	{
		private int width;
		private int height;
		private final int mazeIndentX = 25;
		private final int mazeIndentY = 70;

		private ScreenData screenData[];
		private final int nrow = 25;
		private final int ncollumn = 23;
		private final int blocksize = 24;

		private final int LEFT = -1;
		private final int UP = -1;
		private final int RIGHT = 1;

		private final Color mazecolor = new Color(96, 128, 255);
		private final Color dotcolor = new Color(240, 143, 238);
		private final Color ghostDoorColor = new Color(240, 143, 238);
		private final Color scorecolor = new Color(109, 142, 255);
		private final Color winTextColor = new Color(255, 244, 138);
		private final Color gameOverTextColor = new Color(255, 244, 138);

		private final Image blinky[] = new Image[8];
		private final Image pinky[] = new Image[8];
		private final Image inky[] = new Image[8];
		private final Image clyde[] = new Image[8];
		private final Image scaredGhost[] = new Image[2];
		private final Image scaredGhostEnd[] = new Image[4];

		private Image imageBlinky;
		private Image imagePinky;
		private Image imageInky;
		private Image imageClyde;

		private final Image pacmanLeft[] = new Image[4];
		private final Image pacmanRight[] = new Image[4];
		private final Image pacmanDown[] = new Image[4];

		Map<String, Image[]> ghostImages = new HashMap<>(4);
		private final Image pacmanUp[] = new Image[4];

		private final int delayToMoveMouth = 1;
		private final int delayToMoveLegs = 5;

		private int waitPacManToMoveMouth = delayToMoveMouth;
		private int waitGhostsToMoveLegs = delayToMoveLegs;
		private int scaredIdxChangeDelay = 3;

		private final int maxOpenedMouth = 3;
		private final int closedMouth = 0;
		private int openedMouthDegree = maxOpenedMouth;
		private int openedMouthMovementDirection = -1;
		int imageVersion = 0;

		private boolean win = false;
		private boolean gameOver = false;

		private int scaredEndIdx = 0;

		private Controller controller;

		public GamePanel(int w, int h, Controller controller_)
		{
			controller = controller_;
			width = w;
			height = h;
			loadImages();
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
			setPreferredSize(new Dimension(width, height));
			attachRestartButton();
		}

		public void setWin()
		{
			win = true;
		}

		public void setGameOver()
		{
			gameOver = true;
		}

		public void setScreenData(ScreenData[] screenData)
		{
			this.screenData = screenData;
		}

		@Override
		protected void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			Graphics2D g2d = (Graphics2D)graphics;
			drawMaze(g2d);
			drawDots(g2d);
			drawScore(g2d);
			drawLeftLives(g2d);
            drawGhosts(g2d);
			if(gameOver)
			{
				showGameOverScreen(g2d);
			}
			else if(win)
			{
				showWinScreen(g2d);
			}
			else
			{
				drawPacMan(g2d);
			}
			changeAnimationWaiting();
		}

		private void loadImages()
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

			blinky[0] = new ImageIcon("images/ghosts/BlinkyLeft1.png").getImage();
			blinky[1] = new ImageIcon("images/ghosts/BlinkyLeft2.png").getImage();
			blinky[2] = new ImageIcon("images/ghosts/BlinkyRight1.png").getImage();
			blinky[3] = new ImageIcon("images/ghosts/BlinkyRight2.png").getImage();
			blinky[4] = new ImageIcon("images/ghosts/BlinkyUp1.png").getImage();
			blinky[5] = new ImageIcon("images/ghosts/BlinkyUp2.png").getImage();
			blinky[6] = new ImageIcon("images/ghosts/BlinkyDown1.png").getImage();
			blinky[7] = new ImageIcon("images/ghosts/BlinkyDown2.png").getImage();

			pinky[0] = new ImageIcon("images/ghosts/PinkyLeft1.png").getImage();
			pinky[1] = new ImageIcon("images/ghosts/PinkyLeft2.png").getImage();
			pinky[2] = new ImageIcon("images/ghosts/PinkyRight1.png").getImage();
			pinky[3] = new ImageIcon("images/ghosts/PinkyRight2.png").getImage();
			pinky[4] = new ImageIcon("images/ghosts/PinkyUp1.png").getImage();
			pinky[5] = new ImageIcon("images/ghosts/PinkyUp2.png").getImage();
			pinky[6] = new ImageIcon("images/ghosts/PinkyDown1.png").getImage();
			pinky[7] = new ImageIcon("images/ghosts/PinkyDown2.png").getImage();

			inky[0] = new ImageIcon("images/ghosts/InkyLeft1.png").getImage();
			inky[1] = new ImageIcon("images/ghosts/InkyLeft2.png").getImage();
			inky[2] = new ImageIcon("images/ghosts/InkyRight1.png").getImage();
			inky[3] = new ImageIcon("images/ghosts/InkyRight2.png").getImage();
			inky[4] = new ImageIcon("images/ghosts/InkyUp1.png").getImage();
			inky[5] = new ImageIcon("images/ghosts/InkyUp2.png").getImage();
			inky[6] = new ImageIcon("images/ghosts/InkyDown1.png").getImage();
			inky[7] = new ImageIcon("images/ghosts/InkyDown2.png").getImage();

			clyde[0] = new ImageIcon("images/ghosts/ClydeLeft1.png").getImage();
			clyde[1] = new ImageIcon("images/ghosts/ClydeLeft2.png").getImage();
			clyde[2] = new ImageIcon("images/ghosts/ClydeRight1.png").getImage();
			clyde[3] = new ImageIcon("images/ghosts/ClydeRight2.png").getImage();
			clyde[4] = new ImageIcon("images/ghosts/ClydeUp1.png").getImage();
			clyde[5] = new ImageIcon("images/ghosts/ClydeUp2.png").getImage();
			clyde[6] = new ImageIcon("images/ghosts/ClydeDown1.png").getImage();
			clyde[7] = new ImageIcon("images/ghosts/ClydeDown2.png").getImage();

			scaredGhost[0] = new ImageIcon("images/ghosts/ScaredGhost1.png").getImage();
			scaredGhost[1] = new ImageIcon("images/ghosts/ScaredGhost2.png").getImage();
			scaredGhostEnd[0] = new ImageIcon("images/ghosts/ScaredGhostEnd2.png").getImage();
			scaredGhostEnd[1] = new ImageIcon("images/ghosts/ScaredGhostEnd1.png").getImage();
			scaredGhostEnd[2] = scaredGhost[1];
			scaredGhostEnd[3] = scaredGhost[0];

			ghostImages.put("Blinky", blinky);
			ghostImages.put("Pinky", pinky);
			ghostImages.put("Inky", inky);
			ghostImages.put("Clyde", clyde);
		}

		private void changeAnimationWaiting()
		{
			waitPacManToMoveMouth--;
			waitGhostsToMoveLegs--;
			if(waitPacManToMoveMouth == -1)
			{
				waitPacManToMoveMouth = delayToMoveMouth;
			}

			scaredIdxChangeDelay--;
			if(scaredIdxChangeDelay == 0)
			{
				scaredEndIdx = (scaredEndIdx + 2) % 4;
				scaredIdxChangeDelay = 3;
			}
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
			for(int i = 0; i < controller.getCountOfLives(); ++i)
			{
				coordX = i * 28 + mazeIndentX;
				g.drawImage(pacmanLeft[2], coordX, coordY, this);
			}
		}

		private void drawGhosts(Graphics2D g2d)
		{
			imageBlinky = chooseGhostImage("Blinky");
			imagePinky = chooseGhostImage("Pinky");
			imageInky = chooseGhostImage("Inky");
			imageClyde = chooseGhostImage("Clyde");

			int coordX = controller.getBlinkyCoordX() - (blocksize / 2) + mazeIndentX;
			int coordY = controller.getBlinkyCoordY() - (blocksize / 2) + mazeIndentY;
			g2d.drawImage(imageBlinky, coordX, coordY, this);

			coordX = controller.getPinkyCoordX() - (blocksize / 2) + mazeIndentX;
			coordY = controller.getPinkyCoordY() - (blocksize / 2) + mazeIndentY;
			g2d.drawImage(imagePinky, coordX, coordY, this);

			coordX = controller.getInkyCoordX() - (blocksize / 2) + mazeIndentX;
			coordY = controller.getInkyCoordY() - (blocksize / 2) + mazeIndentY;
			g2d.drawImage(imageInky, coordX, coordY, this);

			coordX = controller.getClydeCoordX() - (blocksize / 2) + mazeIndentX;
			coordY = controller.getClydeCoordY() - (blocksize / 2) + mazeIndentY;
			g2d.drawImage(imageClyde, coordX, coordY, this);
		}

		private Image chooseGhostImage(String ghostName)
		{
			Mode mode = controller.getGhostMode(ghostName);
			int direction = controller.getGhostDirection(ghostName);

			if(waitGhostsToMoveLegs == 0)
			{
				if(imageVersion == 1)
				{
					imageVersion = 0;
				}
				else
				{
					imageVersion = 1;
				}
				waitGhostsToMoveLegs = delayToMoveLegs;
			}

			switch(mode)
			{
				case Frightened:
					return scaredGhost[imageVersion];
				case FrightenedEnd:
				{
					return scaredGhostEnd[scaredEndIdx + imageVersion];
				}
				default:
					return ghostImages.get(ghostName)[2 * direction + imageVersion];
			}
		}

		private void drawPacMan(Graphics2D g2d)
		{
			if(waitPacManToMoveMouth == 0)
			{
				openedMouthDegree += openedMouthMovementDirection;

				if(openedMouthDegree == maxOpenedMouth || openedMouthDegree == closedMouth)
				{
					openedMouthMovementDirection = -openedMouthMovementDirection;
				}
			}

			int coordX = controller.getPacManCoordX() - (blocksize / 2) + mazeIndentX;
			int coordY = controller.getPacManCoordY() - (blocksize / 2) + mazeIndentY;

			if(controller.getPacManDirectionX() == LEFT)
			{
				g2d.drawImage(pacmanLeft[openedMouthDegree], coordX, coordY, this);
			}
			else if(controller.getPacManDirectionX() == RIGHT)
			{
				g2d.drawImage(pacmanRight[openedMouthDegree], coordX, coordY, this);
			}
			else if(controller.getPacManDirectionY() == UP)
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

			for(int y = mazeIndentY; y < blocksize * nrow + mazeIndentY; y += blocksize)
			{
				for(int x = mazeIndentX; x < blocksize * ncollumn + mazeIndentX; x += blocksize)
				{
					if(screenData[i] == ScreenData.Wall)
					{
						g2d.fillRect(x, y, blocksize, blocksize);
					}
					else if(screenData[i] == ScreenData.GhostDoor)
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

			for(int y = mazeIndentY; y < blocksize * nrow + mazeIndentY; y += blocksize)
			{
				for(int x = mazeIndentX; x < blocksize * ncollumn + mazeIndentX; x += blocksize)
				{
					if(screenData[i] == ScreenData.Dot)
					{
						g2d.fillRect(x + 11, y + 11, 4, 4);
					}
					else if(screenData[i] == ScreenData.SuperDot)
					{
						g2d.fillOval(x + 6, y + 3, 13, 13);
					}
					i++;
				}
			}
		}

		private void showWinScreen(Graphics2D g)
		{
			g.setFont(bigfont);
            g.setColor(mazecolor);
            g.fillRect(mazeIndentX + ncollumn / 2 * blocksize - 80, mazeIndentY + nrow / 2 * blocksize + 40, 186, 70);
            g.setColor(winTextColor);
            g.drawRect(mazeIndentX + ncollumn / 2 * blocksize - 80, mazeIndentY + nrow / 2 * blocksize + 40, 186, 70);
			g.drawString("You win!", mazeIndentX + ncollumn / 2 * blocksize - 40, mazeIndentY + nrow / 2 * blocksize + 83);
		}

		private void showGameOverScreen(Graphics2D g)
		{
            g.setFont(bigfont);
            g.setColor(mazecolor);
            g.fillRect(mazeIndentX + ncollumn / 2 * blocksize - 80, mazeIndentY + nrow / 2 * blocksize + 40, 186, 73);
            g.setColor(gameOverTextColor);
            g.drawRect(mazeIndentX + ncollumn / 2 * blocksize - 80, mazeIndentY + nrow / 2 * blocksize + 40, 186, 73);
			g.drawString("Game over!", mazeIndentX + ncollumn / 2 * blocksize - 55, mazeIndentY + nrow / 2 * blocksize + 85);
		}

        public void init()
        {
            win = false;
            gameOver = false;
        }

		private void attachRestartButton()
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
                    restart();
				}
			});
			add(restart);
		}
	}

	private class RulesPanel extends JPanel
	{
        private JPanel buttonPanel = new JPanel();
        private JButton backButton = new JButton(backButtonIcon);

		public RulesPanel()
		{
			setPreferredSize(new Dimension(width, height));

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, height - 110)));
            buttonPanel.setLayout(new BoxLayout( buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(Color.black);
            add(buttonPanel);

            backButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    if(!ingame)
                    {
                        setShownPanel(introPanel);
                    }
                    else
                    {
                        setShownPanel(mainPanel);
                    }
                }
            });

            backButton.setBackground(Color.black);
            backButton.setFocusPainted(false);
            backButton.setBorderPainted(false);

            buttonPanel.add(backButton);
            buttonPanel.add(Box.createHorizontalGlue());
		}

		@Override
		protected void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			Graphics2D g2d = (Graphics2D) graphics;
			g2d.drawImage(rulesImage, 0, 0, this);
		}
	}

    private class ChooseMazePanel extends JPanel
    {
        private int idx = 0;
        private final int mazeCount = 3;
        private final Image mazeImage[] = new Image[mazeCount];
        private JPanel buttonPanel = new JPanel();
        private JButton backButton = new JButton(backButtonIcon);
        private JButton nextButton = new JButton(nextButtonIcon);
        private JButton chooseButton = new JButton(chooseButtonIcon);

        public ChooseMazePanel()
        {
            loadImages();
            setBackground(Color.black);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, height - 110)));
            buttonPanel.setLayout(new BoxLayout( buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(Color.black);
            add(buttonPanel);

            backButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    idx = 0;
                    nextButton.setVisible(true);
                    if(!ingame)
                    {
                        setShownPanel(introPanel);
                    }
                    else
                    {
                        setShownPanel(mainPanel);
                    }
                }
            });
            nextButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    ++idx;
                    repaint();
                }
            });
            chooseButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    controller.setMaze(idx);
                    controller.initGame();
                    controller.restart();
                    setShownPanel(gamePanel);
                    nextButton.setVisible(true);
                    idx = 0;
                }
            });

            backButton.setBackground(Color.black);
            nextButton.setBackground(Color.black);
            chooseButton.setBackground(Color.black);

            backButton.setFocusPainted(false);
            nextButton.setFocusPainted(false);
            chooseButton.setFocusPainted(false);

            backButton.setBorderPainted(false);
            nextButton.setBorderPainted(false);
            chooseButton.setBorderPainted(false);

            setPreferredSize(new Dimension(width, height));

            buttonPanel.add(backButton);
            buttonPanel.add(chooseButton);
            buttonPanel.add(nextButton);
        }

        private void loadImages()
        {
            mazeImage[0] = new ImageIcon("images/menu/maze1.png").getImage();
            mazeImage[1] = new ImageIcon("images/menu/maze2.png").getImage();
            mazeImage[2] = new ImageIcon("images/menu/maze3.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics graphics)
        {
            super.paintComponent(graphics);
            graphics.drawImage(mazeImage[idx], 0, 0, this);
            if(idx == mazeCount - 1)
            {
                nextButton.setVisible(false);
            }
        }
    }
}
