#include <Arduino.h>
#include <avr/eeprom.h>

#include "Sisbarc.h"
#include "SisbarcProtocol.h"
#include "SisbarcEEPROM.h"

#include "vo/ArduinoStatus.h"
#include "vo/ArduinoUSART.h"
#include "vo/ArduinoEEPROM.h"

#define D13_LED 13 //Pino 13 Digital

#define D12_BUZZER 12

#define BUZZER_FREQUENCY 2500

#define INTERVALO_3SEGUNDOS 3000  // 3 segundos

#define ALERTA 0 // Acende ou apaga

#define EEPROM_LENGTH 255

//Declared weak in Arduino.h to allow user redefinitions.
int atexit(void (*func)()) {
	return 0;
}

// Weak empty variant initialization function.
// May be redefined by variant files.
void initVariant() __attribute__((weak));
void initVariant() {
}

using namespace SISBARC;

void clearEEPROM(void);
bool callD12Buzzer(ArduinoStatus* const);

uint8_t evento = ALERTA;
boolean sireneAtiva = false;
boolean sireneLigada = sireneAtiva;

int main(void) {
	init();

	initVariant();

#if defined(USBCON)
	USBDevice.attach();
#endif

	setup();

	for (;;)
		loop();

	return 0;
}

void setup(void) {
	pinMode(D13_LED, OUTPUT);
	pinMode(D12_BUZZER, OUTPUT);

	clearEEPROM();

	Sisbarc.begin(&Serial);

	Sisbarc.addThreadInterval(0x00, INTERVALO_3SEGUNDOS);

	int16_t evento = Sisbarc.onRun(ArduinoStatus::DIGITAL, D12_BUZZER, callD12Buzzer);
	if (evento != -1) {
	}
}

void loop(void) {
	Sisbarc.run();
}

void clearEEPROM(void) {
	digitalWrite(D13_LED, HIGH);

	for (byte i = 0 ; i < EEPROM_LENGTH; i++)
		eeprom_write_byte((unsigned char *) i, 0x00);

	digitalWrite(D13_LED, LOW);
}

//Foi chamado através da serial
bool isCallBySerial(ArduinoStatus* const arduino) {
	if (ArduinoStatus::OTHER_DEVICE != arduino->getTransmitterValue())
		return false;

	if (!(ArduinoStatus::SEND_RESPONSE == arduino->getStatusValue() || ArduinoStatus::RESPONSE_RESPONSE == arduino->getStatusValue()))
		return false;

	//OTHER_DEVICE and SEND_RESPONSE
	//OTHER_DEVICE and RESPONSE_RESPONSE

	return true;
}

//Foi chamado através da serial é o pino é digital
bool isCallBySerialToPinDigital(ArduinoStatus* const arduino) {
	if (!isCallBySerial(arduino))
		return false;

	if (arduino->getPinType() != ArduinoStatus::DIGITAL)
		return false;

	return true;
}


void alteraEEPROM(ArduinoStatus* const arduino) {
	ArduinoEEPROMWrite* arduinoEEPROMWrite = ((ArduinoEEPROMWrite*) arduino);

	int16_t returnValue = SisbarcEEPROM::write(arduinoEEPROMWrite);
	if (returnValue == 0x0000 || returnValue == 0x0001) {
		if (arduino->getPin() == D12_BUZZER) {
			evento = arduinoEEPROMWrite->getActionEvent();
		}

		arduinoEEPROMWrite->setTransmitterValue(ArduinoStatus::ARDUINO_DEVICE);
		arduinoEEPROMWrite->setStatusValue(ArduinoStatus::RESPONSE);
		Sisbarc.send(arduinoEEPROMWrite);
	}
}

void buscaEEPROM(ArduinoStatus* const arduino) {
	ArduinoEEPROMRead* arduinoEEPROMRead = ((ArduinoEEPROMRead*) arduino);

	EEPROMData* data = SisbarcEEPROM::read(arduinoEEPROMRead);
	if (data == NULL)
		return;

	arduinoEEPROMRead->setThreadInterval(data->getThreadInterval());
	arduinoEEPROMRead->setActionEvent(data->getActionEvent());

	arduinoEEPROMRead->setTransmitterValue(ArduinoStatus::ARDUINO_DEVICE);
	arduinoEEPROMRead->setStatusValue(ArduinoStatus::RESPONSE);

	Sisbarc.send(arduinoEEPROMRead);
}

void tocarSirene(uint8_t  pino) {
	if(sireneAtiva) {
		if(!sireneLigada) {
			tone(pino, BUZZER_FREQUENCY);
			sireneLigada = true;
		}
	} else {
		if(sireneLigada) {
			noTone(pino);
			sireneLigada = false;
		}
	}
}

void alteraEstadoSirenePorSerial(ArduinoStatus* const arduino) {
	ArduinoUSART* arduinoUSART = ((ArduinoUSART*) arduino);

	arduinoUSART->setTransmitterValue(ArduinoStatus::ARDUINO_DEVICE);
	arduinoUSART->setStatusValue(ArduinoStatus::RESPONSE);

	sireneAtiva = arduinoUSART->getPinValue() == 0x0001;
	tocarSirene(arduinoUSART->getPin());

	Sisbarc.send(arduinoUSART);
}

void buscaEstadoSirene(ArduinoStatus* const arduino) {
	ArduinoUSARTMessage* arduinoUSART = ((ArduinoUSARTMessage*) arduino);

	arduinoUSART->setTransmitterValue(ArduinoStatus::ARDUINO_DEVICE);
	arduinoUSART->setStatusValue(ArduinoStatus::RESPONSE);

	arduinoUSART->setPinValue(sireneAtiva ? 0x0001 : 0x0000);

	Sisbarc.send(arduinoUSART);
}

bool isValidEvent(ArduinoStatus* const arduino) {
	if (arduino->getEventValue() == ArduinoStatus::EXECUTE) {
		alteraEstadoSirenePorSerial(arduino);
		return true;
	}

	if (arduino->getEventValue() == ArduinoStatus::MESSAGE) {
		buscaEstadoSirene(arduino);
		return true;
	}

	if (arduino->getEventValue() == ArduinoStatus::WRITE) {
		alteraEEPROM(arduino);
		return true;
	}

	if (arduino->getEventValue() == ArduinoStatus::READ) {
		buscaEEPROM(arduino);
		return true;
	}

	return false;
}

void alteraEstadoSirene(uint8_t const pin) {
	if (sireneAtiva) {
		switch (evento) {
		case ALERTA: {
			break;
		}
		default:
			break;

		}
	}
}

bool callD12Buzzer(ArduinoStatus* const arduino) {
	if (arduino == NULL) {
		alteraEstadoSirene(D12_BUZZER);
		return false;
	}

	if (!isCallBySerialToPinDigital(arduino))
		return false;

	if (arduino->getPin() != D12_BUZZER)
		return false;

	return isValidEvent(arduino);
}
