/**
 * 
 */
package br.com.cams7.safewaterfall.swsensor.arduino.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cams7
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "pinType", "pin" }, callSuper = false)
public abstract class ArduinoPin {

	private final static String PIN_DIGITAL = "2, 4, 7, 8, 12, 13";
	private final static String PIN_DIGITAL_PWM = "3, 5, 6, 9, 10, 11";
	private final static String PIN_ANALOG = "0, 1, 2, 3, 4, 5";

	// Numero maximo da PORTA DIGITAL e 63
	public static final byte DIGITAL_PIN_MAX = 0x3F;
	// Numero maximo da PORTA ANALOGICA e 15
	public static final byte ANALOG_PIN_MAX = 0x0F;

	private static byte[] pinsDigital;
	private static byte[] pinsDigitalPWM;
	private static byte[] pinsAnalog;

	private ArduinoPinType pinType;
	private byte pin;

	static {
		final String SEPARATE = ",";

		pinsDigital = getPins(PIN_DIGITAL.split(SEPARATE));
		pinsDigitalPWM = getPins(PIN_DIGITAL_PWM.split(SEPARATE));
		pinsAnalog = getPins(PIN_ANALOG.split(SEPARATE));
	}

	/**
	 * 
	 */
	public ArduinoPin() {
		setPin((byte) 0x00);
	}

	public ArduinoPin(ArduinoPinType pinType, byte pin) {
		this();

		setPinType(pinType);
		setPin(pin);
	}

	public void changeCurrentValues(ArduinoPin arduino) {
		setPinType(arduino.getPinType());
		setPin(arduino.getPin());
	}

	private static byte[] getPins(final String[] values) {
		byte[] pins = new byte[values.length];
		for (byte i = 0; i < values.length; i++)
			pins[i] = Byte.parseByte(values[i].trim());
		return pins;
	}

	public enum ArduinoPinType {
		DIGITAL('d'), // Porta Digital
		ANALOG('a'); // Porta Analogica

		private char abbreviation;

		private ArduinoPinType(char abbreviation) {
			this.abbreviation = abbreviation;
		}

		public char getAbbreviation() {
			return abbreviation;
		}

	}

	public static byte[] getPinsDigital() {
		return pinsDigital;
	}

	public static byte[] getPinsDigitalPWM() {
		return pinsDigitalPWM;
	}

	public static byte[] getPinsAnalog() {
		return pinsAnalog;
	}

}
