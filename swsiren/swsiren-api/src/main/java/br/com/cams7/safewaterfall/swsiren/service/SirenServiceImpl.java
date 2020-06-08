/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swsiren.model.repository.SirenRepository;
import br.com.cams7.safewaterfall.swsiren.model.vo.SirenVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Service
public class SirenServiceImpl implements SirenService {

  @Autowired
  private SirenRepository repository;

  @Override
  public SirenVO save(SirenVO siren) {
    log.info("save -> {}", siren);
    return repository.save(siren);
  }

  @Override
  public SirenVO findById(String id) {
    SirenVO siren = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "Não foi encontrado nenhuma sirene pelo id %d", id)));
    return siren;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
