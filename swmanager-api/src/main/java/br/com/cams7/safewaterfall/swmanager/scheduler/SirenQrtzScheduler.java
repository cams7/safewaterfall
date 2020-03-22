/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.scheduler;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
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
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import br.com.cams7.safewaterfall.common.scheduler.AppJobFactory;

/**
 * @author CAMs7
 *
 */
@Configuration
@EnableAutoConfiguration
public class SirenQrtzScheduler {

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
  public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job, DataSource quartzDataSource) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

    schedulerFactory.setJobFactory(springBeanJobFactory());
    schedulerFactory.setJobDetails(job);
    schedulerFactory.setTriggers(trigger);

    // Comment the following line to use the default Quartz job store.
    schedulerFactory.setDataSource(quartzDataSource);

    return schedulerFactory;
  }

  @Bean
  public JobDetailFactoryBean sirenJobDetail() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    jobDetailFactory.setJobClass(SirenJob.class);
    jobDetailFactory.setName("Qrtz_SirenJob_Detail");
    jobDetailFactory.setDescription("Invoke SirenJob service...");
    jobDetailFactory.setDurability(true);
    return jobDetailFactory;
  }

  @Bean
  public SimpleTriggerFactoryBean trigger(JobDetail job) {
    SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
    trigger.setJobDetail(job);

    int frequencyInSec = 10;
    trigger.setRepeatInterval(frequencyInSec * 1000);
    trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    trigger.setName("Qrtz_SirenTrigger");
    return trigger;
  }

  @Bean
  @QuartzDataSource
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource quartzDataSource() {
    return DataSourceBuilder.create().build();
  }

}
