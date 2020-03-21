/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.arduino.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ArduinoUSARTMessage extends ArduinoUSART {

  /**
   * 
   */
  public ArduinoUSARTMessage() {
    super();
    setEvent(ArduinoEvent.MESSAGE);
  }

  /**
   * @param status
   * @param pinType
   * @param pin
   * @param pinValue
   */
  private ArduinoUSARTMessage(ArduinoStatus status, ArduinoPinType pinType, byte pin, short pinValue) {
    super(status, ArduinoEvent.MESSAGE, pinType, pin, pinValue);
  }

  public ArduinoUSARTMessage(ArduinoStatus status, ArduinoPinType pinType, byte pin) {
    this(status, pinType, pin, (byte) 0);
  }

}
