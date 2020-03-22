/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import javax.sql.DataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import br.com.cams7.safewaterfall.common.scheduler.AppJobFactory;
import br.com.cams7.safewaterfall.common.scheduler.QuartzUtil;

/**
 * @author CAMs7
 *
 */
@Configuration
@EnableAutoConfiguration
public class ArduinoQrtzScheduler {

  private static final String ARDUINO_JOB = "arduinoJob";
  private static final String ARDUINO_TRIGGER = "arduinoTrigger";
  private static final String CRON_EVERY_30SECONDS = "0/30 * * ? * * *";

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

    if (ArrayUtils.isNotEmpty(triggers))
      schedulerFactory.setTriggers(triggers);

    return schedulerFactory;
  }

  @Bean(name = ARDUINO_JOB)
  public JobDetailFactoryBean arduinoJobDetail() {
    JobDetailFactoryBean jobDetailFactory = QuartzUtil.createJobDetail(ArduinoJob.class, "Qrtz_ArduinoJob_Detail");
    return jobDetailFactory;
  }

  @Bean(name = ARDUINO_TRIGGER)
  public CronTriggerFactoryBean trigger(@Qualifier(ARDUINO_JOB) JobDetail job) {
    CronTriggerFactoryBean trigger = QuartzUtil.createCronTrigger(job, CRON_EVERY_30SECONDS,
        "Qrtz_ArduinoTrigger");
    return trigger;
  }

  @Bean
  @QuartzDataSource
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource quartzDataSource() {
    return DataSourceBuilder.create().build();
  }

}
