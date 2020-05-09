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
@ApiModel(description = "VO que representa a sirene.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@RedisHash("siren")
public class AppSirenVO {

  @ApiModelProperty(notes = "Identificador único da sirene", example = "1", required = true, position = 1)
  private Long id;

  @ApiModelProperty(notes = "Sirene esta ativa", example = "true", required = true, position = 2)
  private boolean active;

  public AppSirenVO(Long id) {
    this();
    this.id = id;
  }
}
