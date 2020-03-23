/**
 * 
 */
package br.com.cams7.safewaterfall.common.error;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author CAMs7
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(value = INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

  public AppException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public AppException(String msg) {
    super(msg);
  }

  public AppException(Throwable cause) {
    super(cause);
  }

}
