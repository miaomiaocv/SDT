package top.openyuan.sdt.http;

import top.openyuan.sdt.auth.Credential;
import top.openyuan.sdt.auth.Validator;
import top.openyuan.sdt.http.model.HttpRequest;
import top.openyuan.sdt.http.model.HttpResponse;
import top.openyuan.sdt.http.model.OriginalResponse;
import top.openyuan.sdt.http.model.enums.HttpMethod;

import java.io.InputStream;

import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Objects.requireNonNull;
import static top.openyuan.sdt.http.constant.HttpConstant.*;

/** 请求客户端抽象基类 */
public abstract class AbstractHttpClient implements HttpClient {

  protected final Credential credential;
  protected final Validator validator;

  public AbstractHttpClient(Credential credential, Validator validator) {
    this.credential = requireNonNull(credential);
    this.validator = requireNonNull(validator);
  }

  @Override
  public <T> HttpResponse<T> execute(HttpRequest httpRequest, Class<T> responseClass) {
    HttpRequest innerRequest = credential.assembleHttpRequest(httpRequest);
    OriginalResponse originalResponse = innerExecute(innerRequest);
    return assembleHttpResponse(validator.assembleOriginalResponse(originalResponse), responseClass);
  }

  @Override
  public InputStream download(String url) {
    HttpRequest originRequest =
        new HttpRequest.Builder().httpMethod(HttpMethod.GET).url(url).build();
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .url(url)
            .httpMethod(HttpMethod.GET)
            .addHeader(USER_AGENT, getUserAgent())
            .build();
    return innerDownload(httpRequest);
  }

  protected abstract InputStream innerDownload(HttpRequest httpRequest);

  protected abstract OriginalResponse innerExecute(HttpRequest httpRequest);

  protected boolean isInvalidHttpCode(int httpCode) {
    return httpCode < HTTP_OK || httpCode >= HTTP_MULT_CHOICE;
  }

  private <T> HttpResponse<T> assembleHttpResponse(
      OriginalResponse originalResponse, Class<T> responseClass) {
    return new HttpResponse.Builder<T>()
        .originalResponse(originalResponse)
        .serviceResponseType(responseClass)
        .build();
  }

  private String getUserAgent() {
    return String.format(
        USER_AGENT_FORMAT,
        getClass().getPackage().getImplementationVersion(),
        OS,
        VERSION == null ? "Unknown" : VERSION,
        credential.getClass().getSimpleName(),
        validator.getClass().getSimpleName(),
        getHttpClientInfo());
  }

  /**
   * 获取http客户端信息，用于User-Agent。 格式：客户端名称/版本 示例：okhttp3/4.9.3
   *
   * @return 客户端信息
   */
  protected abstract String getHttpClientInfo();
}
