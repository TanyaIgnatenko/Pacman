package ru.nsu.ignatenko;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PacManDemo extends JFrame
{
    public PacManDemo()
    {
        PacManGame pacmanGame = new PacManGame();
        Controller controller = new Controller();
        View view = new View(607, 750, controller);

        pacmanGame.addController(controller);
        controller.addPacManGame(pacmanGame);
        controller.addView(view);
        add(view);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(607, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                PacManDemo window = new PacManDemo();
                window.setVisible(true);
            }
        });
    }
}