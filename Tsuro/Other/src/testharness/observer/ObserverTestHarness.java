package testharness.observer;


import game.observer.Observer;
import game.observer.ObserverImpl;
import game.observer.image.VectorImage;
import javafx.util.Pair;
import testharness.AbstractTestHarness;
import testharness.TestHarness;
import testharness.TestInputExtractor;
import testharness.rulechecker.RuleCheckerTestInput;
import testharness.rulechecker.RuleCheckerTestOutput;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ObserverTestHarness extends AbstractTestHarness<ObserverTestInput, ObserverTestOutput> implements TestHarness<ObserverTestInput, ObserverTestOutput> {
    private ObserverTestHarness() {
        super(new ObserverTestInputExtractor());
    }

    public static void main(String[] args) {
        TestHarness harness = new ObserverTestHarness();
        try(FileOutputStream fileOutputStream = new FileOutputStream("tsuro.jpg")) {
            harness.executeTest(System.in, System.out, fileOutputStream);
        } catch (Exception ignored) {
        }
    }

    @Override
    public ObserverTestOutput test(ObserverTestInput input) {
        Observer<VectorImage> observer = new ObserverImpl();
        if (input.initialPlacement != null) {
            observer.update(input.gameState, input.initialPlacement, input.choices, input.turn);
        } else if (input.intermediatePlacement != null){
            observer.update(input.gameState, input.intermediatePlacement, input.choices, input.turn);
        } else {
            observer.update(input.gameState);
        }
        return new ObserverTestOutput(observer.render());
    }
}
