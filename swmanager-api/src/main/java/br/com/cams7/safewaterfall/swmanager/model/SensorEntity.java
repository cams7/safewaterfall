/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.PIN_VALUE_MIN;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART.DIGITAL_PIN_VALUE_MAX;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
public class SensorEntity extends BaseEntity<Long> {

  @ApiModelProperty(notes = "Identificador único do sensor", example = "1", required = true, position = 1)
  @Id
  @SequenceGenerator(name = "SQ_SENSOR", sequenceName = "SQ_SENSOR", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "SQ_SENSOR")
  @Column(name = "ID_SENSOR", nullable = false, updatable = false)
  private Long id;

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

  @ApiModelProperty(notes = "Sirene", required = true, position = 7)
  // @NotNull
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "ID_SIRENE", referencedColumnName = "ID_SIRENE")
  private SirenEntity siren;

  @ApiModelProperty(notes = "Endereço do sensor", example = "http://127.0.0.1:80", required = true, position = 8)
  @NotBlank
  @Size(min = 19, max = 100)
  @Column(name = "ENDERECO_SENSOR", nullable = false)
  private String sensorAddress;

  /**
   * @param id ID do sensor
   */
  public SensorEntity(Long id) {
    this();
    this.id = id;
  }

  /**
   * @param appSensor VO do sensor
   * @return
   */
  public static SensorEntity getSensor(AppSensorVO appSensor) {
    SensorEntity sensor = new SensorEntity(appSensor.getId());
    sensor.setStatusArduinoCron(appSensor.getStatusArduinoCron());
    sensor.setSendStatusMessageCron(appSensor.getSendStatusMessageCron());
    sensor.setSendAlertMessageCron(appSensor.getSendAlertMessageCron());
    sensor.setMinimumAllowedDistance(appSensor.getMinimumAllowedDistance());
    sensor.setMaximumMeasuredDistance(appSensor.getMaximumMeasuredDistance());
    return sensor;
  }

  /**
   * @param sensor Entidade sensor
   * @return
   */
  public static AppSensorVO getSensor(SensorEntity sensor) {
    AppSensorVO appSensor = new AppSensorVO(sensor.getId());
    appSensor.setStatusArduinoCron(sensor.getStatusArduinoCron());
    appSensor.setSendStatusMessageCron(sensor.getSendStatusMessageCron());
    appSensor.setSendAlertMessageCron(sensor.getSendAlertMessageCron());
    appSensor.setMinimumAllowedDistance(sensor.getMinimumAllowedDistance());
    appSensor.setMaximumMeasuredDistance(sensor.getMaximumMeasuredDistance());
    return appSensor;
  }

}
