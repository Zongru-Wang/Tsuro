import game.observer.Observer;
import game.observer.image.VectorImage;
import game.player.Dumb;
import game.player.Player;
import game.player.PlayerImpl;
import game.player.Second;
import game.referee.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * using for display purpose, we can delete if not for presentation purpose
 */
public interface LoggingObserver<T> extends Observer<T> {
    class VectorImagePanel extends JPanel {
        private final List<VectorImage> images;
        private int currentFrame;

        VectorImagePanel(List<VectorImage> images) {
            this.images = new ArrayList<>(images);
            Timer timer = new Timer(1000, e -> {
                currentFrame = (currentFrame + 1) % images.size();
                repaint();
            });
            timer.start();
        }

        public Dimension getPreferredSize() {
            return new Dimension(400,400);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (images.get(currentFrame) != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                int x = (getWidth() - images.get(currentFrame).getWidth()) / 2;
                int y = (getHeight() - images.get(currentFrame).getHeight()) / 2;
                g2d.drawImage(images.get(currentFrame).asBufferedImage(), x, y, this);
                g2d.dispose();
            }
        }
    }

    static void main(String[] a){
        GameManager gameManager = new GameManager();
        LoggingObserverImpl log = new LoggingObserverImpl();
        for (Player player: Arrays.asList(
                new PlayerImpl("a", 0, new Second()),
                new PlayerImpl("b", 1, new Dumb()),
                new PlayerImpl("c", 2, new Second()),
                new PlayerImpl("d", 3, new Dumb()),
                new PlayerImpl("e", 4, new Dumb()))) {
            gameManager.addPlayer(player);
        }
        gameManager.addObserver(log);
        gameManager.startGame();
        JFrame frame = new JFrame("TSURO");
        frame.add(new VectorImagePanel(log.getLogs()));
        frame.pack();
        frame.setVisible(true);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    T getLogAt(int index);
    List<T> getLogs();
}
