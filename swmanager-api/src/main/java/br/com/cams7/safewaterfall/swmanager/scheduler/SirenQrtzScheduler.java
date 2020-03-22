/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.scheduler;

import javax.annotation.PostConstruct;
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
public class SirenQrtzScheduler {

  private static final String SIREN_JOB = "sirenJob";
  private static final String SIREN_TRIGGER = "sirenTrigger";
  private static final String CRON_EVERY_1MINUTE = "0 0/1 * ? * * *";

  @Autowired
  private ApplicationContext applicationContext;

  @PostConstruct
  public void init() {}

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

  @Bean(name = SIREN_JOB)
  public JobDetailFactoryBean sirenJobDetail() {
    JobDetailFactoryBean jobDetailFactory = QuartzUtil.createJobDetail(SirenJob.class, "Qrtz_SirenJob_Detail");
    return jobDetailFactory;
  }

  @Bean(name = SIREN_TRIGGER)
  public CronTriggerFactoryBean trigger(@Qualifier(SIREN_JOB) JobDetail job) {
    CronTriggerFactoryBean trigger = QuartzUtil.createCronTrigger(job, CRON_EVERY_1MINUTE, "Qrtz_SirenTrigger");
    return trigger;
  }

  @Bean
  @QuartzDataSource
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource quartzDataSource() {
    return DataSourceBuilder.create().build();
  }

}
