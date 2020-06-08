/**
 * 
 */
package br.com.cams7.safewaterfall.swmanager.endpoint.common;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import java.lang.reflect.ParameterizedType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import br.com.cams7.safewaterfall.common.error.AppException;

/**
 * @author CAMs7
 *
 */
public abstract class BaseEndpoint<VO> {

  private static final int VO_INDEX = 0;

  @Autowired
  private RestOperations restTemplate;

  protected void changeValue(String url) {
    // setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();

    // request entity is created with request body and headers
    HttpEntity<VO> requestEntity = new HttpEntity<>(requestHeaders);

    try {
      restTemplate.exchange(url, HttpMethod.GET, requestEntity, Void.class);
    } catch (ResourceAccessException e) {
      throw new AppException(e);
    }
  }

  protected void changeValue(String url, VO body) {
    // setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(APPLICATION_JSON);

    // request entity is created with request body and headers
    HttpEntity<VO> requestEntity = new HttpEntity<>(body, requestHeaders);

    try {
      restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
    } catch (ResourceAccessException e) {
      throw new AppException(e);
    }
  }

  protected VO getValue(String url) {
    ResponseEntity<VO> responseEntity = restTemplate.getForEntity(url, getVOType());
    return responseEntity.getBody();

  }

  protected Class<VO> getVOType() {
    @SuppressWarnings("unchecked")
    Class<VO> type = (Class<VO>) getTypeFromTemplate(VO_INDEX);
    return type;
  }

  private Class<?> getTypeFromTemplate(int index) {
    return getTypeFromTemplate(getClass(), index);
  }

  private Class<?> getTypeFromTemplate(Class<?> type, int index) {
    return (Class<?>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[index];
  }
}
