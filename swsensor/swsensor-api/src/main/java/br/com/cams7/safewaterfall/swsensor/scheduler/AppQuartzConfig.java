/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.scheduler;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
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
import br.com.cams7.safewaterfall.common.error.AppException;
import br.com.cams7.safewaterfall.common.error.AppResourceNotFoundException;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import br.com.cams7.safewaterfall.common.scheduler.AppJobFactory;
import br.com.cams7.safewaterfall.common.scheduler.AppQuartzUtil;
import br.com.cams7.safewaterfall.common.service.AppSensorService;

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

  public static final long SENSOR_ID = 1;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private AppSensorService appSensorService;

  @Bean
  public SpringBeanJobFactory springBeanJobFactory() {
    AppJobFactory jobFactory = new AppJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean scheduler(DataSource quartzDataSource, Trigger... triggers) {

    try {
      Connection conn = quartzDataSource.getConnection();
      try (PreparedStatement pstmt = conn.prepareStatement(
          "select ARDUINO_STATUS_CRON, ENV_STATUS_CRON, ENV_ALERTA_CRON, DISTANCIA_MIN, DISTANCIA_MAX from TB_SENSOR where ID_SENSOR = ?")) {
        pstmt.setLong(1, SENSOR_ID);
        try (ResultSet rs = pstmt.executeQuery()) {
          if (rs.next()) {
            String statusArduinoCron = rs.getString("ARDUINO_STATUS_CRON");
            String sendStatusMessageCron = rs.getString("ENV_STATUS_CRON");
            String sendAlertMessageCron = rs.getString("ENV_ALERTA_CRON");
            Short minimumAllowedDistance = (short) rs.getInt("DISTANCIA_MIN");
            Short maximumMeasuredDistance = (short) rs.getInt("DISTANCIA_MAX");

            AppSensorVO sensor = new AppSensorVO(SENSOR_ID);
            sensor.setStatusArduinoCron(statusArduinoCron);
            sensor.setSendStatusMessageCron(sendStatusMessageCron);
            sensor.setSendAlertMessageCron(sendAlertMessageCron);
            sensor.setMinimumAllowedDistance(minimumAllowedDistance);
            sensor.setMaximumMeasuredDistance(maximumMeasuredDistance);

            appSensorService.save(sensor);
          }
        }
      }
    } catch (SQLException e) {
      throw new AppException(e);
    }

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

  @Bean(name = SEND_MESSAGE_JOB)
  public JobDetailFactoryBean sendMessageJobDetail() {
    JobDetailFactoryBean jobDetailFactory = AppQuartzUtil.createJobDetail(SendMessageJob.class, SEND_MESSAGE_JOB);
    return jobDetailFactory;
  }

  @Bean(name = SEND_MESSAGE_TRIGGER)
  public CronTriggerFactoryBean sendMessageTrigger(DataSource quartzDataSource,
      @Qualifier(SEND_MESSAGE_JOB) JobDetail job) {
    try {
      String sendStatusMessageCron = getCronExpression(quartzDataSource.getConnection(), "ENV_STATUS_CRON");
      CronTriggerFactoryBean trigger = AppQuartzUtil.createCronTrigger(job, sendStatusMessageCron,
          SEND_MESSAGE_TRIGGER);
      return trigger;
    } catch (SQLException e) {
      throw new AppException(e);
    }
  }

  @Bean(name = STATUS_ARDUINO_JOB)
  public JobDetailFactoryBean arduinoJobDetail() {
    JobDetailFactoryBean jobDetailFactory = AppQuartzUtil.createJobDetail(StatusArduinoJob.class,
        STATUS_ARDUINO_JOB);
    return jobDetailFactory;
  }

  @Bean(name = STATUS_ARDUINO_TRIGGER)
  public CronTriggerFactoryBean trigger(DataSource quartzDataSource,
      @Qualifier(STATUS_ARDUINO_JOB) JobDetail job) {
    try {
      String statusArduinoCron = getCronExpression(quartzDataSource.getConnection(), "ARDUINO_STATUS_CRON");
      CronTriggerFactoryBean trigger = AppQuartzUtil.createCronTrigger(job, statusArduinoCron,
          STATUS_ARDUINO_TRIGGER);
      return trigger;
    } catch (SQLException e) {
      throw new AppException(e);
    }
  }

  @Bean
  @QuartzDataSource
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource quartzDataSource() {
    return DataSourceBuilder.create().build();
  }

  /**
   * @param conn
   * @param fieldName
   * @return
   * @throws SQLException
   */
  private static final String getCronExpression(Connection conn, String fieldName) throws SQLException {
    try (PreparedStatement pstmt = conn.prepareStatement(String.format(
        "select %s from TB_SENSOR where ID_SENSOR = ?", fieldName))) {
      pstmt.setLong(1, SENSOR_ID);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next())
          return rs.getString(fieldName);

        throw new AppResourceNotFoundException("A expressão cron não foi encontrada");
      }
    }
  }

}
