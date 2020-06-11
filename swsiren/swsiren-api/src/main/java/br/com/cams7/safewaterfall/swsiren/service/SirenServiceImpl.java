/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swsiren.model.Siren;
import br.com.cams7.safewaterfall.swsiren.model.repository.SirenRepository;

/**
 * @author CAMs7
 *
 */
@Service
public class SirenServiceImpl implements SirenService {

  @Autowired
  private SirenRepository repository;

  @CacheEvict(cacheNames = Siren.CACHE_NAME, key = "#siren.getId()")
  @Override
  public Siren save(Siren siren) {
    return repository.save(siren);
  }

  @Cacheable(cacheNames = Siren.CACHE_NAME, key = "#id")
  @Override
  public Siren findById(String id) {
    Siren siren = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "A sirene cujo ID é %d não foi encontrada", id)));
    return siren;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
