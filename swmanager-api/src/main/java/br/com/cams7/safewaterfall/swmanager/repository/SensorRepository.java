/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends CrudRepository<SensorEntity, Long> {

  @Query("SELECT s.siren.id FROM SensorEntity s WHERE s.id = ?1")
  Optional<Long> findSirenIdById(Long id);
}
