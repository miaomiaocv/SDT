package top.openyuan.sdt.auth;

import top.openyuan.sdt.http.model.OriginalResponse;

/**
 * 响应数据验证器
 *
 * @author lzy
 */
public interface Validator {
  /**
   * 组装响应请求
   *
   * @return 响应请求
   */
  OriginalResponse assembleOriginalResponse(OriginalResponse originalResponse);
}
