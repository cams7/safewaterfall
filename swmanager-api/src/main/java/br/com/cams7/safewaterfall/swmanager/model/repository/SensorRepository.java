/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.repository;

import static br.com.cams7.safewaterfall.swmanager.model.SensorEntity.WITH_SIREN;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

  @EntityGraph(value = WITH_SIREN)
  Optional<SensorEntity> findById(Long id);
}
