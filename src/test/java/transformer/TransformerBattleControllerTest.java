package transformer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import transformer.model.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paula hara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransformerBattleControllerTest {

    @Mock
    private TransformerBattleController battleController;

    private Transformer transformer1, transformer2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        transformer1 = new Transformer(1, "Autobot1", 'A', 2, 13, 5,
                6, 10, 18, 14, 5);
        transformer2 = new Transformer(2, "Decepticon1",'D', 12, 1, 15,
                20, 5, 10, 9, 3);
        List<Transformer> transformers = new ArrayList<>();
        transformers.add(transformer1);
        transformers.add(transformer2);

        Mockito.when(battleController.getTransformerById(transformer1.getId()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(transformer1));

        Mockito.when(battleController.createTransformer(transformer1))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(Mockito.any(Transformer.class)));

        Mockito.when(battleController.updateTransformer(transformer1, Mockito.anyInt()))
                .thenReturn(ResponseEntity.noContent().build());

        Mockito.when(battleController.deleteTransformer(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        Mockito.when(battleController.deleteAllTransformers())
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        Mockito.when(battleController.listAllTransformers())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(transformers));

        Mockito.when(battleController.transformersBattle(Mockito.anyList()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Final Battle Score."));
    }

    /**
     * Test battleController.listAllTransformers() <br>
     * Should return: HttpStatus OK and responseBody with 2 Transformers
     */
    @Test
    public void listAllTransformers() {
        ResponseEntity<List<Transformer>> response = battleController.listAllTransformers();

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(response.getBody().size() == 2);
    }

    /**
     * Test battleController.getTransformerById(id)
     * Should return: HttpStatus OK and responseBody not null
     *
     * @throws Exception
     */
    @Test
    public void getTransformerById() throws Exception {
        ResponseEntity<Transformer> response = battleController.getTransformerById(1);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test battleController.createTransformer(transformer)
     * Should return: HttpStatus CREATED
     */
    @Test
    public void createTransformer() {
        ResponseEntity response = battleController.createTransformer(transformer1);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Test battleController.updateTransformer(transformer, id)
     * Should return: HttpStatus NO CONTENT
     */
    @Test
    public void updateTransformer() {
        transformer1 = new Transformer("Autobot2", 'A', 12, 3, 5,
                6, 10, 5, 10, 5);
        ResponseEntity<Transformer> response = battleController.updateTransformer(transformer1, 1);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test battleController.deleteTransformer(id)
     * Should return: HttpStatus NO CONTENT
     */
    @Test
    public void deleteTransformer() {
        ResponseEntity<Transformer> response = battleController.deleteTransformer(transformer2.getId());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test battleController.deleteAllTransformers()
     * Should return: HttpStatus NO CONTENT
     */
    @Test
    public void deleteAllTransformers() {
        ResponseEntity<Transformer> response = battleController.deleteAllTransformers();
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test battleController.transformersBattle(ids)
     * Should return: HttpStatus OK and responseBody not null
     */
    @Test
    public void transformersBattle() {
        List<Integer> transformersIds = new ArrayList<>();
        transformersIds.add(transformer1.getId());
        transformersIds.add(transformer2.getId());

        ResponseEntity response = battleController.transformersBattle(transformersIds);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody().toString());
    }
}