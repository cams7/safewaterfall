/**
 * 
 */
package br.com.cams7.safewaterfall.common.model.vo;

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
@ApiModel(description = "VO que representa o escalonador.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("scheduler")
public class AppSchedulerVO {

  @ApiModelProperty(notes = "Identificador único do escalonador", example = "1", required = true, position = 1)
  private String id; // Trigger name

  @ApiModelProperty(notes = "Expressão Cron", example = "0/3 * * ? * * *", required = true, position = 2)
  private String cronExpression;

  public AppSchedulerVO(String id) {
    this();
    this.id = id;
  }
}
