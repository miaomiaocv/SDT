package top.openyuan.sdt.http.adapter.okhttp;

import top.openyuan.sdt.auth.Credential;
import top.openyuan.sdt.auth.Validator;
import top.openyuan.sdt.exception.HttpException;
import top.openyuan.sdt.exception.MalformedMessageException;
import top.openyuan.sdt.exception.ServiceException;
import top.openyuan.sdt.http.AbstractHttpClient;
import top.openyuan.sdt.http.model.HttpRequest;
import top.openyuan.sdt.http.model.OriginalResponse;
import top.openyuan.sdt.http.model.data.FileRequestBody;
import top.openyuan.sdt.http.model.data.JsonRequestBody;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/** OkHttp请求客户端 */
public final class OkHttpClientAdapter extends AbstractHttpClient {

  private static final Logger logger = LoggerFactory.getLogger(OkHttpClientAdapter.class);
  private static final String META_NAME = "meta";
  private static final String FILE_NAME = "file";

  private final OkHttpClient okHttpClient;

  public OkHttpClientAdapter(
          Credential credential, Validator validator, OkHttpClient client) {
    super(credential, validator);
    this.okHttpClient = requireNonNull(client);
  }

  @Override
  protected String getHttpClientInfo() {
    return "okhttp3/" + okHttpClient.getClass().getPackage().getImplementationVersion();
  }

  @Override
  public OriginalResponse innerExecute(HttpRequest httpRequest) {
    Request okHttpRequest = buildOkHttpRequest(httpRequest);
    try (Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute()) {
      return assembleOriginalResponse(httpRequest, okHttpResponse);
    } catch (IOException e) {
      throw new HttpException(httpRequest, e);
    }
  }

  private Request buildOkHttpRequest(HttpRequest wechatPayRequest) {
    Request.Builder okHttpRequestBuilder = new Request.Builder().url(wechatPayRequest.getUrl());
    Map<String, String> headers = wechatPayRequest.getHeaders().getHeaders();
    headers.forEach(okHttpRequestBuilder::addHeader);
    String method = wechatPayRequest.getHttpMethod().name();
    RequestBody okHttpRequestBody =
        method.equals("GET") ? null : buildOkHttpRequestBody(wechatPayRequest.getBody());
    okHttpRequestBuilder.method(method, okHttpRequestBody);
    return okHttpRequestBuilder.build();
  }

  private RequestBody buildOkHttpRequestBody(
      top.openyuan.sdt.http.model.data.RequestBody heliPayRequestBody) {
    if (heliPayRequestBody == null) {
      // create an empty request body
      return RequestBody.create("", null);
    }
    if (heliPayRequestBody instanceof JsonRequestBody) {
      return createOkHttpRequestBody(heliPayRequestBody);
    }
    if (heliPayRequestBody instanceof FileRequestBody) {
      return createOkHttpMultipartRequestBody(heliPayRequestBody);
    }
    logger.error(
        "When an http request is sent and the okhttp request body is constructed, the requestBody parameter"
            + " type cannot be found,requestBody class name[{}]",
        heliPayRequestBody.getClass().getName());
    return null;
  }

  @SuppressWarnings("deprecation")
  private RequestBody createRequestBody(String content, MediaType mediaType) {
    // use an OkHttp3.x compatible method
    // see https://github.com/wechatpay-apiv3/wechatpay-java/issues/70
    return RequestBody.create(mediaType, content);
  }

  @SuppressWarnings("deprecation")
  private RequestBody createRequestBody(byte[] content, MediaType mediaType) {
    return RequestBody.create(mediaType, content);
  }

  private RequestBody createOkHttpRequestBody(
      top.openyuan.sdt.http.model.data.RequestBody wechatPayRequestBody) {
    return createRequestBody(
        ((JsonRequestBody) wechatPayRequestBody).getBody(),
        MediaType.parse(wechatPayRequestBody.getContentType()));
  }

  private RequestBody createOkHttpMultipartRequestBody(
      top.openyuan.sdt.http.model.data.RequestBody wechatPayRequestBody) {
    FileRequestBody fileRequestBody = (FileRequestBody) wechatPayRequestBody;
    RequestBody okHttpFileBody =
        createRequestBody(
            fileRequestBody.getFile(), MediaType.parse(fileRequestBody.getContentType()));
    return new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(META_NAME, fileRequestBody.getMeta())
        .addFormDataPart(FILE_NAME, fileRequestBody.getFileName(), okHttpFileBody)
        .build();
  }

  private Map<String, String> assembleResponseHeader(Response okHttpResponse) {
    Map<String, String> responseHeaders = new ConcurrentHashMap<>();
    int headerSize = okHttpResponse.headers().size();
    for (int i = 0; i < headerSize; ++i) {
      responseHeaders.put(okHttpResponse.headers().name(i), okHttpResponse.headers().value(i));
    }
    return responseHeaders;
  }

  private OriginalResponse assembleOriginalResponse(
      HttpRequest clientRequest, Response okHttpResponse) {
    Map<String, String> responseHeaders = assembleResponseHeader(okHttpResponse);
    try {
      return new OriginalResponse.Builder()
          .request(clientRequest)
          .headers(responseHeaders)
          .statusCode(okHttpResponse.code())
          .contentType(
                  Optional.ofNullable(okHttpResponse.body())
                          .map(ResponseBody::contentType)
                          .map(MediaType::toString)
                          .orElse(null))
          .body(okHttpResponse.body().string())
          .build();
    } catch (IOException e) {
      throw new MalformedMessageException(
          String.format("Assemble OriginalResponse,get responseBody failed.%nHttpRequest[%s]", clientRequest));
    }
  }

  @Override
  protected InputStream innerDownload(HttpRequest httpRequest) {
    Request okHttpRequest = buildOkHttpRequest(httpRequest);
    try {
      Response okHttpResponse = okHttpClient.newCall(okHttpRequest).execute();
      if (isInvalidHttpCode(okHttpResponse.code())) {
        throw new ServiceException(httpRequest, okHttpResponse.code(), "");
      }
      InputStream responseBodyStream = null;
      if (okHttpResponse.body() != null) {
        responseBodyStream = okHttpResponse.body().byteStream();
      }
      return responseBodyStream;
    } catch (IOException e) {
      throw new HttpException(httpRequest, e);
    }
  }
}
