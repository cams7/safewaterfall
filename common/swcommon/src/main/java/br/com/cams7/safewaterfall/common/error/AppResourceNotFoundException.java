/**
 * 
 */
package br.com.cams7.safewaterfall.common.error;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author CAMs7
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(value = NOT_FOUND)
public class AppResourceNotFoundException extends RuntimeException {

  public AppResourceNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public AppResourceNotFoundException(String msg) {
    super(msg);
  }

  public AppResourceNotFoundException(Throwable cause) {
    super(cause);
  }

}
