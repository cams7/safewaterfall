package br.com.cams7.safewaterfall.swsiren.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import br.com.cams7.safewaterfall.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Entidade que representa a sirene")
@SuppressWarnings("serial")
@Document
@NoArgsConstructor
@Data(staticConstructor = "of")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Siren extends BaseEntity<String> {

  public static final String CACHE_NAME = "siren";

  @ApiModelProperty(notes = "Identificador único da sirene", example = "UUID V4", required = true, position = 1)
  @NotBlank
  @Pattern(regexp = UUID_V4_REGEX)
  @Id
  private String id;

  @ApiModelProperty(notes = "Sirene esta ativa", example = "true", required = true, position = 2)
  private boolean active;

  public Siren(@NotBlank @Pattern(regexp = UUID_V4_REGEX) String id) {
    this();
    this.id = id;
  }

}
