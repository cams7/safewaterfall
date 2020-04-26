package br.com.cams7.safewaterfall.arduino;

import static br.com.cams7.safewaterfall.arduino.SisbarcProtocol.TOTAL_BYTES_PROTOCOL;
import static br.com.cams7.safewaterfall.arduino.SisbarcProtocol.decode;
import static br.com.cams7.safewaterfall.arduino.SisbarcProtocol.getProtocolEEPROM;
import static br.com.cams7.safewaterfall.arduino.SisbarcProtocol.getProtocolUSART;
import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.PIN_VALUE_MIN;
import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoStatus.RESPONSE_RESPONSE;
import static br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoTransmitter.ARDUINO;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType.ANALOG;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType.DIGITAL;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART.ANALOG_PIN_VALUE_MAX;
import static br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART.DIGITAL_PIN_VALUE_MAX;
import static gnu.io.SerialPort.DATABITS_8;
import static gnu.io.SerialPort.PARITY_NONE;
import static gnu.io.SerialPort.STOPBITS_1;
import static gnu.io.SerialPortEvent.BI;
import static gnu.io.SerialPortEvent.CD;
import static gnu.io.SerialPortEvent.CTS;
import static gnu.io.SerialPortEvent.DATA_AVAILABLE;
import static gnu.io.SerialPortEvent.DSR;
import static gnu.io.SerialPortEvent.FE;
import static gnu.io.SerialPortEvent.OE;
import static gnu.io.SerialPortEvent.OUTPUT_BUFFER_EMPTY;
import static gnu.io.SerialPortEvent.PE;
import static gnu.io.SerialPortEvent.RI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import br.com.cams7.safewaterfall.arduino.error.ArduinoException;
import br.com.cams7.safewaterfall.arduino.model.CurrentStatus;
import br.com.cams7.safewaterfall.arduino.model.repository.CurrentStatusRepository;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoEvent;
import br.com.cams7.safewaterfall.arduino.model.vo.Arduino.ArduinoStatus;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoEEPROM;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoEEPROMRead;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoEEPROMWrite;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoPin.ArduinoPinType;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSART;
import br.com.cams7.safewaterfall.arduino.model.vo.ArduinoUSARTMessage;
import br.com.cams7.safewaterfall.arduino.util.Binary;
import br.com.cams7.safewaterfall.arduino.util.Bytes;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ArduinoServiceImpl implements ArduinoService, Runnable, SerialPortEventListener {

  private OutputStream output;
  private InputStream input;

  private Byte[] serialData;
  private byte serialDataIndex;

  @Value("${SERIAL_PORT}")
  private String serialPort;

  @Value("${SERIAL_BAUD_RATE}")
  private String serialBaudRate;

  @Value("${SERIAL_THREAD_TIME}")
  private String serialThreadTime; // Serial verification in MILLISECOUNDS

  @Autowired
  protected CurrentStatusRepository statusRepository;

  protected ArduinoServiceImpl() {
    super();
  }

  /**
   * Metodo que verifica se a comunicação com a porta serial esta OK
   */
  @PostConstruct
  protected void init() {
    serialData = new Byte[TOTAL_BYTES_PROTOCOL];
    serialDataIndex = 0x00;

    // close();

    // Define uma variavel portId do tipo CommPortIdentifier para
    // realizar a comunicacao serial
    CommPortIdentifier portId = null;
    try {
      // Tenta verificar se a porta COM informada existe
      portId = CommPortIdentifier.getPortIdentifier(getSerialPort());
    } catch (NoSuchPortException e) {
      // Caso a porta COM nao exista sera exibido um erro
      throw new ArduinoException(String.format("Porta %s não encontrada", getSerialPort()), e.getCause());
    }

    try {
      // Abre a porta COM
      SerialPort port = (SerialPort) portId.open("Comunicação serial", getSerialBaudRate());

      output = port.getOutputStream();
      input = port.getInputStream();

      port.addEventListener(this);

      port.notifyOnDataAvailable(true);

      port.setSerialPortParams(getSerialBaudRate(), // taxa de transferencia da
          // porta serial
          DATABITS_8, // taxa de 10 bits 8 (envio)
          STOPBITS_1, // taxa de 10 bits 1 (recebimento)
          PARITY_NONE); // receber e enviar dados

      Thread readThread = new Thread(this);
      readThread.start();
    } catch (PortInUseException | IOException | TooManyListenersException | UnsupportedCommOperationException e) {
      throw new ArduinoException("Erro na comunicação serial", e.getCause());
    } finally {
      close();
    }

  }

  /**
   * Método que fecha a comunicação com a porta serial
   */
  private void close() {
    ArduinoException exception = new ArduinoException(String.format("Não foi possivel fechar a porta %s",
        getSerialPort()));
    try {
      if (input != null)
        input.close();
    } catch (IOException e) {
      exception.addSuppressed(e);
    }

    try {
      if (output != null)
        output.close();
    } catch (IOException e) {
      exception.addSuppressed(e);
    }

    if (exception.getSuppressed().length > 0)
      throw exception;
  }

  /**
   * @param opcao - Valor a ser enviado pela porta serial
   */
  private void serialWrite(byte[] data) {
    if (output == null)
      throw new ArduinoException("O 'OutputStream' nao foi inicializado");

    try {
      // escreve o valor na porta serial para ser enviado
      output.write(data);
    } catch (IOException e) {
      throw new ArduinoException("Não foi possivel enviar o dado", e.getCause());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
   */
  public void serialEvent(SerialPortEvent event) {
    switch (event.getEventType()) {
      case BI:
      case OE:
      case FE:
      case PE:
      case CD:
      case CTS:
      case DSR:
      case RI:
      case OUTPUT_BUFFER_EMPTY:
        break;
      case DATA_AVAILABLE:
        try {
          while (input.available() > 0)
            receiveDataBySerial((byte) input.read());
        } catch (IOException e) {
          log.error(e.getMessage());
        }
        break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {
    try {
      Thread.sleep(getSerialThreadTime());
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  private void receiveDataBySerial(byte data) {
    byte lastBit = Binary.getLastBitByte(data);

    if (0x01 == lastBit) {
      serialData[0] = data;
      serialDataIndex = 0x01;
    } else if (serialDataIndex > 0x00) {
      serialData[serialDataIndex] = data;
      serialDataIndex++;

      if (serialDataIndex == TOTAL_BYTES_PROTOCOL) {
        byte[] datas = Bytes.toPrimitiveArray(serialData);
        try {
          Arduino arduino = receive(datas);
          addCurrentStatus(arduino);
          receiveDataBySerial(arduino);
        } catch (ArduinoException e) {
          log.error(e.getMessage());
        }
        serialDataIndex = 0x00;
      }

    } else {
      log.warn("O dado {} foi corrompido", Integer.toBinaryString(data));
    }
  }

  protected void addCurrentStatus(Arduino arduino) {
    String id = getKeyCurrentStatus(arduino.getEvent(), arduino.getPinType(), arduino.getPin());

    CurrentStatus currentStatus;
    if (!statusRepository.existsById(id)) {
      currentStatus = new CurrentStatus(id);
      currentStatus.setArduino(arduino);
    } else {
      currentStatus = statusRepository.findById(id).orElseThrow(() -> new ArduinoException(String.format(
          "Não foi encontrado nenhum estado do arduino pelo id %s", id)));
      if (arduino instanceof ArduinoUSART)
        ((ArduinoUSART) currentStatus.getArduino()).changeCurrentValues((ArduinoUSART) arduino);
      else if (arduino instanceof ArduinoEEPROM)
        ((ArduinoEEPROM) currentStatus.getArduino()).changeCurrentValues((ArduinoEEPROM) arduino);
    }
    statusRepository.save(currentStatus);
  }

  public static String getKeyCurrentStatus(ArduinoEvent event, ArduinoPinType pinType, byte pin) {
    String key = event.getAbbreviation() + "_" + pinType.getAbbreviation() + pin;
    return key;
  }

  private void receiveDataBySerial(Arduino arduino) {
    if (ARDUINO != arduino.getTransmitter()) {
      log.warn("O dado não vem do Arduino");
      return;
    }

    switch (arduino.getStatus()) {
      case SEND:
      case RESPONSE:
        switch (arduino.getEvent()) {
          case EXECUTE:
            receiveExecute(arduino.getPinType(), arduino.getPin(), ((ArduinoUSART) arduino).getPinValue());
            break;
          case MESSAGE:
            receiveMessage(arduino.getPinType(), arduino.getPin(), ((ArduinoUSART) arduino).getPinValue());
            break;
          case WRITE:
            receiveWrite(arduino.getPinType(), arduino.getPin(), ((ArduinoEEPROM) arduino).getThreadInterval(),
                ((ArduinoEEPROM) arduino).getActionEvent());
            break;
          case READ:
            receiveRead(arduino.getPinType(), arduino.getPin(), ((ArduinoEEPROM) arduino).getThreadInterval(),
                ((ArduinoEEPROM) arduino).getActionEvent());
            break;
          default:
            break;
        }

        break;

      case SEND_RESPONSE:
      case RESPONSE_RESPONSE:
        switch (arduino.getEvent()) {
          case EXECUTE:
            short pinValue = sendResponse(arduino.getPinType(), arduino.getPin(), ((ArduinoUSART) arduino)
                .getPinValue());

            try {
              switch (arduino.getPinType()) {
                case DIGITAL: {
                  boolean isPinPWD = false;
                  for (byte pinPWD : Arduino.getPinsDigitalPWM())
                    if (arduino.getPin() == pinPWD) {
                      isPinPWD = true;
                      break;
                    }

                  if (isPinPWD)
                    sendPinPWMUSART(RESPONSE_RESPONSE, arduino.getPin(), pinValue);
                  else
                    sendPinDigitalUSART(RESPONSE_RESPONSE, arduino.getPin(), pinValue == 0x0001);
                  break;
                }
                case ANALOG: {
                  sendPinAnalogUSART(RESPONSE_RESPONSE, arduino.getPin(), pinValue);
                  break;
                }
                default:
                  break;
              }
            } catch (ArduinoException e) {
              log.warn(e.getMessage());
            }

            break;
          case MESSAGE:
            break;
          case WRITE:
          case READ:
            break;
          default:
            break;
        }
        break;

      default:
        break;
    }

  }

  protected abstract void receiveExecute(ArduinoPinType pinType, byte pin, short pinValue);

  protected abstract void receiveWrite(ArduinoPinType pinType, byte pin, byte threadTime, byte actionEvent);

  protected abstract void receiveRead(ArduinoPinType pinType, byte pin, byte threadTime, byte actionEvent);

  protected abstract void receiveMessage(ArduinoPinType pinType, byte pin, short codMessage);

  protected abstract short sendResponse(ArduinoPinType pinType, byte pin, short pinValue);

  private static Arduino receive(byte[] values) {
    return decode(values);
  }

  private void sendPinDigital(ArduinoUSART arduino) {
    boolean pinOk = false;
    for (byte pin : Arduino.getPinsDigital())
      if (arduino.getPin() == pin) {
        pinOk = true;
        break;
      }

    if (!pinOk)
      for (byte pin : Arduino.getPinsDigitalPWM())
        if (arduino.getPin() == pin) {
          pinOk = true;
          break;
        }

    if (!pinOk)
      throw new ArduinoException("O PINO Digital nao e valido");

    arduino.setPinType(DIGITAL);

    serialWrite(getProtocolUSART(arduino));

    addCurrentStatus(arduino);
  }

  protected void sendPinDigitalUSART(ArduinoStatus status, byte digitalPin, boolean pinValue) {
    ArduinoUSART arduino = new ArduinoUSART(status, DIGITAL, digitalPin, (short) (pinValue ? 0x0001 : 0x0000));
    sendPinDigital(arduino);
  }

  protected void sendPinDigitalUSARTMessage(ArduinoStatus status, byte digitalPin) {
    ArduinoUSARTMessage arduino = new ArduinoUSARTMessage(status, DIGITAL, digitalPin);
    sendPinDigital(arduino);
  }

  private void sendPinPWM(ArduinoUSART arduino) {
    boolean pinOk = false;
    for (byte pin : Arduino.getPinsDigitalPWM())
      if (arduino.getPin() == pin) {
        pinOk = true;
        break;
      }

    if (!pinOk)
      throw new ArduinoException("O PINO PWM nao e valido");

    if (((ArduinoUSART) arduino).getPinValue() < PIN_VALUE_MIN)
      throw new ArduinoException(String.format("O valor do PINO PWM e maior ou igual a %d", PIN_VALUE_MIN));

    if (((ArduinoUSART) arduino).getPinValue() > DIGITAL_PIN_VALUE_MAX)
      throw new ArduinoException(String.format("O valor do PINO PWM e menor ou igual a %d",
          DIGITAL_PIN_VALUE_MAX));

    arduino.setPinType(DIGITAL);

    serialWrite(getProtocolUSART(arduino));

    addCurrentStatus(arduino);
  }

  protected void sendPinPWMUSART(ArduinoStatus status, byte digitalPin, short pinValue) {
    ArduinoUSART arduino = new ArduinoUSART(status, DIGITAL, digitalPin, pinValue);
    sendPinPWM(arduino);
  }

  protected void sendPinPWMUSARTMessage(ArduinoStatus status, byte digitalPin) {
    ArduinoUSARTMessage arduino = new ArduinoUSARTMessage(status, DIGITAL, digitalPin);
    sendPinPWM(arduino);
  }

  private void sendPinAnalog(ArduinoUSART arduino) {
    boolean pinOk = false;
    for (byte pin : Arduino.getPinsAnalog())
      if (arduino.getPin() == pin) {
        pinOk = true;
        break;
      }

    if (!pinOk)
      throw new ArduinoException("O PINO Analogico nao e valido");

    if (((ArduinoUSART) arduino).getPinValue() < PIN_VALUE_MIN)
      throw new ArduinoException(String.format("O valor do PINO Analogico e maior ou igual a %d", PIN_VALUE_MIN));

    if (((ArduinoUSART) arduino).getPinValue() > ANALOG_PIN_VALUE_MAX)
      throw new ArduinoException(String.format("O valor do PINO Analogico e menor ou igual a %d",
          ANALOG_PIN_VALUE_MAX));

    arduino.setPinType(ANALOG);

    serialWrite(getProtocolUSART(arduino));

    addCurrentStatus(arduino);
  }

  protected void sendPinAnalogUSART(ArduinoStatus status, byte analogPin, short pinValue) {
    ArduinoUSART arduino = new ArduinoUSART(status, ANALOG, analogPin, pinValue);
    sendPinAnalog(arduino);
  }

  protected void sendPinAnalogUSARTMessage(ArduinoStatus status, byte analogPin) {
    ArduinoUSARTMessage arduino = new ArduinoUSARTMessage(status, ANALOG, analogPin);
    sendPinAnalog(arduino);
  }

  protected void sendEEPROMWrite(ArduinoStatus status, ArduinoPinType pinType, byte pin, byte threadTime,
      byte actionEvent) {
    ArduinoEEPROMWrite arduino = new ArduinoEEPROMWrite(status, pinType, pin, threadTime, actionEvent);
    serialWrite(getProtocolEEPROM(arduino));

    addCurrentStatus(arduino);
  }

  protected void sendEEPROMRead(ArduinoStatus status, ArduinoPinType pinType, byte pin) {
    ArduinoEEPROMRead arduino = new ArduinoEEPROMRead(status, pinType, pin);
    serialWrite(getProtocolEEPROM(arduino));

    addCurrentStatus(arduino);
  }

  @Override
  public String getSerialPort() {
    return serialPort;
  }

  @Override
  public int getSerialBaudRate() {
    return Integer.valueOf(serialBaudRate);
  }

  @Override
  public long getSerialThreadTime() {
    return Long.valueOf(serialThreadTime);
  }

}
