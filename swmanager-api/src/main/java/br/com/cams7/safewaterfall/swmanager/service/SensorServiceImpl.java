/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swmanager.model.SensorEntity;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.model.repository.SensorRepository;

/**
 * @author CAMs7
 *
 */
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

  @Autowired
  private SensorRepository repository;

  @CacheEvict(cacheNames = SensorEntity.CACHE_NAME, key = "#sensor.getId()")
  @Override
  public SensorEntity save(SensorEntity sensor) {
    return repository.save(sensor);
  }

  @Cacheable(cacheNames = SirenEntity.CACHE_NAME, key = "#id")
  @Transactional(readOnly = true)
  @Override
  public SensorEntity findById(Long id) {
    SensorEntity sensor = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "O sensor cujo ID é %d não foi encontrado", id)));
    return sensor;
  }

}
