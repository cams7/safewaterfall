/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import br.com.cams7.safewaterfall.common.model.Pin;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CAMs7
 *
 */
@ApiModel(description = "Entidade que representa o Sensor.")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "sensor")
public class SensorEntity extends Pin {

}
