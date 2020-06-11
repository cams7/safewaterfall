/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends CrudRepository<SensorEntity, Long> {

  @Query("SELECT s.siren FROM SensorEntity s WHERE s.deviceId = ?1")
  Optional<SirenEntity> findSirenByDeviceId(String deviceId);
}
