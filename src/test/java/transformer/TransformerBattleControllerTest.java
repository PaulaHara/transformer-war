package transformer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import transformer.Service.BattleScoreService;
import transformer.exceptions.TransformerNotFoundException;
import transformer.model.Transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author paula hara
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransformerBattleControllerTest {

    @Mock
    private TransformerRepository transformerRepository;

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

        Mockito.when(transformerRepository.findById(transformer1.getId())).thenReturn(java.util.Optional.of(transformer1));

        Mockito.when(battleController.getTransformerById(transformer1.getId()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(transformer1));

        Mockito.when(battleController.createTransformer(Mockito.any(Transformer.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        Mockito.when(battleController.updateTransformer(Mockito.any(Transformer.class), Mockito.anyInt()))
                .thenReturn(ResponseEntity.noContent().build());

        Mockito.when(battleController.deleteTransformer(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        Mockito.when(battleController.deleteAllTransformers())
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        List<Transformer> transformers = new ArrayList<>();
        transformers.add(transformer1);
        transformers.add(transformer2);
        Mockito.when(battleController.listAllTransformers())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(transformers));

        Mockito.when(battleController.transformersBattle(Mockito.anyList()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Final Battle Score."));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllTransformers() {
        ResponseEntity<List<Transformer>> response = battleController.listAllTransformers();

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(((List<Transformer>) response.getBody()).size() == 2);
    }

    @Test
    public void getTransformerById() throws Exception {
        ResponseEntity<Transformer> response = battleController.getTransformerById(1);

        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(transformer1.getId(), ((Transformer) response.getBody()).getId());
        Assert.assertEquals(transformer1.getType(), ((Transformer) response.getBody()).getType());
    }

    @Test
    public void createTransformer() {
        //ResponseEntity response = battleController.createTransformer( "Autobot1", 'A', 25,
          //      41, 5, 28, 2, 38, 25, 35);
        ResponseEntity response = battleController.createTransformer(transformer1);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void updateTransformer() {
        transformer1.setType('D');
        ResponseEntity<Transformer> response = battleController.updateTransformer(transformer1, 1);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteTransformer() {
        ResponseEntity<Transformer> response = battleController.deleteTransformer(transformer2.getId());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteAllTransformers() {
        ResponseEntity<Transformer> response = battleController.deleteAllTransformers();
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void transformersBattle() {
        List<Integer> transformersIds = new ArrayList<>();
        transformersIds.add(transformer1.getId());
        transformersIds.add(transformer2.getId());

        ResponseEntity response = battleController.transformersBattle(transformersIds);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Final Battle Score.", response.getBody().toString());
    }
}