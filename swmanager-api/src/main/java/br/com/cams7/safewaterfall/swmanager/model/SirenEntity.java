/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.model;

import static javax.persistence.GenerationType.SEQUENCE;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import br.com.cams7.safewaterfall.common.model.BaseEntity;
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
@ApiModel(description = "Entidade que representa o sirene")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "TB_SIRENE")
public class SirenEntity extends BaseEntity<Long> {

  @ApiModelProperty(notes = "Identificador único do sirene", example = "1", required = true, position = 1)
  @Id
  @SequenceGenerator(name = "SQ_SIRENE", sequenceName = "SQ_SIRENE", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "SQ_SIRENE")
  @Column(name = "ID_SIRENE", nullable = false, updatable = false)
  private Long id;

  @OneToMany(mappedBy = "siren")
  private List<SensorEntity> sensors;

}
