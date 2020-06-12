package br.com.cams7.safewaterfall.swmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.model.repository.SirenRepository;

@Service
@Transactional
public class SirenServiceImpl implements SirenService {

  @Autowired
  private SirenRepository repository;

  @CacheEvict(cacheNames = SirenEntity.CACHE_NAME, key = "#siren.getId()")
  @Override
  public SirenEntity save(SirenEntity siren) {
    return repository.save(siren);
  }

  @Cacheable(cacheNames = SirenEntity.CACHE_NAME, key = "#id")
  @Transactional(readOnly = true)
  @Override
  public SirenEntity findById(Long id) {
    SirenEntity siren = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "A sirene cujo ID é %d não foi encontrada", id)));
    return siren;
  }

  @Cacheable(cacheNames = "siren_by_sensor", key = "#deviceId")
  @Transactional(readOnly = true)
  @Override
  public SirenEntity findBySensorDeviceId(String deviceId) {
    return repository.findBySensorDeviceId(deviceId).orElseThrow(() -> new AppResourceNotFoundException(String
        .format(
            "Não foi possivel encontrar a sirene já que o sensor cujo ID do dispositivo é %s não foi encontrado",
            deviceId)));
  }

}
