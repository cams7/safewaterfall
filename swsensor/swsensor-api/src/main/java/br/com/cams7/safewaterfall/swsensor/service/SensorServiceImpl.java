/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swsensor.model.SensorEntity;
import br.com.cams7.safewaterfall.swsensor.model.repository.SensorRepository;

/**
 * @author CAMs7
 *
 */
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private SensorRepository repository;

  @Override
  public SensorEntity save(SensorEntity sensor) {
    String id = sensor.getId();
    if (!sensorId.equals(id))
      throw new AppResourceNotFoundException(String.format("O ID %d não corresponde ao ID do sensor", id));

    return repository.save(sensor);
  }

  @Transactional(readOnly = true)
  @Override
  public SensorEntity findById(String id) {
    SensorEntity sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "O sensor cujo ID é %d não foi encontrado", id)));
    return sensor;
  }

  @Transactional(readOnly = true)
  @Override
  public String findStatusArduinoCronById(String id) {
    return repository.findStatusArduinoCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public String findSendStatusMessageCronById(String id) {
    return repository.findSendStatusMessageCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public String findSendAlertMessageCronById(String id) {
    return repository.findSendAlertMessageCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public Short findMinimumAllowedDistanceById(String id) {
    return repository.findMinimumAllowedDistanceById(id);
  }

}
