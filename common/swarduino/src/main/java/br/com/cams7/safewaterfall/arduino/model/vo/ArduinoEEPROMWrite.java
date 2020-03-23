/**
 * 
 */
package br.com.cams7.safewaterfall.arduino.model.vo;

import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent.WRITE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
public class ArduinoEEPROMWrite extends ArduinoEEPROM {

  /**
   * 
   */
  public ArduinoEEPROMWrite() {
    super();
    setEvent(WRITE);
  }

  /**
   * @param status
   * @param event
   * @param pinType
   * @param pin
   * @param threadTime
   * @param actionEvent
   */
  public ArduinoEEPROMWrite(ArduinoStatus status, ArduinoPinType pinType, byte pin, byte threadTime,
      byte actionEvent) {
    super(status, WRITE, pinType, pin, threadTime, actionEvent);
  }

}
