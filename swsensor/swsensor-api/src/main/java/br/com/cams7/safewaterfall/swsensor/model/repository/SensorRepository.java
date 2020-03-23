/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swsensor.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends CrudRepository<SensorEntity, Long> {

}
