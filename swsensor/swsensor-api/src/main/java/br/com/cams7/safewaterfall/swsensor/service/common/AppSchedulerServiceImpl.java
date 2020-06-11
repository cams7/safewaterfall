package br.com.cams7.safewaterfall.swsensor.service.common;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.swsensor.helper.AutowiringSpringBeanJobFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AppSchedulerServiceImpl implements AppSchedulerService {

  private SchedulerFactoryBean scheduler;

  @Autowired
  private AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;

  @PostConstruct
  void init() {
    scheduler = quartzScheduler();
  }

  @Bean
  public SchedulerFactoryBean quartzScheduler() {
    try {
      SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
      quartzScheduler.setOverwriteExistingJobs(true);
      quartzScheduler.setSchedulerName("wms-quartz-scheduler");
      quartzScheduler.setJobFactory(autowiringSpringBeanJobFactory);
      quartzScheduler.setQuartzProperties(quartzProperties());
      log.info("Quartz Scheduler initialized");
      return quartzScheduler;
    } catch (RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not be initialized, the error is %s", e
          .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  @PreDestroy
  void destroy() {
    try {
      scheduler.destroy();
    } catch (SchedulerException | RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not be shutdown, the error is %s", e
          .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  @Bean
  public Properties quartzProperties() {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    Properties properties = null;
    try {
      propertiesFactoryBean.afterPropertiesSet();
      properties = propertiesFactoryBean.getObject();
      return properties;
    } catch (IOException e) {
      final String message = String.format("Quartz Scheduler can not read properties file, the error is %s", e
          .getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  @Override
  public void register(JobDetail jobDetail, CronTrigger cronTrigger) {

    try {
      /*
       * We have 3 ways to restart the application 1. Reschedule the job to it's original schedule. 2. Resume the
       * job to keep the latest schedule. It will also restart the job that been paused. 3. Do nothing, the jobs
       * will remain the same as they are before the restart.
       * 
       * Here we choose option 2. The benefit is in the future we can use the reschedule function to manage the
       * schedule in DB. We can rely on the persistent of the schedule in DB.
       */

      if (scheduler.getScheduler().checkExists(cronTrigger.getKey())) {
        // Option one:
        // scheduler.getScheduler().rescheduleJob(cronTrigger.getKey(), cronTrigger);
        // log.info("Quartz Scheduler reschedule trigger {}", cronTrigger.getKey());

        // Option two:
        // scheduler.getScheduler().resumeTrigger(cronTrigger.getKey());
        // log.info("Quartz Scheduler resume trigger {}", cronTrigger.getKey());

        // Option three:
        log.info("Quartz Scheduler keep the trigger status {}", cronTrigger.getKey());
      } else {
        scheduler.getScheduler().scheduleJob(jobDetail, cronTrigger);
        log.info("Quartz Scheduler register new trigger {}", cronTrigger.getKey());
      }
    } catch (SchedulerException | RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not register trigger %s. The error is %s",
          cronTrigger.getKey(), e.getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

  @Override
  public void reschedule(CronTrigger cronTrigger) {
    try {
      log.info("Reschedule trigger {}", cronTrigger.getKey());
      log.info("The new schedule is {}", cronTrigger.getCronExpression());

      scheduler.getScheduler().rescheduleJob(cronTrigger.getKey(), cronTrigger);

    } catch (SchedulerException | RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not reschedule trigger %s. the error is %s",
          cronTrigger.getKey(), e.getMessage());
      log.error(message);
      throw new AppException(message, e);
    }

  }

  @Override
  public void pause(CronTrigger cronTrigger) {
    try {
      log.info("Pause trigger {}", cronTrigger.getKey());
      scheduler.getScheduler().pauseTrigger(cronTrigger.getKey());
    } catch (SchedulerException | RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not pause trigger %s. the error is %s",
          cronTrigger.getKey(), e.getMessage());
      log.error(message);
      throw new AppException(message, e);
    }

  }

  @Override
  public void resume(CronTrigger cronTrigger) {
    try {
      log.info("Pause trigger {}", cronTrigger.getKey());
      scheduler.getScheduler().resumeTrigger(cronTrigger.getKey());
    } catch (SchedulerException | RuntimeException e) {
      final String message = String.format("Quartz Scheduler can not resume trigger %s. the error is %s",
          cronTrigger.getKey(), e.getMessage());
      log.error(message);
      throw new AppException(message, e);
    }
  }

}
