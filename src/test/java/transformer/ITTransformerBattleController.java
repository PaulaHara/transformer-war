package transformer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import transformer.model.Transformer;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ITTransformerBattleController {

    @Autowired
    private TransformerBattleController battleController;

    private Transformer autobot, decepticon;

    @Before
    public void setUp() {
        autobot = new Transformer("Autobot", 'A', 21, 13,
                15, 6, 5, 30, 20, 15);
        decepticon = new Transformer("Decepticon", 'D', 12, 20,
                5, 26, 4, 23, 18, 18);
    }

    @Test
    public void listAllTransformersTest() {
        // First create two transformers
        ResponseEntity<Transformer> transformerA = battleController.createTransformer(autobot);
        ResponseEntity<Transformer> transformerD = battleController.createTransformer(decepticon);
        Assert.assertNotNull(transformerA);
        Assert.assertNotNull(transformerA.getBody());
        Assert.assertNotNull(transformerD);
        Assert.assertNotNull(transformerD.getBody());

        // Now get the list of transformers
        ResponseEntity<List<Transformer>> response = battleController.listAllTransformers();

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(2, response.getBody().size());
    }

    @Test
    public void createTransformerTest() {
        ResponseEntity<Transformer> response = battleController.createTransformer(autobot);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(autobot.getName(), response.getBody().getName());
    }

    @Test
    public void createTransformerConflictTest() {
        ResponseEntity<Transformer> response = battleController.createTransformer(autobot);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());

        // Trying to create the same transformer
        ResponseEntity<Transformer> sameTransformer = battleController.createTransformer(response.getBody());
        Assert.assertNotNull(sameTransformer);
        Assert.assertEquals(HttpStatus.CONFLICT, sameTransformer.getStatusCode());
        Assert.assertNull(sameTransformer.getBody());
    }

    @Test
    public void getTransformerByIdTest() {
        // First create the transformer
        ResponseEntity<Transformer> transformer = battleController.createTransformer(autobot);
        Assert.assertNotNull(transformer);
        Assert.assertNotNull(transformer.getBody());

        // Now do the search
        ResponseEntity<Transformer> response = battleController.getTransformerById(transformer.getBody().getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Autobot", response.getBody().getName());
    }

    @Test
    public void getTransformerByIdNotFoundTest() {
        ResponseEntity<Transformer> response = battleController.getTransformerById(100);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assert.assertNull(response.getBody());
    }

    @Test
    public void updateTransformerTest() {
        // Create
        ResponseEntity<Transformer> response = battleController.createTransformer(autobot);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());

        // Update
        Transformer uTransformer = response.getBody();
        uTransformer.setName("Optimus Prime");

        ResponseEntity<Transformer> updateTransformer = battleController.updateTransformer(uTransformer, uTransformer.getId());

        Assert.assertNotNull(updateTransformer);
        Assert.assertEquals(HttpStatus.OK, updateTransformer.getStatusCode());
        Assert.assertNotNull(updateTransformer.getBody());
        Assert.assertEquals(uTransformer.getName(), updateTransformer.getBody().getName());
    }

    @Test
    public void updateTransformerNotFoundTest() {
        autobot.setId(100);
        autobot.setName("Optimus Prime");

        ResponseEntity<Transformer> updateTransformer = battleController.updateTransformer(autobot, autobot.getId());

        Assert.assertNotNull(updateTransformer);
        Assert.assertEquals(HttpStatus.NOT_FOUND, updateTransformer.getStatusCode());
        Assert.assertNull(updateTransformer.getBody());
    }

    @Test
    public void updateTransformerNullIdTest() {
        autobot.setName("Optimus Prime");

        ResponseEntity<Transformer> updateTransformer = battleController.updateTransformer(autobot, autobot.getId());

        Assert.assertNotNull(updateTransformer);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, updateTransformer.getStatusCode());
    }

    @Test
    public void deleteTransformerTest() {
        // Create
        ResponseEntity<Transformer> response = battleController.createTransformer(autobot);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());

        // Delete
        ResponseEntity deleteTransformer = battleController.deleteTransformer(response.getBody().getId());

        Assert.assertNotNull(deleteTransformer);
        Assert.assertEquals(HttpStatus.NO_CONTENT, deleteTransformer.getStatusCode());
    }

    @Test
    public void deleteTransformerNotFoundTest() {
        ResponseEntity response = battleController.deleteTransformer(100);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteAllTransformerTest() {
        // Create
        ResponseEntity<Transformer> transformerA = battleController.createTransformer(autobot);
        ResponseEntity<Transformer> transformerD = battleController.createTransformer(decepticon);
        Assert.assertNotNull(transformerA);
        Assert.assertNotNull(transformerA.getBody());
        Assert.assertNotNull(transformerD);
        Assert.assertNotNull(transformerD.getBody());

        // Delete All
        ResponseEntity response = battleController.deleteAllTransformers();

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteAllTransformerEmptyTest() {
        ResponseEntity response = battleController.deleteAllTransformers();

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void transformersBattleTest() {
        // Create
        ResponseEntity<Transformer> transformerA = battleController.createTransformer(autobot);
        ResponseEntity<Transformer> transformerD = battleController.createTransformer(decepticon);
        Assert.assertNotNull(transformerA);
        Assert.assertNotNull(transformerA.getBody());
        Assert.assertNotNull(transformerD);
        Assert.assertNotNull(transformerD.getBody());

        // Battle
        List<Integer> ids = new ArrayList<>();
        ids.add(transformerA.getBody().getId());
        ids.add(transformerD.getBody().getId());

        ResponseEntity<String> response = battleController.transformersBattle(ids);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().contains("Winning team (Autobots): Autobot"));
    }

    @Test
    public void transformersBattleEmptyListTest() {
        List<Integer> ids = new ArrayList<>();
        ResponseEntity<String> response = battleController.transformersBattle(ids);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
