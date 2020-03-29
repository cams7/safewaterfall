/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.scheduler;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import javax.sql.DataSource;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import br.com.cams7.safewaterfall.common.scheduler.AppJobFactory;

/**
 * @author CAMs7
 *
 */
@Configuration
@EnableAutoConfiguration
public class AppQuartzConfig {

  public static final String STATUS_ARDUINO_JOB = "STATUS_ARDUINO_JOB";
  public static final String STATUS_ARDUINO_TRIGGER = "STATUS_ARDUINO_TRIGGER";

  public static final String SEND_MESSAGE_JOB = "SEND_MESSAGE_JOB";
  public static final String SEND_MESSAGE_TRIGGER = "SEND_MESSAGE_TRIGGER";

  public static final String STATUS_ARDUINO_CRON = "0/3 * * ? * * *";// Every 3 secounds
  public static final String SEND_ALERT_MESSAGE_CRON = "0/10 * * ? * * *";// Every 10 secounds
  public static final String SEND_STATUS_MESSAGE_CRON = "0 0/1 * ? * * *";// Every 1 minute

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public SpringBeanJobFactory springBeanJobFactory() {
    AppJobFactory jobFactory = new AppJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean scheduler(DataSource quartzDataSource, Trigger... triggers) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

    schedulerFactory.setOverwriteExistingJobs(true);
    schedulerFactory.setAutoStartup(true);
    schedulerFactory.setDataSource(quartzDataSource);
    schedulerFactory.setJobFactory(springBeanJobFactory());
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);

    if (isNotEmpty(triggers))
      schedulerFactory.setTriggers(triggers);

    return schedulerFactory;
  }

  // @Bean(name = SEND_MESSAGE_JOB)
  // public JobDetailFactoryBean sendMessageJobDetail() {
  // JobDetailFactoryBean jobDetailFactory = AppQuartzUtil.createJobDetail(SendMessageJob.class, SEND_MESSAGE_JOB);
  // return jobDetailFactory;
  // }
  //
  // @Bean(name = SEND_MESSAGE_TRIGGER)
  // public CronTriggerFactoryBean sendMessageTrigger(DataSource quartzDataSource,
  // @Qualifier(SEND_MESSAGE_JOB) JobDetail job) {
  // String sendStatusMessageCron = SEND_STATUS_MESSAGE_CRON;
  // CronTriggerFactoryBean trigger = AppQuartzUtil.createCronTrigger(job, sendStatusMessageCron,
  // SEND_MESSAGE_TRIGGER);
  // return trigger;
  // }
  //
  // @Bean(name = STATUS_ARDUINO_JOB)
  // public JobDetailFactoryBean arduinoJobDetail() {
  // JobDetailFactoryBean jobDetailFactory = AppQuartzUtil.createJobDetail(StatusArduinoJob.class,
  // STATUS_ARDUINO_JOB);
  // return jobDetailFactory;
  // }
  //
  // @Bean(name = STATUS_ARDUINO_TRIGGER)
  // public CronTriggerFactoryBean trigger(DataSource quartzDataSource,
  // @Qualifier(STATUS_ARDUINO_JOB) JobDetail job) {
  //
  // String statusArduinoCron = STATUS_ARDUINO_CRON;
  // CronTriggerFactoryBean trigger = AppQuartzUtil.createCronTrigger(job, statusArduinoCron,
  // STATUS_ARDUINO_TRIGGER);
  // return trigger;
  // }

  @Bean
  @QuartzDataSource
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource quartzDataSource() {
    return DataSourceBuilder.create().build();
  }

}
