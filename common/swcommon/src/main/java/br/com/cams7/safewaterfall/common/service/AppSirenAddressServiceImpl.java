/**
 * 
 */
package br.com.cams7.safewaterfall.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.repository.AppSirenAddressRepository;
import br.com.cams7.safewaterfall.common.model.vo.AppSirenAddressVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Service
public class AppSirenAddressServiceImpl implements AppSirenAddressService {

  @Autowired
  private AppSirenAddressRepository repository;

  @Override
  public AppSirenAddressVO save(AppSirenAddressVO sirenAddress) {
    log.info("save -> {}", sirenAddress);
    return repository.save(sirenAddress);
  }

  @Override
  public AppSirenAddressVO findById(String id) {
    AppSirenAddressVO sirenAddress = repository.findById(id).orElseThrow(() -> new AppResourceNotFoundException(
        String.format("Não foi encontrado nenhum endereço de sirene pelo id %d", id)));
    return sirenAddress;
  }

  @Override
  public boolean existsById(String id) {
    return repository.existsById(id);
  }

}
