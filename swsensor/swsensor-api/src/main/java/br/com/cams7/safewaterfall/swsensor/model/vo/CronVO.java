/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ceanm
 *
 */
@ApiModel(description = "VO utilizado para informar uma expressão Cron")
@Data
public class CronVO {

  /**
   * Example: 0/3 * * ? * * *
   */
  @ApiModelProperty(notes = "Expressão Cron", example = "0/3 * * ? * * *", required = true, position = 1)
  @NotBlank
  @Size(min = 13, max = 30)
  private String cronExpression;

}
