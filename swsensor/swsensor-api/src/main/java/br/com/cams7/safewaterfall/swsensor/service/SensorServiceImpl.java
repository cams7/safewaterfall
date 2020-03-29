/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private SensorRepository repository;

  @Override
  public void save(SensorEntity sensor) {
    repository.save(sensor);
  }

  @Transactional(readOnly = true)
  @Override
  public SensorEntity findById(Long id) {
    SensorEntity sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "O sensor cujo ID é %d não foi encontrado", id)));
    return sensor;
  }

  @Transactional(readOnly = true)
  @Override
  public String findStatusArduinoCronById(Long id) {
    return repository.findStatusArduinoCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public String findSendStatusMessageCronById(Long id) {
    return repository.findSendStatusMessageCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public String findSendAlertMessageCronById(Long id) {
    return repository.findSendAlertMessageCronById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public Short findMinimumAllowedDistanceById(Long id) {
    return repository.findMinimumAllowedDistanceById(id);
  }

}
