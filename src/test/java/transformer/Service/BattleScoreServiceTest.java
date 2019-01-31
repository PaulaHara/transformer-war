package transformer.Service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import transformer.model.Transformer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author paula hara
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BattleScoreServiceTest {

    @Autowired
    private BattleScoreService scoreService;

    List<Transformer> transformers = new ArrayList<>();

    @Before
    public void setUp() {
        transformers.add(new Transformer(1, "Soundwave", 'D', 8, 9, 2,
                6, 7, 5, 6, 10));
        transformers.add(new Transformer(2, "Bluestreak", 'A', 6, 6, 7,
                9, 5, 2, 9, 7));
        transformers.add(new Transformer(3, "Hubcap", 'A', 4, 4, 4,
                4, 4, 4, 4, 4));
    }

    @Test
    public void evaluateFightTest() {
        String battleScore = scoreService.transformerBattleScore(transformers);

        Assert.assertEquals("1 battle\n" +
                "Winning team (Decepticons): Soundwave\n" +
                "Survivors from the losing team (Autobots): Hubcap", battleScore);
    }

    @Test
    public void evaluateFightWithOptimusPrimeTest() {
        transformers = new ArrayList<>();
        transformers.add(new Transformer(1, "Soundwave", 'D', 8, 9, 2,
                6, 7, 5, 6, 10));
        transformers.add(new Transformer(2, "Optimus Prime", 'A', 1, 1, 1,
                1, 1, 1, 1, 1));

        String battleScore = scoreService.transformerBattleScore(transformers);

        Assert.assertEquals("1 battle\n" +
                "Winning team (Autobots): Optimus Prime\n" +
                "Survivors from the losing team (Decepticons): ", battleScore);
    }

    @Test
    public void evaluateFightWithOptimusPrimeAndPredakingTest() {
        transformers = new ArrayList<>();
        transformers.add(new Transformer(1, "Predaking", 'D', 1, 1, 1,
                1, 1, 1, 1, 1));
        transformers.add(new Transformer(2, "Optimus Prime", 'A', 1, 1, 1,
                1, 1, 1, 1, 1));

        String battleScore = scoreService.transformerBattleScore(transformers);

        Assert.assertEquals("1 battle\nTied battle.", battleScore);
    }
}