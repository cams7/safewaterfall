/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swmanager.model.repository.AppSirenRepository;
import br.com.cams7.safewaterfall.swmanager.model.vo.AppSirenVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Service
public class AppSirenServiceImpl implements AppSirenService {

  @Autowired
  private AppSirenRepository repository;

  @Override
  public AppSirenVO save(AppSirenVO siren) {
    log.info("save -> {}", siren);
    return repository.save(siren);
  }

  @Override
  public AppSirenVO findById(String id) {
    AppSirenVO sirenAddress = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String
        .format("Não foi encontrado nenhuma sirene pelo id %d", id)));
    return sirenAddress;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
