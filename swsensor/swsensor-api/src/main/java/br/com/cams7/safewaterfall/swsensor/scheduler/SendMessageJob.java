/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import static br.com.cams7.safewaterfall.swsensor.scheduler.AppQuartzConfig.SENSOR_ID;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.common.service.AppSensorService;
import br.com.cams7.safewaterfall.swsensor.service.StatusArduinoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CAMs7
 *
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class SendMessageJob implements Job {

  @Value("${MANAGER_URL}")
  private String managerUrl;

  @Autowired
  private RestOperations restTemplate;

  @Autowired
  private StatusArduinoService statusArduinoService;

  @Autowired
  private AppSensorService appSensorService;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("SendMessageJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context
        .getFireTime());

    short distance = statusArduinoService.getDistance();

    // setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

    // setting up the request body
    AppSensorVO sensor = appSensorService.findById(SENSOR_ID);
    sensor.setDistance(distance);

    // request entity is created with request body and headers
    HttpEntity<AppSensorVO> requestEntity = new HttpEntity<>(sensor, requestHeaders);

    try {
      restTemplate.exchange(String.format("%s/siren/change_status", managerUrl), HttpMethod.POST, requestEntity,
          Void.class);
    } catch (ResourceAccessException e) {
      throw new AppException(e);
    }

    log.info("Next SendMessageJob scheduled @ {}", context.getNextFireTime());
  }
}
