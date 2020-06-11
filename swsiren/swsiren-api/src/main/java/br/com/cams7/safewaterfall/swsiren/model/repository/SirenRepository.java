/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swsiren.model.Siren;

/**
 * @author ceanm
 *
 */
@Repository
public interface SirenRepository extends MongoRepository<Siren, String> {
}
