/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
}
