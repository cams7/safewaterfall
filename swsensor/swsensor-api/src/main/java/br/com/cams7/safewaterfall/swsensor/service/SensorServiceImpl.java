/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swsensor.model.Sensor;
import br.com.cams7.safewaterfall.swsensor.model.repository.SensorRepository;

/**
 * @author CAMs7
 *
 */
@Service
public class SensorServiceImpl implements SensorService {

  @Autowired
  private SensorRepository repository;

  @CacheEvict(cacheNames = Sensor.CACHE_NAME, key = "#sensor.getId()")
  @Override
  public Sensor save(Sensor sensor) {
    return repository.save(sensor);
  }

  @Cacheable(cacheNames = Sensor.CACHE_NAME, key = "#id")
  @Override
  public Sensor findById(String id) {
    Sensor sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "O sensor cujo ID é %d não foi encontrado", id)));
    return sensor;
  }

}
