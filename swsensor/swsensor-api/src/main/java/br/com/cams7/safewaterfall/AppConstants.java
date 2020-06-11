/**
 * 
 */
package br.com.cams7.safewaterfall;

/**
 * @author ceanm
 *
 */
public final class AppConstants {

  public static final String JOB_GROUP_NAME = "SWSENSOR_JOB";

  public static final String RESCHEDULE_PATH = "/reschedule";
  public static final String PAUSE_PATH = "/pause";
  public static final String RESUME_PATH = "/resume";

  public static final String STATUS_ARDUINO_CRON = "0/3 * * ? * * *";
  public static final String SEND_STATUS_MESSAGE_CRON = "0 0/1 * ? * * *";

}
