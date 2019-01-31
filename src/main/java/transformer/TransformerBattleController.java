package transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import transformer.Service.BattleScoreService;
import transformer.Service.TransformerService;
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
     * @param transformer
     * @return HttpStatus OK
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Transformer> createTransformer(@RequestBody Transformer transformer){
        if(transformer.getId() != null) {
            if (this.transformerService.findById(transformer.getId()).isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        Transformer newTransformer = this.transformerService.save(transformer);

        return new ResponseEntity<>(newTransformer, HttpStatus.CREATED);
    }

    /**
     * Search transformer by Id
     *
     * @param id
     * @return HttpStatus OK and one transformer
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Transformer> getTransformerById(@PathVariable Integer id) {
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
    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Transformer> updateTransformer(@RequestBody Transformer transformer, @PathVariable Integer id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Transformer> transformerOptional = transformerService.findById(id);
        if(!transformerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        transformer.setId(id);
        transformerService.save(transformer);
        return new ResponseEntity<>(transformer, HttpStatus.OK);
    }

    /**
     * Delete transformer by Id
     *
     * @param id
     * @return HttpStatus NO CONTENT
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTransformer(@PathVariable Integer id) {
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
    public ResponseEntity deleteAllTransformers() {
        transformerService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Evaluate battle between Autobots and Decepticons.
     *
     * @param ids
     * @return HttpStatus OK and the battle score
     */
    @GetMapping(value = "/battle/{ids}", produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> transformersBattle(@PathVariable List<Integer> ids) {
        List<Transformer> transformers = transformerService.findAllById(ids);

        String battleScore = "";
        try {
            battleScore = scoreService.transformerBattleScore(transformers);
        } catch (Exception e) {
            return new ResponseEntity<>("Not enough transformers to battle", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(battleScore, HttpStatus.OK);
    }
}
