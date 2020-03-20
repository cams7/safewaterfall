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
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
public class ArduinoEEPROMRead extends ArduinoEEPROM {

	/**
	 * 
	 */
	public ArduinoEEPROMRead() {
		super();
		setEvent(ArduinoEvent.READ);
	}

	/**
	 * @param status
	 * @param event
	 * @param pinType
	 * @param pin
	 * @param threadTime
	 * @param actionEvent
	 */
	private ArduinoEEPROMRead(ArduinoStatus status, ArduinoPinType pinType, byte pin, byte threadTime,
			byte actionEvent) {
		super(status, ArduinoEvent.READ, pinType, pin, threadTime, actionEvent);
	}

	public ArduinoEEPROMRead(ArduinoStatus status, ArduinoPinType pinType, byte pin) {
		this(status, pinType, pin, (byte) 0, (byte) 0);
	}

}
