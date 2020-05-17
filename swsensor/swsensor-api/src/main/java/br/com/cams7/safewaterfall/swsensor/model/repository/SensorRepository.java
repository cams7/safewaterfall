/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.com.cams7.safewaterfall.swsensor.model.SensorEntity;

/**
 * @author CAMs7
 *
 */
@Repository
public interface SensorRepository extends CrudRepository<SensorEntity, String> {

  @Query("SELECT s.statusArduinoCron FROM SensorEntity s WHERE s.id = ?1")
  String findStatusArduinoCronById(String id);

  @Query("SELECT s.sendStatusMessageCron FROM SensorEntity s WHERE s.id = ?1")
  String findSendStatusMessageCronById(String id);

  @Query("SELECT s.sendAlertMessageCron FROM SensorEntity s WHERE s.id = ?1")
  String findSendAlertMessageCronById(String id);

  @Query("SELECT s.minimumAllowedDistance FROM SensorEntity s WHERE s.id = ?1")
  Short findMinimumAllowedDistanceById(String id);
}
