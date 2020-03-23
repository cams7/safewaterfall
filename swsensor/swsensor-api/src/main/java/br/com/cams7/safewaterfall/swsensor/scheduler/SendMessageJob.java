/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import static br.com.cams7.safewaterfall.arduino.ArduinoServiceImpl.getKeyCurrentStatus;
import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent.MESSAGE;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType.DIGITAL;
import static br.com.cams7.safewaterfall.swsensor.service.StatusArduinoServiceImpl.DIGITAL_PIN;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import br.com.cams7.safewaterfall.arduino.error.ArduinoException;
import br.com.cams7.safewaterfall.arduino.model.CurrentStatus;
import br.com.cams7.safewaterfall.arduino.model.repository.CurrentStatusRepository;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class SendMessageJob implements Job {

  private final static String MANAGER_URL = "http://localhost:8180";

  @Autowired
  private CurrentStatusRepository statusRepository;

  @Autowired
  private RestOperations restTemplate;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("SendMessageJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context
        .getFireTime());

    String id = getKeyCurrentStatus(MESSAGE, DIGITAL, DIGITAL_PIN);
    CurrentStatus currentStatus = statusRepository.findById(id).orElseThrow(() -> new ArduinoException(String
        .format("Não foi encontrado nenhum estado do arduino pelo id %s", id)));
    ArduinoUSART arduino = (ArduinoUSART) currentStatus.getArduino();
    short pinValue = arduino.getPinValue();

    // setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

    // setting up the request body
    AppSensorVO sensor = new AppSensorVO("1");
    sensor.setDistancia(pinValue);

    // request entity is created with request body and headers
    HttpEntity<AppSensorVO> requestEntity = new HttpEntity<>(sensor, requestHeaders);

    try {
      ResponseEntity<Void> responseEntity = restTemplate.exchange(String.format("%s/sensor/atualizar",
          MANAGER_URL), HttpMethod.POST, requestEntity, Void.class);

      if (responseEntity.getStatusCode() == HttpStatus.OK) {
        log.info("Response retrieved");
      }
    } catch (ResourceAccessException e) {
      log.error(e.getMessage());
    }

    log.info("Next SendMessageJob scheduled @ {}", context.getNextFireTime());
  }
}
