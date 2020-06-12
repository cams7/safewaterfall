/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swsensor.model.repository.SensorRepository;

/**
 * @author CAMs7
 *
 */
@Service
public class SensorServiceImpl implements SensorService {

  @Autowired
  private SensorRepository repository;

  @CacheEvict(cacheNames = Sensor.CACHE_NAME, allEntries = true)
  @Override
  public Sensor create(Sensor sensor) {
    return repository.save(sensor);
  }

  @CachePut(cacheNames = Sensor.CACHE_NAME, key = "#sensor.getId()")
  @Override
  public Sensor update(Sensor sensor) {
    return repository.save(sensor);
  }

  @Cacheable(cacheNames = Sensor.CACHE_NAME, key = "#id")
  @Override
  public Sensor findById(String id) {
    Sensor sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "O sensor cujo ID é %d não foi encontrado", id)));
    return sensor;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
