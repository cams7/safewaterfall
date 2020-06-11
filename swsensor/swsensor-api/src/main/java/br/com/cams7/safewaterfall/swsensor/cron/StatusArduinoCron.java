package br.com.cams7.safewaterfall.swsensor.cron;

import static br.com.cams7.safewaterfall.AppConstants.JOB_GROUP_NAME;
import javax.annotation.PostConstruct;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import br.com.cams7.safewaterfall.common.cron.AppCronImpl;
import br.com.cams7.safewaterfall.common.model.Sensor;
import br.com.cams7.safewaterfall.swsensor.service.SensorService;
import br.com.cams7.safewaterfall.swsensor.service.StatusArduinoService;

@Configuration
public class StatusArduinoCron extends AppCronImpl {

  public static final String TRIGGER_NAME = "STATUS_ARDUINO_TRIGGER";

  @Value("${SENSOR_ID}")
  private String sensorId;

  @Autowired
  private SensorService sensorService;

  @PostConstruct
  void init() {
    cronTriggerFactoryBean = statusArduinoTriggerFactoryBean();
    jobDetailFactoryBean = statusArduinoJobDetailFactory();
    register();

  }

  @Bean
  public JobDetailFactoryBean statusArduinoJobDetailFactory() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();

    jobDetailFactory.setJobClass(StatusArduinoJob.class);
    jobDetailFactory.setName(StatusArduinoJob.NAME);
    jobDetailFactory.setGroup(JOB_GROUP_NAME);
    return jobDetailFactory;

  }

  @Bean
  public CronTriggerFactoryBean statusArduinoTriggerFactoryBean() {
    Sensor sensor = sensorService.findById(sensorId);
    CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();

    cronTriggerFactoryBean.setJobDetail(statusArduinoJobDetailFactory().getObject());
    cronTriggerFactoryBean.setCronExpression(sensor.getStatusArduinoCron());
    cronTriggerFactoryBean.setName(TRIGGER_NAME);
    cronTriggerFactoryBean.setGroup(JOB_GROUP_NAME);
    return cronTriggerFactoryBean;
  }

  public static class StatusArduinoJob implements Job {

    public static final String NAME = "STATUS_ARDUINO_JOB";

    @Autowired
    public StatusArduinoService statusArduinoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      statusArduinoService.runTask();
    }
  }

}
