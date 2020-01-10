package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import game.board.Board;
import game.remote.EndOfGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EndOfGameGenerator extends Generator<EndOfGame> {
    public EndOfGameGenerator() {
        super(EndOfGame.class);
    }

    @Override
    public EndOfGame generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        Board board = new BoardGenerator().generate(sourceOfRandomness, generationStatus);
        List<List<String>> winners = new ArrayList<>();
        List<String> losers = new ArrayList<>();
        List<String> allPlayers = Arrays.asList("playerA", "playerB", "playerC", "playerD", "playerE");
        for (String player: allPlayers) {
            if (sourceOfRandomness.nextBoolean()) {
                losers.add(player);
            } else {
                if (winners.size() > 0 && sourceOfRandomness.nextBoolean()) {
                    winners.get(winners.size() - 1).add(player);
                } else {
                    List<String> newWinner = new ArrayList<>();
                    newWinner.add(player);
                    winners.add(newWinner);
                }
            }
        }
        return new EndOfGame(winners, losers, board);
    }
}
