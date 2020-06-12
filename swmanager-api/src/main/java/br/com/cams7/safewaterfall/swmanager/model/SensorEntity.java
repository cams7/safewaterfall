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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import br.com.cams7.safewaterfall.common.model.BaseEntity;
import br.com.cams7.safewaterfall.common.model.Sensor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "Entidade que representa o sensor")
@NoArgsConstructor
@Data(staticConstructor = "of")
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "TB_SENSOR")
public class SensorEntity extends BaseEntity<Long> {

  private static final long serialVersionUID = -6045751962349097160L;

  public static final String CACHE_NAME = "sensor";

  @ApiModelProperty(notes = "Identificador único do sensor", example = "1", required = true, position = 1)
  @Id
  @SequenceGenerator(name = "SQ_SENSOR", sequenceName = "SQ_SENSOR", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "SQ_SENSOR")
  @Column(name = "ID_SENSOR", nullable = false, updatable = false)
  private Long id;

  @ApiModelProperty(notes = "Identificador único do dispositivo relacionado", example = "UUID V4", required = true,
      position = 2)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  @Column(name = "ID_DISPOSITIVO", nullable = false, updatable = false)
  private String deviceId;

  @ApiModelProperty(notes = "Expressão Cron para as consulta da distancia enviadas pelo arduino",
      example = "0/3 * * ? * * *", required = true, position = 3)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ARDUINO_STATUS_CRON", nullable = false)
  private String statusArduinoCron;

  @ApiModelProperty(notes = "Expressão Cron para envio do status do sensor", example = "0 0/1 * ? * * *",
      required = true, position = 4)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ENV_STATUS_CRON", nullable = false)
  private String sendStatusMessageCron;

  @ApiModelProperty(
      notes = "Expressão Cron para envio de alertas quando quando uma distancia minima foi alcançada",
      example = "0/10 * * ? * * *", required = true, position = 5)
  @NotBlank
  @Size(min = 13, max = 30)
  @Column(name = "ENV_ALERTA_CRON", nullable = false)
  private String sendAlertMessageCron;

  @ApiModelProperty(notes = "Distancia minima permitida", example = "100", required = true, position = 6)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  @Column(name = "DISTANCIA_MIN")
  private Short minimumAllowedDistance;

  @ApiModelProperty(notes = "Distancia maxima medida", example = "255", required = true, position = 7)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  @Column(name = "DISTANCIA_MAX")
  private Short maximumMeasuredDistance;

  @ApiModelProperty(notes = "Sirene", required = true, position = 8)
  @NotNull
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "ID_SIRENE", referencedColumnName = "ID_SIRENE")
  private SirenEntity siren;

  @ApiModelProperty(notes = "Endereço do sensor", example = "http://127.0.0.1:80", required = true, position = 9)
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
   * @param sensorEntity Entidade sensor
   * @param sensor VO do sensor
   */
  public static void setSensor(SensorEntity sensorEntity, Sensor sensor) {
    sensorEntity.setDeviceId(sensor.getId());
    sensorEntity.setStatusArduinoCron(sensor.getStatusArduinoCron());
    sensorEntity.setSendStatusMessageCron(sensor.getSendStatusMessageCron());
    sensorEntity.setSendAlertMessageCron(sensor.getSendAlertMessageCron());
    sensorEntity.setMinimumAllowedDistance(sensor.getMinimumAllowedDistance());
    sensorEntity.setMaximumMeasuredDistance(sensor.getMaximumMeasuredDistance());
  }

  /**
   * @param sensor VO do sensor
   * @return Entidade sensor
   */
  public static SensorEntity getSensor(Sensor sensor) {
    SensorEntity sensorEntity = new SensorEntity();
    setSensor(sensorEntity, sensor);
    return sensorEntity;
  }

  /**
   * @param sensor VO do sensor
   * @param sensorEntity Entidade sensor
   */
  public static void setSensor(Sensor sensor, SensorEntity sensorEntity) {
    sensor.setId(sensorEntity.getDeviceId());
    sensor.setStatusArduinoCron(sensorEntity.getStatusArduinoCron());
    sensor.setSendStatusMessageCron(sensorEntity.getSendStatusMessageCron());
    sensor.setSendAlertMessageCron(sensorEntity.getSendAlertMessageCron());
    sensor.setMinimumAllowedDistance(sensorEntity.getMinimumAllowedDistance());
    sensor.setMaximumMeasuredDistance(sensorEntity.getMaximumMeasuredDistance());
  }

  /**
   * @param sensorEntity Entidade sensor
   * @return VO do sensor
   */
  public static Sensor getSensor(SensorEntity sensorEntity) {
    Sensor sensor = new Sensor();
    setSensor(sensor, sensorEntity);
    return sensor;
  }

}
