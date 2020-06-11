package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.swsensor.model.Sensor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatusMessageService {

  @Value("${MANAGER_URL}")
  private String managerUrl;

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private StatusArduinoService statusArduinoService;

  @Autowired
  private SensorService sensorService;

  public void runTask() {
    final String url = String.format("%s/siren/change_status", managerUrl);
    log.info("Sending message to {}", url);

    short distance = statusArduinoService.getDistance();

    // setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

    // setting up the request body
    Sensor sensor = sensorService.findById(sensorId);
    sensor.setDistance(distance);

    // request entity is created with request body and headers
    HttpEntity<Sensor> requestEntity = new HttpEntity<>(sensor, requestHeaders);

    try {
      restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
      sensorService.save(sensor);
    } catch (ResourceAccessException e) {
      throw new AppException(e);
    }
  }

}
