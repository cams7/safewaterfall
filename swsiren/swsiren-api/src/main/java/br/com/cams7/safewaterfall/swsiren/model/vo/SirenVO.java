/**
 * 
 */
package br.com.cams7.safewaterfall.swsiren.model.vo;

import static br.com.cams7.safewaterfall.common.model.BaseEntity.UUID_V4_REGEX;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class SirenVO {

  @ApiModelProperty(notes = "Identificador único da sirene", example = "UUID V4", required = true, position = 1)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  private String id;

  @ApiModelProperty(notes = "Sirene esta ativa", example = "true", required = true, position = 2)
  private boolean active;

  public SirenVO(@NotBlank @Pattern(regexp = UUID_V4_REGEX) String id) {
    this();
    this.id = id;
  }

}
