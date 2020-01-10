package testharness.observer;

import game.observer.image.VectorImage;
import testharness.TestOutput;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ObserverTestOutput implements TestOutput {
    VectorImage image;

    public ObserverTestOutput(VectorImage image) {
        this.image = image;
    }

    @Override
    public void print(OutputStream out) {
        try {
            ImageIO.write(image.asBufferedImage(), "JPG", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
