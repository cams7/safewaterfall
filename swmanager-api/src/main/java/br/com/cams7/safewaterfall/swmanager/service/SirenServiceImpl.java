package br.com.cams7.safewaterfall.swmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.swmanager.model.SirenEntity;
import br.com.cams7.safewaterfall.swmanager.repository.SirenRepository;

@Service
@Transactional
public class SirenServiceImpl implements SirenService {

  @Autowired
  private SirenRepository repository;

  @Override
  public SirenEntity save(SirenEntity siren) {
    return repository.save(siren);
  }

  @Transactional(readOnly = true)
  @Override
  public SirenEntity findById(Long id) {
    SirenEntity siren = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(String.format(
        "A sirene cujo ID é %d não foi encontrado", id)));
    return siren;
  }

}
