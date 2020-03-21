#include <Arduino.h>
#include "Sisbarc.h"
#include "SisbarcProtocol.h"
#include "SisbarcEEPROM.h"

#include "vo/ArduinoStatus.h"
#include "vo/ArduinoUSART.h"
#include "vo/ArduinoEEPROM.h"

#define D09_TRIGGER 9 //Pino 09 PWM
#define D08_ECHO 8 //Pino 08 Digital

#define INTERVALO_10MICROSEGUNDOS  10    // 10 microsegundos
#define INTERVALO_100MILISEGUNDOS 100  // 100 milisegundos

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

bool callGetDistance(ArduinoStatus* const);

float pulse;
uint16_t distance;

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
	pinMode(D09_TRIGGER, OUTPUT);
	pinMode(D08_ECHO, INPUT);

	digitalWrite(D09_TRIGGER, LOW);

	Sisbarc.begin(&Serial);

	Sisbarc.addThreadInterval(0x00, INTERVALO_100MILISEGUNDOS);

	int16_t evento = Sisbarc.onRun(ArduinoStatus::DIGITAL, D08_ECHO, callGetDistance);
	if (evento != -1) {
	}
}

void loop(void) {
	Sisbarc.run();
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

void triggerPulses() {
	digitalWrite(D09_TRIGGER, HIGH);
	delayMicroseconds(INTERVALO_10MICROSEGUNDOS);
	digitalWrite(D09_TRIGGER, LOW);
}

void getDistance(ArduinoStatus* const arduino) {
	ArduinoUSARTMessage* arduinoUSART = ((ArduinoUSARTMessage*) arduino);

	arduinoUSART->setTransmitterValue(ArduinoStatus::ARDUINO_DEVICE);
	arduinoUSART->setStatusValue(ArduinoStatus::RESPONSE);

	triggerPulses();

	pulse = pulseIn(arduinoUSART->getPin(), HIGH);
	distance = (uint16_t)(pulse / 5.8823); //distancia em milimetros

	arduinoUSART->setPinValue(distance);

	Sisbarc.send(arduinoUSART);
}

bool isValidEvent(ArduinoStatus* const arduino) {
	if (arduino->getEventValue() == ArduinoStatus::MESSAGE) {
		getDistance(arduino);
		return true;
	}

	return false;
}

bool callGetDistance(ArduinoStatus* const arduino) {
	if (!isCallBySerialToPinDigital(arduino))
		return false;

	if (arduino->getPin() != D08_ECHO)
		return false;

	return isValidEvent(arduino);
}
