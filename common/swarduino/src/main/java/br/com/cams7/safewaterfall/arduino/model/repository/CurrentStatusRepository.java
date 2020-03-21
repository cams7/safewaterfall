/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.arduino.model.CurrentStatus;

/**
 * @author CAMs7
 *
 */
@Repository
public interface CurrentStatusRepository extends CrudRepository<CurrentStatus, String> {

}
