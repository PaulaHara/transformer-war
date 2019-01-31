package transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import transformer.Service.BattleScoreService;
import transformer.Service.TransformerService;
import transformer.exceptions.TransformerNotFoundException;
import transformer.model.Transformer;

import java.util.List;
import java.util.Optional;

/**
 * @author paula hara
 */
@Controller
@RequestMapping("/transformer")
public class TransformerBattleController {

    @Autowired
    private TransformerService transformerService;

    @Autowired
    private BattleScoreService scoreService;

    /**
     * Get all transformers in the DB
     *
     * @return HttpStatus OK and a list of transformers
     */
    @GetMapping("/all")
    public ResponseEntity<List<Transformer>> listAllTransformers() {
        return new ResponseEntity<>(transformerService.findAll(), HttpStatus.OK);
    }

    /**
     * Create new transformer
     *
     * @return HttpStatus OK
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET})
    /*public ResponseEntity createTransformer(@RequestParam String name, @RequestParam char type,
                                            @RequestParam int strength, @RequestParam int intelligence,
                                            @RequestParam int speed, @RequestParam int endurance,
                                            @RequestParam int rank, @RequestParam int courage,
                                            @RequestParam int firepower, @RequestParam int skill) {*/
    public ResponseEntity createTransformer(@RequestBody Transformer transformer){
        if(transformer.getId() != null) {
            if (this.transformerService.findById(transformer.getId()).isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        this.transformerService.save(transformer);

        return new ResponseEntity<>("New transformer created", HttpStatus.CREATED);
    }

    /**
     * Search transformer by Id
     *
     * @param id
     * @return HttpStatus OK and one transformer
     * @throws TransformerNotFoundException
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Transformer> getTransformerById(@PathVariable Integer id) throws TransformerNotFoundException {
        Optional<Transformer> transformer = transformerService.findById(id);

        if(!transformer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transformer.get(), HttpStatus.OK);
    }

    /**
     * Search transformer by Id and update
     *
     * @param transformer
     * @param id
     * @return HttpStatus No CONTENT
     */
    //@PutMapping("/update/{id}")
    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Transformer> updateTransformer(@RequestBody Transformer transformer, @PathVariable Integer id) {
        Optional<Transformer> transformerOptional = transformerService.findById(id);

        if(!transformerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        transformer.setId(id);
        transformerService.save(transformer);
        return new ResponseEntity<>(transformerOptional.get(), HttpStatus.OK);
    }

    /**
     * Delete transformer by Id
     *
     * @param id
     * @return HttpStatus NO CONTENT
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Transformer> deleteTransformer(@PathVariable Integer id) {
        if(!transformerService.findById(id).isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        transformerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete all transformers of the DB
     *
     * @return HttpStatus NO CONTENT
     */
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Transformer> deleteAllTransformers() {
        transformerService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Evaluate battle between Autobots and Decepticons.
     *
     * @param ids
     * @return HttpStatus OK and the battle score
     */
    @GetMapping("/battle/{ids}")
    public ResponseEntity<String> transformersBattle(@PathVariable List<Integer> ids) {
        List<Transformer> transformers = transformerService.findAllById(ids);

        String battleScore = scoreService.transformerBattleScore(transformers);

        return new ResponseEntity<>(battleScore, HttpStatus.OK);
    }
}
