/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.PIN_VALUE_MIN;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART.DIGITAL_PIN_VALUE_MAX;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import br.com.cams7.safewaterfall.common.model.BaseEntity;
import br.com.cams7.safewaterfall.common.model.vo.AppSensorVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "Entidade que representa o sensor")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "TB_SENSOR")
public class SensorEntity extends BaseEntity<String> {

  @ApiModelProperty(notes = "Identificador único do sensor", example = "UUID V4", required = true, position = 1)
  @Id
  @Column(name = "ID_SENSOR", nullable = false, updatable = false)
  private String id;

  @ApiModelProperty(notes = "Expressão Cron para as consulta da distancia enviadas pelo arduino",
      example = "0/3 * * ? * * *", required = true, position = 2)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ARDUINO_STATUS_CRON", nullable = false)
  private String statusArduinoCron;

  @ApiModelProperty(notes = "Expressão Cron para envio do status do sensor", example = "0 0/1 * ? * * *",
      required = true, position = 3)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ENV_STATUS_CRON", nullable = false)
  private String sendStatusMessageCron;

  @ApiModelProperty(
      notes = "Expressão Cron para envio de alertas quando quando uma distancia minima foi alcançada",
      example = "0/10 * * ? * * *", required = true, position = 4)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ENV_ALERTA_CRON", nullable = false)
  private String sendAlertMessageCron;

  @ApiModelProperty(notes = "Distancia minima permitida", example = "100", required = true, position = 5)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  @Column(name = "DISTANCIA_MIN")
  private Short minimumAllowedDistance;

  @ApiModelProperty(notes = "Distancia maxima medida", example = "255", required = true, position = 6)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  @Column(name = "DISTANCIA_MAX")
  private Short maximumMeasuredDistance;

  /**
   * @param id ID do sensor
   */
  public SensorEntity(String id) {
    this();
    this.id = id;
  }

  /**
   * @param sensor Entidade sensor
   * @param appSensor VO do sensor
   */
  public static void setSensor(SensorEntity sensor, AppSensorVO appSensor) {
    sensor.setId(appSensor.getId());
    sensor.setStatusArduinoCron(appSensor.getStatusArduinoCron());
    sensor.setSendStatusMessageCron(appSensor.getSendStatusMessageCron());
    sensor.setSendAlertMessageCron(appSensor.getSendAlertMessageCron());
    sensor.setMinimumAllowedDistance(appSensor.getMinimumAllowedDistance());
    sensor.setMaximumMeasuredDistance(appSensor.getMaximumMeasuredDistance());
  }

  /**
   * @param appSensor VO do sensor
   * @return Entidade sensor
   */
  public static SensorEntity getSensor(AppSensorVO appSensor) {
    SensorEntity sensor = new SensorEntity();
    setSensor(sensor, appSensor);
    return sensor;
  }

  /**
   * @param appSensor VO do sensor
   * @param sensor Entidade sensor
   */
  public static void setSensor(AppSensorVO appSensor, SensorEntity sensor) {
    appSensor.setId(sensor.getId());
    appSensor.setStatusArduinoCron(sensor.getStatusArduinoCron());
    appSensor.setSendStatusMessageCron(sensor.getSendStatusMessageCron());
    appSensor.setSendAlertMessageCron(sensor.getSendAlertMessageCron());
    appSensor.setMinimumAllowedDistance(sensor.getMinimumAllowedDistance());
    appSensor.setMaximumMeasuredDistance(sensor.getMaximumMeasuredDistance());
  }

  /**
   * @param sensor Entidade sensor
   * @return VO do sensor
   */
  public static AppSensorVO getSensor(SensorEntity sensor) {
    AppSensorVO appSensor = new AppSensorVO();
    setSensor(appSensor, sensor);
    return appSensor;
  }

}
