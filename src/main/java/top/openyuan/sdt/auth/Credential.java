package top.openyuan.sdt.auth;

import top.openyuan.sdt.http.model.HttpRequest;

/**
 * 请求凭据生成器
 *
 * @author lzy
 */
public interface Credential {
  /**
   * 组装请求数据格式
   *
   * @param httpRequest 请求相关入参
   * @return 请求参数
   */
  HttpRequest assembleHttpRequest(HttpRequest httpRequest);
}
