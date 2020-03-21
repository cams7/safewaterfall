/**
 * 
 */
package br.com.cams7.safewaterfall.common.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"pinType", "pin"}, callSuper = false)
@Embeddable
public class PinPK implements Serializable {

  @NotNull
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "tipo_pino", insertable = false, updatable = false)
  private ArduinoPinType pinType;

  @NotNull
  @Column(name = "pino_arduino", insertable = false, updatable = false)
  private Short pin;

  public PinPK(ArduinoPinType pinType, Short pin) {
    this();
    this.pinType = pinType;
    this.pin = pin;
  }

}
