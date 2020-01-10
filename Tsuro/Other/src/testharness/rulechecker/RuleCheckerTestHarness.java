package testharness.rulechecker;

import game.board.IntermediatePlacement;
import game.rulechecker.RuleChecker;
import testharness.AbstractTestHarness;
import testharness.TestHarness;

public class RuleCheckerTestHarness
        extends AbstractTestHarness<RuleCheckerTestInput, RuleCheckerTestOutput>
        implements TestHarness<RuleCheckerTestInput, RuleCheckerTestOutput> {

    public static void main(String[] args) {
        RuleCheckerTestHarness harness = new RuleCheckerTestHarness();
        harness.executeTest(System.in, System.out);
    }

    public RuleCheckerTestHarness() {
        super(new RuleCheckerTestInputExtractor());
    }

    @Override
    public RuleCheckerTestOutput test(RuleCheckerTestInput input) {
        RuleChecker ruleChecker = new RuleChecker();
        if (ruleChecker.checkIntermediatePlacement(input.gameState, input.token, IntermediatePlacement.of(input.tile, input.position), input.choices)) {
            return new RuleCheckerTestOutput("legal");
        } else {
            return new RuleCheckerTestOutput("illegal");
        }
    }
}
