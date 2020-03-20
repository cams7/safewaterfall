package br.com.cams7.safewaterfall.swsensor.service;

import org.springframework.stereotype.Service;

import br.com.cams7.safewaterfall.common.model.PinPK;
import br.com.cams7.safewaterfall.swsensor.arduino.ArduinoException;
import br.com.cams7.safewaterfall.swsensor.arduino.ArduinoServiceImpl;
import br.com.cams7.safewaterfall.swsensor.arduino.model.CurrentStatus;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.Arduino;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.ArduinoUSART;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.Arduino.ArduinoStatus;
import br.com.cams7.safewaterfall.swsensor.arduino.model.vo.ArduinoPin.ArduinoPinType;

@Service
public class AppArduinoServiceImpl extends ArduinoServiceImpl implements AppArduinoService {

	private final static String SERIAL_PORT = "COM3";
	private final static int SERIAL_BAUD_RATE = 9600;
	private final static long SERIAL_THREAD_TIME = 500l; // Serial verification in MILLISECOUNDS

	public AppArduinoServiceImpl() {
		super(SERIAL_PORT, SERIAL_BAUD_RATE, SERIAL_THREAD_TIME);
	}

	protected void receiveExecute(ArduinoPinType pinType, byte pin, short pinValue) {
		getLog().info(String.format("receiveExecute -> pinType: %s, pin: %s, pinValue: %s", pinType, pin, pinValue));

	}

	protected void receiveMessage(ArduinoPinType pinType, byte pin, short pinValue) {
		getLog().info(String.format("receiveMessage -> pinType: %s, pin: %s, pinValue: %s", pinType, pin, pinValue));
	}

	protected void receiveWrite(ArduinoPinType pinType, byte pin, byte threadInterval, byte actionEvent) {
		getLog().info(String.format("receiveWrite -> pinType: %s, pin: %s, threadInterval: %s, actionEvent: %s",
				pinType, pin, threadInterval, actionEvent));
	}

	protected void receiveRead(ArduinoPinType pinType, byte pin, byte threadInterval, byte actionEvent) {
		getLog().info(String.format("receiveRead -> pinType: %s, pin: %s, threadInterval: %s, actionEvent: %s", pinType,
				pin, threadInterval, actionEvent));
	}

	protected short sendResponse(ArduinoPinType pinType, byte pin, short pinValue) {
		getLog().info(String.format("sendResponse -> pinType: %s, pin: %s, pinValue: %s", pinType, pin, pinValue));
		return 0;
	}

	@Override
	public void carregarDistancia(PinPK pino) {
		ArduinoPinType tipoPino = pino.getPinType();
		byte pinoEco = pino.getPin().byteValue();

		if (tipoPino == ArduinoPinType.DIGITAL)
			sendPinDigitalUSARTMessage(ArduinoStatus.SEND_RESPONSE, pinoEco);

	}

	private Arduino getArduinoResponse(ArduinoEvent event, PinPK pinPK) {
		ArduinoPinType pinType = pinPK.getPinType();
		byte pin = pinPK.getPin().byteValue();

		String id = getKeyCurrentStatus(event, pinType, pin);

		if (!repository.existsById(id))
			return null;

		CurrentStatus currentStatus = repository.findById(id).orElseThrow(() -> new ArduinoException(
				String.format("Não foi encontrado nenhum estado do arduino pelo id %s", id)));
		Arduino arduino = currentStatus.getArduino();

		if (arduino.getTransmitter() != Arduino.ArduinoTransmitter.ARDUINO)
			return null;

		if (arduino.getStatus() != ArduinoStatus.RESPONSE)
			return null;

		return arduino;
	}

	@Override
	public Short buscarDistancia(PinPK pino, ArduinoEvent arduinoEvent) {
		if (arduinoEvent != ArduinoEvent.EXECUTE && arduinoEvent != ArduinoEvent.MESSAGE)
			return null;

		Arduino arduino = getArduinoResponse(arduinoEvent, pino);
		if (arduino == null)
			return null;

		short distancia = ((ArduinoUSART) arduino).getPinValue();
		return distancia;
	}

}
