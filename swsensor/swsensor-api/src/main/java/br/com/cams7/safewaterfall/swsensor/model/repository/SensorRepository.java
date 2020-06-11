/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.common.model.Sensor;

/**
 * @author ceanm
 *
 */
@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {
}
