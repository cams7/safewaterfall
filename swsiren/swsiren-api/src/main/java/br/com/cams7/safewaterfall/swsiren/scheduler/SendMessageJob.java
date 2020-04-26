/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import br.com.cams7.safewaterfall.swsiren.service.StatusArduinoService;
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

  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("SendMessageJob ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context
        .getFireTime());

    log.info("Next SendMessageJob scheduled @ {}", context.getNextFireTime());
  }
}
