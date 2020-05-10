/**
 * 
 */
package br.com.cams7.safewaterfall.common.model.vo;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.PIN_VALUE_MIN;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART.DIGITAL_PIN_VALUE_MAX;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.redis.core.RedisHash;
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
@ApiModel(description = "VO que representa o sensor.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("sensor")
public class AppSensorVO {

  @ApiModelProperty(notes = "Identificador único do sensor", example = "UUID V4", required = true, position = 1)
  private String id;

  @ApiModelProperty(notes = "Expressão Cron para as consulta da distancia enviadas pelo arduino",
      example = "0/3 * * ? * * *", required = true, position = 2)
  @NotBlank
  @Size(min = 13, max = 30)
  private String statusArduinoCron;

  @ApiModelProperty(notes = "Expressão Cron para envio do status do sensor", example = "0 0/1 * ? * * *",
      required = true, position = 3)
  @NotBlank
  @Size(min = 13, max = 30)
  private String sendStatusMessageCron;

  @ApiModelProperty(
      notes = "Expressão Cron para envio de alertas quando quando uma distancia minima foi alcançada",
      example = "0/10 * * ? * * *", required = true, position = 4)
  @NotBlank
  @Size(min = 13, max = 30)
  private String sendAlertMessageCron;

  @ApiModelProperty(notes = "Distancia minima permitida", example = "100", required = true, position = 5)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  private Short minimumAllowedDistance;

  @ApiModelProperty(notes = "Distancia maxima medida", example = "255", required = true, position = 6)
  @Min(PIN_VALUE_MIN)
  @Max(DIGITAL_PIN_VALUE_MAX)
  @NotNull
  private Short maximumMeasuredDistance;

  @ApiModelProperty(notes = "Distancia medida", example = "255", required = true, position = 7)
  private Short distance;

  public AppSensorVO(String id) {
    this();
    this.id = id;
  }

}
