package transformer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import transformer.model.Transformer;

/**
 * @author paula hara
 */
@Repository
public interface TransformerRepository extends JpaRepository<Transformer, Integer> {
}
