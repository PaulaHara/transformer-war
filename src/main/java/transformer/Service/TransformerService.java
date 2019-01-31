package transformer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transformer.TransformerRepository;
import transformer.model.Transformer;

import java.util.List;
import java.util.Optional;

/**
 * @author paula hara
 */
@Service
public class TransformerService {

    @Autowired
    private TransformerRepository transformerRepository;

    public List<Transformer> findAll() {
        return transformerRepository.findAll();
    }

    public Optional<Transformer> findById(Integer id) {
        return transformerRepository.findById(id);
    }

    public Transformer save(Transformer transformer) {
        return transformerRepository.save(transformer);
    }

    public void deleteById(Integer id) {
        transformerRepository.deleteById(id);
    }

    public void deleteAll() {
        transformerRepository.deleteAll();
    }

    public List<Transformer> findAllById(List<Integer> ids){
        return transformerRepository.findAllById(ids);
    }
}
